package com.example.reminder.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.adapter.AllTasksAdapter;
import com.example.reminder.utilities.AlarmSettingClass;
import com.example.reminder.utilities.MyTimeSettingClass;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.R;
import com.example.reminder.models.AllTasksModel;
import com.example.reminder.utilities.SortingTypes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.reminder.utilities.MyTimeSettingClass.getToday9am;
import static com.example.reminder.utilities.MyTimeSettingClass.todayPlaceDate;
import static com.example.reminder.utilities.MyTimeSettingClass.tomorrowPlaceDate;
import static com.example.reminder.utilities.MyTimeSettingClass.nextWeekPlaceDate;


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
    private Button addtodayBtn, addtomorrowbtn, addupcomingbtn, addsomedaybtn;
    private String text = "", reminder_date = "", date_to_place_task = "";
    private SimpleDateFormat sformat;
    private RelativeLayout addUpRLL, mainAddRLL;

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
    @SuppressLint("NewApi")
    private SimpleDateFormat taskCreatedDateSF = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm:ss a" );
    private int checkYear, currentYear, istomorrow, mtomorrow, isToday, mtoday;

    private DataBaseHelper dataBaseHelper;

    FragmentManager fragmentManager;

    private InputListFrag inputListFrag = new InputListFrag();
    private Bundle bundle = new Bundle();

    SharedPreferences sharedPreferences;
    List<AllTasksModel> todayFilterList = new ArrayList<>();
    List<AllTasksModel> tomorrowFilterList = new ArrayList<>();
    List<AllTasksModel> upcomingFiltertodayList = new ArrayList<>();
    List<AllTasksModel> somedayFilterList = new ArrayList<>();

    List<AllTasksModel> defaultTodayList = new ArrayList<>();

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
        sharedPreferences = getContext().getSharedPreferences( "MY_PREFERENCES", Context.MODE_PRIVATE );

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
        mainAddRLL = view.findViewById( R.id.mainAddLottieAnimationView );
        addUpRLL = view.findViewById( R.id.AddUpLottieAnimationView );

        horizontalScrollViewTags = view.findViewById( R.id.tagsLlayout );


        dataBaseHelper = new DataBaseHelper( getContext() );
        addUpRLL.setVisibility( View.GONE );

        hidelinearLayoutTags();
//        defaultTaskList();
        actionBarFun();
        loadedfragonbtnclick();
        witchTagLayoutIsTouched();
        String sOrder = sharedPreferences.getString( "sort_order", "Default Order" );
        if (sOrder.matches( "Default Order" )) {
            defaultTaskList();

        } else if (sOrder.matches( "Ascending Order" )) {
            taskSortInAscendingOrderFuc();

        } else if (sOrder.matches( "Descending Order" )) {
            taskSortInDescendingOrderFuc();
        }

        getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN );
        add_task_edittext.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    addUpRLL.setVisibility( View.VISIBLE );
                    mainAddRLL.setVisibility( View.GONE );

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


        addUpRLL.setOnClickListener( new View.OnClickListener() {
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
                    addUpRLL.setVisibility( View.GONE );
                    mainAddRLL.setVisibility( View.VISIBLE );
                    setDataInRecyclerView();
                    add_task_edittext.setText( "" );

                }
                return false;
            }
        } );


        return view;
    }

    //actionbar
    private void actionBarFun() {

        tasksMenuImageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasksMenuImageView.setImageResource( R.drawable.option );
                PopupWindow popupwindow_obj = popupWindowDisplay();
                popupwindow_obj.showAsDropDown( tasksMenuImageView );

            }
        } );
    }

    // popupWindow menu function  😍😍😍😍😍😍
    private PopupWindow popupWindowDisplay() {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final PopupWindow popupWindow = new PopupWindow( getContext() );
        // inflate your layout or dynamically add view
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View view = inflater.inflate( R.layout.completedtaskfilterpopupmenu, null );

        LinearLayout sortTasksItemLL = view.findViewById( R.id.sortTasksItemLL );
        final TextView sortAscedAndDecendTv = view.findViewById( R.id.sortAscedAndDecendTv );
//        LinearLayout allDataItem = view.findViewById( R.id.sortTasksItemLL );
        LinearLayout clearCompletedTasksItemLL = view.findViewById( R.id.clearCompletedTasksItemLL );
        popupWindow.setFocusable( true );
        popupWindow.setWidth( WindowManager.LayoutParams.WRAP_CONTENT );
        popupWindow.setHeight( WindowManager.LayoutParams.WRAP_CONTENT );
        popupWindow.setContentView( view );
        popupWindow.setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        sortAscedAndDecendTv.setText( sharedPreferences.getString( "sort_order", "Default Order" ) );

        clearCompletedTasksItemLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Cursor cursor = dataBaseHelper.getAllTasks();
                    if (cursor.getCount() == 0) {
                    }
                    while (cursor.moveToNext()) {
                        String position = cursor.getString( 0 );
                        String isCompleted = cursor.getString( 6 );
                        if (isCompleted == null) {
                        } else if (isCompleted.matches( "yes" )) {
                            alarmSettingClass.deleteRepeatAlarm( Integer.parseInt( position ) );
                            dataBaseHelper.deleteEachCompletedTask( position );
                        }
                    }
                    mainActivity.setTaskFragDefaultBNBItem();
                    popupWindow.dismiss();
                } catch (Exception e) {
                    //error
                }
            }
        } );
        sortTasksItemLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sOrder = sharedPreferences.getString( "sort_order", "Default Order" );
                if (sOrder.matches( "Default Order" )) {
                    mainActivity.setTaskFragDefaultBNBItem();
                    sortAscedAndDecendTv.setText( "Ascending Order" );
                    editor.putString( "sort_order", "Ascending Order" ).commit();
                } else if (sOrder.matches( "Ascending Order" )) {
                    //TODO
                    mainActivity.setTaskFragDefaultBNBItem();
                    sortAscedAndDecendTv.setText( "Descending Order" );
                    editor.putString( "sort_order", "Descending Order" ).commit();
                } else if (sOrder.matches( "Descending Order" )) {
                    //TODO
                    mainActivity.setTaskFragDefaultBNBItem();
                    sortAscedAndDecendTv.setText( "Default Order" );
                    editor.putString( "sort_order", "Default Order" ).commit();
                }


            }
        } );
        popupWindow.setOnDismissListener( new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tasksMenuImageView.setImageResource( R.drawable.menu );
            }
        } );

        return popupWindow;
    }

    public void sortFun(List<AllTasksModel> list, SortingTypes types, final boolean isAscending) {
        if (types == SortingTypes.ByDate) {
            Collections.sort( list, new Comparator<AllTasksModel>() {
                public int compare(AllTasksModel obj1, AllTasksModel obj2) {
                    if (obj1.getDate() == null || obj2.getDate() == null) return 0;

                    if (isAscending) {
                        // ## Ascending order
                        return obj1.getDate().compareToIgnoreCase( obj2.getDate() ); // To compare Date values
                    } else {
                        // ## Descending order
                        return obj2.getDate().compareToIgnoreCase( obj1.getDate() ); // To compare Date values

                    }
                }
            } );
        }
    }

//ascending
    private void taskSortInAscendingOrderFuc() {

        todayFilterList.addAll( returnTodayListFromDb() );
        tomorrowFilterList.addAll( returnTomorrowListFromDb() );
        upcomingFiltertodayList.addAll( returnUpcomingListFromDb() );
        somedayFilterList.addAll( returnSomedayListFromDb() );
        sortFun( todayFilterList, SortingTypes.ByDate, true );
        sortFun( tomorrowFilterList, SortingTypes.ByDate, true );
        sortFun( upcomingFiltertodayList, SortingTypes.ByDate, true );
        sortFun( somedayFilterList, SortingTypes.ByDate, true );

        setTodayInRV( todayFilterList );
        setTomorrowInRV( tomorrowFilterList );
        setUpcomingInRV( upcomingFiltertodayList );
        setSomedayInRV( somedayFilterList );

    }
//descending
    private void taskSortInDescendingOrderFuc() {

        todayFilterList.addAll( returnTodayListFromDb() );
        tomorrowFilterList.addAll( returnTomorrowListFromDb() );
        upcomingFiltertodayList.addAll( returnUpcomingListFromDb() );
        somedayFilterList.addAll( returnSomedayListFromDb() );
        sortFun( todayFilterList, SortingTypes.ByDate, false );
        sortFun( tomorrowFilterList, SortingTypes.ByDate, false );
        sortFun( upcomingFiltertodayList, SortingTypes.ByDate, false );
        sortFun( somedayFilterList, SortingTypes.ByDate, false );

        setTodayInRV( todayFilterList );
        setTomorrowInRV( tomorrowFilterList );
        setUpcomingInRV( upcomingFiltertodayList );
        setSomedayInRV( somedayFilterList );
    }

    // default sorted tasks' list
    private void defaultTaskList() {
        setTodayInRV( returnTodayListFromDb() );
        setTomorrowInRV( returnTomorrowListFromDb() );
        setUpcomingInRV( returnUpcomingListFromDb() );
        setSomedayInRV( returnSomedayListFromDb() );
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
                    addUpRLL.setVisibility( View.GONE );
                    mainAddRLL.setVisibility( View.VISIBLE );
                    mainActivity.setTaskFragDefaultBNBItem();
                    return true;
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
                setfrag( "today_Clicked" );
            }
        } );
        addtomorrowbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag( "tomorrow_Clicked" );
            }
        } );
        addupcomingbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag( "upcoming_Clicked" );
            }
        } );
        addsomedaybtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag( "someday_Clicked" );
            }
        } );
        mainAddRLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfrag( "main_Clicked" );
            }
        } );

    }

    private void setfrag(String whichBTn) {
        Fragment fragment = new InputListFrag();
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        bundle.putString( "get_btn", whichBTn );
        fragment.setArguments( bundle );
        fragmentTransaction.addToBackStack( null );
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
            @SuppressLint({"NewApi", "LocalSuppress"}) int isInsert = dataBaseHelper.insert( text, reminder_date, todayPlaceDate(),
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "1" );
            if (isInsert == 0) {

            } else {
                alarmSettingClass.setOneAlarm( text, myTimeSettingClass.getMilliFromDate( reminder_date ), isInsert );
                //inserted
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
            @SuppressLint({"NewApi", "LocalSuppress"}) int isInsert = dataBaseHelper.insert( text, reminder_date, todayPlaceDate(),
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "1" );
            if (isInsert == 0) {
            } else {
                alarmSettingClass.setOneAlarm( text, myTimeSettingClass.getMilliFromDate( reminder_date ), isInsert );
                //inserted

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
            @SuppressLint({"NewApi", "LocalSuppress"}) int isInsert = dataBaseHelper.insert( text, reminder_date, todayPlaceDate(),
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "1" );
            if (isInsert == 0) {
            } else {
                alarmSettingClass.setOneAlarm( text, myTimeSettingClass.getMilliFromDate( reminder_date ), isInsert );
                //inserted
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
            @SuppressLint({"NewApi", "LocalSuppress"}) int isInsert = dataBaseHelper.insert( text, reminder_date, tomorrow,
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "1" );
            if (isInsert == 0) {
            } else {
                alarmSettingClass.setOneAlarm( text, myTimeSettingClass.getMilliFromDate( reminder_date ), isInsert );
                //inserted
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
            @SuppressLint({"NewApi", "LocalSuppress"}) int isInsert = dataBaseHelper.insert( text, reminder_date, upcoming,
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "1" );
            if (isInsert == 0) {

            } else {
                alarmSettingClass.setOneAlarm( text, myTimeSettingClass.getMilliFromDate( reminder_date ), isInsert );
                //inserted
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
            @SuppressLint({"NewApi", "LocalSuppress"}) int isInsert = dataBaseHelper.insert( text, "", someday,
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "0" );
            if (isInsert == 0) {

            } else {
                //inserted
            }
            mainActivity.setTaskFragDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
        }

    }

    private void insertCustomTaskFun() {
        text = add_task_edittext.getText().toString();
        if (text.matches( "" )) {
            //do nothing
        } else {
            @SuppressLint({"NewApi", "LocalSuppress"}) int isInsert = dataBaseHelper.insert( text, reminder_date, date_to_place_task,
                    taskCreatedDateSF.format( taskCreatedDate.getTime() ), "", "1" );
            if (isInsert == 0) {
            } else {
                alarmSettingClass.setOneAlarm( text, myTimeSettingClass.getMilliFromDate( reminder_date ), isInsert );
                // inserted
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
        @SuppressLint("NewApi")
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
        if (timeOfDay < 14) {
            latertodaytagLL.setVisibility( View.VISIBLE );
        } else {
            latertodaytagLL.setVisibility( View.GONE );
        }
        if (timeOfDay > 0 && timeOfDay < 18) {
            thisEveningTagLL.setVisibility( View.VISIBLE );
        } else {
            thisEveningTagLL.setVisibility( View.GONE );
        }

    }

    private void setTodayInRV(List<AllTasksModel> inList) {
        AllTasksAdapter allTasksAdapter;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_today.setLayoutManager( linearLayoutManager );
        List<AllTasksModel> list = new ArrayList<>();
        list.addAll( inList );
        allTasksAdapter = new AllTasksAdapter( getContext(), list, dataBaseHelper, fragmentManager );
        recyclerView_today.setAdapter( allTasksAdapter );
        allTasksAdapter.notifyDataSetChanged();

    }

    private void setTomorrowInRV(List<AllTasksModel> inList) {

        AllTasksAdapter allTasksAdapter;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_tomorrow.setLayoutManager( linearLayoutManager );
        List<AllTasksModel> list = new ArrayList<>();
        list.addAll( inList );
        allTasksAdapter = new AllTasksAdapter( getContext(), list, dataBaseHelper, fragmentManager );
        recyclerView_tomorrow.setAdapter( allTasksAdapter );
        allTasksAdapter.notifyDataSetChanged();


    }

    private void setUpcomingInRV(List<AllTasksModel> inList) {
        AllTasksAdapter allTasksAdapter1;
        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );

        recyclerView_upcoming.setLayoutManager( linearLayoutManager );
        List<AllTasksModel> list = new ArrayList<>();
        list.addAll( inList );

        allTasksAdapter1 = new AllTasksAdapter( getContext(), list, dataBaseHelper, fragmentManager );
        recyclerView_upcoming.setAdapter( allTasksAdapter1 );
        allTasksAdapter1.notifyDataSetChanged();

    }

    private void setSomedayInRV(List<AllTasksModel> inList) {

        AllTasksAdapter allTasksAdapter;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        List<AllTasksModel> list = new ArrayList<>();
        list.addAll( inList );
        recyclerView_someday.setLayoutManager( linearLayoutManager );
        allTasksAdapter = new AllTasksAdapter( getContext(), list, dataBaseHelper, fragmentManager );
        recyclerView_someday.setAdapter( allTasksAdapter );
        allTasksAdapter.notifyDataSetChanged();


    }


    private List<AllTasksModel> returnTodayListFromDb() {
        List<AllTasksModel> model2List = new ArrayList<>();
         Cursor cursor = dataBaseHelper.getToday();
        if (cursor.getCount() == 0) {
            Log.i( "Data", "no data" );
        }
        while (cursor.moveToNext()) {
            String date = cursor.getString( 2 );
            String isCom = cursor.getString( 6 );
            boolean isComBoolean = false;
            if (isCom == null) {
                isComBoolean = false;
            } else if (isCom.matches( "yes" )) {
                isComBoolean = true;
            }
            model2List.add( new AllTasksModel( cursor.getString( 0 ), cursor.getString( 1 ), date, isComBoolean ) );
        }
        return model2List;
    }

    private List<AllTasksModel> returnTomorrowListFromDb() {
        List<AllTasksModel> model2List = new ArrayList<>();
        Cursor cursor = dataBaseHelper.getTomorrow();
        if (cursor.getCount() == 0) {
            Log.i( "Data", "no data" );
        }
        while (cursor.moveToNext()) {
            String date = cursor.getString( 2 );
            String isCom = cursor.getString( 6 );
            boolean isComBoolean = false;
            if (isCom == null) {
                isComBoolean = false;
            } else if (isCom.matches( "yes" )) {
                isComBoolean = true;
            }
            model2List.add( new AllTasksModel( cursor.getString( 0 ), cursor.getString( 1 ), date, isComBoolean ) );
        }
        return model2List;
    }

    private List<AllTasksModel> returnUpcomingListFromDb() {
        List<AllTasksModel> allTasksModels = new ArrayList<>();

        Cursor cursor = dataBaseHelper.getUpcoming();
        if (cursor.getCount() == 0) {
            Log.i( "Data", "no data" );
        }
        while (cursor.moveToNext()) {
            String isCom = cursor.getString( 6 );
            boolean isComBoolean = false;
            if (isCom == null) {
                isComBoolean = false;
            } else if (isCom.matches( "yes" )) {
                isComBoolean = true;
            }
            AllTasksModel filter = new AllTasksModel( cursor.getString( 0 ), cursor.getString( 1 ), cursor.getString( 2 ), isComBoolean );
            String today = todayPlaceDate();
            String tommorow = tomorrowPlaceDate();
            String someday = "";
            String provDate = cursor.getString( 3 );
//            String rd = cursor.getString( 2 );
//            long reminderDate = MyTimeSettingClass.getMilliFromDate( cursor.getString( 2 ) );
//            Calendar cal = Calendar.getInstance();
//            @SuppressLint({"NewApi", "LocalSuppress"}) SimpleDateFormat f = new SimpleDateFormat( "dd MMM yyyy EEE, h:mm a" );
//            cal.set( Calendar.HOUR_OF_DAY, 1 );
//            cal.set( Calendar.MINUTE, 0 );
//            cal.set( Calendar.SECOND, 0 );
//            cal.add( Calendar.DATE, 1 );
//            long testRDWithMe = Long.parseLong( f.format( cal.getTimeInMillis() ) );
            if (!provDate.equals( tommorow ) && !provDate.equals( today ) && !provDate.equals( someday )  )

                allTasksModels.add( filter );
        }
        return allTasksModels;
    }

    private List<AllTasksModel> returnSomedayListFromDb() {
        List<AllTasksModel> model2List = new ArrayList<>();
        Cursor cursor = dataBaseHelper.getSomeday();
        if (cursor.getCount() == 0) {
            Log.i( "Data", "no data" );
        }
        while (cursor.moveToNext()) {
            String isCom = cursor.getString( 6 );
            boolean isComBoolean = false;
            if (isCom == null) {
                isComBoolean = false;
            } else if (isCom.matches( "yes" )) {
                isComBoolean = true;
            }
            model2List.add( new AllTasksModel( cursor.getString( 0 ), cursor.getString( 1 ), cursor.getString( 2 ), isComBoolean ) );
        }
        return model2List;
    }

}
