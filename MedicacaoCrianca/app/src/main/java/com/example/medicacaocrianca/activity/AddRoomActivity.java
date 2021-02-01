package com.example.medicacaocrianca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.model.Children;
import com.example.medicacaocrianca.model.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddRoomActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button backBtn;
    private Button saveBtn;
    private EditText roomNumber;
    private Spinner spin;
    DatabaseReference reference;
    DatabaseReference referenceToRoom;
    FirebaseFirestore db;
    FirebaseAuth firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        this.backBtn = findViewById(R.id.back_btn_id);
        this.saveBtn = findViewById(R.id.save_btn_id);
        this.roomNumber = findViewById(R.id.room_number_text_id);
        this.spin = findViewById(R.id.spinner_id);
        this.db = FirebaseFirestore.getInstance();
        this.spin.setOnItemSelectedListener(this);
        this.reference = FirebaseDatabase.getInstance().getReference().child("Teacher");
        this.referenceToRoom = FirebaseDatabase.getInstance().getReference().child("Room");
        this.firebase = FirebaseAuth.getInstance();

        //Retrieves all the names from the "Teacher" collection and add them to a list to be shown in the spinner
        db.collection("teacher").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getString("name"));
                    }
                    //adapter used to fill the spinner with a list of teacher names
                    ArrayAdapter arrayAdapter = new ArrayAdapter(AddRoomActivity.this,android.R.layout.simple_spinner_item, list);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(arrayAdapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



        /**
         * Calls on register method
         */
        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(roomNumber.getText().toString())){
                    roomNumber.setError("Insert a room number");
                }else{
                    register();
                }
            }
        });

        /**
         * Listener to go back home activity
         */
        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }

    /**
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) view).setTextColor(getResources().getColor(R.color.black));
    }

    /**
     *
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * method to Register an object of Room into database
     * gather all information from edit text and spinner selection and pushes into database
     */
    private void register() {
        List<Children> list = new ArrayList<>();
        int number = Integer.parseInt(this.roomNumber.getText().toString());
        String name = spin.getSelectedItem().toString();
        Room room = new Room(name, list, number);
        db.collection("room").add(room).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddRoomActivity.this, R.string.addRoomSuccess, Toast.LENGTH_SHORT).show();
                finish();
            }
        })  .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


}
