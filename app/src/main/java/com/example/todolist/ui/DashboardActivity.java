package com.example.todolist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.example.todolist.R;
import com.example.todolist.adapter.AppDatabase;
import com.example.todolist.adapter.AppExecutors;
import com.example.todolist.adapter.SessionManager;

public class DashboardActivity extends AppCompatActivity {
    Button btnTask;
    Button btnDashboard;
    Button btnProfile;

    private TextView totalTasksTextView;
    private TextView completedTasksTextView;
    private TextView incompleteTasksTextView;
    private AppDatabase mdb;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        btnTask = findViewById(R.id.button_task);
        btnDashboard = findViewById(R.id.button_dashboard);
        btnProfile = findViewById(R.id.button_profile);

        totalTasksTextView = findViewById(R.id.text_total_tasks);
        completedTasksTextView = findViewById(R.id.text_completed_tasks);
        incompleteTasksTextView = findViewById(R.id.text_incomplete_tasks);

        // Khởi tạo cơ sở dữ liệu
        mdb = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "ToDoListDb").build();

        // Lấy userId từ SessionManager
        currentUserId = getCurrentUserId();

        if (currentUserId == null) {
            Toast.makeText(this, "User not found. Please log in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, TaskActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Tải số liệu task
        loadTaskStatistics();
    }

    private String getCurrentUserId() {
        SessionManager sessionManager = new SessionManager(DashboardActivity.this);
        return sessionManager.getUserId();
    }

    private void loadTaskStatistics() {
        // Sử dụng LiveData để quan sát các thay đổi
        mdb.taskDao().getTotalTaskCount(currentUserId).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer totalTasks) {
                totalTasksTextView.setText("Total Tasks: " + totalTasks);
            }
        });

        mdb.taskDao().getCompletedTaskCount(currentUserId).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer completedTasks) {
                completedTasksTextView.setText("Completed Tasks: " + completedTasks);
            }
        });

        mdb.taskDao().getIncompleteTaskCount(currentUserId).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer incompleteTasks) {
                incompleteTasksTextView.setText("Incomplete Tasks: " + incompleteTasks);
            }
        });
    }
}
