package com.example.todolist.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.todolist.R;
import com.example.todolist.adapter.AppDatabase;
import com.example.todolist.adapter.AppExecutors;
import com.example.todolist.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    TextView textSignIn;
    TextView textError;
    Button btnSignUp;
    TextInputEditText txtFullName;
    TextInputEditText txtUsername;
    TextInputEditText txtPassword;
    RadioGroup genderRadioGroup;
    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        textSignIn = (TextView) findViewById(R.id.signin_option);
        textError = (TextView) findViewById(R.id.textError);
        btnSignUp = (Button) findViewById(R.id.signup_button);
        txtFullName = (TextInputEditText) findViewById(R.id.fullname);
        txtUsername = (TextInputEditText) findViewById(R.id.username);
        txtPassword = (TextInputEditText) findViewById(R.id.password);
        genderRadioGroup = findViewById(R.id.gender_radio_group);

        mDb = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "ToDoListDb").build();

        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(txtFullName.getText().toString())) {
                    textError.setText("Please input full name");
                    return;
                }

                if (TextUtils.isEmpty(txtUsername.getText().toString())) {
                    textError.setText("Please input username");
                    return;
                }

                if (TextUtils.isEmpty(txtPassword.getText().toString())) {
                    textError.setText("Please input password");
                    return;
                }
                int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                String gender = "";

                if (selectedId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    gender = selectedRadioButton.getText().toString();
                } else {
                    textError.setText("Please select your gender");
                    return;
                }


                String finalGender = gender;
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        boolean userExists = mDb.userDao().userExist(txtUsername.getText().toString());
                        if (userExists) {
                            runOnUiThread(() -> textError.setText("Username already exists"));
                        } else {
                            User user = new User(UUID.randomUUID().toString(),
                                    txtUsername.getText().toString(),
                                    txtPassword.getText().toString(),
                                    txtFullName.getText().toString(),
                                    finalGender);

                            mDb.userDao().insert(user);
                            finish(); // Close the activity
                        }
                    }
                });
            }
        });
    }
}
