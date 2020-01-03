package com.example.reminder.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.adapter.AllTasksAdapter;
import com.example.reminder.classes.HideAndShowViewClass;
import com.example.reminder.classes.MyTimeSettingClass;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.R;
import com.example.reminder.interfaces.MyItemClickListener;
import com.example.reminder.models.AllTasksModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.example.reminder.classes.MyTimeSettingClass.getCustomPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.getLaterToday;
import static com.example.reminder.classes.MyTimeSettingClass.getNextWeek;
import static com.example.reminder.classes.MyTimeSettingClass.getToday9am;
import static com.example.reminder.classes.MyTimeSettingClass.todayPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.tomorrowPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.nextWeekPlaceDate;


public class AllTasksFrag extends Fragment {
    private MainActivity mainActivity ;
    private MyTimeSettingClass myTimeSettingClass;

    private LinearLayout linearLayoutTags;
    private RelativeLayout relativeLayoutAddEt;

    private RecyclerView recyclerView_today, recyclerView_tomorrow, recyclerView_upcoming, recyclerView_someday;


    private EditText add_task_edittext, input_textView;
    private Button addtodayBtn, addtomorrowbtn, addupcomingbtn, addsomedaybtn, mainAddbtn, addUpbtn;
    private String text = "", reminder_date = "", date_to_place_task = "";
    private SimpleDateFormat sformat;

    private boolean thisMorning = false,
            laterToday  = false,
            tomorrow    = false,
            upcoming    = false,
            someday     = false,
            custom      = false;


    private LinearLayout latertodaytagLL,thisMorningTagLL, tomorrowtagLL, upcomingtagLL, somedaytagLL,customTagLL;
    private ImageView thisMorningTagImageView, laterTodayTagImageView,
            tomorrowTagImageView,nextWeekTagImageView,somedayTagImageView,customTagImageView;

    Calendar calendar =  Calendar.getInstance();


    private DataBaseHelper dataBaseHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_tasks, container, false );

        mainActivity = (MainActivity) getActivity();
        myTimeSettingClass = new MyTimeSettingClass();
        recyclerView_today = view.findViewById( R.id.todaytasksRecyclerView );
        recyclerView_tomorrow = view.findViewById( R.id.TomorrowtasksRecyclerView );
        recyclerView_upcoming = view.findViewById( R.id.upcomingtasksRecyclerView );
        recyclerView_someday = view.findViewById( R.id.somedaytasksRecyclerView );

        thisMorningTagLL = view.findViewById( R.id.thisMorningtagLL );
        latertodaytagLL  = view.findViewById( R.id.laterTodaytagLL );
        tomorrowtagLL    = view.findViewById( R.id.tomorrowtagLL );
        upcomingtagLL    = view.findViewById( R.id.nextweekLL );
        somedaytagLL     = view.findViewById( R.id.somedayLL );
        customTagLL      = view.findViewById( R.id.customLL );

        thisMorningTagImageView = view.findViewById( R.id.thisMorningIv );
        laterTodayTagImageView  = view.findViewById( R.id.laterTodayIv );
        tomorrowTagImageView    = view.findViewById( R.id.tomorrowIv );
        nextWeekTagImageView    = view.findViewById( R.id.nextweekIv );
        somedayTagImageView     = view.findViewById( R.id.somedayIv );
        customTagImageView      = view.findViewById( R.id.customIv );


        add_task_edittext = view.findViewById( R.id.addtaskedittext );
        input_textView = view.findViewById( R.id.inputRVItemTV );
        addtodayBtn = view.findViewById( R.id.Todaytaskaddbutton );
        addtomorrowbtn = view.findViewById( R.id.tomorrowtaskaddbutton );
        addupcomingbtn = view.findViewById( R.id.upcomingtaskaddbutton );
        addsomedaybtn = view.findViewById( R.id.somedaytaskaddbutton );
        mainAddbtn = view.findViewById( R.id.mainAddButton );
        addUpbtn = view.findViewById( R.id.AddUpButton );

        linearLayoutTags =view.findViewById( R.id.tagsLlayout );
        relativeLayoutAddEt = view.findViewById( R.id.addETRelativeLayout );



        dataBaseHelper = new DataBaseHelper( getContext() );
        addUpbtn.setVisibility( View.GONE );


        hidelinearLayoutTags();

        readTodayfromDb();
        readTomorrowFromDb();
        readUpcomingfromDb();
        loadedfragonbtnclick();
        witchTagLayoutIsTouched();


        add_task_edittext.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addUpbtn.setVisibility( View.VISIBLE );
                    mainAddbtn.setVisibility( View.GONE );

                    mainActivity.hideBottomNView();
                    add_task_edittext.post( new Runnable() {
                        @Override
                        public void run() {
                            add_task_edittext.requestFocus();
                            InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull( getActivity() ).getSystemService( Context.INPUT_METHOD_SERVICE );
                            inputMethodManager.showSoftInput( add_task_edittext, InputMethodManager.SHOW_FORCED );
                            add_task_edittext.setImeOptions( EditorInfo.IME_ACTION_DONE );
                            add_task_edittext.setSingleLine();
                            showlinearLayoutTags();

                        }
                    } );
                }
                return true;
            }
        } );


        addUpbtn.setOnClickListener( new View.OnClickListener() {
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


                    HideAndShowViewClass.hideView( linearLayoutTags );
                    HideAndShowViewClass.hideView( addUpbtn );
                    HideAndShowViewClass.showView(mainAddbtn);
                    mainActivity.showBottomNView();
                    setDataInRecyclerView();
                    add_task_edittext.setText( "" );


                }
                return false;
            }
        } );


        return view;
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
                        addUpbtn.setVisibility( View.GONE );
                        mainAddbtn.setVisibility( View.VISIBLE );
//                        HideAndShowViewClass.hideView( addUpbtn );
//                        HideAndShowViewClass.showView( mainAddbtn );
                        mainActivity.showBottomNView();

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
    public void onDetach() {
        super.onDetach();
    }

    private void setfrag() {
        Fragment fragment = new InputListFrag();
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.fragcontainer, fragment );
        fragmentTransaction.commit();
    }

    private void loadedfragonbtnclick() {

        addtodayBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag();
            }
        } );
        addtomorrowbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag();
            }
        } );
        addupcomingbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag();
            }
        } );
        addsomedaybtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag();
            }
        } );
        mainAddbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag();
            }
        } );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.throwmenu:
        }
        return false;
    }


    private void hidelinearLayoutTags() {
        linearLayoutTags.setVisibility( View.GONE );

    }

    private void showlinearLayoutTags() {
        linearLayoutTags.setVisibility( View.VISIBLE );
    }


    @Override
    public void onStart() {
        super.onStart();

    }



    // checking which tag is selected.then add task to that tag
    private void setDataInRecyclerView() {


        if (thisMorning)
        {
            insertTodayMorningFun();
            thisMorning = false;
        }
       else if (laterToday)
       {
           insertLaterTodayTasksFun();
           laterToday =false;
       }
       else if (tomorrow)
       {
           insertTomorrowTasksFun();
           tomorrow=false;
       }
       else if (upcoming)
       {
           insertUpcomingTasksFun();
           upcoming=false;
       }
       else if (someday)
       {
           insertSomedayTasksFun();
           someday=false;
       }
       else
           if (custom)
           {
               insertCustomTaskFun();
             custom = false;
           }
       else
       {
           insertLaterTodayTasksFun();
       }
    }

    public void witchTagLayoutIsTouched()
    {
        thisMorningTagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMorning = true;
                laterToday =false;
                tomorrow = false;
                upcoming = false;
                someday  = false;
                custom = false;
                setTagsBackground(thisMorningTagLL,thisMorningTagImageView,latertodaytagLL,tomorrowtagLL ,upcomingtagLL ,somedaytagLL,
                        customTagLL,tomorrowTagImageView,laterTodayTagImageView,nextWeekTagImageView,
                        somedayTagImageView,customTagImageView );
            }
        } );
        latertodaytagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                laterToday =true;
                thisMorning = false;
                tomorrow = false;
                upcoming = false;
                someday  = false;
                custom   = false;
                setTagsBackground(latertodaytagLL  ,laterTodayTagImageView  ,thisMorningTagLL,tomorrowtagLL ,upcomingtagLL,
                        somedaytagLL,customTagLL,tomorrowTagImageView,thisMorningTagImageView,nextWeekTagImageView,somedayTagImageView,customTagImageView );
            }
        } );
        tomorrowtagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMorning = false;
                laterToday =false;
                tomorrow = true;
                upcoming = false;
                someday  = false;
                custom   = false;
                setTagsBackground(tomorrowtagLL ,tomorrowTagImageView , latertodaytagLL,thisMorningTagLL,upcomingtagLL ,somedaytagLL,
                        customTagLL,laterTodayTagImageView,thisMorningTagImageView,nextWeekTagImageView,
                        somedayTagImageView,customTagImageView );
            }
        } );

        upcomingtagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMorning = false;
                laterToday =false;
                tomorrow = false;
                upcoming = true;
                someday  = false;
                custom   = false;
                setTagsBackground( upcomingtagLL,nextWeekTagImageView, latertodaytagLL,thisMorningTagLL,tomorrowtagLL,somedaytagLL,
                        customTagLL,laterTodayTagImageView,thisMorningTagImageView,tomorrowTagImageView,somedayTagImageView,customTagImageView );
            }
        } );

        somedaytagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMorning = false;
                laterToday =false;
                tomorrow = false;
                upcoming = false;
                someday  = true;
                custom   = false;

                setTagsBackground( somedaytagLL,somedayTagImageView, latertodaytagLL,thisMorningTagLL,tomorrowtagLL,upcomingtagLL
                        ,customTagLL,laterTodayTagImageView,thisMorningTagImageView,tomorrowTagImageView,
                        nextWeekTagImageView,customTagImageView );
            }
        } );

        customTagLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMorning = false;
                laterToday  = false;
                tomorrow = false;
                upcoming = false;
                someday  = false;
                custom   = true;

                setTagsBackground( customTagLL, customTagImageView ,thisMorningTagLL,latertodaytagLL, tomorrowtagLL, upcomingtagLL
                        ,somedaytagLL,laterTodayTagImageView,thisMorningTagImageView,tomorrowTagImageView,nextWeekTagImageView,
                        somedayTagImageView );

                Calendar calendar = Calendar.getInstance();
                final int year =  calendar.get( Calendar.YEAR );
                final int month = calendar.get( Calendar.MONTH );
                final int day = calendar.get( Calendar.DAY_OF_MONTH );
                final int hour = calendar.get( Calendar.HOUR_OF_DAY );
                final int minutes = calendar.get( Calendar.MINUTE );

                TimePickerDialog tP = new TimePickerDialog( getContext(),timePickerListener,hour,minutes,false );
                tP.show();
                DatePickerDialog dP = new DatePickerDialog( Objects.requireNonNull( getContext() ),datePickerDialog,year,month,day );
                dP.show();


            }
        } );
    }


    private void setTagsBackground(View enAbleView,ImageView enAbleImageView, View disAbleView1,
                                   View disAbleView2, View disAbleView3,View disAbleView4,View disAbleView5,ImageView disAbleImageView1,
                                   ImageView disAbleImageView2,ImageView disAbleImageView3,ImageView disAbleImageView4,
                                   ImageView disAbleImageView5)
    {
        //set layout background
        enAbleView.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundafterclick ) );
        disAbleView1.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundbeforeclick ) );
        disAbleView2.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundbeforeclick ) );
        disAbleView3.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundbeforeclick ) );
        disAbleView4.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundbeforeclick ) );
        disAbleView5.setBackground( getResources().getDrawable( R.drawable.tagsbackgroundbeforeclick ) );

        //set images
        enAbleImageView.setImageResource( R.drawable.notification_active_foreground );
        disAbleImageView1.setImageResource( R.drawable.notification_foreground );
        disAbleImageView2.setImageResource( R.drawable.notification_foreground );
        disAbleImageView3.setImageResource( R.drawable.notification_foreground );
        disAbleImageView4.setImageResource( R.drawable.notification_foreground );
        disAbleImageView5.setImageResource( R.drawable.notification_foreground );

    }

    private void insertTodayMorningFun()
    {
        text = add_task_edittext.getText().toString();
        reminder_date = getToday9am();
        if (text.matches( "" ))
        {

        }
        else {
            boolean isInsert = dataBaseHelper.insert( text, reminder_date, todayPlaceDate() );
            if (isInsert) {
                Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
            }
            mainActivity.setTaskFragDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
        }
    }

    private void insertLaterTodayTasksFun() {
        text = add_task_edittext.getText().toString();
        reminder_date = MyTimeSettingClass.getLaterToday();
        if (text.matches( "" ))
        {
          //do nothing
        }
        else {
            boolean isInsert = dataBaseHelper.insert( text, reminder_date, todayPlaceDate() );
            if (isInsert) {
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
        if (text.matches( "" ))
        {

        }
        else {
            boolean isInsert = dataBaseHelper.insert( text, reminder_date, tomorrow );
            if (isInsert) {
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
        if (text.matches( "" ))
        {
            //do nothing
        }
        else {
            boolean isInsert = dataBaseHelper.insert( text, reminder_date, upcoming );
            if (isInsert) {
                Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
            }
            mainActivity.setTaskFragDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
        }
    }

    private void insertSomedayTasksFun() {
        text = add_task_edittext.getText().toString();
        reminder_date = "";

    }

    private void insertCustomTaskFun()
    {
        text = add_task_edittext.getText().toString();
        if (text.matches( "" ))
        {
            //do nothing
        }
        else {
            boolean isInsert = dataBaseHelper.insert( text, reminder_date, date_to_place_task );
            if (isInsert) {
                Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
            }
            mainActivity.setTaskFragDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            calendar.set( Calendar.YEAR,year );
            myTimeSettingClass.setCustomPlaceDate( dayOfMonth,month,year );
            date_to_place_task = MyTimeSettingClass.getCustomPlaceDate();


        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calendar1 = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE,minute );

            int checkYear = calendar.get( Calendar.YEAR );
            int currentYear =calendar1.get( Calendar.YEAR );
            int istomorrow = calendar.get(Calendar.DAY_OF_MONTH);
            int tomorrow   = calendar1.get( Calendar.DAY_OF_MONTH )+1;
            int isToday  = calendar.get( Calendar.DAY_OF_MONTH);
            int today    = calendar1.get( Calendar.DAY_OF_MONTH );

            if (minute == 0 && (checkYear>currentYear || checkYear<currentYear) )
            {
                sformat = new SimpleDateFormat( "dd MMM yyyy, h a" );
            }
            else
            if (minute != 0 && (checkYear>currentYear || checkYear<currentYear))
            {
                sformat = new SimpleDateFormat( "d MMM yyyy, h:mm a" );
            }

            if (minute == 0 && checkYear == currentYear  )
            {
                sformat = new SimpleDateFormat( "d MMM, h a" );
            }
            else
            if (minute != 0 && checkYear == currentYear  )
            {
                sformat = new SimpleDateFormat( "d MMM, h:mm a" );
            }

            if(minute == 0 && istomorrow==tomorrow  ) {
                sformat = new SimpleDateFormat( "EEE, h a " );
            }
            else
            if(minute != 0 && istomorrow==tomorrow )
            {
                sformat = new SimpleDateFormat( "EEE, h:mm a" );
            }

            if (minute == 0 && isToday==today)
            {
                sformat = new SimpleDateFormat( "h a" );
            }
            else
            if (minute!=0 && isToday==today )
            {
                sformat =new SimpleDateFormat( "h:mm a" );
            }
            reminder_date = sformat.format( calendar.getTime() );

        }
    };
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
            Toast.makeText( getContext(), "No Data In Db", Toast.LENGTH_SHORT ).show();
        }
        while (cursor.moveToNext()) {
            model2List.add( new AllTasksModel(cursor.getString( 0 ),cursor.getString( 1 ), cursor.getString( 2 ) ) );
        }
        allTasksAdapter = new AllTasksAdapter( getContext(), model2List,dataBaseHelper );
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
            Toast.makeText( getContext(), "No Data In Db", Toast.LENGTH_SHORT ).show();
        }
        while (cursor.moveToNext()) {
            model2List.add( new AllTasksModel(cursor.getString( 0 ),cursor.getString( 1 ), cursor.getString( 2 ) ) );
        }
        allTasksAdapter = new AllTasksAdapter( getContext(), model2List,dataBaseHelper );
        recyclerView_tomorrow.setAdapter( allTasksAdapter );
        allTasksAdapter.notifyDataSetChanged();


    }

    private void readUpcomingfromDb() {
        AllTasksAdapter allTasksAdapter;
        List<AllTasksModel> allTasksModels = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_upcoming.setLayoutManager( linearLayoutManager );


        Cursor cursor = dataBaseHelper.getUpcoming();
        if (cursor.getCount() == 0) {
            Toast.makeText( getContext(), "No Data In Db", Toast.LENGTH_SHORT ).show();
        }
        while (cursor.moveToNext()) {
            allTasksModels.add( new AllTasksModel(cursor.getString( 0 ),cursor.getString( 1 ), cursor.getString( 2 ) ) );
        }

        allTasksAdapter = new AllTasksAdapter( getContext(), allTasksModels,dataBaseHelper );
        recyclerView_upcoming.setAdapter( allTasksAdapter );
        allTasksAdapter.notifyDataSetChanged();

    }

}
