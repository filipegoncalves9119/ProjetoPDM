package com.example.medicacaocrianca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void AddRoom (View view){
        Intent intent = new Intent(this, AddRoomActivity.class);
        AdminHomeActivity.this.startActivity(intent);
    }
}