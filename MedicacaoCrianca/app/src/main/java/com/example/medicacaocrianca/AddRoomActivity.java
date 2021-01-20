package com.example.medicacaocrianca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AddRoomActivity extends AppCompatActivity {

   private Button backBtn;
   private Button saveBtn;

    private FirebaseAuth firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        this.backBtn = findViewById(R.id.back_btn_id);
        this.saveBtn = findViewById(R.id.save_btn_id);
        this.firebase = FirebaseAuth.getInstance();

        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBackHome = new Intent(AddRoomActivity.this,AdminHomeActivity.class);
                startActivity(intentBackHome);
            }
        });

        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSave = new Intent(AddRoomActivity.this,AdminHomeActivity.class);
                Toast.makeText(AddRoomActivity.this,"Room added sucessfuly",Toast.LENGTH_SHORT).show();
                startActivity(intentSave);
            }
        });
    }
}