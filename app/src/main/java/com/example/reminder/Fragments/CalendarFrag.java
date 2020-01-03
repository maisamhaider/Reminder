package com.example.reminder.Fragments;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.CalendarAdapter;
import com.example.reminder.classes.ItemType;
import com.example.reminder.models.CalendarModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kodmap.library.kmrecyclerviewstickyheader.KmHeaderItemDecoration;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class CalendarFrag extends Fragment {

    private static final int REQUEST_PERMISSION =1;
    MainActivity mainActivity;
    AllTasksFrag allTasksFrag;
    private CalendarAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private KmHeaderItemDecoration kmHeaderItemDecoration;

    BoomMenuButton bmb;
    BottomSheetBehavior bottomSheetBehavior;


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
        checkPermission();

        mainActivity = (MainActivity)getActivity();
        allTasksFrag = new AllTasksFrag();
        recyclerView =view.findViewById(R.id.recyclerView);
        adapter = new CalendarAdapter();
        bmb = view.findViewById( R.id.bmb );

        initAdapter();
        initData();
        setBMB();


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

                                MyBottomSheetDialogFrag myBottomSheetDialogFrag =
                                        MyBottomSheetDialogFrag.newInstance();
                                myBottomSheetDialogFrag.show( Objects.requireNonNull( getActivity() ).getSupportFragmentManager(),
                                        "BSheet");
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
        if (checkPermission()) {
            Cursor cursor;
            ContentResolver contentResolver = getContext().getContentResolver();
            cursor = contentResolver.query( Uri.parse( "content://com.android.calendar/events" ), (new String[]{"title", "dtstart"}), null, null, null );

            List<GoogleCalendar> gCalendar = new ArrayList<GoogleCalendar>();
            List<CalendarModel> modelList = new ArrayList<>();
            try {

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        GoogleCalendar googleCalendar = new GoogleCalendar();

                        // title of Event
                        String title = cursor.getString( 0 );
                        googleCalendar.setTitle( title );

                        CalendarModel model = new CalendarModel( googleCalendar.getTitle(), ItemType.EVENT );
                        modelList.add( model );

                        // Date start of Event
                        String dtStart = cursor.getString( 1 );
                        googleCalendar.setDtstart( dtStart );
                        gCalendar.add( googleCalendar );

                        CalendarModel headerModel = new CalendarModel( googleCalendar.getDtstart(), ItemType.EVENT_DATE );
                        modelList.add( headerModel );


                    }
                    adapter.submitList( modelList );
                }
            } catch (AssertionError ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private boolean checkPermission()
    {
        int calendarWritePermission = ContextCompat.checkSelfPermission( getContext(), Manifest.permission.WRITE_CALENDAR );
        int calendarReaDPermission =ContextCompat.checkSelfPermission( getContext(),Manifest.permission.READ_CALENDAR );

        if (calendarWritePermission == PackageManager.PERMISSION_GRANTED && calendarReaDPermission == PackageManager.PERMISSION_GRANTED)
        {
            Log.i( "Calendar READ/WRITE permssion","Granted" );
            return true;
        }
        else
        {
            ActivityCompat.requestPermissions( getActivity(),new String[]{Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR}
                    ,REQUEST_PERMISSION);
            return false;
        }

    }


}
