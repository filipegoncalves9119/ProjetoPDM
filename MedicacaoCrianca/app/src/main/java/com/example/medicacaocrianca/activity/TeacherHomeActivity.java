package com.example.medicacaocrianca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.adapter.ChildAdapter;
import com.example.medicacaocrianca.model.Children;
import com.google.android.gms.tasks.Continuation;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeacherHomeActivity extends AppCompatActivity {

    private TextView teacherName;
    private TextView roomNumber;
    private FirebaseUser user;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private ChildAdapter adapter;
    private List<Children> list = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int number;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.roomNumber = findViewById(R.id.text_view_room_n_id);
        this.teacherName = findViewById(R.id.nome_id);
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.recyclerView = findViewById(R.id.recycler_view);

        //method to get the user information and display on the view
        setCurrentUserInfo();

    }


    @Override
    protected void onStart() {

        loadChildren();
        super.onStart();
    }

    /**
     * Method to return e-mail
     * @return authenticated email user
     */
    private String getUserEmail() {
        return this.user.getEmail();

    }

    /**
     * Method to initialize the adapter passing the context and the list of childrens
     */
    public void loadChildren() {

        this.adapter = new ChildAdapter(this.getApplicationContext(), this.list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(this.adapter);
    }


    /**
     * Method to query the database on Teacher collection and search for the email used on the authentication
     * Continue with task to get the teacher current room
     * on Complete previous task queries for the children that belong to the room
     * fills list to be shown in the recycler view
     * gets the children name according and sets it to the textView
     */
    private void setCurrentUserInfo() {

        db.collection("teacher").whereEqualTo("email", getUserEmail()).get().continueWithTask(new Continuation<QuerySnapshot, Task<QuerySnapshot>>() {
            @Override
            public Task<QuerySnapshot> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                String field = (String) task.getResult().getDocuments().get(0).get("name");
                teacherName.setText(field);
                return db.collection("teacher").whereEqualTo("name", field).get();
            }
        }).continueWithTask(task -> {
            String name = (String) (task.getResult().getDocuments().get(0).get("name"));
            db.collection("room").whereEqualTo("teacher", name).get().addOnCompleteListener(task1 -> {
              this.number = task1.getResult().getDocuments().get(0).getDouble("number").intValue();
                String num = number+"";
                roomNumber.setText(num);
            });
            return db.collection("room").whereEqualTo("number",this.number).get();

        }).addOnCompleteListener(task -> {
            db.collection("childroom").whereEqualTo("roomNumber",this.number).get().addOnCompleteListener(task12 -> {
                for(DocumentSnapshot documentSnapshot : task12.getResult()){
                    Children children = new Children();
                    children.setFullName(documentSnapshot.get("name").toString());
                    children.setPhoneNumber(documentSnapshot.get("phoneNum").toString());
                    list.add(children);
                }
                adapter.notifyDataSetChanged();
            });
        });

    }
}