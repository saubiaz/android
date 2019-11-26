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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpScreen extends AppCompatActivity {


    private EditText  signup_phone;

    private Button signup_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);


        signup_register = (Button) findViewById(R.id.signup_register);
        signup_phone = (EditText) findViewById(R.id.signup_phone);

        signup_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = signup_phone.getText().toString().trim();
                validNo(phone);
                Intent intent = new Intent(SignUpScreen.this,LoginScreen.class);
                intent.putExtra("mobile",phone);
                startActivity(intent);
                Toast.makeText(SignUpScreen.this,phone,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void validNo(String phone){
        if(phone.isEmpty() || phone.length() < 9){
            signup_phone.setError("Enter a valid mobile number");
            signup_phone.requestFocus();
            return;
        }
    }

}
