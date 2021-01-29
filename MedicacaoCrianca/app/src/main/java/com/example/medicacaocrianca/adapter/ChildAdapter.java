package com.example.medicacaocrianca.adapter;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.activity.TeacherHomeActivity;
import com.example.medicacaocrianca.model.Children;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.MyViewHolder> {

    private List<Children> list;
    private Context context;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference images = storageReference.child("images");


    public ChildAdapter(Context context, List<Children> list) {

        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View childrenList = LayoutInflater.from(parent.getContext()).inflate(R.layout.children_list_item, parent, false);
        return new MyViewHolder(childrenList);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Children children = list.get(position);
        holder.name.setText(children.getFullName());

        StorageReference imageReference = images.child(children.getFullName() + ".jpg");

        imageReference.getDownloadUrl().addOnSuccessListener(s -> {
            Picasso.get().load(s).into(holder.picture);
        });

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView picture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.child_name_text_id);
            picture = itemView.findViewById(R.id.image_view_child_id);

        }
    }
}
