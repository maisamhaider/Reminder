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
import com.example.reminder.models.CompletedTasksModel;

import java.util.List;

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.CompletedTasksHolder> {

    Context context;
    List<CompletedTasksModel>list;
    DataBaseHelper dataBaseHelper;

    public CompletedTaskAdapter(Context context, List<CompletedTasksModel> list, DataBaseHelper dataBaseHelper) {
        this.context = context;
        this.list = list;
        this.dataBaseHelper = dataBaseHelper;
    }

    @NonNull
    @Override
    public CompletedTasksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.completed_tasks_rvitem_layout,parent,false );
        return new CompletedTasksHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedTasksHolder holder, final int position) {
    holder.completedTasksTitleTv.setText( list.get( position ).getTaskTitle() );
    holder.completedTaskDateTV.setText( list.get( position ).getTaskDate() );

    holder.completedTaskDeleteIV.setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dataBaseHelper.deleteEachCompletedTask( list.get( position ).getId() );
            list.remove( position );
            notifyDataSetChanged();
        }
    } );

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CompletedTasksHolder extends RecyclerView.ViewHolder
    {
       TextView completedTasksTitleTv,completedTaskDateTV;
       ImageView completedTaskDeleteIV;

        public CompletedTasksHolder(@NonNull View itemView) {
            super( itemView );
            completedTasksTitleTv = itemView.findViewById( R.id.completedTasksTitleTv );
            completedTaskDateTV = itemView.findViewById( R.id.completedTaskDateTV );
            completedTaskDeleteIV = itemView.findViewById( R.id.completedTaskDeleteIV );

        }
    }
}
