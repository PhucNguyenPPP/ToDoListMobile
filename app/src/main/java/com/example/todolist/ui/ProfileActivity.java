package com.example.todolist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;
import com.example.todolist.adapter.SessionManager;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvFullName;
    private TextView tvUserName;
    private TextView tvGender;
    private Button btnTask;
    private Button btnDashboard;
    private Button btnLogout; // Khai báo nút đăng xuất

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Khởi tạo các TextView
        tvFullName = findViewById(R.id.tv_full_name);
        tvUserName = findViewById(R.id.tv_user_name);
        tvGender = findViewById(R.id.tv_gender);

        // Khởi tạo các Button
        btnTask = findViewById(R.id.button_task);
        btnDashboard = findViewById(R.id.button_dashboard);
        btnLogout = findViewById(R.id.button_logout); // Khởi tạo nút đăng xuất

        // Lấy thông tin người dùng từ SessionManager
        SessionManager sessionManager = new SessionManager(this);
        String fullName = sessionManager.getFullName();
        String userName = sessionManager.getUserName();
        String gender = sessionManager.getGender();

        // Hiển thị thông tin lên TextView
        tvFullName.setText("Full Name: " + fullName);
        tvUserName.setText("Username: " + userName);
        tvGender.setText("Gender: " + gender);

        // Điều hướng tới TaskActivity
        btnTask.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, TaskActivity.class);
            startActivity(intent);
        });

        // Điều hướng tới DashboardActivity
        btnDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện khi nhấn nút Log Out
        btnLogout.setOnClickListener(v -> {
            sessionManager.logout(); // Đăng xuất
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class); // Điều hướng về màn hình đăng nhập
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Dọn dẹp stack activity
            startActivity(intent);
            finish(); // Kết thúc activity hiện tại
        });
    }
}
