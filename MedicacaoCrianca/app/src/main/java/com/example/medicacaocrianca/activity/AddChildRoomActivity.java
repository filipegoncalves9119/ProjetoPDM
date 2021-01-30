package com.example.medicacaocrianca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.UiAutomation;
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

import com.example.medicacaocrianca.model.Children;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddChildRoomActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerNames;
    private Spinner spinnerRooms;
    private Button confirmBtn;
    private Button backBtn;
    private String uri;
    FirebaseFirestore db;
    DatabaseReference referenceForUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_room);
        this.spinnerNames = findViewById(R.id.spinner_name_id);
        this.spinnerRooms = findViewById(R.id.spinner_room_id);
        this.backBtn = findViewById(R.id.back_btn_idd);
        this.confirmBtn = findViewById(R.id.save_btn_idd);
        this.db = FirebaseFirestore.getInstance();
        this.referenceForUri = FirebaseDatabase.getInstance().getReference();


        //gets list of children from the database
        db.collection("children").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> list = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    list.add(documentSnapshot.getString("fullName"));
                }
                //Creates the adapter and set it's values to the list created previously and fill the spinner with list of children names
                ArrayAdapter names = new ArrayAdapter(AddChildRoomActivity.this, android.R.layout.simple_spinner_item, list);
                names.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerNames.setAdapter(names);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        //get the rooms registered in the database
        db.collection("room").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Integer> list = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    list.add(documentSnapshot.getDouble("number").intValue());
                }
                //Creates the adapter and set it's values to the list created previously
                ArrayAdapter arrayAdapter = new ArrayAdapter(AddChildRoomActivity.this, android.R.layout.simple_spinner_item, list);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRooms.setAdapter(arrayAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        this.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerChildRoom();
                //Intent backHome = new Intent(AddChildRoomActivity.this, AdminHomeActivity.class);
                //startActivity(backHome);
                finish();
            }
        });

        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent backHome = new Intent(AddChildRoomActivity.this, AdminHomeActivity.class);
                //startActivity(backHome);
                finish();
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


    /**
     * Method to regist a child
     * gets the chose name and room
     * creates an object of children and give the name and room as parameters and saves it in the firebase
     */
    private void registerChildRoom() {
        String name = spinnerNames.getSelectedItem().toString();

        int roomNumb = Integer.parseInt(spinnerRooms.getSelectedItem().toString());

        db.collection("children").whereEqualTo("fullName", name).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    this.uri = queryDocumentSnapshot.getString("uri");
                }
            }

            ChildRoom childRoom = new ChildRoom(roomNumb, name, uri);
            db.collection("childroom").add(childRoom).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Toast.makeText(AddChildRoomActivity.this, "Children added to a room successfully!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        });


    }

}


