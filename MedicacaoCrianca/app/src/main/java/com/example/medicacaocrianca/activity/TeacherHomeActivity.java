package com.example.medicacaocrianca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.adapter.ChildAdapter;
import com.example.medicacaocrianca.model.Children;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherHomeActivity extends AppCompatActivity {

    private TextView teacherName;
    private TextView roomNumber;
    private FirebaseUser user;
    private DatabaseReference database;
    DatabaseReference reference;
    private RecyclerView recyclerView;
    private ChildAdapter adapter;
    private List<Children> childrenList = new ArrayList<>();
    private List<Children> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.roomNumber = findViewById(R.id.text_view_room_n_id);
        this.teacherName = findViewById(R.id.nome_id);
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.database = FirebaseDatabase.getInstance().getReference();
        this.recyclerView = findViewById(R.id.recycler_view);
        //roomNumber.setText("1");
        setTeacherName();
        setRoomNumber();
        getChildrenByRoom();

    }

    public void loadChildren() {

        //listar crianças
        Children children1 = new Children();
        children1.setAddress("aaa");
        children1.setBirthDate("10/10/2000");
        children1.setFullName("joao estevacio");

        childrenList.add(children1);

        Children children2 = new Children();
        children2.setAddress("aaa");
        children2.setBirthDate("10/10/2000");
        children2.setFullName("joao estevacio");

        childrenList.add(children2);

        //exibir os garotos no reyclerview

        List<Children> children = new ArrayList<>();
        children = childrenList;

        //configurar adatper
        adapter = new ChildAdapter(list);

        //confiogurar recyclervie
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {

        Log.i("testar", list.toString());
        loadChildren();
        super.onStart();
    }

    /**
     * Method to return e-mail
     *
     * @return authenticated email user
     */
    private String getUserEmail() {
        return this.user.getEmail();

    }

    /**
     * Method to query the database on Teacher collection and search for the email used on the authentication
     * gets the children name according and sets it to the textView
     */

    private void setTeacherName() {
        Query query = database.child("Teacher").orderByChild("email").equalTo(getUserEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String name = data.child("name").getValue().toString();
                    teacherName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*
    private void setTeacherName(){
        database.child("Teacher").orderByChild("email").equalTo(getUserEmail()).get().continueWithTask(new Continuation<DataSnapshot, Task<Query>>() {
            @Override
            public Task<Query> then(@NonNull Task<DataSnapshot> task) throws Exception {
                String a = "asda";
                Toast.makeText(TeacherHomeActivity.this,a,Toast.LENGTH_LONG).show();

            }
        }).addOnSuccessListener(this, new OnSuccessListener<Query>() {
            @Override
            public void onSuccess(Query query) {
                teacherName.setText(query.equalTo("name").toString());
                String a = teacherName.getText().toString();
                Toast.makeText(TeacherHomeActivity.this,a,Toast.LENGTH_LONG).show();
            }
        });


    }
    */

    private void setRoomNumber() {
        Query query = database.child("Room").orderByChild("teacher").equalTo(teacherName.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String number = data.child("number").getValue().toString();
                    roomNumber.setText(number);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChildrenByRoom() {
        Query query = database.child("ChildRoom").orderByChild("roomNumber").equalTo(Integer.parseInt(roomNumber.getText().toString()));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Children children = new Children();
                    children.setFullName(data.child("name").getValue().toString());

                    list.add(children);
                    Log.i("FIREBASE-fire", data.child("name").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}