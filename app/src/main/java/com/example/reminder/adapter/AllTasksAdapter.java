package com.example.reminder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.Fragments.EditTask;
import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.interfaces.MyItemClickListener;
import com.example.reminder.models.AllTasksModel;

import java.util.List;

public class AllTasksAdapter extends RecyclerView.Adapter<AllTasksAdapter.MyHolder> {

    MainActivity mainActivity;
    private final Context context;
    public static List<AllTasksModel> modelListForDeletion;
    private List<AllTasksModel> myModelList;
    DataBaseHelper dataBaseHelper;
    FragmentManager fragmentManager;

    public AllTasksAdapter(Context context, List<AllTasksModel> myModelList, DataBaseHelper dataBaseHelper,FragmentManager fragmentManager) {
        this.modelListForDeletion = myModelList;
        this.context = context;
        this.myModelList = myModelList;
        this.dataBaseHelper = dataBaseHelper;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.listindividualitenlayout, null );
        // create ViewHolder
        MyHolder myHolder = new MyHolder( itemLayoutView );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.notes_TextView.setText( myModelList.get( position ).getTask() );
        holder.date_textView.setText( myModelList.get( position ).getDate() );

        holder.deleteItemIv.setVisibility( View.GONE );
        holder.attachmentsIv.setVisibility( View.GONE );
        holder.subTasksIv.setVisibility( View.GONE );


        holder.setItemClickListener( new MyItemClickListener() {
            @Override
            public void onMyClick(View view, int pos) {
                CheckBox checkBox = (CheckBox)view;

                if (checkBox.isChecked())
                {
                    holder.deleteItemIv.setVisibility( View.VISIBLE );
                }
                else
                    holder.deleteItemIv.setVisibility( View.GONE );
            }
        } );
        holder.deleteItemIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.deleteOneItem(myModelList.get( position ).getId());
                myModelList.remove( position );
                notifyDataSetChanged();

            }
        } );
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle(  );


                Cursor DC =  dataBaseHelper.getCreatedDate(  myModelList.get( position ).getId() );
                Cursor NC = dataBaseHelper.getTaskNote( myModelList.get( position ).getId() );//NC = Note Cursor
                Cursor RDC = dataBaseHelper.getReminderDate( myModelList.get( position ).getId() );// RDC = reminder date cursor
                if (DC.getCount()== 0 )
                {}
                while (DC.moveToNext())
                {
                    bundle.putString( "Task_Created_Date",DC.getString( 0 )); }

                if (NC.getCount()==0)
                { }
                while (NC.moveToNext()){
                    String isNote =NC.getString( 0 );
                    if (isNote == null)
                    { bundle.putString( "Task_Note","" );}
                    else
                    {bundle.putString( "Task_Note",NC.getString( 0 ) );}

                }
                if (RDC.getCount()== 0)
                {}
                while (RDC.moveToNext())
                {
                  String isDate = RDC.getString( 0 );
                  if (isDate == null)
                  {
                      bundle.putString( "Reminder_date",null);
                  }
                  else
                      {
                          bundle.putString( "Reminder_date",RDC.getString( 0 ) );
                      }
                }


                bundle.putString( "Task_Title",myModelList.get( position ).getTask() );
                bundle.putString( "task_position", myModelList.get( position ).getId() );
                EditTask editTask = EditTask.editTaskInstence();
                editTask.show( fragmentManager,"editTask BSheet " );
                editTask.setArguments( bundle );

            }
        } );

    }

    @Override
    public int getItemCount() {
        return myModelList.size();

    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView notes_TextView, date_textView;
        ImageView deleteItemIv,attachmentsIv,subTasksIv;
        RelativeLayout mainLayout;
        CheckBox checkBox;
        MyItemClickListener mItemClickListener;

        public MyHolder(@NonNull final View itemView) {
            super( itemView );

            notes_TextView = itemView.findViewById( R.id.noteTextView );
            date_textView  = itemView.findViewById( R.id.dateTextView );
            deleteItemIv   = itemView.findViewById( R.id.deleteSingletaskIv );
            attachmentsIv  = itemView.findViewById( R.id.AttachmentImageView );
            subTasksIv     = itemView.findViewById( R.id.subtasksImageView );
            checkBox       = itemView.findViewById( R.id.checkbox );
            checkBox.setOnClickListener( this);


            mainLayout = itemView.findViewById( R.id.mainindivduallayout );
            mainLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Toast.makeText( itemView.getContext(), "Position:" + Integer.toString( getPosition() ), Toast.LENGTH_SHORT ).show();
                }
            } );

        }

        public void setItemClickListener(MyItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }


        @Override
        public void onClick(View v) {
            this.mItemClickListener.onMyClick(v,getLayoutPosition());

        }
    }
}
