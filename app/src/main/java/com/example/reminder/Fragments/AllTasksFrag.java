package com.example.reminder.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.reminder.Activity.MainActivity;
import com.example.reminder.adapter.AllTasksAdapter;
import com.example.reminder.classes.AlarmSettingClass;
import com.example.reminder.classes.MyTimeSettingClass;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.R;
import com.example.reminder.models.AllTasksModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.reminder.classes.MyTimeSettingClass.getToday9am;
import static com.example.reminder.classes.MyTimeSettingClass.todayPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.tomorrowPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.nextWeekPlaceDate;


public class AllTasksFrag extends Fragment {
    private MainActivity mainActivity;
    private MyTimeSettingClass myTimeSettingClass;
    private AlarmSettingClass alarmSettingClass;


    //actionbar
    ImageView tasksMenuImageView;

   //
    private HorizontalScrollView horizontalScrollViewTags;
    private RelativeLayout relativeLayoutAddEt;

    private RecyclerView recyclerView_today, recyclerView_tomorrow, recyclerView_upcoming, recyclerView_someday;


    private EditText add_task_edittext;
    private Button addtodayBtn, addtomorrowbtn, addupcomingbtn, addsomedaybtn ;
    private String text = "", reminder_date = "", date_to_place_task = "";
    private SimpleDateFormat sformat;
    private LottieAnimationView addUpLottieAnimationView, mainAddLottieAnimationView;

    private boolean thisMorning = false,
            laterToday = false,
            thisEvening = false,
            tomorrow = false,
            upcoming = false,
            someday = false,
            custom = false;


    private LinearLayout latertodaytagLL, thisMorningTagLL, thisEveningTagLL, tomorrowtagLL, upcomingtagLL, somedaytagLL, customTagLL;
    private ImageView thisMorningTagImageView, laterTodayTagImageView, thisEveningTagImageView,
            tomorrowTagImageView, nextWeekTagImageView, somedayTagImageView, customTagImageView;

    private Calendar calendar = Calendar.getInstance();
    private Calendar taskCreatedDate = Calendar.getInstance();
    private SimpleDateFormat taskCreatedDateSF = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm:ss a" );
    private int checkYear, currentYear, istomorrow, mtomorrow, isToday, mtoday;

    private DataBaseHelper dataBaseHelper;

    FragmentManager fragmentManager;

    private InputListFrag inputListFrag = new InputListFrag();
    private Bundle bundle = new Bundle();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_tasks, container, false );
        alarmSettingClass = new AlarmSettingClass( getActivity() );


        mainActivity = (MainActivity) getActivity();
        myTimeSettingClass = new MyTimeSettingClass();
        fragmentManager = getActivity().getSupportFragmentManager();
        //actionbar
        tasksMenuImageView = view.findViewById( R.id.tasksMenuImageView );

        recyclerView_today = view.findViewById( R.id.todaytasksRecyclerView );
        recyclerView_tomorrow = view.findViewById( R.id.TomorrowtasksRecyclerView );
        recyclerView_upcoming = view.findViewById( R.id.upcomingtasksRecyclerView );
        recyclerView_someday = view.findViewById( R.id.somedaytasksRecyclerView );

        thisMorningTagLL = view.findViewById( R.id.thisMorningtagLL );
        latertodaytagLL = view.findViewById( R.id.laterTodaytagLL );
        thisEveningTagLL = view.findViewById( R.id.thisEveningtagLL );
        tomorrowtagLL = view.findViewById( R.id.tomorrowtagLL );
        upcomingtagLL = view.findViewById( R.id.nextweekLL );
        somedaytagLL = view.findViewById( R.id.somedayLL );
        customTagLL = view.findViewById( R.id.customLL );

        thisMorningTagImageView = view.findViewById( R.id.thisMorningIv );
        laterTodayTagImageView = view.findViewById( R.id.mainTaskFraglaterTodayIv );
        thisEveningTagImageView = view.findViewById( R.id.thisEveningIv );
        tomorrowTagImageView = view.findViewById( R.id.tomorrowIv );
        nextWeekTagImageView = view.findViewById( R.id.nextweekIv );
        somedayTagImageView = view.findViewById( R.id.somedayIv );
        customTagImageView = view.findViewById( R.id.customIv );


        add_task_edittext = view.findViewById( R.id.addtaskedittext );
        addtodayBtn = view.findViewById( R.id.Todaytaskaddbutton );
        addtomorrowbtn = view.findViewById( R.id.tomorrowtaskaddbutton );
        addupcomingbtn = view.findViewById( R.id.upcomingtaskaddbutton );
        addsomedaybtn = view.findViewById( R.id.somedaytaskaddbutton );
        mainAddLottieAnimationView = view.findViewById( R.id.mainAddLottieAnimationView );
        addUpLottieAnimationView = view.findViewById( R.id.AddUpLottieAnimationView );

        horizontalScrollViewTags = view.findViewById( R.id.tagsLlayout );


        dataBaseHelper = new DataBaseHelper( getContext() );
        addUpLottieAnimationView.setVisibility( View.GONE );

        hidelinearLayoutTags();
        actionBarFun();
        readTodayfromDb();
        readTomorrowFromDb();
        readUpcomingfromDb();
        readSomedayFromDb();
        loadedfragonbtnclick();
        witchTagLayoutIsTouched();

       getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        add_task_edittext.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addUpLottieAnimationView.setVisibility( View.VISIBLE );
                    mainAddLottieAnimationView.setVisibility( View.GONE );

                    add_task_edittext.post( new Runnable() {
                        @Override
                        public void run() {
                            add_task_edittext.requestFocus();
                            InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull( getActivity() ).getSystemService( Context.INPUT_METHOD_SERVICE );
                            inputMethodManager.showSoftInput( add_task_edittext, InputMethodManager.SHOW_FORCED );
                            add_task_edittext.setImeOptions( EditorInfo.IME_ACTION_DONE );
                            add_task_edittext.setSingleLine();
                            showlinearLayoutTags();
                            showTagsWRTTime();

                        }
                    } );
                }
                return true;
            }
        } );


        addUpLottieAnimationView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataInRecyclerView();
                add_task_edittext.setText( "" );

            }
        } );


        add_task_edittext.setOnEditorActionListener( new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    horizontalScrollViewTags.setVisibility( View.GONE );
                    addUpLottieAnimationView.setVisibility( View.GONE );
                    mainAddLottieAnimationView.setVisibility( View.VISIBLE );
                    setDataInRecyclerView();
                    add_task_edittext.setText( "" );

                }
                return false;
            }
        } );


        return view;
    }
    //actionbar
    private void actionBarFun()
    {
        tasksMenuImageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasksMenuImageView.setImageResource( R.drawable.option );
                final PopupMenu popupMenu = new PopupMenu( getContext(),tasksMenuImageView );
                popupMenu.getMenuInflater().inflate( R.menu.taskactionmenu,popupMenu.getMenu() );
                popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.clearCompletedTasks) {
                            try {
                                Cursor cursor = dataBaseHelper.getAllTasks();
                                if (cursor.getCount()==0)
                                {}
                                while (cursor.moveToNext())
                                {
                                    String position = cursor.getString( 0 );
                                    String isCompleted = cursor.getString( 6 );
                                    if (isCompleted==null)
                                    {}
                                    else
                                    if (isCompleted.matches( "yes" ))
                                    {
                                        alarmSettingClass.deleteRepeatAlarm( Integer.parseInt( position ) );
                                        dataBaseHelper.deleteEachCompletedTask(position);

                                    }
                                }

                                mainActivity.setTaskFragDefaultBNBItem();
                            }
                            catch (Exception e)
                            {
                                //error
                            }

                            tasksMenuImageView.setImageResource( R.drawable.menu );

                            popupMenu.dismiss();
                        }
                        return true;
                    }
                } );
                popupMenu.show();
                popupMenu.setOnDismissListener( new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        tasksMenuImageView.setImageResource( R.drawable.menu );

                    }
                } );

            }
        } );
    }



    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull( getView() ).setFocusableInTouchMode( true );
        getView().requestFocus();
        getView().setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == MotionEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {

                    hidelinearLayoutTags();
                    addUpLottieAnimationView.setVisibility( View.GONE );
                    mainAddLottieAnimationView.setVisibility( View.VISIBLE );
                    mainActivity.setTaskFragDefaultBNBItem();
                    return  true;
                }
                return false;
            }
        } );
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach( context );

    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull( getActivity() ).getSystemService( Context.INPUT_METHOD_SERVICE );
        inputMethodManager.hideSoftInputFromWindow( add_task_edittext.getWindowToken(), 0 );
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void loadedfragonbtnclick() {


        addtodayBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag("today_Clicked");
            }
        } );
        addtomorrowbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setfrag("tomorrow_Clicked");
            }
        } );
        addupcomingbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setfrag("upcoming_Clicked");
            }
        } );
        addsomedaybtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag("someday_Clicked");
            }
        } );
        mainAddLottieAnimationView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag("main_Clicked");
            }
        } );

    }

    private void setfrag(String whichBTn) {
        Fragment fragment = new InputListFrag();
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        bundle.putString( "get_btn",whichBTn );
        fragment.setArguments( bundle );
        fragmentTransaction.replace( R.id.fragcontainer, fragment );
        fragmentTransaction.commit();
    }


    private void hidelinearLayoutTags() {
        horizontalScrollViewTags.setVisibility( View.GONE );

    }

    private void showlinearLayoutTags() {
        horizontalScrollViewTags.setVisibility( View.VISIBLE );
    }


    @Override
    public void onStart() {
        super.onStart();

    }


    // checking which tag is selected.then add task to that tag
    private void setDataInRecyclerView() {


        if (thisMorning) {
            insertTodayMorningFun();
            thisMorning = false;
        } else if (laterToday) {
            insertLaterTodayTasksFun();
            laterToday = false;
        } else if (thisEvening) {
            insertThisEveningTaskFun();
            thisEvening = false;
        } else if (tomorrow) {
            insertTomorrowTasksFun();
            tomorrow = false;
        } else if (upcoming) {
            insertUpcomingTasksFun();
            upcoming = false;
        } else if (someday) {
            insertSomedayTasksFun();
            someday = false;
        } else if (custom) {
            insertCustomTaskFun();
            custom = false;
        } else {
            insertLaterTodayTasksFun();
        }
    }

    public void witchTagLayoutIsTouched() {
        thisMorningTagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMorning = true;
                laterToday = false;
                thisEvening = false;
                tomorrow = false;
                upcoming = false;
                someday = false;
                custom = false;
                setTagsBackground( thisMorningTagLL, thisMorningTagImageView, latertodaytagLL, thisEveningTagLL, tomorrowtagLL,
                        upcomingtagLL, somedaytagLL, customTagLL, tomorrowTagImageView, laterTodayTagImageView, thisEveningTagImageView,
                        nextWeekTagImageView, somedayTagImageView, customTagImageView );
            }
        } );
        latertodaytagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                laterToday = true;
                thisMorning = false;
                thisEvening = false;
                tomorrow = false;
                upcoming = false;
                someday = false;
                custom = false;
                setTagsBackground( latertodaytagLL, laterTodayTagImageView, thisMorningTagLL, thisEveningTagLL, tomorrowtagLL, upcomingtagLL,
                        somedaytagLL, customTagLL, tomorrowTagImageView, thisMorningTagImageView, thisEveningTagImageView, nextWeekTagImageView,
                        somedayTagImageView, customTagImageView );
            }
        } );
        thisEveningTagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laterToday = false;
                thisEvening = true;
                tomorrow = false;
                upcoming = false;
                someday = false;
                custom = false;

                setTagsBackground( thisEveningTagLL, thisEveningTagImageView, thisMorningTagLL, latertodaytagLL, tomorrowtagLL, upcomingtagLL,
                        somedaytagLL, customTagLL, tomorrowTagImageView, laterTodayTagImageView, thisMorningTagImageView, nextWeekTagImageView, somedayTagImageView, customTagImageView );


            }
        } );
        tomorrowtagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMorning = false;
                laterToday = false;
                thisEvening = false;
                tomorrow = true;
                upcoming = false;
                someday = false;
                custom = false;
                setTagsBackground( tomorrowtagLL, tomorrowTagImageView, latertodaytagLL, thisMorningTagLL, thisEveningTagLL,
                        upcomingtagLL, somedaytagLL, customTagLL, laterTodayTagImageView, thisMorningTagImageView,
                        thisEveningTagImageView, nextWeekTagImageView, somedayTagImageView, customTagImageView );
            }
        } );

        upcomingtagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMorning = false;
                laterToday = false;
                thisEvening = false;
                tomorrow = false;
                upcoming = true;
                someday = false;
                custom = false;
                setTagsBackground( upcomingtagLL, nextWeekTagImageView, latertodaytagLL, thisMorningTagLL, thisEveningTagLL,
                        tomorrowtagLL, somedaytagLL, customTagLL, laterTodayTagImageView, thisMorningTagImageView, thisEveningTagImageView,
                        tomorrowTagImageView, somedayTagImageView, customTagImageView );
            }
        } );

        somedaytagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMorning = false;
                laterToday = false;
                thisEvening = false;
                tomorrow = false;
                upcoming = false;
                someday = true;
                custom = false;

                setTagsBackground( somedaytagLL, somedayTagImageView, latertodaytagLL, thisMorningTagLL, thisEveningTagLL, tomorrowtagLL,
                        upcomingtagLL, customTagLL, laterTodayTagImageView, thisMorningTagImageView, thisEveningTagImageView,
                        tomorrowTagImageView, nextWeekTagImageView, customTagImageView );
            }
        } );

        customTagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMorning = false;
                laterToday = false;
                thisEvening = false;
                tomorrow = false;
                upcoming = false;
                someday = false;
                custom = true;

                setTagsBackground( customTagLL, customTagImageView, thisMorningTagLL, latertodaytagLL, thisEveningTagLL, tomorrowtagLL, upcomingtagLL
                        , somedaytagLL, laterTodayTagImageView, thisMorningTagImageView, thisEveningTagImageView, tomorrowTagImageView, nextWeekTagImageView,
                        somedayTagImageView );

                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get( Calendar.YEAR );
                final int month = calendar.get( Calendar.MONTH );
                final int day = calendar.get( Calendar.DAY_OF_MONTH );


                Date newDate = calendar.getTime();


                DatePickerDialog dP = new DatePickerDialog( Objects.requireNonNull( getContext() ), datePickerDialog, year, month, day );
                dP.getDatePicker().setMinDate( System.currentTimeMillis() - 1000 );

                dP.show();


            }
        } );
    }

    private void setTagsBackground(View enAbleView, ImageView enAbleImageView, View disAbleView1,
                                   View disAbleView2, View disAbleView3, View disAbleView4, View disAbleView5, View disAbleView6, ImageView disAbleImageView1,
                                   ImageView disAbleImageView2, ImageView disAbleImageView3, ImageView disAbleImageView4,
                                   ImageView disAbleImageView5, ImageView disAbleImageView6) {
        //set layout background
        enAbleView.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundafterclick ) );
        disAbleView1.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundbeforeclick ) );
        disAbleView2.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundbeforeclick ) );
        disAbleView3.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundbeforeclick ) );
        disAbleView4.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundbeforeclick ) );
        disAbleView5.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundbeforeclick ) );
        disAbleView6.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundbeforeclick ) );

        //set images
        enAbleImageView.setImageResource( R.drawable.bell );
        disAbleImageView1.setImageResource( R.drawable.notifi );
        disAbleImageView2.setImageResource( R.drawable.notifi );
        disAbleImageView3.setImageResource( R.drawable.notifi );
        disAbleImageView4.setImageResource( R.drawable.notifi );
        disAbleImageView5.setImageResource( R.drawable.notifi );
        disAbleImageView6.setImageResource( R.drawable.notifi );
    }

    private void insertTodayMorningFun() {
        text = add_task_edittext.getText().toString();
        reminder_date = getToday9am();
        if (text.matches( "" )) {

        } else {
            int isInsert = dataBaseHelper.insert( text, reminder_date, todayPlaceDate(),
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ),"","1" );
            if (isInsert==0) {

                Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();

            } else {
                alarmSettingClass.setOneAlarm(text,myTimeSettingClass.getMilliFromDate( reminder_date ),isInsert);
                Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
            }
            mainActivity.setTaskFragDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
        }
    }

    private void insertLaterTodayTasksFun() {
        text = add_task_edittext.getText().toString();
        reminder_date = MyTimeSettingClass.getLaterToday();
        if (text.matches( "" )) {
            //do nothing
        } else {
            int isInsert = dataBaseHelper.insert( text, reminder_date, todayPlaceDate(),
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ),"","1");
            if (isInsert==0) {
                Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();

            } else {
                alarmSettingClass.setOneAlarm(text,myTimeSettingClass.getMilliFromDate( reminder_date ),isInsert);
                Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
            }
            mainActivity.setTaskFragDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
        }
    }

    private void insertThisEveningTaskFun() {
        text = add_task_edittext.getText().toString();
        reminder_date = MyTimeSettingClass.getToday6pm();
        if (text.matches( "" )) {
            //do nothing
        } else {
            int isInsert = dataBaseHelper.insert( text, reminder_date, todayPlaceDate(),
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ),"","1" );
            if (isInsert==0) {
                alarmSettingClass.setOneAlarm(text,myTimeSettingClass.getMilliFromDate( reminder_date ),isInsert);
                Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
            }
            mainActivity.setTaskFragDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
        }
    }

    private void insertTomorrowTasksFun() {
        text = add_task_edittext.getText().toString();
        reminder_date = MyTimeSettingClass.getTomorrowMorning();
        String tomorrow = tomorrowPlaceDate();
        if (text.matches( "" )) {

        } else {
            int isInsert = dataBaseHelper.insert( text, reminder_date, tomorrow,
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ),"","1" );
            if (isInsert==0) {
                alarmSettingClass.setOneAlarm(text,myTimeSettingClass.getMilliFromDate( reminder_date ),isInsert);
                Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
            }
            mainActivity.setTaskFragDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
        }
    }

    private void insertUpcomingTasksFun() {
        text = add_task_edittext.getText().toString();
        reminder_date = MyTimeSettingClass.getNextWeek();
        String upcoming = nextWeekPlaceDate();
        if (text.matches( "" )) {
            //do nothing
        } else {
            int isInsert = dataBaseHelper.insert( text, reminder_date, upcoming,
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ),"","1");
            if (isInsert==0) {
                Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();

            } else {
                alarmSettingClass.setOneAlarm(text,myTimeSettingClass.getMilliFromDate( reminder_date ),isInsert);
                Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
            }
            mainActivity.setTaskFragDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
        }
    }

    private void insertSomedayTasksFun() {
        text = add_task_edittext.getText().toString();
        reminder_date = "";
        text = add_task_edittext.getText().toString();
        reminder_date = MyTimeSettingClass.getNextWeek();
        String someday = "";
        if (text.matches( "" )) {
            //do nothing
        } else {
            int isInsert = dataBaseHelper.insert( text, "", someday,
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ),"", "0" );
            if (isInsert==0) {
                Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();

            } else {
                Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
            }
            mainActivity.setTaskFragDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
        }

    }

    private void insertCustomTaskFun() {
        text = add_task_edittext.getText().toString();
        if (text.matches( "" )) {
            //do nothing
        } else {
            int isInsert = dataBaseHelper.insert( text, reminder_date, date_to_place_task,
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ),"","1" );
            if (isInsert==0) {
                Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
            } else {
                alarmSettingClass.setOneAlarm(text,myTimeSettingClass.getMilliFromDate( reminder_date ),isInsert);
                Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
            }
            mainActivity.setTaskFragDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

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
            final int hour = calendar.get( Calendar.HOUR_OF_DAY );
            final int minutes = calendar.get( Calendar.MINUTE );

            myTimeSettingClass.setCustomPlaceDate( dayOfMonth, month, year );
            date_to_place_task = MyTimeSettingClass.getCustomPlaceDate();

            TimePickerDialog tP = new TimePickerDialog( getContext(), timePickerListener, hour, minutes, false );
            tP.show();



        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            calendar.set( Calendar.HOUR_OF_DAY, hourOfDay );
            calendar.set( Calendar.MINUTE, minute );
            sformat = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm a" );
            reminder_date = sformat.format( calendar.getTime() );
            Log.i( "formated date", reminder_date );

        }
    };

    private void showTagsWRTTime() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get( Calendar.HOUR_OF_DAY );

        if (timeOfDay > 0 && timeOfDay < 9) {
            thisMorningTagLL.setVisibility( View.VISIBLE );
        } else {
            thisMorningTagLL.setVisibility( View.GONE );
        }
        if (timeOfDay > 14) {
            latertodaytagLL.setVisibility( View.GONE );
        } else {
            latertodaytagLL.setVisibility( View.VISIBLE );
        }
        if (timeOfDay > 0 && timeOfDay < 18) {
            thisEveningTagLL.setVisibility( View.VISIBLE );
        } else {
            thisEveningTagLL.setVisibility( View.GONE );
        }

    }

    private void readTodayfromDb() {
        AllTasksAdapter allTasksAdapter;
        List<AllTasksModel> model2List = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_today.setLayoutManager( linearLayoutManager );


        Cursor cursor = dataBaseHelper.getToday();
        if (cursor.getCount() == 0) {
            Log.i( "Data","no data" );
        }
        while (cursor.moveToNext()) {
            String date =cursor.getString( 2 );
            String isCom = cursor.getString( 6 );
            boolean isComBoolean = false ;
            if (isCom==null)
            {
                isComBoolean = false;
            }
            else
            if (isCom.matches( "yes" ))
            {
                isComBoolean = true;
            }
            model2List.add( new AllTasksModel( cursor.getString( 0 ), cursor.getString( 1 ), date,isComBoolean ));
        }
        allTasksAdapter = new AllTasksAdapter( getContext(), model2List, dataBaseHelper, fragmentManager );
        recyclerView_today.setAdapter( allTasksAdapter );
        allTasksAdapter.notifyDataSetChanged();

    }

    private void readTomorrowFromDb() {

        AllTasksAdapter allTasksAdapter;

        List<AllTasksModel> model2List = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_tomorrow.setLayoutManager( linearLayoutManager );


        Cursor cursor = dataBaseHelper.getTomorrow();
        if (cursor.getCount() == 0) {
            Log.i( "Data","no data" );
        }
        while (cursor.moveToNext()) {
            String date =cursor.getString( 2 );
            String isCom = cursor.getString( 6 );
            boolean isComBoolean = false ;
            if (isCom==null)
            {
                isComBoolean = false;
            }
            else
            if (isCom.matches( "yes" ))

            {
                isComBoolean = true;
            }
            model2List.add( new AllTasksModel( cursor.getString( 0 ), cursor.getString( 1 ),date,isComBoolean  ) );
        }
        allTasksAdapter = new AllTasksAdapter( getContext(), model2List, dataBaseHelper, fragmentManager );
        recyclerView_tomorrow.setAdapter( allTasksAdapter );
        allTasksAdapter.notifyDataSetChanged();


    }

    private void readUpcomingfromDb() {
        AllTasksAdapter allTasksAdapter1;
        List<AllTasksModel> allTasksModels = new ArrayList<>();
        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );

        recyclerView_upcoming.setLayoutManager( linearLayoutManager );


        Cursor cursor = dataBaseHelper.getUpcoming();
        if (cursor.getCount() == 0) {
            Log.i( "Data","no data" );
        }
        while (cursor.moveToNext()) {
            String isCom = cursor.getString( 6 );
            boolean isComBoolean = false;
            if (isCom==null)
            {
                isComBoolean = false;
            }
            else
                if (isCom.matches( "yes" ))
            {
                isComBoolean = true;
            }
            AllTasksModel filter = new AllTasksModel( cursor.getString( 0 ), cursor.getString( 1 ), cursor.getString( 2 ),isComBoolean);
            String today = todayPlaceDate();
            String tommorow = tomorrowPlaceDate();
            String someday = "";
            String provDate = cursor.getString( 3 );
            if (!provDate.equals( tommorow ) && !provDate.equals( today ) && !provDate.equals( someday ))
                allTasksModels.add( filter );
        }

        allTasksAdapter1 = new AllTasksAdapter( getContext(), allTasksModels, dataBaseHelper, fragmentManager );
        recyclerView_upcoming.setAdapter( allTasksAdapter1 );
        allTasksAdapter1.notifyDataSetChanged();

    }


    private void readSomedayFromDb() {

        AllTasksAdapter allTasksAdapter;

        List<AllTasksModel> model2List = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_someday.setLayoutManager( linearLayoutManager );


        Cursor cursor = dataBaseHelper.getSomeday();
        if (cursor.getCount() == 0) {
            Log.i( "Data","no data" );
        }
        while (cursor.moveToNext()) {
            String isCom = cursor.getString( 6 );
            boolean isComBoolean = false;
            if (isCom==null)
            {
                isComBoolean = false;
            }
            else
            if (isCom.matches( "yes" ))
            {
                isComBoolean = true;
            }
            model2List.add( new AllTasksModel( cursor.getString( 0 ), cursor.getString( 1 ), cursor.getString( 2 ),isComBoolean ) );
        }
        allTasksAdapter = new AllTasksAdapter( getContext(), model2List, dataBaseHelper, fragmentManager );
        recyclerView_someday.setAdapter( allTasksAdapter );
        allTasksAdapter.notifyDataSetChanged();


    }





}
