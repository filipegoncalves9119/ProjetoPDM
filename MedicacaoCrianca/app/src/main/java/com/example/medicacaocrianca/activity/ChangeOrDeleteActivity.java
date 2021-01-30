package com.example.medicacaocrianca.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.database.ChildrenDatabase;
import com.example.medicacaocrianca.model.Children;

import org.w3c.dom.Text;

import java.io.Serializable;

public class ChangeOrDeleteActivity extends AppCompatActivity implements Serializable {

    private EditText pill;
    private EditText timer;
    private Button update;
    private Button delete;
    private String picture;
    private final int REQUEST_CODE_BACK_OPEN_CHILD_SELECTED = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_or_delete);

        this.pill = findViewById(R.id.pill_id);
        this.timer = findViewById(R.id.timer_id);
        this.delete = findViewById(R.id.delete_children_id);
        this.update = findViewById(R.id.update_children_id);
        Intent intent = getIntent();

        this.pill.setText(intent.getStringExtra("pill"));
        this.timer.setText(intent.getStringExtra("time"));
        Children children = (Children) intent.getSerializableExtra("children");
        this.picture = intent.getStringExtra("image");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChildrenDatabase.getInstance(getApplicationContext()).childrenDao().delete(children);
                finish();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Children children1 = children;
                children1.setPills(pill.getText().toString());
                children1.setTime(timer.getText().toString());
                ChildrenDatabase.getInstance(getApplicationContext()).childrenDao().update(children1);
                finish();
            }
        });

    }


}