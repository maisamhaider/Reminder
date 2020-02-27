package com.example.reminder.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.models.CompletedTasksModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.CompletedTasksHolder>
//        implements Filterable
{

    Context context;
    List<CompletedTasksModel>list;
    List<CompletedTasksModel>fullList;
    DataBaseHelper dataBaseHelper;

    public CompletedTaskAdapter(Context context, List<CompletedTasksModel> list, DataBaseHelper dataBaseHelper)  {
        this.context = context;
        this.list = list;
        this.dataBaseHelper = dataBaseHelper;
        fullList = new ArrayList<>(  );
        fullList.addAll( list );
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
            AlertDialog.Builder builder = new AlertDialog.Builder( context );
            View view = LayoutInflater.from( context ).inflate( R.layout.deletetaskalertmessagelayout,null  );
            Button deleteBtn = view.findViewById( R.id.popupMenuAllTaskDeleletBtn );
            Button cancelBtn =  view.findViewById( R.id.popupMenuAllTaskCancelBtn );

            builder.setView( view ).setCancelable( true );
            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
            deleteBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataBaseHelper.deleteEachCompletedTask( list.get( position ).getId() );
                    list.remove( position );
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            } );

            cancelBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            } );

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
//    @Override
//    public Filter getFilter() {
//        return null;
//    }
//
//    Filter completedTasksFilter = new Filter()
//    {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<CompletedTasksModel>filterList = new ArrayList<>(  );
//            if (constraint == null || constraint.length()==0)
//            {
//                filterList.addAll( fullList );
//            }
//            else
//            {
//                String string = constraint.toString().toLowerCase();
//                for (CompletedTasksModel data : fullList) {
//                    if (data.getTaskDate().toLowerCase().startsWith( string ))
//                    {
//                        filterList.add( data );
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filterList ;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//        list.clear();
//        list.addAll( (Collection<? extends CompletedTasksModel>) results.values );
//        notifyDataSetChanged();
//        }
//    };


}
