package com.example.swipe_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.swipe_it.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
ActivityLoginBinding binding;
FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth=FirebaseAuth.getInstance();


        Animation slideDown = AnimationUtils.loadAnimation(this,R.anim.slide_down);
        binding.waveView.setAnimation(slideDown);

        binding.showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    binding.passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password;
                email=binding.emailEdt.getText().toString().trim();
                password=binding.passwordEdt.getText().toString().trim();
                if (email.isEmpty()){
                    binding.emailEdt.setError("Empty Not Allowed");
                }else {
                    email=binding.emailEdt.getText().toString().trim();
                }
                if (password.isEmpty()){
                    binding.passwordEdt.setError("Empty Not Allowed");
                }else {
                    password=binding.passwordEdt.getText().toString().trim();
                }
                loginUser(email,password);
            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Sign_Up.class);
                startActivity(intent);
            }
        });
    }


    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Login.this, "Successfully Login", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, ""+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}