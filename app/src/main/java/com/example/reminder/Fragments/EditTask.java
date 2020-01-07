package com.example.reminder.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.reminder.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditTask extends BottomSheetDialogFragment {

    CardView setTimeCV,tomorrow9AmCV,daily_Weekly,etcCv;


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

        lounchReminderDialogFun();

        return view;
    }

    private void lounchReminderDialogFun()
    {
        final EditAddReminderDialogFrag editAddReminderDialogFrag = new EditAddReminderDialogFrag();

        setTimeCV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddReminderDialogFrag.show( getActivity().getSupportFragmentManager(),"reminder dialog" );
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
