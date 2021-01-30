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
    private TextView displayPill;
    private Button selectTime;
    private ImageView photo;
    private Button confirmBtn;
    private SelectedChildAdapter adapter;
    private RecyclerView recyclerView;
    private List<Children> list;
    private String name;


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

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        String picture = intent.getStringExtra("picture");
        displayName.setText(name);
        Picasso.get().load(picture).into(photo);



       selectTime.setOnClickListener(v -> {
           showTimePickerDialog(v);

       });

       confirmBtn.setOnClickListener(v -> {
           String pill = displayPill.getText().toString();
           String timer = displayTime.getText().toString();

           Children children = new Children();
           children.setId(0);
           children.setFullName(name);
           children.setTime(timer);
           children.setPills(pill);

           ChildrenDatabase.getInstance(getApplicationContext()).childrenDao().inset(children);
           finish();
       });

    }

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

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            displayTime.setText(hourOfDay +":"+minute);

        }
    }


    @Override
    protected void onStart() {
        updateList();
        super.onStart();
    }

    private void updateList(){
        this.list = ChildrenDatabase.getInstance(getApplicationContext())
                .childrenDao()
                .get(name);


        this.adapter = new SelectedChildAdapter(this.getApplicationContext(), this.list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(this.adapter);
        adapter.notifyDataSetChanged();
    }

    private void updateTime(Calendar calendar){
        String time = "Alarm set for: ";
        time += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar);
    }

    private void startAlarm(Calendar calendar){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
       // Intent intent = new Intent(this, AlertReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
        //alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }



}