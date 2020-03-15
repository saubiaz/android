package com.example.salonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.salonapp.dtos.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpScreen extends AppCompatActivity {


    private EditText username, emailId, phoneNumber, password, confirmPassword;

    private Button createAccount;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        //getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        initializeUI();

        createAccount.setOnClickListener(v -> registerNewUser());
    }

    private void registerNewUser() {

        String email, pwd, usrnme, phn, confmPwd;
        email = emailId.getText().toString();
        pwd = password.getText().toString();
        phn = phoneNumber.getText().toString();
        usrnme = username.getText().toString();
        confmPwd = confirmPassword.getText().toString();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        if (!pwd.equals(confmPwd)) {
            Toast.makeText(getApplicationContext(), "Passwords didnt match..", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Users users = new Users();
                        users.setEmailId(email);
                        users.setPhoneNumber(phn);
                        users.setUsername(usrnme);
                        mDatabase.child("Users").push().setValue(users, (DatabaseReference.CompletionListener)
                                (databaseError, databaseReference) -> {
                                    //Problem with saving the data
                                    if (databaseError != null) {

                                    } else {
                                        //Data uploaded successfully on the server
                                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SignUpScreen.this, LoginScreen.class);
                                        startActivity(intent);
                                    }

                                });
                        /*Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(SignUpScreen.this, LoginScreen.class);
                        startActivity(intent);*/
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void initializeUI() {
        username = findViewById(R.id.username_sign_up);
        emailId = findViewById(R.id.email_sign_up);
        phoneNumber = findViewById(R.id.phone_sign_up);
        password = findViewById(R.id.password_sign_up);
        confirmPassword = findViewById(R.id.confirm_password_sign_up);
        createAccount = findViewById(R.id.create_account_sign_up);

    }
}
