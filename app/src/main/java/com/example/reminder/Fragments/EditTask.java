package com.example.reminder.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditTask extends BottomSheetDialogFragment {

    CardView setTimeCV,tomorrow9AmCV,daily_Weekly,etcCv;
    TextView edittaskTitle,createdTextView,notesHolderTv;
    LinearLayout editTaskNotesLL;
    String taskPosition;

    Button myNotesSaveBtn;
    ImageView myNotesBackIv;
    EditText myNotesET;

    Button oneTimeBtn,repeatBtn, locationBtn;
    LinearLayout repeatLL,editTagsLL,editLocationLL;

    DataBaseHelper dataBaseHelper;

    String notesHolderStg;

    boolean isDbNotesEmpty = false;




    public static EditTask editTaskInstence()
    {
        return new EditTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.edit_task,container,false );

        dataBaseHelper = new DataBaseHelper( getContext() );

        taskPosition = getArguments().getString( "task_position" );


        setTimeCV = view.findViewById( R.id.setTaskTimeCV );
        tomorrow9AmCV =view.findViewById( R.id.tomorrow9amCv );
        daily_Weekly = view.findViewById( R.id.setTaskRepeatCv );
        edittaskTitle = view.findViewById( R.id.taskTitle_tv );
        createdTextView = view.findViewById( R.id.createdDateTv );
        notesHolderTv = view.findViewById( R.id.holdNotesTv );
        editTaskNotesLL = view.findViewById( R.id.tapToAddNotesLL );

//        notesHolderTv.setVisibility( View.GONE );

        launchReminderDialogFun();
        launchNotesDialogFragFun();
        getDataInBSHDF();



        return view;
    }


    private void launchReminderDialogFun()
    {

        setTimeCV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddReminderDialogFrag();
            }
        } );
    }
    public  void launchNotesDialogFragFun()
    {
        editTaskNotesLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myNotesAlertDialog();
            }
        } );
    }

    public void getDataInBSHDF()
    {

        edittaskTitle.setText( getArguments().getString("Task_Title") );
        createdTextView.setText( getArguments().getString( "Task_Created_Date" ) );

        String isNotesHolderEmpty = getArguments().getString( "Task_Note" );

        if (isNotesHolderEmpty.matches( "Error" ) || isNotesHolderEmpty.matches( ""))
        {
           isDbNotesEmpty = true;
           editTaskNotesLL.setVisibility( View.VISIBLE );

        }
        else
            {
                isDbNotesEmpty=false;
                notesHolderTv.setVisibility( View.VISIBLE);
                editTaskNotesLL.setVisibility( View.GONE );
                notesHolderTv.setText( isNotesHolderEmpty );

            }

//
//
//        if (isNotesHolderEmpty.matches( "" ))
//        {
//            notesHolderTv.setVisibility( View.GONE );
//            editTaskNotesLL.setVisibility( View.VISIBLE );
//
//        }
//        else
//            {
//                notesHolderTv.setVisibility( View.VISIBLE );
//                editTaskNotesLL.setVisibility( View.GONE );
//            }
    }

    private void  editAddReminderDialogFrag()
    {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate( R.layout.editaddreminderdialogfrag,null );


        oneTimeBtn = view.findViewById( R.id.oneTime_button );
        repeatBtn  = view.findViewById( R.id.repeat_button );
        locationBtn = view.findViewById( R.id.location_button );

        repeatLL =view.findViewById( R.id.repeatLL );
        editTagsLL = view.findViewById( R.id.editTagsLL );
        editLocationLL = view.findViewById( R.id.editLocationLL );

        repeatLL.setVisibility( View.GONE );
        editLocationLL.setVisibility( View.GONE );

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() ).setView( view ).setCancelable( true );

        AlertDialog dialog = builder.create();
                dialog.show();

        oneTimeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTagsLL.setVisibility( View.VISIBLE );
                repeatLL.setVisibility( View.GONE );
                editLocationLL.setVisibility( View.GONE );
                changeButtonBg(oneTimeBtn,repeatBtn, locationBtn );
            }
        } );

        repeatBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonBg(repeatBtn,oneTimeBtn, locationBtn );
                editTagsLL.setVisibility( View.GONE );
                repeatLL.setVisibility( View.VISIBLE );
                editLocationLL.setVisibility( View.GONE );

            }
        } );
        locationBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonBg( locationBtn,oneTimeBtn,repeatBtn);
                editTagsLL.setVisibility( View.GONE );
                repeatLL.setVisibility( View.GONE );
                editLocationLL.setVisibility( View.VISIBLE );
            }
        } );
    }
    @SuppressLint("ResourceAsColor")
    public void changeButtonBg(Button setBg, Button removeBg1, Button removeBg2)
    {
        setBg.setBackground( getResources().getDrawable( R.drawable.threebuttonsafterclickbg) );
        setBg.setTextColor( Color.WHITE );
        setBg.setTypeface( setBg.getTypeface(), Typeface.BOLD );
        removeBg1.setBackground( getResources().getDrawable( R.drawable.threebuttonsbeforeclickbg ) );
        removeBg1.setTextColor(Color.BLACK );
        removeBg1.setTypeface( removeBg1.getTypeface(),Typeface.NORMAL );
        removeBg2.setBackground( getResources().getDrawable( R.drawable.threebuttonsbeforeclickbg ) );
        removeBg2.setTextColor(Color.BLACK );
        removeBg2.setTypeface( removeBg2.getTypeface(),Typeface.NORMAL );


    }

    private void myNotesAlertDialog()
    {
        LayoutInflater layoutInflater= getLayoutInflater();
        final View view = layoutInflater.inflate( R.layout.fragment_edit_task_bshnotes_d,null );
        myNotesBackIv = view.findViewById( R.id.editAddNotesBackIV );
        myNotesET = view.findViewById( R.id. editAddNotesET);
        myNotesSaveBtn = view.findViewById( R.id.editNotesSaveBTn );


        String notesFromDBStg ;
     //   notesHolderStg =


        final AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setView( view );
        builder.setCancelable( true );

        final AlertDialog myDialog = builder.create();
        myDialog.show();



        myNotesSaveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (notesHolderStg.matches( "" ))
                {

                }
                else
                {
                   boolean isUpdate = dataBaseHelper.updateNotesColumn( notesHolderStg,taskPosition);
                if (isUpdate)
                {
                    Toast.makeText( getContext(), "updated", Toast.LENGTH_SHORT ).show();
                }
                else
                {
                    Toast.makeText( getContext(), "not updated", Toast.LENGTH_SHORT ).show();

                }
                }
                myDialog.dismiss();


            }
        } );

        myNotesBackIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );



    }



    @Override
    public void onAttach(Context context) {
        super.onAttach( context );

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
