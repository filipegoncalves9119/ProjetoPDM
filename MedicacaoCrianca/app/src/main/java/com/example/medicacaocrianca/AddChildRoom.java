package com.example.medicacaocrianca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medicacaocrianca.dbobjects.ChildRoom;
import com.example.medicacaocrianca.dbobjects.Children;
import com.example.medicacaocrianca.dbobjects.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddChildRoom extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinnerNames;
    private Spinner spinnerRooms;
    private Button confirmBtn;
    private Button backBtn;
    DatabaseReference referenceName;
    DatabaseReference referenceRoom;
    DatabaseReference reference;

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


        ValueEventListener names = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> list = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String names = data.child("fullName").getValue().toString();
                    list.add(names);
                }
                //Creates the adapter and set it's values to the list created previously
                ArrayAdapter arrayAdapter = new ArrayAdapter(AddChildRoom.this,android.R.layout.simple_spinner_item, list);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerNames.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        referenceName.addListenerForSingleValueEvent(names);


        ValueEventListener rooms = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> list = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    String names = data.child("number").getValue().toString();
                    list.add(names);
                }
                //Creates the adapter and set it's values to the list created previously
                ArrayAdapter arrayAdapter = new ArrayAdapter(AddChildRoom.this,android.R.layout.simple_spinner_item, list);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRooms.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        referenceRoom.addListenerForSingleValueEvent(rooms);

        this.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerChildRoom();
                Intent backHomeBtn = new Intent(AddChildRoom.this, AdminHomeActivity.class);
                startActivity(backHomeBtn);
            }
        });

        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHomeBtn = new Intent(AddChildRoom.this, AdminHomeActivity.class);
                startActivity(backHomeBtn);
            }
        });
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void registerChildRoom(){

        String name = spinnerNames.getSelectedItem().toString();
        int roomNumb = Integer.parseInt(spinnerRooms.getSelectedItem().toString());
        ChildRoom childRoom = new ChildRoom(roomNumb, name);
        reference.push().setValue(childRoom);
        Toast.makeText(AddChildRoom.this,"Children added to a room successfully!", Toast.LENGTH_LONG).show();

    }
}