package com.example.medicacaocrianca.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicacaocrianca.activity.ChangeOrDeleteActivity;
import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.model.Children;

import java.util.List;

public class SelectedChildAdapter extends RecyclerView.Adapter<SelectedChildAdapter.MyViewHolder>{

    private List<Children> list;
    Context context;

    public SelectedChildAdapter(Context context, List<Children> list){
        this.context = context;
        this.list = list;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View childrenList = LayoutInflater.from(parent.getContext()).inflate(R.layout.pills_list_item, parent, false);
        return new MyViewHolder(childrenList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Children children = list.get(position);
        String pills = children.getPills();
        String time = children.getTime();

        holder.pillName.setText(pills);
        holder.time.setText(time);


        // Listener to delete pressed item from recycler view
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, ChangeOrDeleteActivity.class);
            intent.putExtra("pill", pills);
            intent.putExtra("time", time);
            intent.putExtra("children", children);
            intent.putExtra("name",children.getFullName());


            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            holder.itemView.getContext().startActivity(intent);
         //  delete(position);

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pillName;
        TextView time;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pillName = itemView.findViewById(R.id.pill_recycler_id);
            time = itemView.findViewById(R.id.timer_recycler_id);


        }
    }


}
