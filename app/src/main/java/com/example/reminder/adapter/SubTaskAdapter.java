package com.example.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.models.MySubTaskModel;

import java.util.List;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.MySubTaskHolder> {

    Context context;
    String[] list;

    public SubTaskAdapter(Context context, String[] list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MySubTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.subtasksitemslayout,parent,false );
        return new MySubTaskHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MySubTaskHolder holder, int position) {
        holder.subTaskTitleTextView.setText( list[position] );

    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    class MySubTaskHolder extends RecyclerView.ViewHolder
    {
        CheckBox subTaskCheckBox;
        TextView subTaskTitleTextView;
        ImageView subTaskImageView;

        public MySubTaskHolder(@NonNull View itemView) {
            super( itemView );

            subTaskTitleTextView = itemView.findViewById( R.id.subTask_TextView );
            subTaskCheckBox = itemView.findViewById( R.id.subTask_checkBox );
            subTaskImageView = itemView.findViewById( R.id.subTask_RImageView );
        }
    }
}
