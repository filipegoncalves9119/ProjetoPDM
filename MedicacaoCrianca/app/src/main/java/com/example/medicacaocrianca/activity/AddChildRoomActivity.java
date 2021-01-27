package com.example.medicacaocrianca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.model.ChildRoom;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddChildRoomActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerNames;
    private Spinner spinnerRooms;
    private Button confirmBtn;
    private Button backBtn;
     String uri;
    DatabaseReference referenceName;
    DatabaseReference referenceRoom;
    DatabaseReference reference;
    DatabaseReference referenceForUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_room);
        this.spinnerNames = findViewById(R.id.spinner_name_id);
        this.spinnerRooms = findViewById(R.id.spinner_room_id);
        this.backBtn = findViewById(R.id.back_btn_idd);
        this.confirmBtn = findViewById(R.id.save_btn_idd);
        this.referenceName = FirebaseDatabase.getInstance().getReference().child("Children");
        this.referenceRoom = FirebaseDatabase.getInstance().getReference().child("Room");
        this.reference = FirebaseDatabase.getInstance().getReference().child("ChildRoom");
        this.referenceForUri = FirebaseDatabase.getInstance().getReference();

        ValueEventListener names = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> list = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String names = data.child("fullName").getValue().toString();
                    list.add(names);
                }
                //Creates the adapter and set it's values to the list created previously
                ArrayAdapter names = new ArrayAdapter(AddChildRoomActivity.this, android.R.layout.simple_spinner_item, list);
                names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerNames.setAdapter(names);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        this.referenceName.addListenerForSingleValueEvent(names);


        ValueEventListener rooms = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> list = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    String names = data.child("number").getValue().toString();
                    list.add(names);
                }
                //Creates the adapter and set it's values to the list created previously
                ArrayAdapter arrayAdapter = new ArrayAdapter(AddChildRoomActivity.this, android.R.layout.simple_spinner_item, list);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRooms.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        this.referenceRoom.addListenerForSingleValueEvent(rooms);


        //Query to filter the uri for the chosen children
        Query query = referenceForUri.child("Children").orderByChild("fullName").equalTo("boyz");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    uri = data.child("uri").getValue().toString();
                   // Toast.makeText(AddChildRoomActivity.this, uri, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        this.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerChildRoom();
                Intent backHomeBtn = new Intent(AddChildRoomActivity.this, AdminHomeActivity.class);
                startActivity(backHomeBtn);
            }
        });

        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHomeBtn = new Intent(AddChildRoomActivity.this, AdminHomeActivity.class);
                startActivity(backHomeBtn);
            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) view).setTextColor(Color.BLACK);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void registerChildRoom() {

        String name = spinnerNames.getSelectedItem().toString();
        int roomNumb = Integer.parseInt(spinnerRooms.getSelectedItem().toString());

        ChildRoom childRoom = new ChildRoom(roomNumb, name, uri);
        reference.push().setValue(childRoom);
        Toast.makeText(AddChildRoomActivity.this, "Children added to a room successfully!", Toast.LENGTH_LONG).show();

    }

    

}