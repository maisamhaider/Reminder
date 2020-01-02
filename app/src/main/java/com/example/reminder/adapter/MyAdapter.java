package com.example.reminder.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.interfaces.MyItemClickListener;
import com.example.reminder.models.MyModel;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

   private final Context context;
   private ArrayList<MyModel> myModelList;

    public MyAdapter(Context context, ArrayList<MyModel> myModelList) {
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


        Log.d("onBindViewHoler ", myModelList.size() + "");
        holder.notes_TextView.setText(myModelList.get(position).getNotes());
        holder.date_textView.setText(myModelList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return(null != myModelList?myModelList.size():0);

    }


    public void notifyData(ArrayList<MyModel> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myModelList = myList;
        notifyDataSetChanged();
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
