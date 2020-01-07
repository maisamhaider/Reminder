package com.example.reminder.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.reminder.R;

public class EditAddReminderDialogFrag extends DialogFragment {
    HorizontalScrollView oneTimeHSV;
    LinearLayout repeatLL;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.editaddreminderdialogfrag,container,false);

        oneTimeHSV = view.findViewById( R.id.oneTimeHorizontalScrollView );
        repeatLL =view.findViewById( R.id.repeatLL );

        return view;


    }
}
