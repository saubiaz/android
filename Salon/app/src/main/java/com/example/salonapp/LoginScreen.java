package com.example.salonapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;


public class LoginScreen extends AppCompatActivity {

    private static final String TAG = "LoginScreen";

    private FirebaseAuth mAuth;

    private EditText username, password;
    private TextView  forgotPassword;
    private Button signIn,createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Firebase auth instance
        /*getSupportActionBar().hide();*/
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login_screen);

        initializeUI();

        signIn.setOnClickListener(v -> loginUserAccount());

        forgotPassword.setOnClickListener( v -> {
            Intent intent = new Intent(LoginScreen.this, ResetPasswordActivity.class);
            LoginScreen.this.startActivity(intent);
        });

        createAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginScreen.this, SignUpScreen.class);
            LoginScreen.this.startActivity(intent);
        });

    }

    private void loginUserAccount() {

        String email, pwd;
        email = username.getText().toString();
        pwd = password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                      //  progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(LoginScreen.this, ProductsServicesScreen.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void initializeUI() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
        createAccount = findViewById(R.id.create_account);
        forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setMovementMethod(LinkMovementMethod.getInstance());
    }

   /* public void onClickToSignUp(View view) {

        Intent intent = new Intent(LoginScreen.this, SignUpScreen.class);
        startActivity(intent);
    }*/
}

