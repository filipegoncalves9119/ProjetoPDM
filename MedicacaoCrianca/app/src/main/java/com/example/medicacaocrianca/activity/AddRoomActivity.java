package com.example.medicacaocrianca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.model.Children;
import com.example.medicacaocrianca.model.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AddRoomActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button backBtn;
    private Button saveBtn;
    private EditText roomNumber;
    private Spinner spin;
    DatabaseReference reference;
    DatabaseReference referenceToRoom;

    FirebaseAuth firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        this.backBtn = findViewById(R.id.back_btn_id);
        this.saveBtn = findViewById(R.id.save_btn_id);
        this.roomNumber = findViewById(R.id.room_number_text_id);
        this.spin = findViewById(R.id.spinner_id);

        this.spin.setOnItemSelectedListener(this);
        this.reference = FirebaseDatabase.getInstance().getReference().child("Teacher");
        this.referenceToRoom = FirebaseDatabase.getInstance().getReference().child("Room");
        this.firebase = FirebaseAuth.getInstance();

        //Retrieves all the names from the "Teacher" collection and add them to a list to be shown in the spinner
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> list = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String names = data.child("name").getValue().toString();
                    list.add(names);
                }

                //Creates the adapter and set it's values to the list created previously
                ArrayAdapter arrayAdapter = new ArrayAdapter(AddRoomActivity.this,android.R.layout.simple_spinner_item, list);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addListenerForSingleValueEvent(valueEventListener);

        /**
         * Calls on register method
         */
        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHomeBtn = new Intent(AddRoomActivity.this, AdminHomeActivity.class);
                startActivity(backHomeBtn);
            }
        });

    }

    /**
     * method to Regist an object of Room into database
     * gather all information from edit text and spinner selection and pushes into database
     */
    private void register() {
        List<Children> list = new ArrayList<>();
        int number = Integer.parseInt(this.roomNumber.getText().toString());
        String name = spin.getSelectedItem().toString();
        Room room = new Room(name, list, number);
        referenceToRoom.push().setValue(room);
        Toast.makeText(AddRoomActivity.this,"Room added successfully!", Toast.LENGTH_LONG).show();
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

    }

    /**
     *
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
