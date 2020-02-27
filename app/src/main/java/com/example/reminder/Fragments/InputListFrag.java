package com.example.reminder.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.InputTaskListAdapter;
import com.example.reminder.utilities.AlarmSettingClass;
import com.example.reminder.utilities.MyTimeSettingClass;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.interfaces.EditTextStringListener;
import com.example.reminder.models.InputRemiderModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class InputListFrag extends Fragment {


    private static final int REQUEST_PERMISSION = 1;
    private RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;

    private DataBaseHelper dataBaseHelper;

    private InputTaskListAdapter inputTaskListAdapter;

    AllTasksFrag allTasksFrag;
    private EditText input_ET;
    private Button doneBtn;
    private LinearLayout close_LL;

    private String input_from_inputRV, dateToPlaceTask = "", alarmTime = "";
    private EditTextStringListener editTextStringListener;

    private MainActivity mainActivity;
    private MyTimeSettingClass myTimeSettingClass;

    private LinearLayout dayslLo, timeLo;
    private TextView tvM,tvAfter,tvEvnng;
    private ImageView listFragImageView3,listFragImageView9,listFragImageView6;
    private boolean today = false, tomorrow = false, nextWeek = false, custom = false;
    private ConstraintLayout nineLo, threeLo, sixLo, todayLo, tomorrowLo, nextweekLo, daycustomLo, timeCustomLo;
    private Calendar calendar = Calendar.getInstance();
    private int checkYear, currentYear, istomorrow, mtomorrow, isToday, mtoday;

    private Calendar whatTime = Calendar.getInstance();
    @SuppressLint("NewApi")
    private SimpleDateFormat whatTimeFormat = new SimpleDateFormat( "h:mm a" );
    private Calendar c = Calendar.getInstance();
    private int timeOfDay = c.get( Calendar.HOUR_OF_DAY );
    SimpleDateFormat sformat;
    private Calendar taskCreatedDate = Calendar.getInstance();
    @SuppressLint("NewApi")
    private SimpleDateFormat taskCreatedDateSF = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm a" );
    private AlarmSettingClass alarmSettingClass;
    private String getBundleData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_input_list, container, false );
        getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );

        mainActivity = (MainActivity) getActivity();
        myTimeSettingClass = new MyTimeSettingClass();
        dataBaseHelper = new DataBaseHelper( getContext() );
        alarmSettingClass = new AlarmSettingClass( getActivity() );

        recyclerView = view.findViewById( R.id.inputRemiderRV );

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( linearLayoutManager );

        input_ET = view.findViewById( R.id.searchET );
        close_LL = view.findViewById( R.id.list_layout_backLL );
        doneBtn = view.findViewById( R.id.donebtn );

        dayslLo = view.findViewById( R.id.dayslayout );
        todayLo = view.findViewById( R.id.today_layout );
        tomorrowLo = view.findViewById( R.id.tomorrow_layout );
        nextweekLo = view.findViewById( R.id.nextweek_layout );
        daycustomLo = view.findViewById( R.id.custom_layout );

        timeLo = view.findViewById( R.id.time_layout );
        nineLo = view.findViewById( R.id.nine_layout );
        threeLo = view.findViewById( R.id.three_layout );
        sixLo = view.findViewById( R.id.six_layout );
        tvM = view.findViewById( R.id.listFragTextViewmrng );
        tvAfter = view.findViewById( R.id.listFragTextViewAftrnn );
        tvEvnng = view.findViewById( R.id.listFragTextViewEvnng );
        listFragImageView9 = view.findViewById( R.id.listFragImageView9 );
        listFragImageView3 = view.findViewById( R.id.listFragImageView3 );
        listFragImageView6 = view.findViewById( R.id.listFragImageView6 );

        timeCustomLo = view.findViewById( R.id.custom_time_layout );
        mainActivity.hideBottomNView();
        dayslLo.setVisibility( View.GONE );
        timeLo.setVisibility( View.GONE );
        getBundleData = getArguments().getString( "get_btn" );


        setDateTimeOfTask();

        input_ET.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 1) {
                    dayslLo.setVisibility( View.VISIBLE );
                    timeLo.setVisibility( View.GONE );
                } else {
                    dayslLo.setVisibility( View.GONE );
                    timeLo.setVisibility( View.GONE );
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

        input_ET.post( new Runnable() {
            @Override
            public void run() {
                input_ET.requestFocus();
                InputMethodManager imgr = (InputMethodManager) Objects.requireNonNull( getActivity() ).getSystemService( Context.INPUT_METHOD_SERVICE );
                imgr.showSoftInput( input_ET, InputMethodManager.SHOW_FORCED );
                input_ET.setImeOptions( EditorInfo.IME_ACTION_DONE );
                input_ET.setSingleLine();

            }
        } );
        input_ET.setOnEditorActionListener( new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENDCALL) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    InputMethodManager imgr = (InputMethodManager) Objects.requireNonNull( getActivity() ).getSystemService( Context.INPUT_METHOD_SERVICE );
                    imgr.hideSoftInputFromWindow( input_ET.getWindowToken(), 0 );
                    onDoneButtonClick();

                }
                return false;
            }

        } );


        List<InputRemiderModel> inputRemiderModelList = new ArrayList<>();

        inputRemiderModelList.add( new InputRemiderModel( "Call", R.drawable.call1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Check", R.drawable.check1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Get", R.drawable.get ) );
        inputRemiderModelList.add( new InputRemiderModel( "Email", R.drawable.email1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Buy", R.drawable.shopping1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Meet/Schedule", R.drawable.meeting1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Clean", R.drawable.clean1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Take", R.drawable.take1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Send", R.drawable.send1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Pay", R.drawable.pay1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Make", R.drawable.make1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Pick", R.drawable.pick1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Do", R.drawable.do1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Read", R.drawable.reading1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Print", R.drawable.print1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Finish", R.drawable.finish1 ) );
        inputRemiderModelList.add( new InputRemiderModel( "Study", R.drawable.study1 ) );

        inputTaskListAdapter = new InputTaskListAdapter( getContext(), inputRemiderModelList );
        inputTaskListAdapter.addListener( new EditTextStringListener() {
            @Override
            public void myString(String ss) {
                input_ET.setText( ss + " " );
                input_ET.setSelection( input_ET.getText().length() );
            }

            @Override
            public void myItemPosition(int pos) {

            }
        } );


        //on done button data should go to task fragment.

        //search
        input_ET.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputTaskListAdapter.getFilter().filter( s );
            }
        } );


        //go to task fragment
        close_LL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.showBottomNView();
                mainActivity.onBackPressed();
                mainActivity.setTaskFragDefaultBNBItem();//it will go on AllTasks fragment and
                // make highlighted Task item of bottom navigation view
            }
        } );


        recyclerView.setAdapter( inputTaskListAdapter );
        inputTaskListAdapter.notifyDataSetChanged();

        //this saves task from InputListFrag into AllTasks Fragment
        doneBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneButtonClick();
            }
        } );


        return view;
    }

    private void onDoneButtonClick() {


        if (input_ET.length() == 0) {
            mainActivity.setTaskFragDefaultBNBItem();
            mainActivity.showBottomNView();
        } else if (input_ET.length() != 0) {
            if (dateToPlaceTask.matches( "" ) || alarmTime.matches( "" )) {
                whatIsThisFun();
                mainActivity.setTaskFragDefaultBNBItem();
                mainActivity.showBottomNView();

            } else {
                @SuppressLint({"NewApi", "LocalSuppress"}) int isInsert = dataBaseHelper.insert( input_ET.getText().toString(), alarmTime, dateToPlaceTask,
                        taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "1" );
                if (isInsert == 0) {
                } else {
                    alarmSettingClass.setOneAlarm( input_ET.getText().toString(), MyTimeSettingClass.getMilliFromDate( alarmTime ), isInsert );
                }
            }
            mainActivity.setTaskFragDefaultBNBItem();
            mainActivity.showBottomNView();
            InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull( getActivity() ).getSystemService( Context.INPUT_METHOD_SERVICE );
            inputMethodManager.hideSoftInputFromWindow( input_ET.getWindowToken(), 0 );
        }

    }


    @SuppressLint("NewApi")
    public void whatIsThisFun() {
        if (getBundleData.matches( "today_Clicked" )) {
            @SuppressLint({"NewApi", "LocalSuppress"}) int isInsert = dataBaseHelper.insert( input_ET.getText().toString(), "",
                    MyTimeSettingClass.todayPlaceDate(), taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "0" );
            if (isInsert == 0) {
            } else {
            }

        } else if (getBundleData.matches( "tomorrow_Clicked" )) {

            dataBaseHelper.insert( input_ET.getText().toString(), "",
                    MyTimeSettingClass.tomorrowPlaceDate(), taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "0" );

        } else if (getBundleData.matches( "upcoming_Clicked" )) {

            int isInsert = dataBaseHelper.insert( input_ET.getText().toString(), "",
                    MyTimeSettingClass.nextWeekPlaceDate(), taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "0" );


        } else if (getBundleData.matches( "someday_Clicked" )) {

            int isInsert = dataBaseHelper.insert( input_ET.getText().toString(), "",
                    "", taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "0" );

        } else if (getBundleData.matches( "main_Clicked" )) {

            int isInsert = dataBaseHelper.insert( input_ET.getText().toString(), "",
                    MyTimeSettingClass.todayPlaceDate(), taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "0" );

        }
    }

    private DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("NewApi")
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            nextWeek = false;
            tomorrow = false;
            today = false;
            custom = true;
            dayslLo.setVisibility( View.GONE );
            timeLo.setVisibility( View.VISIBLE );
            nineLo.setClickable( true );
            threeLo.setClickable( true );
            sixLo.setClickable( true );
            timeCustomLo.setEnabled( true );

            calendar.set( Calendar.YEAR, year );
            calendar.set( Calendar.MONTH, month );
            calendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );


            Calendar calendar1 = Calendar.getInstance();

            checkYear = calendar.get( Calendar.YEAR );
            currentYear = calendar1.get( Calendar.YEAR );
            istomorrow = calendar.get( Calendar.DAY_OF_MONTH );
            mtomorrow = calendar1.get( Calendar.DAY_OF_MONTH ) + 1;
            isToday = calendar.get( Calendar.DAY_OF_MONTH );
            mtoday = calendar1.get( Calendar.DAY_OF_MONTH );
            @SuppressLint({"NewApi", "LocalSuppress"}) SimpleDateFormat format = new SimpleDateFormat( "dd MMM yyyy EEE, " );

            myTimeSettingClass.setCustomPlaceDate( dayOfMonth, month, year );
            dateToPlaceTask = MyTimeSettingClass.getCustomPlaceDate();
            alarmTime = format.format( calendar.getTime() );

        }
    };

    // return left string from first ,
    public String returnPartOfString1(String s, String pointString) {
        String d = s;
        String ssDate = d.substring( d.lastIndexOf( pointString ) );
        d = d.replace( ssDate, "" );
        return d;
    }



    private void setDateTimeOfTask() {

        todayLo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeOfDay > 0 && timeOfDay < 9) {
                    nineLo.setClickable( true );
                } else {
                    nineLo.setClickable( false );
                    tvM.setTextColor( Color.parseColor( "#d1d1d1" ) );
                    listFragImageView9.setImageResource( R.drawable.unselect );
                }
                if (timeOfDay >0 && timeOfDay < 15) {
                    threeLo.setClickable( true );
                } else {
                    threeLo.setEnabled( false );
                    tvAfter.setTextColor( Color.parseColor( "#d1d1d1" ) );
                    listFragImageView3.setImageResource( R.drawable.unselect );


                }
                if (timeOfDay > 0 && timeOfDay < 18) {
                    sixLo.setClickable( true );
                } else {
                    sixLo.setClickable( false );
                    tvEvnng.setTextColor( Color.parseColor( "#d1d1d1" ) );
                    listFragImageView6.setImageResource( R.drawable.unselect );


                }
                today = true;
                tomorrow = false;
                nextWeek = false;
                custom = false;
                dateToPlaceTask = MyTimeSettingClass.todayPlaceDate();
                dayslLo.setVisibility( View.GONE );
                timeLo.setVisibility( View.VISIBLE );

            }
        } );
        tomorrowLo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tomorrow = true;
                today = false;
                nextWeek = false;
                custom = false;
                dateToPlaceTask = MyTimeSettingClass.tomorrowPlaceDate();
                dayslLo.setVisibility( View.GONE );
                timeLo.setVisibility( View.VISIBLE );
                nineLo.setEnabled( true );
                threeLo.setEnabled( true );
                sixLo.setEnabled( true );
                timeCustomLo.setEnabled( true );

            }
        } );
        nextweekLo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWeek = true;
                tomorrow = false;
                today = false;
                custom = false;
                dateToPlaceTask = MyTimeSettingClass.nextWeekPlaceDate();
                dayslLo.setVisibility( View.GONE );
                timeLo.setVisibility( View.VISIBLE );
                nineLo.setClickable( true );
                threeLo.setClickable( true );
                sixLo.setClickable( true );
                timeCustomLo.setEnabled( true );
            }
        } );

        nineLo.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (today) {
                    alarmTime = MyTimeSettingClass.getToday9am();
                } else if (tomorrow) {
                    alarmTime = MyTimeSettingClass.getTomorrowMorning();
                } else if (nextWeek) {
                    alarmTime = MyTimeSettingClass.getNextWeek9am();
                } else {
                    whatTime.set( Calendar.HOUR_OF_DAY, 9 );
                    whatTime.set( Calendar.MINUTE, 0 );
                    whatTime.set( Calendar.SECOND, 0 );
                    alarmTime = alarmTime + whatTimeFormat.format( whatTime.getTime() );
                }
                onDoneButtonClick();


            }
        } );

        threeLo.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                if (today) {
                    alarmTime = MyTimeSettingClass.getToday3pm();
                } else if (tomorrow) {
                    alarmTime = MyTimeSettingClass.getTomorrow3pm();
                } else if (nextWeek) {
                    alarmTime = MyTimeSettingClass.getNextWeek3pm();
                } else {
                    whatTime.set( Calendar.HOUR_OF_DAY, 15 );
                    whatTime.set( Calendar.MINUTE, 0 );
                    whatTime.set( Calendar.SECOND, 0 );
                    alarmTime = alarmTime + whatTimeFormat.format( whatTime.getTime() );
                }
                onDoneButtonClick();
            }
        } );

        sixLo.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                if (today) {
                    alarmTime = MyTimeSettingClass.getToday6pm();
                } else if (tomorrow) {
                    alarmTime = MyTimeSettingClass.getTomorrow6pm();
                } else if (nextWeek) {
                    alarmTime = MyTimeSettingClass.getNextWeek6pm();
                } else {
                    whatTime.set( Calendar.HOUR_OF_DAY, 18 );
                    whatTime.set( Calendar.MINUTE, 0 );
                    whatTime.set( Calendar.SECOND, 0 );
                    alarmTime = alarmTime + whatTimeFormat.format( whatTime.getTime() );
                }
                onDoneButtonClick();
            }
        } );

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get( Calendar.YEAR );
        final int month = calendar.get( Calendar.MONTH );
        final int day = calendar.get( Calendar.DAY_OF_MONTH );
        final int hour = calendar.get( Calendar.HOUR_OF_DAY );
        final int minutes = calendar.get( Calendar.MINUTE );

        daycustomLo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dP = new DatePickerDialog( Objects.requireNonNull( getContext() ), datePickerDialog, year, month, day );
                dP.getDatePicker().setMinDate( System.currentTimeMillis() - 1000 );
                dP.show();

            }
        } );
        timeCustomLo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tP = new TimePickerDialog( getContext(), timePickerListener, hour, minutes, false );
                tP.show();
            }
        } );
    }
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @SuppressLint("NewApi")
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            calendar.set( Calendar.HOUR_OF_DAY, hourOfDay );
            calendar.set( Calendar.MINUTE, minute );

            sformat = new SimpleDateFormat( " h:mm a" );

            if (nextWeek)
            {
            String date = returnPartOfString1(MyTimeSettingClass.getNextWeek(),",");
                alarmTime = date + sformat.format( calendar.getTime() );
            }
            else if (tomorrow)
            {
                String date = returnPartOfString1(MyTimeSettingClass.getTomorrow(),",");
                alarmTime = date+"," + sformat.format( calendar.getTime() );
            }
            else if (today)
            {
                String date = returnPartOfString1(MyTimeSettingClass.getLaterToday(),",");
                alarmTime = date + sformat.format( calendar.getTime() );

            }else if (custom)
            {
                alarmTime = alarmTime + sformat.format( calendar.getTime() );
            }
        }
    };



    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull( getActivity() ).getSystemService( Context.INPUT_METHOD_SERVICE );
        inputMethodManager.hideSoftInputFromWindow( input_ET.getWindowToken(), 0 );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach( context );

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode( true );
        getView().requestFocus();
        getView().setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        } );


    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    private void goToTaskFrag() {
        MainActivity mainActivity = (MainActivity) getActivity();
        AllTasksFrag allTasksFrag = new AllTasksFrag();
        assert mainActivity != null;
        mainActivity.loadmyfrag( allTasksFrag );
    }

}
