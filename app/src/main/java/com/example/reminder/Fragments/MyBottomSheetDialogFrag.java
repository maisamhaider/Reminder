package com.example.reminder.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.reminder.R;
import com.example.reminder.interfaces.EditTextStringListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;


public class MyBottomSheetDialogFrag extends BottomSheetDialogFragment implements EditTextStringListener {

    TextView startDateTv,endDateTv,startTimeTv,endTimeTv,addRepeatTv, addAlarmTv1;
    EditText eventTitleEt,addNotesEt;
    Button saveBtn;
    String formattedDate,formattedTime;
    Long sDate,sTime,eDate,eTime,sDateAndTime,eDateAndTime;
    Switch allTimeSwitch;

    boolean isFirstFieldSelected=false;
    private boolean isStartDate = false;
    private boolean isStartTime = false;
    private boolean isAllTime;



    public static MyBottomSheetDialogFrag newInstance() {
        return new MyBottomSheetDialogFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_my_bottom_sheet_dialog, container, false );

        saveBtn = view.findViewById( R.id.event_save_btn);
        startDateTv = view.findViewById( R.id.s_date_Tv );
        endDateTv = view.findViewById( R.id.e_date_Tv );
        startTimeTv =view.findViewById( R.id.event_start_time_tv );
        endTimeTv = view.findViewById( R.id.event_end_time_tv );
        addAlarmTv1 =view.findViewById( R.id.add_alarm_tv1 );
        allTimeSwitch = view.findViewById( R.id.all_time_sb );

        addRepeatTv =view.findViewById( R.id.add_repeat_tv );
        eventTitleEt = view.findViewById( R.id.event_title_Et );
        addNotesEt = view.findViewById( R.id.add_notes_Et );

        eventAddingFun();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

    }


    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(year, monthOfYear, dayOfMonth);


            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
            formattedDate = sdf.format(c.getTime());
            if (isStartDate)
            {

                sDate = c.getTimeInMillis();
                Toast.makeText( getContext(), sDate.toString(), Toast.LENGTH_SHORT ).show();
                startDateTv.setText( formattedDate );
            }
            else
            if(!isStartDate)
            {
                eDate = c.getTimeInMillis();
                endDateTv.setText( formattedDate );
            }
        }
    };

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calendar =  Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE,minute );
            SimpleDateFormat sformat = new SimpleDateFormat( "h:mm a" );

            formattedTime = sformat.format( calendar.getTime() );

            if (isStartTime) {
                sTime = calendar.getTimeInMillis();
                startTimeTv.setText(formattedTime);
            } else if (!isStartTime)
            {
                eTime= calendar.getTimeInMillis();
                endTimeTv.setText( formattedTime );
            }
        }
    };


    // set event's Title,start date,end date,start time, end Time, and other event_bottom_sheet views
    public void eventAddingFun()
    {
        Calendar calendar = Calendar.getInstance();
        final int year =  calendar.get( Calendar.YEAR );
        final int month = calendar.get( Calendar.MONTH );
        final int day = calendar.get( Calendar.DAY_OF_MONTH );
        final int hour = calendar.get( Calendar.HOUR_OF_DAY );
        final int minutes = calendar.get( Calendar.MINUTE );

        startDateTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = true;
                DatePickerDialog d = new DatePickerDialog(getContext(), mDateSetListener, year, month, day);
                d.show();

            }
        } );

        endDateTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = false;
                DatePickerDialog d = new DatePickerDialog(getContext(), mDateSetListener, year, month, day);
                d.show();
            }
        } );


        startTimeTv.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                isStartTime = true;
                TimePickerDialog dpd = new TimePickerDialog(getContext(), (timeSetListener), hour, minutes,false);
                dpd.show();

            }
        } );

        endTimeTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartTime = false;
                TimePickerDialog dpd = new TimePickerDialog(getContext(), (timeSetListener), hour, minutes,false);
                dpd.show();

            }
        } );

        allTimeSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    isAllTime = true;
                }
                else
                    isAllTime =false;
            }
        } );

        addAlarmTv1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBShAlarmRcDialog();
                isFirstFieldSelected = true;
            }
        } );


        addRepeatTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBShRepeatRvDialog();
                isFirstFieldSelected = false;
            }
        } );
        saveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEventSaveClick();
            }
        } );

    }


    public void onEventSaveClick() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat( "EEE, d MMM yyyy" ),
                timeFormat = new SimpleDateFormat( "h:mm a" );




        String evntTitle, alarm, repeat, notes;
        long startDate, endDate;

        TimeZone timeZone = TimeZone.getDefault();

        if (eventTitleEt.length() == 0) {
            eventTitleEt.setText( "Untitled" );
        }
        if (startDateTv.length()==0)
        {
            final int setDay = calendar.get( Calendar.DAY_OF_MONTH ) + 1;
            final int setMonth = calendar.get( Calendar.MONTH );
            final int setYear = calendar.get( Calendar.YEAR );

            String s = String.format( "%d/%d/%d", setDay, setMonth, setYear );
        }



        evntTitle = eventTitleEt.getText().toString();
        startDate = sDate;
        endDate = eDate;
        alarm = addAlarmTv1.getText().toString();
        repeat = addRepeatTv.getText().toString();
        if (addNotesEt.length()==0)
        {
            addNotesEt.setText( "Empty");
        }
        notes = addNotesEt.getText().toString();

        ContentResolver contentResolver = Objects.requireNonNull( getContext() ).getContentResolver();
        ContentValues values = new ContentValues(  );
        values.put( CalendarContract.Events.CALENDAR_ID,1 );
        values.put( CalendarContract.Events.TITLE,evntTitle );
        values.put( CalendarContract.Events.DTSTART,startDate );
        values.put( CalendarContract.Events.DTEND,endDate);
        values.put( CalendarContract.Events.EVENT_TIMEZONE,timeZone.getID() );
        values.put( CalendarContract.Events.DESCRIPTION,notes);
        values.put( CalendarContract.Events.ALL_DAY,this.isAllTime );

        contentResolver.insert( CalendarContract.Events.CONTENT_URI,values );
    }

    public void showBShAlarmRcDialog()
    {
        BottomShAlarmRvFragDialog dialogFragment = new BottomShAlarmRvFragDialog();
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");


    }
    public void showBShRepeatRvDialog()
    {
        BShRepeatFragDiaglog dialogFragment = new BShRepeatFragDiaglog();
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");

    }


    @Override
    public void mystring(String ss) {
        Toast.makeText( getContext(), "Selected "+ss, Toast.LENGTH_SHORT ).show();
        if
        (isFirstFieldSelected){
            addAlarmTv1.setText( ss );
        }else{
            addRepeatTv.setText( ss );
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
