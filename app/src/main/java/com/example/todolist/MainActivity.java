package com.example.todolist;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import com.example.todolist.adapter.SessionManager;
import com.example.todolist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManger = new SessionManager(this);

    }

}