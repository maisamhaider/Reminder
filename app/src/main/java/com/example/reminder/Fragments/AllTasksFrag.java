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
import com.example.reminder.adapter.MyAllTasksAdapter;
import com.example.reminder.classes.HideAndShowViewClass;
import com.example.reminder.classes.MyTimeSettingClass;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.R;
import com.example.reminder.models.MyAllTasksModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.reminder.classes.MyTimeSettingClass.getToday;
import static com.example.reminder.classes.MyTimeSettingClass.getNextWeek;
import static com.example.reminder.classes.MyTimeSettingClass.todayPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.tomorrowPlaceDate;
import static com.example.reminder.classes.MyTimeSettingClass.nextWeekPlaceDate;


public class AllTasksFrag extends Fragment {
    private MainActivity mainActivity ;

    private LinearLayout linearLayoutTags;
    private RelativeLayout relativeLayoutAddEt;

    private RecyclerView recyclerView_today, recyclerView_tomorrow, recyclerView_upcoming, recyclerView_someday;


    private EditText add_task_edittext, input_textView;
    private Button addtodayBtn, addtomorrowbtn, addupcomingbtn, addsomedaybtn, mainAddbtn, addUpbtn;
    private String text = "", reminder_date = "";


    private RadioGroup radioGroup;
    private RadioButton todaytag, tomorrowtag, upcomingtag, somedaytag;


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

        recyclerView_today = view.findViewById( R.id.todaytasksRecyclerView );
        recyclerView_tomorrow = view.findViewById( R.id.TomorrowtasksRecyclerView );
        recyclerView_upcoming = view.findViewById( R.id.upcomingtasksRecyclerView );
        recyclerView_someday = view.findViewById( R.id.somedaytasksRecyclerView );


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
                mainActivity.showBottomNView();
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
        mainActivity.setDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag
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
        mainActivity.setDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag

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
        mainActivity.setDefaultBNBItem();//refresh fragment,just setting Task item of bottomNavigationView.which call task frag

    }

    public void dynamicallyAddInSomeday() {


        text = add_task_edittext.getText().toString();
        reminder_date = "";

    }



    public void readTodayfromDb() {
        MyAllTasksAdapter myAllTasksAdapter;
        List<MyAllTasksModel> model2List = new ArrayList<>();
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
            model2List.add( new MyAllTasksModel(cursor.getString( 0 ), cursor.getString( 1 ), cursor.getString( 2 ) ) );
        }
        myAllTasksAdapter = new MyAllTasksAdapter( getContext(), model2List,dataBaseHelper );
        recyclerView_today.setAdapter( myAllTasksAdapter );
        myAllTasksAdapter.notifyDataSetChanged();

    }

    public void readTomorrowfromDb() {
        MyAllTasksAdapter myAllTasksAdapter;
        List<MyAllTasksModel> model2List = new ArrayList<>();
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
            model2List.add( new MyAllTasksModel(cursor.getString( 0 ), cursor.getString( 1 ), cursor.getString( 2 ) ) );
        }
        myAllTasksAdapter = new MyAllTasksAdapter( getContext(), model2List,dataBaseHelper );
        recyclerView_tomorrow.setAdapter( myAllTasksAdapter );
        myAllTasksAdapter.notifyDataSetChanged();


    }

    public void readUpcomingfromDb() {
        MyAllTasksAdapter myAllTasksAdapter;
        List<MyAllTasksModel> model2List = new ArrayList<>();
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
            model2List.add( new MyAllTasksModel(cursor.getString( 0 ), cursor.getString( 1 ), cursor.getString( 2 ) ) );
        }

        myAllTasksAdapter = new MyAllTasksAdapter( getContext(), model2List,dataBaseHelper );
        recyclerView_upcoming.setAdapter( myAllTasksAdapter );
        myAllTasksAdapter.notifyDataSetChanged();

    }

}
