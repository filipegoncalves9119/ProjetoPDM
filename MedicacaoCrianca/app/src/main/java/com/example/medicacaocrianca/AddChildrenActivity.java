package com.example.medicacaocrianca;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private Button backBtn;
    private Button takePicture;
    private Button openGallery;
    private ImageView picture;
    private DatabaseReference reference;
    private static final int REQUEST_CAMERA_CODE = 100;
    private static final int REQUEST_GALLERY_CODE = 200;


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
        this.backBtn = findViewById(R.id.back_btn_id);
        this.takePicture = findViewById(R.id.picture_btn_id);
        this.picture = findViewById(R.id.image_id);
        this.openGallery = findViewById(R.id.gallery_btn_id);
        this.reference = FirebaseDatabase.getInstance().getReference().child("Children");


        //Camera request
        if(ContextCompat.checkSelfPermission(AddChildrenActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddChildrenActivity.this, new String[]{
                    Manifest.permission.CAMERA
            },REQUEST_CAMERA_CODE);
        }
        if(ContextCompat.checkSelfPermission(AddChildrenActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddChildrenActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },REQUEST_CAMERA_CODE);
        }

        this.takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePic, REQUEST_CAMERA_CODE);

            }
        });



        this.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChildren(fullName.getText().toString(),address.getText().toString(), birthdate.getText().toString(), parent.getText().toString(), phoneNumber.getText().toString());
                Toast.makeText(AddChildrenActivity.this,"Children added sucessfuly",Toast.LENGTH_SHORT).show();
                Intent backHome = new Intent(AddChildrenActivity.this, AdminHomeActivity.class);
                startActivity(backHome);

            }
        });

        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(AddChildrenActivity.this, AdminHomeActivity.class);
                startActivity(backHome);
            }
        });

        this.openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });

    }


    /**
     * Method to start the gallery pick activity
     */
    private void pickImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_CODE);
    }

    /**
     * Method used to place the taken picture into the imageview
     * Or choose between any photo in the phone's gallery
     * if the request code matches
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAMERA_CODE){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            this.picture.setImageBitmap(bitmap);
        }

        if(requestCode == REQUEST_GALLERY_CODE){
            this.picture.setImageURI(data.getData());
        }
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
        Toast.makeText(AddChildrenActivity.this,"Children added successfully!", Toast.LENGTH_LONG).show();
    }
}