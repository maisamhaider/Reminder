package com.example.reminder.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.reminder.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditTask extends BottomSheetDialogFragment {

    CardView setTimeCV,tomorrow9AmCV,daily_Weekly,etcCv;
    TextView edittaskTitle,createdTextView,notesHolderTv;
    LinearLayout editTaskNotesLL;
    String taskPosition;



    public static EditTask editTaskInstence()
    {
        return new EditTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.edit_task,container,false );

        setTimeCV = view.findViewById( R.id.setTaskTimeCV );
        tomorrow9AmCV =view.findViewById( R.id.tomorrow9amCv );
        daily_Weekly = view.findViewById( R.id.setTaskRepeatCv );
        edittaskTitle = view.findViewById( R.id.taskTitle_tv );
        createdTextView = view.findViewById( R.id.createdDateTv );
        notesHolderTv = view.findViewById( R.id.holdNotesTv );
        editTaskNotesLL = view.findViewById( R.id.tapToAddNotesLL );

        notesHolderTv.setVisibility( View.GONE );
        launchReminderDialogFun();
        launchNotesDialogFragFun();
        getDataInBSHDF();



        return view;
    }


    private void launchReminderDialogFun()
    {
        final EditAddReminderDialogFrag editAddReminderDialogFrag = new EditAddReminderDialogFrag();

        setTimeCV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddReminderDialogFrag.show( getActivity().getSupportFragmentManager(),"reminder dialog" );
            }
        } );
    }
    public  void launchNotesDialogFragFun()
    {
        editTaskNotesLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditTaskBSHNotesDF editTaskBSHNotesDF = new EditTaskBSHNotesDF();
                editTaskBSHNotesDF.show( getActivity().getSupportFragmentManager(),"editTaskBSHNotesDF" );
            }
        } );
    }

    public void getDataInBSHDF()
    {
        EditTaskBSHNotesDF editTaskBSHNotesDF =new EditTaskBSHNotesDF();
        Bundle bundle = new Bundle(  );
        edittaskTitle.setText( getArguments().getString("Task_Title") );
        createdTextView.setText( getArguments().getString( "Task_Created_Date" ) );
        notesHolderTv.setText( getArguments().getString( "Task_Note" ) );

        taskPosition = getArguments().getString( "task_position" );
        String isNotesHolderEmpty = notesHolderTv.getText().toString();
        bundle.putString( "FromEditTask_taskPosition",taskPosition );

        editTaskBSHNotesDF.setArguments( bundle );

        if (isNotesHolderEmpty.matches( "" ))
        {
            notesHolderTv.setVisibility( View.GONE );
            editTaskNotesLL.setVisibility( View.VISIBLE );

        }
        else
            {
                notesHolderTv.setVisibility( View.VISIBLE );
                editTaskNotesLL.setVisibility( View.GONE );
            }
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
