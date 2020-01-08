package com.example.reminder.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.InputTaskListAdapter;
import com.example.reminder.classes.MyTimeSettingClass;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.interfaces.EditTextStringListener;
import com.example.reminder.interfaces.VisibilityListener;
import com.example.reminder.models.InputRemiderModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class InputListFrag extends Fragment {


    private static final int REQUEST_PERMISSION =1 ;
    private RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;

    private DataBaseHelper dataBaseHelper;

    private InputTaskListAdapter inputTaskListAdapter;

    AllTasksFrag allTasksFrag;
    private EditText input_ET;
    private Button doneBtn, close_btn;

    private String input_from_inputRV,dateToPlaceTask = "",alamTime="";
    private EditTextStringListener editTextStringListener;

    private MainActivity mainActivity;
    private MyTimeSettingClass myTimeSettingClass;

    private LinearLayout remindTvLo,
            dayslLo, todayLo, tomorrowLo, nextweekLo, daycustomLo,
            timeLo, nineLo, threeLo, sixLo, timeCustomLo;
    private boolean today=false,tomorrow=false,nextWeek=false;

    private Calendar calendar = Calendar.getInstance();
    private int checkYear,currentYear,istomorrow,mtomorrow,isToday,mtoday;

    private Calendar c = Calendar.getInstance();
    private int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
    SimpleDateFormat sformat;
    private  Calendar taskCreatedDate = Calendar.getInstance();
    private SimpleDateFormat taskCreatedDateSF = new SimpleDateFormat( "dd MMM yyyy" );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_input_list, container, false );
        mainActivity = (MainActivity) getActivity();
        myTimeSettingClass = new MyTimeSettingClass();
        dataBaseHelper = new DataBaseHelper( getContext() );



        recyclerView = view.findViewById( R.id.inputRemiderRV );

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( linearLayoutManager );

        input_ET  = view.findViewById( R.id.searchET );
        close_btn = view.findViewById( R.id.search_close_btn );
        doneBtn   = view.findViewById( R.id.donebtn );

        remindTvLo  = view.findViewById( R.id.remindLayout );
        dayslLo     = view.findViewById( R.id.dayslayout );
        todayLo     = view.findViewById( R.id.today_layout );
        tomorrowLo  = view.findViewById( R.id.tomorrow_layout );
        nextweekLo  = view.findViewById( R.id.nextweek_layout );
        daycustomLo = view.findViewById( R.id.custom_layout );

        timeLo  = view.findViewById( R.id.time_layout );
        nineLo  = view.findViewById( R.id.nine_layout );
        threeLo = view.findViewById( R.id.three_layout );
        sixLo   = view.findViewById( R.id.six_layout );

        mainActivity.hideBottomNView();
        remindTvLo.setVisibility( View.GONE );
        dayslLo.setVisibility( View.GONE );
        timeLo.setVisibility( View.GONE );


        if (timeOfDay>0 && timeOfDay>9) {
            nineLo.setClickable( true );
        }
        else
        {
            nineLo.setClickable( false );
        }
        if (timeOfDay>0 && timeOfDay >15)
        {
            threeLo.setClickable( true );
        }
        else
        {threeLo.setClickable( false );
        }
        if (timeOfDay>0 && timeOfDay>18)
        {
            sixLo.setClickable( true );
        }
        else
        {
            sixLo.setClickable( false );
        }

        setDateTimeOfTask();

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
                    imgr.hideSoftInputFromWindow(input_ET.getWindowToken(), 0);
                    onDoneButtonClick();

                }
                return false;
            }

        } );


        List<InputRemiderModel> inputRemiderModelList = new ArrayList<>();


        inputRemiderModelList.add( new InputRemiderModel( "Call", R.drawable.call_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Check", R.drawable.search_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Get", R.drawable.get_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Email", R.drawable.email_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Buy", R.drawable.shopping_cart_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Meet/Schedule", R.drawable.ic_launcher_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Clean", R.drawable.ic_launcher_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Take", R.drawable.ic_launcher_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Send", R.drawable.send_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Pay", R.drawable.pay_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Make", R.drawable.ic_launcher_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Pick", R.drawable.ic_launcher_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Do", R.drawable.ic_launcher_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Read", R.drawable.ic_launcher_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Print", R.drawable.print_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Finish", R.drawable.ic_launcher_foreground ) );
        inputRemiderModelList.add( new InputRemiderModel( "Study", R.drawable.ic_launcher_foreground ) );

        inputTaskListAdapter = new InputTaskListAdapter( getContext(), inputRemiderModelList );
        inputTaskListAdapter.addlistener( new EditTextStringListener() {
            @Override
            public void mystring(String ss) {
                input_ET.setText( ss + " " );
                input_ET.setSelection( input_ET.getText().length() );
            }
        } );

        inputTaskListAdapter.addVisiblelistener(new VisibilityListener() {
            @Override
            public void VeiwVisibility() {
                 remindTvLo.setVisibility( View.VISIBLE );

            }
        });

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
        close_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.showBottomNView();
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

    private void onDoneButtonClick()
    {
        if (input_ET.length()==0)
        {
            mainActivity.setTaskFragDefaultBNBItem();
            mainActivity.showBottomNView();
        }
        if (input_ET.length() != 0)
        {
            if (dateToPlaceTask.matches( "" ))
            {
                boolean isInsert =  dataBaseHelper.insert( input_ET.getText().toString(),alamTime,MyTimeSettingClass.todayPlaceDate(), taskCreatedDateSF.format( taskCreatedDate.getTime()) );

                if (isInsert)
                {
                    Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
                }
                else
                    Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();

            }
            else
            if (alamTime.matches( "" ))
            {
               boolean isInsert = dataBaseHelper.insert( input_ET.getText().toString(),"",dateToPlaceTask, taskCreatedDateSF.format( taskCreatedDate.getTime()) );
                if (isInsert)
                {
                    Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
                }
                else
                    Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();

            }
            else if (alamTime.matches( "" ) && dateToPlaceTask.matches( "" ))
            {
                boolean isInsert = dataBaseHelper.insert( input_ET.getText().toString(),"",MyTimeSettingClass.todayPlaceDate(),taskCreatedDateSF.format( taskCreatedDate.getTime()) );

                if (isInsert)
                {
                    Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
                }
                else
                    Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
            }
            else
            {
              boolean isInsert =   dataBaseHelper.insert( input_ET.getText().toString(),alamTime,dateToPlaceTask, taskCreatedDateSF.format( taskCreatedDate.getTime()) );
                if (isInsert)
                {
                    Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
                }
                else
                    Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
            }


            mainActivity.setTaskFragDefaultBNBItem();
            mainActivity.showBottomNView();

        }


    }

    private DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


            calendar.set( Calendar.YEAR,year );
            calendar.set( Calendar.MONTH,month );
            calendar.set( Calendar.DAY_OF_MONTH,dayOfMonth );


            Calendar calendar1 = Calendar.getInstance();

            checkYear = calendar.get( Calendar.YEAR );
            currentYear =calendar1.get( Calendar.YEAR );
            istomorrow = calendar.get(Calendar.DAY_OF_MONTH);
            mtomorrow   = calendar1.get( Calendar.DAY_OF_MONTH )+1;
            isToday  = calendar.get( Calendar.DAY_OF_MONTH);
            mtoday  = calendar1.get( Calendar.DAY_OF_MONTH );

            myTimeSettingClass.setCustomPlaceDate( dayOfMonth,month,year );
            dateToPlaceTask = MyTimeSettingClass.getCustomPlaceDate();


        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE,minute );

            if (minute == 0 && checkYear>currentYear && istomorrow!=mtomorrow && isToday!=mtoday )
            {
                sformat = new SimpleDateFormat( "dd MMM yyyy, h a" );
            }
            else
            if (minute != 0 && checkYear>currentYear && istomorrow!=mtomorrow && isToday!=mtoday)
            {
                sformat = new SimpleDateFormat( "d MMM yyyy, h:mm a" );
            }

            if (minute == 0 && checkYear == currentYear  && istomorrow!= mtomorrow )
            {
                sformat = new SimpleDateFormat( "d MMM, h a" );
            }
            else
            if (minute != 0 && checkYear == currentYear && istomorrow!=mtomorrow  )
            {
                sformat = new SimpleDateFormat( "d MMM, h:mm a" );
            }

            if(minute == 0 && istomorrow==mtomorrow && isToday!=mtoday  ) {
                sformat = new SimpleDateFormat( "EEE, h a " );
            }
            else
            if(minute != 0 && istomorrow==mtomorrow && isToday!=mtoday )
            {
                sformat = new SimpleDateFormat( "EEE, h:mm a" );
            }

            if (minute == 0 && isToday==mtoday && istomorrow!=mtomorrow)
            {
                sformat = new SimpleDateFormat( "h a" );
            }
            else
            if (minute!=0 && isToday==mtoday && istomorrow!=mtomorrow)
            {
                sformat =new SimpleDateFormat( "h:mm a" );
            }
            alamTime = sformat.format( calendar.getTime() );

        }
    };



      private void setDateTimeOfTask()
          {
        remindTvLo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remindTvLo.setVisibility( View.GONE );
                dayslLo.setVisibility( View.VISIBLE );
                timeLo.setVisibility( View.GONE );
            }
        } );

            todayLo.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    today = true;
                    tomorrow = false;
                    nextWeek =false;

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
                    nextWeek =false;
                    dateToPlaceTask = MyTimeSettingClass.tomorrowPlaceDate();
                    dayslLo.setVisibility( View.GONE );
                    timeLo.setVisibility( View.VISIBLE );
                    nineLo.setClickable( true );
                    threeLo.setClickable( true );
                    sixLo.setClickable( true );

                }
            } );
            nextweekLo.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextWeek =true;
                    tomorrow = false;
                    today = false;
                    dateToPlaceTask= MyTimeSettingClass.nextWeekPlaceDate();
                    dayslLo.setVisibility( View.GONE );
                    timeLo.setVisibility( View.VISIBLE );
                    nineLo.setClickable( true );
                    threeLo.setClickable( true );
                    sixLo.setClickable( true );
                }
            } );

            nineLo.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (today)
                    {
                        alamTime = MyTimeSettingClass.getToday9am();
                    }
                    if (tomorrow)
                    {
                        alamTime = MyTimeSettingClass.getTomorrowMorning();
                    }
                    if (nextWeek)
                    {
                        alamTime = MyTimeSettingClass.getNextWeek9am();
                    }
                }
            } );

            threeLo.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (today)
                    {
                        alamTime = MyTimeSettingClass.getToday3pm();
                    }
                    if (tomorrow)
                    {
                        alamTime = MyTimeSettingClass.getTomorrow3pm();
                    }
                    if (nextWeek)
                    {
                        alamTime = MyTimeSettingClass.getNextWeek3pm();
                    }
                }
            } );

              sixLo.setOnClickListener( new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                      if (today)
                      {
                          alamTime = MyTimeSettingClass.getToday6pm();
                      }
                      if (tomorrow)
                      {
                          alamTime = MyTimeSettingClass.getTomorrow6pm();
                      }
                      if (nextWeek)
                      {
                          alamTime = MyTimeSettingClass.getNextWeek6pm();
                      }
                  }
              } );

              Calendar calendar = Calendar.getInstance();
              final int year =  calendar.get( Calendar.YEAR );
              final int month = calendar.get( Calendar.MONTH );
              final int day = calendar.get( Calendar.DAY_OF_MONTH );
              final int hour = calendar.get( Calendar.HOUR_OF_DAY );
              final int minutes = calendar.get( Calendar.MINUTE );
              daycustomLo.setOnClickListener( new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {


                      TimePickerDialog tP = new TimePickerDialog( getContext(),timePickerListener,hour,minutes,false );
                      tP.show();
                      DatePickerDialog dP = new DatePickerDialog( Objects.requireNonNull( getContext() ),datePickerDialog,year,month,day );
                      dP.show();
                  }
              } );
              timeLo.setOnClickListener( new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      TimePickerDialog tP = new TimePickerDialog( getContext(),timePickerListener,hour,minutes,false );
                      tP.show();
                  }
              } );



    }



    @Override
    public void onPause() {
        super.onPause();
        closeKeyboard();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach( context );

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
                    goToTaskFrag();

                    return true;
                }
                return false;
            }
        } );


    }

    @Override
    public void onDetach() {
        super.onDetach();
        closeKeyboard();
    }


    private void goToTaskFrag() {
        MainActivity mainActivity = (MainActivity) getActivity();
        AllTasksFrag allTasksFrag = new AllTasksFrag();
        assert mainActivity != null;
        mainActivity.loadmyfrag( allTasksFrag );
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull( getContext() ).getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.toggleSoftInput( InputMethodManager.SHOW_FORCED, 0 );
    }


}
