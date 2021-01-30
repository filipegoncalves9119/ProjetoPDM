package com.example.medicacaocrianca.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medicacaocrianca.R;
import com.example.medicacaocrianca.database.ChildrenDatabase;
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
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

    /**
     * Method to delete item from recycler view
     * @param position
     */
    private void delete(int position){
       Children children = this.list.get(position);
       ChildrenDatabase.getInstance(context).childrenDao().deletee(children);
    }


}
