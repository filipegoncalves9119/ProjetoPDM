package com.example.medicacaocrianca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class TeacherHomeActivity extends AppCompatActivity {

    private TextView teacherName;
    private TextView roomNumber;
    private FirebaseUser user;
    private DatabaseReference database;
    private String saveName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        this.roomNumber = findViewById(R.id.text_view_room_n_id);
        this.teacherName = findViewById(R.id.nome_id);
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.database = FirebaseDatabase.getInstance().getReference();

        setTeacherName();
        setRoomNumber();
    }


    /**
     * Method to return e-mail
     * @return authenticated email user
     */
    private String getUserEmail(){
        return this.user.getEmail();

    }

    /**
     * Method to query the database on Teacher collection and search for the email used on the authentication
     * gets the children name according and sets it to the textView
     */

    private void setTeacherName(){
        Query query = database.child("Teacher").orderByChild("email").equalTo(getUserEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    String name = data.child("name").getValue().toString();
                    teacherName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /*
    private void setTeacherName(){
        database.child("Teacher").orderByChild("email").equalTo(getUserEmail()).get().continueWithTask(new Continuation<DataSnapshot, Task<Query>>() {
            @Override
            public Task<Query> then(@NonNull Task<DataSnapshot> task) throws Exception {
                String a = "asda";
                Toast.makeText(TeacherHomeActivity.this,a,Toast.LENGTH_LONG).show();

            }
        }).addOnSuccessListener(this, new OnSuccessListener<Query>() {
            @Override
            public void onSuccess(Query query) {
                teacherName.setText(query.equalTo("name").toString());
                String a = teacherName.getText().toString();
                Toast.makeText(TeacherHomeActivity.this,a,Toast.LENGTH_LONG).show();
            }
        });


    }
    */


    private void setRoomNumber(){
        Query query = database.child("Room").orderByChild("teacher").equalTo("Vitao machine");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    String number = data.child("number").getValue().toString();
                    roomNumber.setText(number);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}