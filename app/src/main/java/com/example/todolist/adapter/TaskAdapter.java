package com.example.todolist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private OnTaskDeleteListener deleteListener; // Interface for delete event

    public TaskAdapter(List<Task> taskList, OnTaskDeleteListener deleteListener) {
        this.taskList = taskList;
        this.deleteListener = deleteListener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskName.setText(task.getTaskName());
        holder.checkBox.setChecked(task.isCompleted());

        // Xử lý sự kiện xóa task
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onTaskDelete(task); // Gọi hàm xóa task
            }
        });

        // Xử lý sự kiện thay đổi trạng thái hoàn thành
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName;
        public CheckBox checkBox;
        public Button btnDelete;

        public TaskViewHolder(View view) {
            super(view);
            taskName = view.findViewById(R.id.task_name);
            checkBox = view.findViewById(R.id.checkbox_complete);
            btnDelete = view.findViewById(R.id.button_delete_task);
        }
    }

    // Interface để xử lý sự kiện xóa
    public interface OnTaskDeleteListener {
        void onTaskDelete(Task task);
    }
}
