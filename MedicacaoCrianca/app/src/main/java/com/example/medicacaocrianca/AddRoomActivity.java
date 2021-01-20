package com.example.medicacaocrianca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class AddRoomActivity extends AppCompatActivity {

    private Button backBtn;
    private Button saveBtn;
    private TextView show;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Teacher");


    private FirebaseAuth firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        this.backBtn = findViewById(R.id.back_btn_id);
        this.saveBtn = findViewById(R.id.save_btn_id);
        this.show = findViewById(R.id.view_id);
        this.firebase = FirebaseAuth.getInstance();

        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBackHome = new Intent(AddRoomActivity.this, AdminHomeActivity.class);
                startActivity(intentBackHome);
            }
        });

        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSave = new Intent(AddRoomActivity.this, AdminHomeActivity.class);
                Toast.makeText(AddRoomActivity.this, "Room added sucessfuly", Toast.LENGTH_SHORT).show();
                startActivity(intentSave);
            }
        });


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> list = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String names = ds.child("name").getValue().toString();
                    list.add(names);
                }
                show.setText(list.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
            }
        };
        reference.addListenerForSingleValueEvent(valueEventListener);

    }
}
