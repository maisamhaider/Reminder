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
import java.util.List;

public class BottomShAlarmRvFragDialog extends DialogFragment{



    EditTextStringListener mEditTextStringListener;
    RecyclerView recyclerView;
    BottomShAlarmRVFragDialogAdapter adapter;
    public String string;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate( R.layout.fragment_bottom_sh__alarm_list___frag__dialog, container, false );

       recyclerView =view.findViewById( R.id.BShAlarmRv );
       recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );

        List<BottomShAlarmRVFragDialogModel>list = new ArrayList<>(  );

        list.add(new BottomShAlarmRVFragDialogModel( "None" ) );
        list.add(new BottomShAlarmRVFragDialogModel( "At time of event" ) );
        list.add(new BottomShAlarmRVFragDialogModel( "5 minutes before" ) );
        list.add(new BottomShAlarmRVFragDialogModel( "15 minutes before" ) );
        list.add(new BottomShAlarmRVFragDialogModel( "30 minutes before" ) );
        list.add(new BottomShAlarmRVFragDialogModel( "1 hour before" ) );
        list.add(new BottomShAlarmRVFragDialogModel( "2 hours before" ) );
        list.add(new BottomShAlarmRVFragDialogModel( "1 day before" ) );
        list.add(new BottomShAlarmRVFragDialogModel( "2 day before" ) );
        list.add(new BottomShAlarmRVFragDialogModel( "1 week before" ) );


        if (mEditTextStringListener!=null) {
            adapter = new BottomShAlarmRVFragDialogAdapter( BottomShAlarmRvFragDialog.this, list, "BottomShAlarmRvFragDialog", mEditTextStringListener );
            recyclerView.setAdapter( adapter );
            adapter.notifyDataSetChanged();
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach( context );
        if (context instanceof EditTextStringListener){
            mEditTextStringListener = (EditTextStringListener) context;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mEditTextStringListener != null) {
            mEditTextStringListener=null;
        }
    }
}
