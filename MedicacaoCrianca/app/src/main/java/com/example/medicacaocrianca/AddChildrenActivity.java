package com.example.medicacaocrianca;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicacaocrianca.dbobjects.Children;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddChildrenActivity extends AppCompatActivity {

    private EditText fullName;
    private EditText address;
    private EditText birthdate;
    private EditText parent;
    private EditText phoneNumber;
    private Button confirmBtn;
    private DatabaseReference reference;


    /**
     * onCreate method
     * used to call allChildren method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_children);

        this.fullName = findViewById(R.id.full_name_text_id);
        this.address = findViewById(R.id.address_text_id);
        this.birthdate = findViewById(R.id.bithdate_text_id);
        this.parent = findViewById(R.id.parent_text_id);
        this.phoneNumber = findViewById(R.id.number_text_id);
        this.confirmBtn = findViewById(R.id.save_btn_id);
        this.reference = FirebaseDatabase.getInstance().getReference().child("Children");

        this.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChildren(fullName.getText().toString(),address.getText().toString(), birthdate.getText().toString(), parent.getText().toString(), phoneNumber.getText().toString());
            }
        });

    }

    /**
     * Method to add children to the real time firebase
     * sends the given information trough the text areas
     * @param name
     * @param address
     * @param birthdate
     * @param parent
     * @param phone
     */
    private void addChildren(String name, String address, String birthdate, String parent, String phone) {
        Children children = new Children(name, address, birthdate, parent, phone);
        this.reference.push().setValue(children);
        Toast.makeText(AddChildrenActivity.this,"Children added sucefully!", Toast.LENGTH_LONG).show();
    }
}