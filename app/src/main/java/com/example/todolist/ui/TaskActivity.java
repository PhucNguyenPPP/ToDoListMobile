package com.example.todolist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.todolist.R;
import com.example.todolist.adapter.AppDatabase;
import com.example.todolist.adapter.AppExecutors;
import com.example.todolist.adapter.SessionManager;
import com.example.todolist.model.Task;
import com.example.todolist.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private Button btnAddTask;
    private Button btnDashboard;
    private Button btnProfile;
    private Button btnSubmitTasks;
    private EditText inputTask;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();
    private AppDatabase mdb;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task);

        // Lấy userId từ SessionManager
        currentUserId = getCurrentUserId();

        if (currentUserId == null) {
            Toast.makeText(this, "User not found. Please log in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo cơ sở dữ liệu
        mdb = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "ToDoListDb").build();

        // Khởi tạo các view
        inputTask = findViewById(R.id.input_task);
        btnAddTask = findViewById(R.id.button_add_task);
        btnDashboard = findViewById(R.id.button_dashboard);
        btnProfile = findViewById(R.id.button_profile);
        btnSubmitTasks = findViewById(R.id.button_submit_tasks);
        recyclerView = findViewById(R.id.recycler_view);

        taskAdapter = new TaskAdapter(taskList, new TaskAdapter.OnTaskDeleteListener() {
            @Override
            public void onTaskDelete(Task task) {
                deleteTask(task); // Xử lý xóa task khi nhấn nút Delete
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        // Xử lý sự kiện cho nút "Add Task"
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = inputTask.getText().toString().trim();
                if (!taskName.isEmpty()) {
                    addTask(taskName);
                } else {
                    Toast.makeText(TaskActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện cho nút "Submit"
        btnSubmitTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTasks();
            }
        });

        // Xử lý sự kiện cho nút Dashboard
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện cho nút Profile
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Tải danh sách task từ Room Database
        loadTasks();
    }

    private String getCurrentUserId() {
        SessionManager sessionManager = new SessionManager(TaskActivity.this);
        return sessionManager.getUserId();
    }

    // Hàm thêm task vào Room Database
    private void addTask(String taskName) {
        Task task = new Task(currentUserId, taskName, false); // Sử dụng currentUserId
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mdb.taskDao().insert(task); // Thêm task vào database
                    loadTasks(); // Tải lại danh sách task
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TaskActivity.this, "Error adding task: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        inputTask.setText(""); // Xóa input sau khi thêm task
    }

    // Hàm xóa task khỏi Room Database
    private void deleteTask(Task task) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mdb.taskDao().delete(task); // Xóa task khỏi database
                    loadTasks(); // Tải lại danh sách task
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TaskActivity.this, "Error deleting task: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    // Tải task từ Room Database theo userId
    private void loadTasks() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Task> tasks = mdb.taskDao().getTasksByUserId(currentUserId); // Lấy task dựa trên userId
                taskList.clear();
                taskList.addAll(tasks); // Thêm vào danh sách
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        taskAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                    }
                });
            }
        });
    }

    // Hàm lưu trạng thái hoàn thành của các task
    private void submitTasks() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Task task : taskList) {
                        mdb.taskDao().update(task); // Cập nhật trạng thái task
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TaskActivity.this, "Tasks updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TaskActivity.this, "Error updating tasks: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
