package com.example.medicacaocrianca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicacaocrianca.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private Button signIn;
    private EditText email;
    private EditText password;
    private FirebaseAuth firebaseAuth;


    /**
     *  onCreate method
     *  used to call login method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.signIn = findViewById(R.id.login_btn_id);
        this.email = findViewById(R.id.email_text_id);
        this.password = findViewById(R.id.password_text_id);
        this.firebaseAuth = FirebaseAuth.getInstance();

        this.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    /**
     * Method to log in
     * This method uses email and password authentication through firebase data
     * Depending on which e-mail is used to sign in it redirects to different activities
     */
    private void login() {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if(TextUtils.isEmpty(email)){
            this.email.setError(getString(R.string.enter_email));
            return;
        }
        else if(TextUtils.isEmpty(password)){
            this.password.setError(getString(R.string.enter_password));
            return;
        }


        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(email.equals("admin@gmail.com")){
                        Toast.makeText(MainActivity.this, getString(R.string.successAdmin) ,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this, getString(R.string.succesLogin) ,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, TeacherHomeActivity.class);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(MainActivity.this, getString(R.string.incorrectData) ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}