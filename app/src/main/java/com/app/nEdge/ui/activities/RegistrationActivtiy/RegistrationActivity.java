package com.app.nEdge.ui.activities.RegistrationActivtiy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nEdge.R;
import com.app.nEdge.models.Users;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity {
    TextView textViewRegistration,textViewStudentLevelOptions,textViewExpertLevelOptions;
    EditText editTextName,editTextEmail,editTextPassword,editTextPhoneNumber;

    Button buttonSignUp;
    Spinner spinnerRegister_as, spinnerStudentLevelOptions,spinnerExpertLevelOptions;

    FirebaseAuth firebaseAuth;
    String userId;
    private FirebaseFirestore db;
    private CollectionReference userCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        setListeners();

    }

    private void setListeners() {
    buttonSignUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            validation();
        }
    });
    }


    private void validation() {
        if  (TextUtils.isEmpty(editTextName.getText().toString())) {
            editTextName.setError(getString(R.string.require_field));
        }

        else if  (TextUtils.isEmpty(editTextEmail.getText().toString())) {
            editTextEmail.setError(getString(R.string.require_field));

        }  else if  (TextUtils.isEmpty(editTextPassword.getText().toString())) {
            editTextPassword.setError(getString(R.string.require_field));
        }
        else if  (TextUtils.isEmpty(editTextPhoneNumber.getText().toString())) {
            editTextPhoneNumber.setError(getString(R.string.require_field));
        }

        else{
            creatingUsers();
        }
    }

    private void creatingUsers() {
        String email=editTextEmail.getText().toString();
        String password=editTextPassword.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((Task<AuthResult> task) -> {
            if(task.isSuccessful()){
                Toast.makeText(RegistrationActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                usersProfile();

            }else{
                Toast.makeText(RegistrationActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void usersProfile() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        userId= user.getUid();

        Users user1= new Users(editTextName.getText().toString(),editTextEmail.getText().toString(),
                editTextPassword.getText().toString());
        DocumentReference docRef=db.collection("User").document(userId);

        docRef.set(user1).addOnCompleteListener(task1 -> {

        });
    }

    private void initViews() {
        //EditText
        editTextName=findViewById(R.id.editTextName);
        editTextPassword=findViewById(R.id.editTextPassword);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPhoneNumber=findViewById(R.id.editTextPhoneNumber);
        //Buttons
        buttonSignUp=findViewById(R.id.buttonSignUp);
        //spinnerRegisterOptions
        spinnerRegister_as=findViewById(R.id.spinnerRegister_as);
        registerSpinner();
        //spinnerStudentOptions
        spinnerStudentLevelOptions =findViewById(R.id.spinnerStudentLevelOptions);
        spinnerStudentLevelOptions.setVisibility(View.GONE);
        StudentOptionSpinner();
        //spinnerExpertOption
        spinnerExpertLevelOptions=findViewById(R.id.spinnerExpertLevelOptions);
        spinnerExpertLevelOptions.setVisibility(View.GONE);
        expertOptionSpinner();
        //firebase
        firebaseAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userCollection = db.collection("User");
        //TextView
        textViewRegistration=findViewById(R.id.textViewRegistration);
        textViewStudentLevelOptions=findViewById(R.id.textViewStudentLevelOptions);
        textViewExpertLevelOptions=findViewById(R.id.textViewExpertLevelOptions);
    }

    private void expertOptionSpinner() {
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.ExpertOptions,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExpertLevelOptions.setAdapter(adapter);
        spinnerExpertLevelOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String ExpertOptions= adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),ExpertOptions,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void StudentOptionSpinner() {
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.StudentOptions,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudentLevelOptions.setAdapter(adapter);
        spinnerStudentLevelOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String StudentOptions= adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),StudentOptions,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void registerSpinner() {
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.RegisterOptions,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegister_as.setAdapter(adapter);
        spinnerRegister_as.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String registerOptions= adapterView.getItemAtPosition(i).toString();
         if(spinnerRegister_as.getSelectedItem().toString().equals("Expert"))
         {
             spinnerExpertLevelOptions.setVisibility(View.VISIBLE);
             textViewExpertLevelOptions.setVisibility(View.VISIBLE);
             spinnerStudentLevelOptions.setVisibility(View.GONE);
             textViewStudentLevelOptions.setVisibility(View.GONE);
         }
         else if(spinnerRegister_as.getSelectedItem().toString().equals("Student"))
         {
             spinnerExpertLevelOptions.setVisibility(View.GONE);
             textViewExpertLevelOptions.setVisibility(View.GONE);
             textViewStudentLevelOptions.setVisibility(View.VISIBLE);
             spinnerStudentLevelOptions.setVisibility(View.VISIBLE);
         }
         else
         {
                    Toast.makeText(adapterView.getContext(),registerOptions,Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }
}