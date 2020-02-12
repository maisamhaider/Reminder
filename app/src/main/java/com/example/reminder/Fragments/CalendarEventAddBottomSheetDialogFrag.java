package com.example.reminder.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.BottomShAlarmRVFragDialogAdapter;
import com.example.reminder.adapter.CalBottomShRepeatDialogAdapter;
import com.example.reminder.classes.MyTimeSettingClass;
import com.example.reminder.interfaces.EditTextStringListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;


public class CalendarEventAddBottomSheetDialogFrag extends BottomSheetDialogFragment {

    TextView startDateTv, endDateTv, addRepeatTv, addAlarmTv1;
    EditText eventTitleEt, addNotesEt;
    Button saveBtn;
    String formattedDate, formattedTime;
    Long sDate, sTime, eDate, eTime, sDateAndTime, eDateAndTime;
    Switch allTimeSwitch;
    LinearLayout fragment_my_bottom_sheet_dialog;

    boolean isFirstFieldSelected = false;
    private boolean isStartDate = true;
    private boolean isStartTime = false;
    private boolean isAllTime;

    long startDate, endDate;


    public static CalendarEventAddBottomSheetDialogFrag newInstance() {


        return new CalendarEventAddBottomSheetDialogFrag();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_my_bottom_sheet_dialog, container, false );


        Calendar ccc = Calendar.getInstance();
        ccc.add( Calendar.HOUR_OF_DAY,1 );
        SimpleDateFormat ff = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm a" );
        Calendar cccc = Calendar.getInstance();
        cccc.add( Calendar.HOUR_OF_DAY,2 );




        saveBtn = view.findViewById( R.id.event_save_btn );
        startDateTv = view.findViewById( R.id.s_date_Tv );
        endDateTv = view.findViewById( R.id.e_date_Tv );

        addAlarmTv1 = view.findViewById( R.id.add_alarm_tv1 );
        allTimeSwitch = view.findViewById( R.id.all_time_sb );

        addRepeatTv = view.findViewById( R.id.add_repeat_tv );
        eventTitleEt = view.findViewById( R.id.event_title_Et );
        addNotesEt = view.findViewById( R.id.add_notes_Et );


        startDateTv.setText( ff.format( ccc.getTime() ) );
        endDateTv.setText( ff.format( cccc.getTime() ) );

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

            c.set( year, monthOfYear, dayOfMonth );
            final int hour = c.get( Calendar.HOUR_OF_DAY );
            final int minutes = c.get( Calendar.MINUTE );


            SimpleDateFormat sdf = new SimpleDateFormat( "dd MMM yyyy EEE, " );
            formattedDate = sdf.format( c.getTime() );
            if (isStartDate) {
                sDate = MyTimeSettingClass.getMilliFromDate( formattedDate );
                TimePickerDialog dpd = new TimePickerDialog( getContext(), (timeSetListener), hour, minutes, false );
                dpd.show();
            } else if (!isStartDate) {
                eDate =MyTimeSettingClass.getMilliFromDate( formattedDate );
                TimePickerDialog dpd = new TimePickerDialog( getContext(), (timeSetListener), hour, minutes, false );
                dpd.show();
            }

        }

    };

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calendar = Calendar.getInstance();
            calendar.set( Calendar.HOUR_OF_DAY, hourOfDay );
            calendar.set( Calendar.MINUTE, minute );
            SimpleDateFormat sformat = new SimpleDateFormat( "h:mm a" );


            formattedTime = sformat.format( calendar.getTime() );


            if (isStartDate) {

                long sTimeMS = calendar.getTimeInMillis();
                startDateTv.setText( formattedDate + formattedTime );
                startDate = MyTimeSettingClass.getMilliFromDate(  formattedDate + formattedTime );

            } else if (!isStartDate) {
                long eTimeMS = calendar.getTimeInMillis();
                endDateTv.setText( formattedDate + formattedTime );
                endDate = MyTimeSettingClass.getMilliFromDate(  formattedDate + formattedTime );
            }

        }
    };


    // set event's Title,start date,end date,start time, end Time, and other event_bottom_sheet views
    public void eventAddingFun() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get( Calendar.YEAR );
        final int month = calendar.get( Calendar.MONTH );
        final int day = calendar.get( Calendar.DAY_OF_MONTH );
        final int hour = calendar.get( Calendar.HOUR_OF_DAY );
        final int minutes = calendar.get( Calendar.MINUTE );

        startDateTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = true;
                DatePickerDialog d = new DatePickerDialog( getContext(), mDateSetListener, year, month, day );
                d.show();

            }
        } );

        endDateTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = false;
                DatePickerDialog d = new DatePickerDialog( getContext(), mDateSetListener, year, month, day );
                d.show();
            }
        } );

        allTimeSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAllTime = true;
                } else {
                    isAllTime = false;

                }
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

        ContentResolver contentResolver = Objects.requireNonNull( getContext() ).getContentResolver();
        ContentValues values = new ContentValues();

        String eventTitle, alarm, repeat, notes;

        TimeZone timeZone = TimeZone.getDefault();

        if (eventTitleEt.length() == 0) {
            eventTitleEt.setText( "Untitled" );
        }
        eventTitle = eventTitleEt.getText().toString();

            startDate = MyTimeSettingClass.getMilliFromDate(startDateTv.getText().toString());

            endDate = MyTimeSettingClass.getMilliFromDate(endDateTv.getText().toString());

        alarm = addAlarmTv1.getText().toString();
        int hasAlarm = 0;


        if (alarm.matches( "None" )) {
            hasAlarm = 0;
            values.put( CalendarContract.Events.HAS_ALARM, hasAlarm );
        } else if (alarm.matches( "At time of event" )) {
            hasAlarm = 1;
            values.put( CalendarContract.Events.HAS_ALARM, hasAlarm );
        } else if (alarm.matches( "5 minutes before" )) {
            hasAlarm = 1;
            values.put( CalendarContract.Events.HAS_ALARM, hasAlarm );
        } else if (alarm.matches( "30 minutes before" )) {
            hasAlarm = 1;
            values.put( CalendarContract.Events.HAS_ALARM, hasAlarm );
        } else if (alarm.matches( "1 hour before" )) {
            hasAlarm = 1;
            values.put( CalendarContract.Events.HAS_ALARM, hasAlarm );
        } else if (alarm.matches( "2 hours before" )) {
            hasAlarm = 1;
            values.put( CalendarContract.Events.HAS_ALARM, hasAlarm );
        } else if (alarm.matches( "1 day before" )) {
            hasAlarm = 1;
            values.put( CalendarContract.Events.HAS_ALARM, hasAlarm );
        } else if (alarm.matches( "2 day before" )) {
            hasAlarm = 1;
            values.put( CalendarContract.Events.HAS_ALARM, hasAlarm );
        } else if (alarm.matches( "1 week before" )) {
            hasAlarm = 1;
            values.put( CalendarContract.Events.HAS_ALARM, hasAlarm );
        }
        repeat = addRepeatTv.getText().toString();
        if (addNotesEt.length() == 0) {
            addNotesEt.setText( "Empty" );
        }
        notes = addNotesEt.getText().toString();


        values.put( CalendarContract.Events.CALENDAR_ID, 1 );
        values.put( CalendarContract.Events.TITLE, eventTitle );
        values.put( CalendarContract.Events.DTSTART, startDate );
        values.put( CalendarContract.Events.DTEND, endDate );
        values.put( CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID() );
        values.put( CalendarContract.Events.DESCRIPTION, notes );
        values.put( CalendarContract.Events.ALL_DAY, this.isAllTime );

        contentResolver.insert( CalendarContract.Events.CONTENT_URI, values );
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.setCalendarBNBItem();
    }

    public void showBShAlarmRcDialog() {
        View view = LayoutInflater.from( getContext() ).inflate( R.layout.fragment_bottom_sh__alarm_list___frag__dialog, null );
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setCancelable( true );
        builder.setView( view );
        AlertDialog dialog = builder.create();
        dialog.show();

        RecyclerView recyclerView;
        BottomShAlarmRVFragDialogAdapter adapter;

        recyclerView = view.findViewById( R.id.BShAlarmRv );
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );

        List<String> list = new ArrayList<>();

        list.add( ("None") );
        list.add( ("At time of event") );
        list.add( ("5 minutes before") );
        list.add( ("15 minutes before") );
        list.add( ("30 minutes before") );
        list.add( ("1 hour before") );
        list.add( ("2 hours before") );
        list.add( ("1 day before") );
        list.add( ("2 day before") );
        list.add( ("1 week before") );


        adapter = new BottomShAlarmRVFragDialogAdapter( getContext(), list, dialog );
        adapter.getAlarmStringListener( new EditTextStringListener() {
            @Override
            public void myString(String ss) {
                addAlarmTv1.setText( ss );
            }
        } );
        recyclerView.setAdapter( adapter );
        adapter.notifyDataSetChanged();


    }

    public void showBShRepeatRvDialog() {
        View view = LayoutInflater.from( getContext() ).inflate( R.layout.fragment_bsh_repeat_frag_diaglog, null );

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setCancelable( true );
        builder.setView( view );
        AlertDialog dialog = builder.create();
        dialog.show();

        RecyclerView recyclerView;
        CalBottomShRepeatDialogAdapter adapter;

        recyclerView = view.findViewById( R.id.bsh_repeat_rv );
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );
        ArrayList<String> list = new ArrayList<>();
        list.add( ("Never") );
        list.add( ("Every day") );
        list.add( ("Every week") );
        list.add( ("Every 2 weeks") );
        list.add( ("Every month") );
        list.add( ("Every year") );

        adapter = new CalBottomShRepeatDialogAdapter( getContext(), list, dialog );
        adapter.getRepeatStringListener( new EditTextStringListener() {
            @Override
            public void myString(String ss) {
                addRepeatTv.setText( ss );

            }
        } );
        recyclerView.setAdapter( adapter );
        adapter.notifyDataSetChanged();

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
