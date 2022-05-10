package com.app.nEdge.ui.activities.loginActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nEdge.R;
import com.app.nEdge.ui.activities.RegistrationActivtiy.RegistrationActivity;
import com.app.nEdge.ui.activities.mainActivity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
     EditText editTextEmailPhone,editTextPassword;
     Button buttonLogin;
     TextView textViewRegistration;
     FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setListeners();
    }

    private void setListeners() {
        buttonLogin.setOnClickListener(view -> validation());
        textViewRegistration.setOnClickListener(view -> {
            Intent intent= new Intent(LoginActivity.this,RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private void validation() {
        if  (TextUtils.isEmpty(editTextEmailPhone.getText().toString())) {
            editTextEmailPhone.setError(getString(R.string.require_field));
        }
        else if  (TextUtils.isEmpty(editTextPassword.getText().toString())) {
            editTextPassword.setError(getString(R.string.require_field));
        }else{

            signInWithFirebase();
        }
    }

    private void signInWithFirebase() {
        String email=editTextEmailPhone.getText().toString();
        String password=editTextPassword.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(LoginActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();

                Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }else{
                Toast.makeText(LoginActivity.this,"No User",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        editTextEmailPhone=findViewById(R.id.editTextEmailPhone);
        editTextPassword=findViewById(R.id.editTextPassword);
        buttonLogin=findViewById(R.id.buttonLogin);
        textViewRegistration=findViewById(R.id.textViewRegistration);
        firebaseAuth= FirebaseAuth.getInstance();
    }
}