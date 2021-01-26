package com.example.medicacaocrianca.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.model.Children;

import java.util.List;


public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.MyViewHolder> {

    private List<Children> list;


    public ChildAdapter(List<Children> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View childrenList = LayoutInflater.from(parent.getContext()).inflate(R.layout.children_list_item, parent,false);
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
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView picture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.child_name_text_id);
            picture = itemView.findViewById(R.id.image_view_child_id);



        }


    }


}
