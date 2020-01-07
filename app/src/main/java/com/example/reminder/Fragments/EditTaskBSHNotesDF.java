package com.example.reminder.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;


public class EditTaskBSHNotesDF extends DialogFragment {

    Button myNotesSaveBtn;
    ImageView myNotesBackIv;
    EditText myNotesET;

    String notes;

    DataBaseHelper dataBaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_edit_task_bshnotes_d, container, false );
        myNotesBackIv = view.findViewById( R.id.editAddNotesBackIV );
        myNotesET = view.findViewById( R.id. editAddNotesET);
        myNotesSaveBtn = view.findViewById( R.id.editNotesSaveBTn );

        final EditTaskBSHNotesDF editTaskBSHNotesDF =  new EditTaskBSHNotesDF();
        dataBaseHelper = new DataBaseHelper( getContext() );
        notes = myNotesET.getText().toString();
        final Bundle bundle = new Bundle(  );

        myNotesSaveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (notes.matches( "" ))
                {
                    dataBaseHelper.updateNotesColumn( "",getArguments().getString( "FromEditTask_taskPosition" ));
                }
                else
                {
                    dataBaseHelper.updateNotesColumn( notes,getArguments().getString( "FromEditTask_taskPosition" ));
                }

                editTaskBSHNotesDF.dismiss();

                }
        } );
        myNotesBackIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTaskBSHNotesDF.dismiss();
            }
        } );


        return view;

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
