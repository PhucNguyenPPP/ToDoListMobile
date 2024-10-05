package com.example.todolist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.adapter.AppDatabase;
import com.example.todolist.adapter.AppExecutors;
import com.example.todolist.adapter.SessionManager;
import com.example.todolist.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.UUID;


public class SignInActivity extends AppCompatActivity {

    TextView txtSignUp;
    Button btnSignIn;
    TextInputEditText txtUserName;
    TextInputEditText txtPassword;
    TextView textError;
    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);

        txtSignUp = (TextView) findViewById(R.id.signup_option);
        txtUserName = (TextInputEditText) findViewById(R.id.username);
        txtPassword = (TextInputEditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.login_button);
        textError = (TextView) findViewById(R.id.textError);

        mDb = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "ToDoListDb")
                .build();

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtUserName.getText().toString())) {
                    textError.setText("Please input username");
                    return;
                }

                if (TextUtils.isEmpty(txtPassword.getText().toString())) {
                    textError.setText("Please input password");
                    return;
                }

                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<User> user = mDb.userDao().getAll();
                        boolean checkLogin = mDb.userDao().checkLogin(
                                txtUserName.getText().toString()
                                , txtPassword.getText().toString());
                        if (!checkLogin) {
                            runOnUiThread(() -> textError.setText("Username or password incorrect"));
                        } else {
                            User userDb = mDb.userDao().loadPersonByUserName(txtUserName.getText().toString());
                            SessionManager sessionManagement = new SessionManager(SignInActivity.this);

                            // Save user session
                            sessionManagement.createLoginSession(userDb.getUserId(), userDb.getUserName(), userDb.getFullName(), userDb.getGender());
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
