package com.example.reminder.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.BottomShAlarmRVFragDialogAdapter;
import com.example.reminder.adapter.CalBottomShRepeatDialogAdapter;
import com.example.reminder.classes.MyTimeSettingClass;
import com.example.reminder.interfaces.EditTextStringListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;


public class CalendarEventAddBottomSheetDialogFrag extends BottomSheetDialogFragment {

    private TextView startDateTv, endDateTv, addRepeatTv, addAlarmTv1;
    private EditText eventTitleEt, addNotesEt;
    private Button saveBtn;
    private String formattedDate, formattedTime;
    private long sDate, sTime, eDate, eTime, sDateAndTime, eDateAndTime;
    private Switch allTimeSwitch;
    LinearLayout fragment_my_bottom_sheet_dialog;

    private boolean isFirstFieldSelected = false;
    private boolean isStartDate = true;
    private boolean isStartTime = false;
    private boolean isAllTime;
    private String addRepeat;
    private boolean hasAlarm = false, hasRepeat = false;


    private long startDate, endDate, addTime = 0;
    private String dd, ee, d, e, f, g;

    static CalendarEventAddBottomSheetDialogFrag newInstance() {


        return new CalendarEventAddBottomSheetDialogFrag();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setStyle( BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme );
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_my_bottom_sheet_dialog, container, false );


        Calendar ccc = Calendar.getInstance();
        ccc.add( Calendar.HOUR_OF_DAY, 1 );
        @SuppressLint({"NewApi", "LocalSuppress"}) SimpleDateFormat ff = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm a" );
        Calendar cccc = Calendar.getInstance();
        cccc.add( Calendar.HOUR_OF_DAY, 2 );


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

        dd = startDateTv.getText().toString();
        ee = endDateTv.getText().toString();
        d = returnPartOfString1( dd, "," );
        e = returnPartOfString1( ee, "," );
        f = returnPartOfString2( dd, "," );
        g = returnPartOfString2( ee, "," );


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
                eDate = MyTimeSettingClass.getMilliFromDate( formattedDate );
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
                allTimeSwitch.setChecked( false );
                startDate = MyTimeSettingClass.getMilliFromDate( formattedDate + formattedTime );

            } else if (!isStartDate) {
                long eTimeMS = calendar.getTimeInMillis();
                endDateTv.setText( formattedDate + formattedTime );
                endDate = MyTimeSettingClass.getMilliFromDate( formattedDate + formattedTime );
                allTimeSwitch.setChecked( false );
            }

        }
    };


    // set event's Title,start date,end date,start time, end Time, and other event_bottom_sheet views
    public void eventAddingFun() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get( Calendar.YEAR );
        final int month = calendar.get( Calendar.MONTH );
        final int day = calendar.get( Calendar.DAY_OF_MONTH );


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
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    isAllTime = true;

                    startDateTv.setText( d );
                    endDateTv.setText( e );

                    startDate = MyTimeSettingClass.getMilliFromDate( d, "dd MMM yyyy EEE" );
                    endDate = MyTimeSettingClass.getMilliFromDate( e, "dd MMM yyyy EEE" );
                }
                if (!isChecked) {
                    isAllTime = false;
                    startDateTv.setText( dd  );
                    endDateTv.setText( ee  );
                    startDate = MyTimeSettingClass.getMilliFromDate( endDateTv.getText().toString() );
                    endDate = MyTimeSettingClass.getMilliFromDate( startDateTv.getText().toString() );

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
                dismiss();
                onEventSaveClick();
            }
        } );
    }

    // return left string from first ,
    public String returnPartOfString1(String s, String pointString) {
        String d = s;
        String ssDate = d.substring( d.lastIndexOf( pointString ) );
        d = d.replace( ssDate, "" );
        return d;
    }

    // return right string from first ,
    public String returnPartOfString2(String s, String pointString) {
        String d = s;
        String ssDate = d.substring( d.lastIndexOf( pointString ) + 1 );

        return ssDate;
    }

    public void onEventSaveClick() {

        ContentResolver contentResolver = Objects.requireNonNull( getContext() ).getContentResolver();
        ContentValues values = new ContentValues();

        String eventTitle, alarm, repeat, notes;

        TimeZone timeZone = TimeZone.getDefault();

        if (eventTitleEt.length() == 0) {
            eventTitle = "Untitled";

        } else {
            eventTitle = eventTitleEt.getText().toString();
        }

        if (isAllTime) {
        } else {
            startDate = MyTimeSettingClass.getMilliFromDate( startDateTv.getText().toString() );

            endDate = MyTimeSettingClass.getMilliFromDate( endDateTv.getText().toString() );
        }


        repeat = addRepeatTv.getText().toString();
        alarm = addAlarmTv1.getText().toString();


        if (repeat.matches( "Never" )) {
            addRepeat = "NEVER";
            hasRepeat = false;

        } else if (repeat.matches( "Daily" )) {
            hasRepeat = true;
            addRepeat = "DAILY";

        } else if (repeat.matches( "Weekly" )) {
            hasRepeat = true;
            addRepeat = "WEEKLY";

        } else if (repeat.matches( "Monthly" )) {
            hasRepeat = true;
            addRepeat = "MONTHLY";

        } else if (repeat.matches( "Yearly" )) {
            hasRepeat = true;
            addRepeat = "YEARLY";
        }


        if (alarm.matches( "None" )) {
            hasAlarm = false;

        } else if (alarm.matches( "At time of event" )) {
            addTime = 0;
            hasAlarm = true;
        } else if (alarm.matches( "5 minutes before" )) {
            addTime = 5;
            hasAlarm = true;
        } else if (alarm.matches( "30 minutes before" )) {
            addTime = 30;
            hasAlarm = true;
        } else if (alarm.matches( "1 hour before" )) {
            addTime = 60;
            hasAlarm = true;
        } else if (alarm.matches( "2 hours before" )) {
            addTime = 2 * 60;

            hasAlarm = true;
        } else if (alarm.matches( "1 day before" )) {
            addTime = 24 * 60;

            hasAlarm = true;
        } else if (alarm.matches( "2 day before" )) {
            addTime = 48 * 60;

            hasAlarm = true;
        } else if (alarm.matches( "1 week before" )) {
            addTime = 7 * 24 * 60;

            hasAlarm = true;
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


        if (hasAlarm) {
            if (hasRepeat) {
                values.put( CalendarContract.Events.RRULE, "FREQ=" + addRepeat );
            }

            Uri url = getContext().getContentResolver().insert( CalendarContract.Events.CONTENT_URI, values );

            long eventId = Long.parseLong( url.getLastPathSegment() );

            ContentValues reminder = new ContentValues();
            reminder.put( CalendarContract.Reminders.EVENT_ID, eventId );
            reminder.put( CalendarContract.Reminders.MINUTES, addTime );
            reminder.put( CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT );
            getContext().getContentResolver().insert( CalendarContract.Reminders.CONTENT_URI, reminder );
            contentResolver.insert( CalendarContract.Events.CONTENT_URI, reminder );

        } else {
            if (hasRepeat) {
                values.put( CalendarContract.Events.RRULE, "FREQ=" + addRepeat );
            }
            contentResolver.insert( CalendarContract.Events.CONTENT_URI, values );
        }


        MainActivity mainActivity = (MainActivity) getActivity();
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

            @Override
            public void myItemPosition(int pos) {

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
        list.add( ("Daily") );
        list.add( ("Weekly") );
        list.add( ("Monthly") );
        list.add( ("Yearly") );


        adapter = new CalBottomShRepeatDialogAdapter( getContext(), list, dialog );
        adapter.getRepeatStringListener( new EditTextStringListener() {
            @Override
            public void myString(String ss) {
                addRepeatTv.setText( ss );

            }

            @Override
            public void myItemPosition(int pos) {

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
