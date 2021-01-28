package com.example.medicacaocrianca.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.model.Children;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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
    private FirebaseFirestore db;
    private static final int REQUEST_CAMERA_CODE = 100;
    private static final int REQUEST_GALLERY_CODE = 200;
    // Create a storage reference from our app


    /**
     * onCreate method
     * used to call allChildren method
     *
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
        this.db = FirebaseFirestore.getInstance();
        // Create a storage reference from our app


        //Camera request
        if (ContextCompat.checkSelfPermission(AddChildrenActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddChildrenActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }
        //Gallery request
        if (ContextCompat.checkSelfPermission(AddChildrenActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddChildrenActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_CAMERA_CODE);
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
                upLoadToFireBase();
                //register(fullName.getText().toString(), address.getText().toString(), birthdate.getText().toString(), parent.getText().toString(), phoneNumber.getText().toString(), picture);

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
     * Method to add children to the real time firebase
     * sends the given information trough the text areas
     *
     * @param name
     * @param address
     * @param birthdate
     * @param parent
     * @param phone
     */
    private void addChildren(String name, String address, String birthdate, String parent, String phone, String uri) {

        if (!name.equals("") && !address.equals("") && !birthdate.equals("") && !parent.equals("") && !phone.equals("")) {
            Children children = new Children(name, address, birthdate, parent, phone, uri);
          db.collection("children").add(children).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
              @Override
              public void onSuccess(DocumentReference documentReference) {

              }
          })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {

                      }
                  });
        }
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
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_CODE) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            this.picture.setImageBitmap(bitmap);
        }

        if (requestCode == REQUEST_GALLERY_CODE) {
            this.picture.setImageURI(data.getData());
        }
    }


    /**
     * Method to upload picture to Storage firebase
     * using Storage reference stores a picture
     * converts an image to an array of bytes
     */
    private void upLoadToFireBase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference databaseReference = storage.getReference();
        StorageReference storageReference = databaseReference.child("images/" + this.fullName.getText().toString() + ".jpg");

        if (TextUtils.isEmpty(fullName.getText().toString())) {
            fullName.setError("Enter a name");
        } else if (TextUtils.isEmpty(address.getText().toString())) {
            address.setError("Enter an address");
        } else if (TextUtils.isEmpty(birthdate.getText().toString())) {
            birthdate.setError("Enter a birth date");
        } else if (TextUtils.isEmpty(parent.getText().toString())) {
            parent.setError("Enter the responsible parent");
        } else if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
            phoneNumber.setError("Enter phone number");
        }

        //prevents from null data to go into database
        if (!fullName.getText().toString().equals("") && !address.getText().toString().equals("") && !birthdate.getText().toString().equals("")
                && !parent.getText().toString().equals("") && !phoneNumber.getText().toString().equals("") && picture.getDrawable() != null) {

            // Get the data from an ImageView as bytes
            picture.setDrawingCacheEnabled(true);
            picture.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) picture.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            //on success image sent to data base storage creates new children with given information
                            addChildren(fullName.getText().toString(), address.getText().toString(), birthdate.getText().toString(), phoneNumber.getText().toString(), parent.getText().toString(), task.toString());
                            Toast.makeText(AddChildrenActivity.this, "Children added sucessfuly", Toast.LENGTH_SHORT).show();
                            Intent backHome = new Intent(AddChildrenActivity.this, AdminHomeActivity.class);
                            startActivity(backHome);

                        }
                    });
                }
            });
        }
    }
}