package com.example.reminder.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.models.MyModel;
import com.example.reminder.models.MyModel2;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyHolder> {

   private final Context context;
   private List<MyModel2> myModelList;

    public MyAdapter2(Context context, List<MyModel2> myModelList) {
        this.context = context;
        this.myModelList = myModelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listindividualitenlayout, null);
        // create ViewHolder
        MyHolder myHolder = new MyHolder(itemLayoutView);
        return myHolder;    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.notes_TextView.setText(myModelList.get(position).getNotes());
        holder.date_textView.setText(myModelList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return myModelList.size();

    }




    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView notes_TextView,date_textView;
        RelativeLayout mainLayout;
        public MyHolder(@NonNull final View itemView) {
            super(itemView);

            notes_TextView = itemView.findViewById(R.id.noteTextView);
            date_textView = itemView.findViewById(R.id.dateTextView);

            mainLayout = (RelativeLayout)itemView.findViewById(R.id.mainindivduallayout);
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Position:" + Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();
                }
            });

        }



    }



}
