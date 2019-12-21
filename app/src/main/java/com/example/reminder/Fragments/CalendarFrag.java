package com.example.reminder.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.CalendarAdapter;
import com.example.reminder.classes.ItemType;
import com.example.reminder.models.CalendarModel;
import com.example.reminder.models.EditTextStringListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kodmap.library.kmrecyclerviewstickyheader.KmHeaderItemDecoration;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class CalendarFrag extends Fragment implements EditTextStringListener {

//    implements DatePickerDialog.OnDateSetListener
    MainActivity mainActivity;
    TasksFrag tasksFrag;
    private CalendarAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private KmHeaderItemDecoration kmHeaderItemDecoration;
    boolean isFirstFieldSelected=false;

    BoomMenuButton bmb;
    BottomSheetBehavior bottomSheetBehavior;

    TextView startDateTv,endDateTv,startTimeTv,endTimeTv,addRepeatTv, addAlarmTv1;
    EditText eventTitleEt,addNotesEt;
    Button saveBtn;
    String formattedDate,formattedTime;
    Long sDate,sTime,eDate,eTime,sDateAndTime,eDateAndTime;
    Switch allTimeSwitch;
    private boolean isStartDate = false;
    private boolean isStartTime = false;
    private boolean isAllTime;
    View bottomsheet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_calendar, container, false);
       mainActivity = (MainActivity)getActivity();
       tasksFrag = new TasksFrag();
        bottomsheet = view.findViewById( R.id.event_bottom_sheet_layout );
       bottomSheetBehavior = BottomSheetBehavior.from( bottomsheet );
        recyclerView =view.findViewById(R.id.recyclerView);
        adapter = new CalendarAdapter();
        bmb = view.findViewById( R.id.bmb );

        saveBtn = bottomsheet.findViewById( R.id.event_save_btn);
        startDateTv = bottomsheet.findViewById( R.id.s_date_Tv );
        endDateTv = bottomsheet.findViewById( R.id.e_date_Tv );
        startTimeTv =bottomsheet.findViewById( R.id.event_start_time_tv );
        endTimeTv = bottomsheet.findViewById( R.id.event_end_time_tv );
        addAlarmTv1 =bottomsheet.findViewById( R.id.add_alarm_tv1 );
        allTimeSwitch = bottomsheet.findViewById( R.id.all_time_sb );

        addRepeatTv =bottomsheet.findViewById( R.id.add_repeat_tv );
        eventTitleEt = bottomsheet.findViewById( R.id.event_title_Et );
        addNotesEt = bottomsheet.findViewById( R.id.add_notes_Et );





        initAdapter();
        initData();
        setBMB();
        eventAddingFun();

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();

        startDate.add(Calendar.YEAR, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR,100);

        Calendar date = Calendar.getInstance();
        date.getTime();

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view,R.id.myHorizontalCalendar)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .defaultSelectedDate( date)
                .configure()
                .sizeTopText( 12 )
                .sizeBottomText( 12 )
                .sizeMiddleText( 12 )
                .end()
                .build();


        horizontalCalendar.setCalendarListener( new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position)
            {

                Toast.makeText( getContext(), date+" is Clicked", Toast.LENGTH_LONG ).show();
            }
            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy)
            {
            }
            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                String myDate= String.valueOf( date );

                Toast.makeText( getContext(), "performed Long Click on "+ myDate , Toast.LENGTH_SHORT ).show();
                return true;
            }
        } );


        return view;

    }


    public void setBMB()
    {
        bmb.setUnableColor( Color.WHITE);
        bmb.setUse3DTransformAnimation( true );
        bmb.setButtonEnum( ButtonEnum.Ham );
        bmb.setPiecePlaceEnum( PiecePlaceEnum.HAM_2 );
        bmb.setButtonPlaceEnum( ButtonPlaceEnum.HAM_2 );
        int[] images = new int[]{R.drawable.task_foreground,R.drawable.event_foreground};
        String[] names = new String[]{"Task","Event"};
        String[] subBmbText = new String[]{"Add your task","Add your event"};

        for (int i = 0; i<bmb.getPiecePlaceEnum().pieceNumber(); i++)
        {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes( images[i] ).subNormalText( subBmbText[i] )
                    .normalText( names[i] ).listener( new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {

                            if (index==1){
                            setBottomSheet();
                            bottomSheetBehavior.setState( BottomSheetBehavior.STATE_EXPANDED );
                            mainActivity.hideBottomNView();
                            }
                            else
                            {
                                InputListFrag inputListFrag = new InputListFrag();
                                mainActivity.loadmyfrag( inputListFrag );
                            }
                        }

                    } );
            bmb.addBuilder( builder );
        }
    }


    private void initAdapter() {

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        kmHeaderItemDecoration = new KmHeaderItemDecoration(adapter);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void initData() {

        Cursor cursor;
        ContentResolver contentResolver = getContext().getContentResolver();
        cursor = contentResolver.query( Uri.parse("content://com.android.calendar/events"), (new String[] {"title", "dtstart"}), null, null, null);

        List<GoogleCalendar> gCalendar = new ArrayList<GoogleCalendar>();
        List<CalendarModel> modelList = new ArrayList<>();
        try {

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    GoogleCalendar googleCalendar = new GoogleCalendar();

                    // title of Event
                    String title = cursor.getString(0);
                    googleCalendar.setTitle(title);

                    CalendarModel model = new CalendarModel(googleCalendar.getTitle(), ItemType.EVENT);
                    modelList.add( model );

                    // Date start of Event
                    String dtStart = cursor.getString(1);
                    googleCalendar.setDtstart(dtStart);
                    gCalendar.add(googleCalendar);

                    CalendarModel headerModel = new CalendarModel(googleCalendar.getDtstart(), ItemType.EVENT_DATE);
                    modelList.add(headerModel);


                }
                adapter.submitList( modelList );
            }
        } catch (AssertionError ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void mystring(String ss) {
        Toast.makeText( getActivity(), "Selected "+ss, Toast.LENGTH_SHORT ).show();
        if
        (isFirstFieldSelected){
        addAlarmTv1.setText( ss );
        }else{
            addRepeatTv.setText( ss );
        }
    }


    public static class GoogleCalendar {

        private int event_id;
        private String title,
                dtstart,
                dtend;



        public int getEvent_id() {
            return event_id;
        }

        public void setEvent_id(int calendar_id) {
            event_id = calendar_id;
        }

        public String getTitle() { return title;}

        public void setTitle(String title) {
            this.title = title;
        }


        public String getDtstart() {

            long millisecond = Long.parseLong(dtstart);
            // or you already have long value of date, use this instead of milliseconds variable.
            String dateString = DateFormat.format("EEEE, d MMMM", new Date(millisecond)).toString();

            return dateString;
        }

        public void setDtstart(String dtstart1) {
            this.dtstart = dtstart1;
        }
    }

    public void setBottomSheet()
    {


        bottomSheetBehavior.setBottomSheetCallback( new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        } );
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
          // Log.d("FRagmentcall","time => " +dateFormat.format(calendar.getTime()+""));

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
                DatePickerDialog d = new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
                d.show();

            }
        } );

        endDateTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartDate = false;
                DatePickerDialog d = new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
                d.show();
            }
        } );


        startTimeTv.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                isStartTime = true;
                TimePickerDialog dpd = new TimePickerDialog(getActivity(), (timeSetListener), hour, minutes,true);
                dpd.show();

            }
        } );

        endTimeTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartTime = false;
                TimePickerDialog dpd = new TimePickerDialog(getActivity(), (timeSetListener), hour, minutes,true);
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
                mainActivity.showBottomNView();
            }
        } );

    }


    public void onEventSaveClick() {

        String evntTitle, alarm, repeat, notes;
        long startDate, endDate, startTime, endTime;
        boolean allTime;
        TimeZone timeZone = TimeZone.getDefault();

        if (eventTitleEt.equals( "" )) {
            evntTitle = "Untitled";
        }
        evntTitle = eventTitleEt.getText().toString();
        startDate = sDate;
        endDate = eDate;
        startTime = sTime;
        endTime = eTime;
         this.isAllTime = isAllTime;
        alarm = addAlarmTv1.getText().toString();
        repeat = addRepeatTv.getText().toString();
        if (addNotesEt.equals( "" ))
        {
            addNotesEt.setText( "nothing in notes ");
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






//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof EditTextStringListener){
//            mEditTextStringListener = (EditTextStringListener) context;
//        }
//
//    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
