package com.example.reminder.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reminder.R;
import com.example.reminder.adapter.BottomShAlarmRVFragDialogAdapter;
import com.example.reminder.models.BottomShAlarmRVFragDialogModel;
import com.example.reminder.interfaces.EditTextStringListener;

import java.util.ArrayList;

public class BShRepeatFragDiaglog extends DialogFragment {

    RecyclerView recyclerView;
    BottomShAlarmRVFragDialogAdapter adapter;
    private EditTextStringListener mEditTextStringListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach( context );
        if (context instanceof EditTextStringListener){
            mEditTextStringListener = (EditTextStringListener) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_bsh_repeat_frag_diaglog, container, false );
        
        recyclerView =view.findViewById( R.id.bsh_repeat_rv );
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
        ArrayList<BottomShAlarmRVFragDialogModel>list =new ArrayList<>(  );
        list.add( new BottomShAlarmRVFragDialogModel( "Never" ) );
        list.add( new BottomShAlarmRVFragDialogModel( "Every day" ) );
        list.add( new BottomShAlarmRVFragDialogModel( "Every week" ) );
        list.add( new BottomShAlarmRVFragDialogModel( "Every 2 weeks" ) );
        list.add( new BottomShAlarmRVFragDialogModel( "Every month" ) );
        list.add( new BottomShAlarmRVFragDialogModel( "Every year" ) );


        if (mEditTextStringListener != null) {
        adapter = new BottomShAlarmRVFragDialogAdapter( BShRepeatFragDiaglog.this,list,"BShRepeatFragDiaglog",mEditTextStringListener);
        recyclerView.setAdapter( adapter );
        adapter.notifyDataSetChanged();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mEditTextStringListener != null) {
            mEditTextStringListener=null;
        }
    }

}
