package com.example.reminder.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.Fragments.EditTask;
import com.example.reminder.R;
import com.example.reminder.utilities.AlarmSettingClass;
import com.example.reminder.utilities.MyTimeSettingClass;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.models.AllTasksModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AllTasksAdapter extends RecyclerView.Adapter<AllTasksAdapter.MyHolder> {

    MainActivity mainActivity;
    AlarmSettingClass alarmSettingClass;
    private final Context context;
    private List<AllTasksModel> myModelList;
    DataBaseHelper dataBaseHelper;
    FragmentManager fragmentManager;
    int checkPosition = -1;

    public AllTasksAdapter(Context context, List<AllTasksModel> myModelList, DataBaseHelper dataBaseHelper, FragmentManager fragmentManager) {
        this.context = context;
        this.myModelList = myModelList;
        this.dataBaseHelper = dataBaseHelper;
        this.fragmentManager = fragmentManager;
        alarmSettingClass = new AlarmSettingClass( context );
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
        AllTasksModel allTasksModel = new AllTasksModel(  );
        holder.notes_TextView.setText( myModelList.get( position ).getTask() );


        holder.deleteItemIv.setVisibility( View.INVISIBLE );
        holder.attachmentsIv.setVisibility( View.GONE );
        holder.subTasksIv.setVisibility( View.GONE );
        holder.repeatTaskIv.setVisibility( View.GONE );

        if (myModelList.get(position).getDate().matches(""))
        {
            holder.date_textView.setVisibility(View.GONE);
        }else {
            holder.date_textView.setVisibility(View.VISIBLE);
            holder.date_textView.setText( myModelList.get( position ).getDate() );
        }

        if (myModelList.get( position ).isCompleted()) {

            holder.completedIv.setImageResource( R.drawable.mark );
            holder.deleteItemIv.setVisibility( View.VISIBLE );
            for (int i=0; i<myModelList.size(); i++)
            {
                if (dataBaseHelper.isRepeated( String.valueOf( i ) ))
                {
                    holder.repeatTaskIv.setVisibility( View.VISIBLE);
                    holder.deleteItemIv.setVisibility( View.GONE );

                }
                else
                {
                    holder.repeatTaskIv.setVisibility( View.GONE);

                }


                // work time 😠 ! find out repeated tasks hurry up 😡
            }


        } else {
            holder.completedIv.setImageResource( R.drawable.unmark );
            holder.deleteItemIv.setVisibility( View.GONE );
            for (int i=0; i<myModelList.size(); i++)
            {
                if (dataBaseHelper.isRepeated( String.valueOf( i ) ))
                {
                    holder.repeatTaskIv.setVisibility( View.VISIBLE);

                }
                else
                {
                    holder.repeatTaskIv.setVisibility( View.GONE);

                }
                // work time 😠 ! find out repeated tasks hurry up 😡
            }


        }



        holder.completedIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pp = Integer.parseInt( myModelList.get( position ).getId() );
                alarmSettingClass.deleteRepeatAlarm( pp ); //cancel completed tasks' alarm
                if (dataBaseHelper.isCompleted( myModelList.get( position ).getId() )) {
                    Cursor cursor = dataBaseHelper.getSingleRow( String.valueOf( pp ) );
                    dataBaseHelper.upDate( myModelList.get( position ).getId(), "no","1" );
                    if (cursor.getCount() == 0) {
                    }
                    while (cursor.moveToNext()) {
                        String title = cursor.getString( 1 );
                        String reminderTime = cursor.getString( 2 );

                        if (reminderTime.matches( "" ))
                        {
                            // do nothing ahahahaha take some rest yahooo.
                        } else
                            {
                            alarmSettingClass.setOneAlarm( title, MyTimeSettingClass.getMilliFromDate( reminderTime ), pp );
                        }
                    }
                    holder.completedIv.setImageResource( R.drawable.unmark );
                    holder.deleteItemIv.setVisibility( View.GONE );


                } else {
                    dataBaseHelper.upDate( myModelList.get( position ).getId(), "yes", "0" );
                    alarmSettingClass.deleteRepeatAlarm( pp ); //cancel completed tasks' alarm
                    holder.completedIv.setImageResource( R.drawable.mark );
                    holder.deleteItemIv.setVisibility( View.VISIBLE );


                }
            }
        } );


        holder.deleteItemIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder( context );
                final View view1 = LayoutInflater.from( context ).inflate( R.layout.deletetaskalertmessagelayout, null );
                Button cancelBtn = view1.findViewById( R.id.popupMenuAllTaskCancelBtn );
                Button deleteBtn = view1.findViewById( R.id.popupMenuAllTaskDeleletBtn );
                builder.setCancelable( true);
                builder.setView( view1 );
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.show();
                cancelBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //do nothing but just dismiss dialog 😉😉
                        dialog.dismiss();
                    }
                } );
                deleteBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataBaseHelper.deleteOneTask( myModelList.get( position ).getId() );
                        dataBaseHelper.deleteSubTasks( myModelList.get( position ).getDate() );
                        dataBaseHelper.deleteAllAttachments( myModelList.get( position ).getId() );
                        int pp = Integer.parseInt( myModelList.get( position ).getId() );
                        alarmSettingClass.deleteRepeatAlarm( pp ); //cancel completed tasks' alarm
                        myModelList.remove( position );
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                } );


            }
        } );
        holder.Cv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                String position1 = myModelList.get( position ).getId();

                Cursor SRRC = dataBaseHelper.getSingleRepeatRowValue( position1 );//SRRC = Single Repeat Row// Cursor
                Cursor STC = dataBaseHelper.getSubTasks( position1 );// STC Sub Task Cursor
                Cursor CDC = dataBaseHelper.getCreatedDate( position1 );// CDC = Created Date Cursor
                Cursor NC = dataBaseHelper.getTaskNote( position1 );//NC = Note Cursor
                Cursor RDC = dataBaseHelper.getReminderDate( position1 );// RDC = reminder date cursor
                Cursor AC = dataBaseHelper.getAttachment( position1 ); // AC = Attachment Cursor

                if (SRRC.getCount() == 0) {
                }
                while (SRRC.moveToNext()) {
                    String repeatValue = SRRC.getString( 0 );
                    if (repeatValue.matches( "" )) {
                        bundle.putString( "Repeat_Value", "" );
                    } else {
                        bundle.putString( "Repeat_Value", repeatValue );
                    }

                }
                if (STC.getCount() == 0) {
                }
                while (STC.moveToNext()) {
                    String subTask = STC.getString( 0 );

                    if (subTask == null) {
                        bundle.putString( "Sub_Tasks", null );
                    } else {
                        bundle.putString( "Sub_Tasks", subTask );
                    }
                }
                if (CDC.getCount() == 0) {
                }
                while (CDC.moveToNext()) {
                    bundle.putString( "Task_Created_Date", CDC.getString( 0 ) );
                }

                if (NC.getCount() == 0) {
                }
                while (NC.moveToNext()) {
                    String isNote = NC.getString( 0 );
                    if (isNote == null) {
                        bundle.putString( "Task_Note", "" );
                    } else {
                        bundle.putString( "Task_Note", isNote );
                    }

                }
                if (RDC.getCount() == 0) {
                }
                while (RDC.moveToNext()) {
                    String isDate = RDC.getString( 0 );
                    if (isDate == null) {
                        bundle.putString( "Reminder_date", "" );
                    } else {
                        bundle.putString( "Reminder_date", isDate );
                    }
                }
                if (AC.getCount() == 0) {
                }
                while (AC.moveToNext()) {
                    String isAttachment = AC.getString( 0 );
                    if (isAttachment == null) {
                        bundle.putString( "Attachment", "" );
                    } else {
                        bundle.putString( "Attachment", isAttachment );

                    }
                }

                bundle.putString( "Task_Title", myModelList.get( position ).getTask() );
                bundle.putString( "task_position", myModelList.get( position ).getId() );
                EditTask editTask = EditTask.editTaskInstence();
                editTask.show( fragmentManager, "editTask BSheet " );
                editTask.setArguments( bundle );

            }
        } );


    }

    @Override
    public int getItemCount() {
        return myModelList.size();

    }

    public long getMilliFromDate(String dateFormat) {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm a" );
        try {
            date = formatter.parse( dateFormat );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        TextView notes_TextView, date_textView;
        ImageView deleteItemIv, attachmentsIv, subTasksIv, repeatTaskIv, completedIv;
        CardView Cv;

        public MyHolder(@NonNull final View itemView) {
            super( itemView );

            notes_TextView = itemView.findViewById( R.id.noteTextView );
            date_textView = itemView.findViewById( R.id.dateTextView );
            deleteItemIv = itemView.findViewById( R.id.deleteSingletaskIv );
            attachmentsIv = itemView.findViewById( R.id.AttachmentImageView );
            subTasksIv = itemView.findViewById( R.id.subtasksImageView );
            repeatTaskIv = itemView.findViewById( R.id.repeatTaskIv );
            completedIv = itemView.findViewById( R.id.CompletedIv );
            Cv = itemView.findViewById( R.id.mainCv );
        }
    }
}
