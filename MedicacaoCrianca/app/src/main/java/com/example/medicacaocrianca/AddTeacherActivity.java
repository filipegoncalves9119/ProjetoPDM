package com.example.medicacaocrianca;

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

import com.example.medicacaocrianca.dbobjects.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.Charset;

public class AddTeacherActivity extends AppCompatActivity {

    private Button backBtn;
    private Button confirmBtn;
    private EditText fullName;
    private EditText email;
    private EditText password;
    private EditText passwordConfirm;
    private FirebaseAuth firebase;
    private DatabaseReference reference;

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


        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(AddTeacherActivity.this, AdminHomeActivity.class);
                startActivity(backHome);
            }
        });

        this.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerToAuth();
            }
        });
    }



    private void registerToAuth(){
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        String passConfirm = passwordConfirm.getText().toString();
        String name = fullName.getText().toString();

        if(TextUtils.isEmpty(mail)){
            this.email.setError("Enter your e-mail");
            return;
        }
        else if(TextUtils.isEmpty(pass) || TextUtils.isEmpty(passConfirm)){
            this.password.setError("Enter your password");
            return;
        }
        else if(TextUtils.isEmpty(name)){
            this.fullName.setError("Enter you name");
            return;
        }
        else if(!pass.equals(passConfirm)){
            this.passwordConfirm.setError("Password doesn't match");
            return;
        }
        else if(pass.length() < 6){
            this.password.setError("Length must be 6 or more characters");
        }
        /*
        else if(isValidEmail(mail)){
            this.email.setError("Invalid e-mail");
            return;
        }
*/
        firebase.createUserWithEmailAndPassword(mail,passConfirm).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    register();
                    Toast.makeText(AddTeacherActivity.this, "Teacher created succefully", Toast.LENGTH_SHORT).show();
                    Intent confirm = new Intent(AddTeacherActivity.this, AdminHomeActivity.class);
                    startActivity(confirm);
                }
            }
        });
    }

    private boolean isValidEmail(CharSequence charSequence){
        return (!TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches());

    }
    private void register(){
        Teacher teacher = new Teacher(this.fullName.getText().toString(), this.email.getText().toString(), this.password.getText().toString());
        reference.push().setValue(teacher);

    }
}