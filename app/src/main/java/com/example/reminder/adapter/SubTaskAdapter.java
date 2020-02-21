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
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.interfaces.MyItemClickListener;
import com.example.reminder.models.MySubTaskModel;

import java.util.List;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.MySubTaskHolder> {

    Context context;
    List<MySubTaskModel> list;
    DataBaseHelper dataBaseHelper;

    public SubTaskAdapter(Context context, List<MySubTaskModel> list, DataBaseHelper dataBaseHelper) {
        this.context = context;
        this.list = list;
        this.dataBaseHelper = dataBaseHelper;
    }

    @NonNull
    @Override
    public MySubTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.subtasksitemslayout,parent,false );
        return new MySubTaskHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final MySubTaskHolder holder, final int position) {
        holder.subTaskTitleTextView.setText( list.get( position ).getSubTaskTitle() );
        holder.subTaskRImageView.setVisibility( View.GONE );


        if (holder.subTaskCheckBox.isChecked())
        {
            holder.subTaskRImageView.setVisibility( View.VISIBLE );

        }
        else
        {
            holder.subTaskRImageView.setVisibility( View.INVISIBLE );

        }
        holder.setItemClickListener( new MyItemClickListener() {
            @Override
            public void onMyClick(View view, int pos) {
                CheckBox checkBox = (CheckBox)view;

                if (checkBox.isChecked())
                {
                    holder.subTaskRImageView.setVisibility( View.VISIBLE );
                }
                else
                    holder.subTaskRImageView.setVisibility( View.GONE );
            }
        } );
        holder.subTaskRImageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.deleteEachSubTask( list.get( position ).getId());
                list.remove( position );
                notifyDataSetChanged();
            }
        } );



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MySubTaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CheckBox subTaskCheckBox;
        TextView subTaskTitleTextView;
        ImageView subTaskRImageView;
        MyItemClickListener mItemClickListener;


        public MySubTaskHolder(@NonNull View itemView) {
            super( itemView );

            subTaskTitleTextView = itemView.findViewById( R.id.subTask_TextView );
            subTaskCheckBox = itemView.findViewById( R.id.subTask_checkBox );
            subTaskRImageView = itemView.findViewById( R.id.subTask_RImageView );
            subTaskCheckBox.setOnClickListener( this);

        }

        public void setItemClickListener(MyItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }

        @Override
        public void onClick(View v) {
        this.mItemClickListener.onMyClick( v,getLayoutPosition() );
        }
    }

}
