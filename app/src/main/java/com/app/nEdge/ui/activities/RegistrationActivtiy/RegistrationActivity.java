package com.app.nEdge.ui.activities.RegistrationActivtiy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    TextView textViewRegistration,textViewLearnerLevel,textViewExpertLevel;
    EditText editTextName,editTextEmail,editTextPassword, editTextLeanerName,
    editTextExpertName;
    Button buttonSignUp,buttonSchoolCollege,buttonUniversity,
            buttonCssProfessional, buttonExpertSchoolCollege,
            buttonExpertUniversity,buttonExpertCssProfessional;
    RadioGroup radioGroup;
    RadioButton radioButtonLearner,radioButtonExpert;

    LinearLayout expertDesign,learnerDesign;

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
        buttonSignUp.setOnClickListener(view -> validation());

        radioButtonLearner.setOnClickListener(view -> {
            learnerDesign.setVisibility(View.VISIBLE);
        });
        radioButtonExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expertDesign.setVisibility(View.VISIBLE);
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
        else if  (TextUtils.isEmpty(radioButtonLearner.getText().toString())) {
            radioButtonLearner.setError(getString(R.string.require_field));
        }
        else if  (TextUtils.isEmpty(radioButtonExpert.getText().toString())) {
            radioButtonExpert.setError(getString(R.string.require_field));
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
        editTextLeanerName =findViewById(R.id.editTextLeanerName);
        editTextExpertName=findViewById(R.id.editTextExpertName);


        //Buttons
        buttonSignUp=findViewById(R.id.buttonSignUp);
        buttonCssProfessional=findViewById(R.id.buttonCssProfessional);
        buttonUniversity=findViewById(R.id.buttonUniversity);
        buttonSchoolCollege=findViewById(R.id.buttonSchoolCollege);

        //ExpertButtons
        buttonExpertSchoolCollege=findViewById(R.id.buttonExpertSchoolCollege);
        buttonExpertUniversity=findViewById(R.id.buttonExpertUniversity);
        buttonExpertCssProfessional=findViewById(R.id.buttonExpertCssProfessional);

        //firebase
        firebaseAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userCollection = db.collection("User");

        //TextView
        textViewRegistration=findViewById(R.id.textViewRegistration);
        textViewLearnerLevel=findViewById(R.id.textViewLearnerLevel);
        textViewExpertLevel=findViewById(R.id.textViewExpertLevel);

        //radioGroup
        radioGroup=findViewById(R.id.radioGroup);
        radioButtonLearner=findViewById(R.id.radioButtonLearner);
        radioButtonExpert=findViewById(R.id.radioButtonExpert);

        //linerLayoutForDesigns
       learnerDesign=findViewById(R.id.leanerDesign);
        learnerDesign.setVisibility(View.GONE);
       expertDesign=findViewById(R.id.expertDesign);
        expertDesign.setVisibility(View.GONE);
    }
}