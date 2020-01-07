package com.example.reminder.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.reminder.R;

public class EditAddReminderDialogFrag extends DialogFragment {

    Button oneTimeBtn,repeatBtn,locationSoonBtn;
    LinearLayout repeatLL,editTagsLL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.editaddreminderdialogfrag,container,false);

        oneTimeBtn = view.findViewById( R.id.oneTime_button );
        repeatBtn  = view.findViewById( R.id.repeat_button );
        locationSoonBtn = view.findViewById( R.id.location_button );

        repeatLL =view.findViewById( R.id.repeatLL );
        editTagsLL = view.findViewById( R.id.editTagsLL );

        repeatLL.setVisibility( View.GONE );

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
                changeButtonBg(oneTimeBtn,repeatBtn,locationSoonBtn);
            }
        } );

        repeatBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonBg(repeatBtn,oneTimeBtn,locationSoonBtn);
                editTagsLL.setVisibility( View.GONE );
                repeatLL.setVisibility( View.VISIBLE );
            }
        } );
        locationSoonBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonBg(locationSoonBtn,oneTimeBtn,repeatBtn);
            }
        } );
    }
    @SuppressLint("ResourceAsColor")
    public void changeButtonBg(Button setBg, Button removeBg1, Button removeBg2)
    {
        setBg.setBackground( getResources().getDrawable( R.drawable.threebuttonsafterclickbg) );
        setBg.setTextColor( R.color.colorWhite );
        removeBg1.setBackground( getResources().getDrawable( R.drawable.threebuttonsbeforeclickbg ) );
        removeBg1.setTextColor( R.color.colorBlack );
        removeBg2.setBackground( getResources().getDrawable( R.drawable.threebuttonsbeforeclickbg ) );
        removeBg2.setTextColor( R.color.colorBlack );

    }
    {

    }
}
