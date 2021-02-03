package com.example.medicacaocrianca.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.medicacaocrianca.R;

import com.example.medicacaocrianca.adapter.SelectedChildAdapter;
import com.example.medicacaocrianca.database.ChildrenDatabase;
import com.example.medicacaocrianca.model.Children;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;


public class OpenChildSelectedActivity extends AppCompatActivity {

    private TextView displayName;
    static TextView displayTime;
    static TextView displayPill;
    static Button selectTime;
    private Button makeCall;
    private ImageView photo;
    static Button confirmBtn;
    private Button backActivity;
    private SelectedChildAdapter adapter;
    private RecyclerView recyclerView;
    private List<Children> list;
    private String name;
    private String picture;

    String number1;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference images = storageReference.child("images");

    private static final int REQUEST_PHONE_CALL_CODE = 1100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_child_selected);

        this.makeCall = findViewById(R.id.call_btn_id);
        this.displayName = findViewById(R.id.child_name_id);
        this.selectTime = findViewById(R.id.select_hours_id);
        displayTime = findViewById(R.id.text_display_hours_id);
        this.photo = findViewById(R.id.image_for_pic);
        this.confirmBtn = findViewById(R.id.confirm_btn_children_add_pill);
        this.displayPill = findViewById(R.id.text_pill_id);
        this.recyclerView = findViewById(R.id.selected_child_recycler_id);
        this.backActivity = findViewById(R.id.back_home_id);
        this.confirmBtn.setEnabled(false);
        Intent extras = getIntent();
        this.name = extras.getStringExtra("name");
        this.picture = extras.getStringExtra("picture");
        this.displayName.setText(name);


        //Permission to use the phone calls
        if (ContextCompat.checkSelfPermission(OpenChildSelectedActivity.this, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OpenChildSelectedActivity.this, new String[]{
                    Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL_CODE);
        }

            //Listener to call for phone call
            makeCall.setOnClickListener(v -> {
                number1 = extras.getStringExtra("phone");
                if(number1.isEmpty()){
                    Toast.makeText(getApplicationContext(),"No number to call to", Toast.LENGTH_LONG).show();
                }else{

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+351"+number1));
                    startActivity(callIntent);

                }
            });



        backActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        StorageReference imageReference = images.child(name + ".jpg");
        imageReference.getDownloadUrl().addOnSuccessListener(s -> {
            Picasso.get().load(s).into(photo);
        });


        //loads picture
        //Picasso.get().load(picture).into(this.photo);

        //calls for time picker
       selectTime.setOnClickListener(v -> {
           showTimePickerDialog(v);
       });

       //Listener for the confirmation button, adds a new children to the Room database.
       confirmBtn.setOnClickListener(v -> {
           MyAsyncTask task = new MyAsyncTask();

           String pill = displayPill.getText().toString();
           String timer = displayTime.getText().toString();
           Children children = new Children();
           children.setId(0);
           children.setFullName(name);
           children.setTime(timer);
           children.setPills(pill);
           task.execute(children);
           finish();
       });

    }

    /**
     * Method to set list with the children
     * set up adapter and recycler view
     */
    private void updateList(){
        this.list = ChildrenDatabase.getInstance(getApplicationContext()).childrenDao().get(this.name);

        this.adapter = new SelectedChildAdapter(this.getApplicationContext(), this.list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        this.recyclerView.setAdapter(this.adapter);
        //  Picasso.get().load(picture).into(this.photo);
        this.adapter.notifyDataSetChanged();
    }

    /**
     * onStart method
     * calls for update list method
     * loads the children picture
     */
    @Override
    protected void onStart() {
        StorageReference imageReference = images.child(this.name + ".jpg");

        //gets the direct link of the image
        imageReference.getDownloadUrl().addOnSuccessListener(s -> {
            Picasso.get().load(s).into(photo);
        });

        updateList();
        super.onStart();
    }


    class MyAsyncTask extends AsyncTask<Children ,Void ,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Children... children) {

            ChildrenDatabase.getInstance(getApplicationContext()).childrenDao().inset(children[0]);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(OpenChildSelectedActivity.this, getString(R.string.local_db_add_child), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }



    /**
     * time picker fragment
     * @param v
     */
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    /**
     * inner class for time picker
     * opens a time picker
     */
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // return an instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), this, hour, minute,true);
        }


        /**
         * Method to set the time on the views
         *
         * @param view view
         * @param hourOfDay hours number
         * @param minute minutes number
         */
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hour ="";
            String min ="";

            //adds a 0 to the time to prevent hours like 2:2 (instead ex.: 02:02 )
            if(hourOfDay < 10 ){
                hour =  0+""+hourOfDay;
            }else{
                hour = hourOfDay+"";
            }
            if(minute < 10){
                min = ""+0+minute;
            }else
            {
                min = minute+"";
            }

            String time = hour +":"+min;

            displayTime.setText(time);

            if(displayTime != null && !displayPill.getText().equals("")){
                confirmBtn.setEnabled(true);
                selectTime.setVisibility(View.INVISIBLE);
                confirmBtn.setVisibility(View.VISIBLE);

            }
        }
    }


}