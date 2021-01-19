package com.example.medicacaocrianca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Currency;

public class MainActivity extends AppCompatActivity {

    Button sendData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendData(View view){
        Intent intent = new Intent(this, AdminHomeActivity.class);
        MainActivity.this.startActivity(intent);
    }
}