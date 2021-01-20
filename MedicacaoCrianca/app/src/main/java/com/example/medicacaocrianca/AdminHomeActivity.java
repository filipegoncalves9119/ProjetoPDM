package com.example.medicacaocrianca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminHomeActivity extends AppCompatActivity {

    Button btnAddChildren;
    Button btnAddRoom;
    Button btnAddTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        btnAddChildren = findViewById(R.id.addChildren_btn_id);
        btnAddRoom = findViewById(R.id.addRoom_btn_id);
        btnAddTeacher = findViewById(R.id.addTeacher_btn_id);

        btnAddChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddChildren = new Intent(AdminHomeActivity.this, AddChildrenActivity.class);
                startActivity(intentAddChildren);
            }
        });

        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddRoom = new Intent(AdminHomeActivity.this, AddRoomActivity.class);
               startActivity(intentAddRoom);
            }
        });

        btnAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddTeacher = new Intent(AdminHomeActivity.this, AddTeacherActivity.class);
                startActivity(intentAddTeacher);
            }
        });


    }
}