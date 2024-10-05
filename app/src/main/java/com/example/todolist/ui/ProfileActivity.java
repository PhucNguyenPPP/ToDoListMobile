package com.example.todolist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    Button btnTask;
    Button btnDashboard;
    Button btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        btnTask = (Button) findViewById(R.id.button_task);
        btnDashboard = (Button) findViewById(R.id.button_dashboard);
        btnProfile = (Button) findViewById(R.id.button_profile);

        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, TaskActivity.class);
                startActivity(intent);
            }
        });

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

    }
}
