package com.example.reminder.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.graphics.Typeface;


import com.example.reminder.R;

public class EditAddReminderDialogFrag extends DialogFragment {

    Button oneTimeBtn,repeatBtn, locationBtn;
    LinearLayout repeatLL,editTagsLL,editLocationLL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.editaddreminderdialogfrag,container,false);

        oneTimeBtn = view.findViewById( R.id.oneTime_button );
        repeatBtn  = view.findViewById( R.id.repeat_button );
        locationBtn = view.findViewById( R.id.location_button );

        repeatLL =view.findViewById( R.id.repeatLL );
        editTagsLL = view.findViewById( R.id.editTagsLL );
        editLocationLL = view.findViewById( R.id.editLocationLL );

        repeatLL.setVisibility( View.GONE );
        editLocationLL.setVisibility( View.GONE );
        onUpperButtonClick();

        return view;


    }


    public void onUpperButtonClick()
    {
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
        setBg.setTypeface( setBg.getTypeface(),Typeface.BOLD );
        removeBg1.setBackground( getResources().getDrawable( R.drawable.threebuttonsbeforeclickbg ) );
        removeBg1.setTextColor(Color.BLACK );
        removeBg1.setTypeface( removeBg1.getTypeface(),Typeface.NORMAL );
        removeBg2.setBackground( getResources().getDrawable( R.drawable.threebuttonsbeforeclickbg ) );
        removeBg2.setTextColor(Color.BLACK );
        removeBg2.setTypeface( removeBg2.getTypeface(),Typeface.NORMAL );


    }
    {

    }
}
