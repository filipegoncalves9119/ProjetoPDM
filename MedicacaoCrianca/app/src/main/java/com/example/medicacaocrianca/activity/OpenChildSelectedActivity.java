package com.example.medicacaocrianca.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Dialog;

import android.app.TimePickerDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.adapter.ChildAdapter;
import com.example.medicacaocrianca.adapter.SelectedChildAdapter;
import com.example.medicacaocrianca.database.ChildrenDatabase;
import com.example.medicacaocrianca.model.Children;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;


public class OpenChildSelectedActivity extends AppCompatActivity {

    private TextView displayName;
    static TextView displayTime;
    static TextView displayPill;
    private Button selectTime;
    private ImageView photo;
    static Button confirmBtn;
    private SelectedChildAdapter adapter;
    private RecyclerView recyclerView;
    private List<Children> list;
    private String name;
    private String picture;
    private Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_child_selected);

        this.displayName = findViewById(R.id.child_name_id);
        this.selectTime = findViewById(R.id.select_hours_id);
        displayTime = findViewById(R.id.text_display_hours_id);
        this.photo = findViewById(R.id.image_for_pic);
        this.confirmBtn = findViewById(R.id.confirm_btn_children_add_pill);
        this.displayPill = findViewById(R.id.text_pill_id);
        this.recyclerView = findViewById(R.id.selected_child_recycler_id);
        this.confirmBtn.setEnabled(false);
        this.extras = getIntent().getExtras();


        this.name = extras.getString("name");
        this.picture = extras.getString("picture");

        this.displayName.setText(this.name);

        //loads picture
        Picasso.get().load(picture).into(this.photo);

        //calls for time picker
       selectTime.setOnClickListener(v -> {
           showTimePickerDialog(v);
       });

       //Listener for the confirmation button, adds a new children to the Room database.
       confirmBtn.setOnClickListener(v -> {
           String pill = displayPill.getText().toString();
           String timer = displayTime.getText().toString();

           Children children = new Children();
           children.setId(0);
           children.setFullName(this.name);
           children.setTime(timer);
           children.setPills(pill);

           ChildrenDatabase.getInstance(getApplicationContext()).childrenDao().inset(children);
           finish();
       });

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
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
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

            displayTime.setText(hour +":"+min);

            if(displayTime != null && !displayPill.getText().equals("")){
                confirmBtn.setEnabled(true);
            }
        }
    }

    /**
     * calls for update list method
     */
    @Override
    protected void onStart() {
        Picasso.get().load(picture).into(this.photo);
        updateList();
        super.onStart();
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
        Picasso.get().load(picture).into(this.photo);
        this.adapter.notifyDataSetChanged();
    }

    private void updateTime(Calendar calendar){
        String time = getString(R.string.alarmSet);
        time += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar);
    }

    private void startAlarm(Calendar calendar){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
       // Intent intent = new Intent(this, AlertReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
        //alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }



}