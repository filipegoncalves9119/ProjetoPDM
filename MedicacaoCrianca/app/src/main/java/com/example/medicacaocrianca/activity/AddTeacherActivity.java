package com.example.medicacaocrianca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddTeacherActivity extends AppCompatActivity {

    private Button backBtn;
    private Button confirmBtn;
    private EditText fullName;
    private EditText email;
    private EditText password;
    private EditText passwordConfirm;
    private FirebaseAuth firebase;
    private DatabaseReference reference;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        this.backBtn = findViewById(R.id.back_btn_id);
        this.confirmBtn = findViewById(R.id.save_btn_id);
        this.fullName = findViewById(R.id.teacher_name_text_id);
        this.email = findViewById(R.id.teacher_email_text_id);
        this.password = findViewById(R.id.teacher_password_text_id);
        this.passwordConfirm = findViewById(R.id.teacher_password_confirm_text_id);
        this.firebase = FirebaseAuth.getInstance();
        this.reference = FirebaseDatabase.getInstance().getReference().child("Teacher");
        this.db = FirebaseFirestore.getInstance();

        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent backHome = new Intent(AddTeacherActivity.this, AdminHomeActivity.class);
               // startActivity(backHome);
                finish();
            }
        });

        this.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerToAuth();
               // finish();
            }
        });
    }


    /**
     * Method to register a user to authentication database
     * checks for null fields and set error message if find any
     * if successfully regist a user calls for register method
     */
    private void registerToAuth(){
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        String passConfirm = passwordConfirm.getText().toString();
        String name = fullName.getText().toString();

        // check for null fields
        if(TextUtils.isEmpty(name)){
            this.fullName.setError(getString(R.string.enterName));
            return;
        }

        else if(TextUtils.isEmpty(mail)){
            this.email.setError(getString(R.string.enter_email));
            return;
        }
        else if(TextUtils.isEmpty(pass) || TextUtils.isEmpty(passConfirm)){
            this.password.setError(getString(R.string.enter_password));
            return;
        }

        else if(!pass.equals(passConfirm)){
            this.password.setError(getString(R.string.passMatch));
            this.passwordConfirm.setError(getString(R.string.passMatch));
            return;
        }
        else if(pass.length() < 6){
            this.password.setError(getString(R.string.passLength));
        }

        /*
        else if(isValidEmail(mail)){
            this.email.setError("Invalid e-mail");
            return;
        }
        */

        //creates user in authentication database
        firebase.createUserWithEmailAndPassword(mail,passConfirm).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    register();

                }
            }
        });
    }

    private boolean isValidEmail(CharSequence charSequence){
        return (!TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches());

    }

    /**
     * Method to add a teacher to Cloud firebase
     */
    private void register(){

        Teacher teacher = new Teacher(this.fullName.getText().toString(), this.email.getText().toString());

        db.collection("teacher").add(teacher).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddTeacherActivity.this, getString(R.string.createdSucessTeacher) , Toast.LENGTH_SHORT).show();

                finish();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }
}