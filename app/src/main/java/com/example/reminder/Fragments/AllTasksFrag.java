package com.example.reminder.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.adapter.MyAdapter2;
import com.example.reminder.classes.HideAndShowViewClass;
import com.example.reminder.classes.MyTimeSettingClass;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.R;
import com.example.reminder.adapter.MyAdapter;
import com.example.reminder.models.MyModel;
import com.example.reminder.models.MyModel2;

import java.util.ArrayList;
import java.util.List;

import static com.example.reminder.classes.MyTimeSettingClass.getToday;
import static com.example.reminder.classes.MyTimeSettingClass.getNextWeek;
import static com.example.reminder.classes.MyTimeSettingClass.todayPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.tomorrowPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.nextWeekPlaceDate;


public class AllTasksFrag extends Fragment {
    MainActivity mainActivity ;

    LinearLayout linearLayoutTags;
    RelativeLayout relativeLayoutAddEt;

    RecyclerView recyclerView_today, recyclerView_tomorrow, recyclerView_upcoming, recyclerView_someday;


    EditText add_task_edittext, input_textView;
    Button addtodayBtn, addtomorrowbtn, addupcomingbtn, addsomedaybtn, mainAddbtn, addUpbtn;
    String text = "", reminder_date = "";


    RadioGroup radioGroup;
    RadioButton todaytag, tomorrowtag, upcomingtag, somedaytag;

    ArrayList<MyModel> todayList;
    ArrayList<MyModel> tomorrowList;
    ArrayList<MyModel> upcomingList;
    ArrayList<MyModel> somedayList;

    MyAdapter todayAdapter;
    MyAdapter tomorrowAdapter;
    MyAdapter upcomingAdapter;
    MyAdapter somedayAdapter;

    DataBaseHelper dataBaseHelper;


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




        todayList = new ArrayList<>();
        tomorrowList = new ArrayList<>();
        upcomingList = new ArrayList<>();
        somedayList = new ArrayList<>();

        todayAdapter = new MyAdapter( getContext(), todayList );
        tomorrowAdapter = new MyAdapter( getContext(), tomorrowList );
        upcomingAdapter = new MyAdapter( getContext(), upcomingList );
        somedayAdapter = new MyAdapter( getContext(), somedayList );

        recyclerView_today = view.findViewById( R.id.todaytasksRecyclerView );
        recyclerView_tomorrow = view.findViewById( R.id.TomorrowtasksRecyclerView );
        recyclerView_upcoming = view.findViewById( R.id.upcomingtasksRecyclerView );
        recyclerView_someday = view.findViewById( R.id.somedaytasksRecyclerView );

        recyclerView_today.setAdapter( todayAdapter );
        recyclerView_tomorrow.setAdapter( tomorrowAdapter );
        recyclerView_upcoming.setAdapter( upcomingAdapter );
        recyclerView_someday.setAdapter( somedayAdapter );

        todaytag = view.findViewById( R.id.todaytagRb );
        tomorrowtag = view.findViewById( R.id.tomorrowtagRb );
        upcomingtag = view.findViewById( R.id.upcomingtagRb );
        somedaytag = view.findViewById( R.id.somedaytagRb );

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
        radioGroup =view.findViewById( R.id.radiogroup );


        dataBaseHelper = new DataBaseHelper( getContext() );
        addUpbtn.setVisibility( View.GONE );


        hidelinearLayoutTags();

        readTodayfromDb();
        readTomorrowfromDb();
        readUpcomingfromDb();

        loadedfragonbtnclick();


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
                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService( Context.INPUT_METHOD_SERVICE );
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
                }
                return false;
            }
        } );


        return view;
    }




    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode( true );
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
    public void onAttach(Context context) {
        super.onAttach( context );

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setfrag() {
        Fragment fragment = new InputListFrag();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.fragcontainer, fragment );
        fragmentTransaction.commit();
    }

    public void loadedfragonbtnclick() {

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


    public boolean hidelinearLayoutTags() {
        linearLayoutTags.setVisibility( View.GONE );
        return false;

    }

    public boolean showlinearLayoutTags() {
        linearLayoutTags.setVisibility( View.VISIBLE );
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();

    }



    // checking which tag is selected.then add task to that tag
    public void setDataInRecyclerView() {

        if (todaytag.isChecked()) {
            dynamicallyAddInToday();
        } else if (tomorrowtag.isChecked()) {
            dynamicallyAddInTomorrow();
        } else if (upcomingtag.isChecked()) {
            dynamicallyAddInUpcoming();
        } else if (somedaytag.isChecked()) {
            dynamicallyAddInSomeday();
        } else
            dynamicallyAddInToday();

    }

    private void dynamicallyAddInToday() {
        text = add_task_edittext.getText().toString();
        reminder_date = getToday();
       boolean isInsert = dataBaseHelper.insert( text, reminder_date, todayPlaceDate() );
        if (isInsert)
        {
            Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
        }
        else {
            Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
        }

        Toast.makeText( getContext(), todayPlaceDate(), Toast.LENGTH_SHORT ).show();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_today.setLayoutManager( linearLayoutManager );

        if (text.matches( "" )) {
            Toast.makeText( getActivity(), "empty field", Toast.LENGTH_SHORT ).show();
        } else {
            MyModel myModel = new MyModel();
            myModel.setDate( reminder_date );
            myModel.setNotes( text );
            todayList.add( myModel );
            todayAdapter.notifyData( todayList );
            add_task_edittext.setText( "" );
        }
    }

    public void dynamicallyAddInTomorrow() {


        text = add_task_edittext.getText().toString();
        reminder_date = MyTimeSettingClass.getTomorrowMorning();
        String tomorrow = tomorrowPlaceDate();
       boolean isInsert =  dataBaseHelper.insert( text, reminder_date, tomorrow );
        if (isInsert)
        {
            Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
        }
        else {
            Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_tomorrow.setLayoutManager( linearLayoutManager );

        if (text.matches( "" )) {
            Toast.makeText( getActivity(), "empty field", Toast.LENGTH_SHORT ).show();
        } else {
            MyModel myModel = new MyModel();
            myModel.setDate( reminder_date );
            myModel.setNotes( text );
            tomorrowList.add( myModel );
            tomorrowAdapter.notifyData( tomorrowList );
            add_task_edittext.setText( "" );
        }
    }

    public void dynamicallyAddInUpcoming() {

        text = add_task_edittext.getText().toString();
        reminder_date = MyTimeSettingClass.getNextWeek();
        String upcoming = nextWeekPlaceDate();
         boolean isInsert =  dataBaseHelper.insert( text, reminder_date, upcoming );
        if (isInsert)
        {
            Toast.makeText( getContext(), "Inserted", Toast.LENGTH_SHORT ).show();
        }
        else {
            Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_upcoming.setLayoutManager( linearLayoutManager );

        if (text.matches( "" )) {
            Toast.makeText( getActivity(), "empty field", Toast.LENGTH_SHORT ).show();
        } else {
            MyModel myModel = new MyModel();
            myModel.setDate( reminder_date );
            myModel.setNotes( text );
            upcomingList.add( myModel );
            upcomingAdapter.notifyData( upcomingList );
            add_task_edittext.setText( "" );
        }
    }

    public void dynamicallyAddInSomeday() {


        text = add_task_edittext.getText().toString();
        reminder_date = "";

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_someday.setLayoutManager( linearLayoutManager );

        if (text.matches( "" )) {
            Toast.makeText( getActivity(), "empty field", Toast.LENGTH_SHORT ).show();
        } else {
            MyModel myModel = new MyModel();
            myModel.setDate( reminder_date );
            myModel.setNotes( text );
            somedayList.add( myModel );
            somedayAdapter.notifyData( somedayList );
            add_task_edittext.setText( "" );
        }
    }



    public void readTodayfromDb() {
        MyAdapter2 myAdapter2;
        List<MyModel2> model2List = new ArrayList<>();
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
            model2List.add( new MyModel2(cursor.getString( 0 ), cursor.getString( 1 ), cursor.getString( 2 ) ) );
        }
        myAdapter2 = new MyAdapter2( getContext(), model2List,dataBaseHelper );
        recyclerView_today.setAdapter( myAdapter2 );
        myAdapter2.notifyDataSetChanged();

    }

    public void readTomorrowfromDb() {
        MyAdapter2 myAdapter2;
        List<MyModel2> model2List = new ArrayList<>();
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
            model2List.add( new MyModel2(cursor.getString( 0 ), cursor.getString( 1 ), cursor.getString( 2 ) ) );
        }
        myAdapter2 = new MyAdapter2( getContext(), model2List,dataBaseHelper );
        recyclerView_tomorrow.setAdapter( myAdapter2 );
        myAdapter2.notifyDataSetChanged();


    }

    public void readUpcomingfromDb() {
        MyAdapter2 myAdapter2;
        List<MyModel2> model2List = new ArrayList<>();
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
            model2List.add( new MyModel2(cursor.getString( 0 ), cursor.getString( 1 ), cursor.getString( 2 ) ) );
        }

        myAdapter2 = new MyAdapter2( getContext(), model2List,dataBaseHelper );
        recyclerView_upcoming.setAdapter( myAdapter2 );
        myAdapter2.notifyDataSetChanged();

    }

}
