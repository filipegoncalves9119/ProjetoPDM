package com.example.medicacaocrianca.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddTeacherActivity extends AppCompatActivity {

    private Button backBtn;
    private Button confirmBtn;
    private EditText fullName;
    private EditText email;
    private EditText password;
    private EditText passwordConfirm;
    private TextView address;
    private FirebaseAuth firebase;
    DatabaseReference reference;
    private FirebaseFirestore db;
    private Button getLocation;
    private String lat;
    private String lng;
    private final int REQUEST_MAPS_CODE = 1101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        this.backBtn = findViewById(R.id.back_btn_id);
        this.confirmBtn = findViewById(R.id.save_btn_id);
        this.fullName = findViewById(R.id.teacher_name_text_id);
        this.email = findViewById(R.id.teacher_email_text_id);
        this.password = findViewById(R.id.teacher_password_text_id);
        this.passwordConfirm = findViewById(R.id.teacher_password_confirm_text_id);
        this.getLocation = findViewById(R.id.btn_get_location_id);
        this.address = findViewById(R.id.coordinates_id);
        this.firebase = FirebaseAuth.getInstance();
        this.reference = FirebaseDatabase.getInstance().getReference().child("Teacher");
        this.db = FirebaseFirestore.getInstance();

        //maps request
        if (ContextCompat.checkSelfPermission(AddTeacherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddTeacherActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_MAPS_CODE);
        }

        this.backBtn.setOnClickListener(v -> finish());

        /**
         * Listener for confirm button to call for registerToAuth method
         */
        this.confirmBtn.setOnClickListener(v -> {
            registerToAuth();

        });

        /**
         * Listener for get location
         * starts maps activity
         */

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(AddTeacherActivity.this, MapsActivity.class), REQUEST_MAPS_CODE);

            }
        });
    }


    /**
     * Method to register a user to authentication database
     * checks for null fields and set error message if find any
     * if successfully register a user calls for register method
     */
    private void registerToAuth() {
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        String passConfirm = passwordConfirm.getText().toString();
        String name = fullName.getText().toString();
        String address = this.address.getText().toString();

        // check for null fields
        if (TextUtils.isEmpty(name)) {
            this.fullName.setError(getString(R.string.enterName));
            return;
        } else if (TextUtils.isEmpty(mail)) {
            this.email.setError(getString(R.string.enter_email));
            return;
        } else if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(passConfirm)) {
            this.password.setError(getString(R.string.enter_password));
            return;
        } else if (!pass.equals(passConfirm)) {
            this.password.setError(getString(R.string.passMatch));
            this.passwordConfirm.setError(getString(R.string.passMatch));
            return;
        } else if (pass.length() < 6) {
            this.password.setError(getString(R.string.passLength));

        } else if (TextUtils.isEmpty(address)) {
            this.address.setError("Please pick an address");
            return;
        }


        //creates user in authentication database
        firebase.createUserWithEmailAndPassword(mail, passConfirm).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    register();

                }
            }
        });
    }

    private boolean isValidEmail(CharSequence charSequence) {
        return (!TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches());

    }

    /**
     * Method to add a teacher to Cloud firebase
     * creates an object of Teacher with given information
     */
    private void register() {

        Teacher teacher = new Teacher(this.fullName.getText().toString(), this.email.getText().toString(), this.address.getText().toString());

        db.collection("teacher").add(teacher).addOnSuccessListener(documentReference -> {

            Toast.makeText(AddTeacherActivity.this, getString(R.string.createdSucessTeacher), Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {

        });
    }

    /**
     * OnActivityResult method
     * gets the data from the user location coordinates and converts it to an address
     * sets the text view address
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MAPS_CODE && resultCode == RESULT_OK) {
            this.lat = data.getStringExtra("lat");
            this.lng = data.getStringExtra("long");

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(this.lat), Double.parseDouble(this.lng), 1);
                Address fullAddress = addresses.get(0);
                String street = fullAddress.getAddressLine(0);
                this.address.setText(street);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}