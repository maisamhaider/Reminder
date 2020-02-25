package com.example.reminder.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PointF;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;


import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.CalendarAdapter;
import com.example.reminder.classes.MyTimeSettingClass;
import com.example.reminder.classes.ViewAnimation;
import com.example.reminder.models.EventModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;


import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class CalendarFrag extends Fragment {

    MainActivity mainActivity;
    AllTasksFrag allTasksFrag;
    private CalendarAdapter adapter;
    private RecyclerView recyclerView;


    private FloatingActionButton mainFab, eventFab, taskFab;
    private TextView eventTextView, taskTextView;

    private ArrayList<EventModel> eventModelList = new ArrayList<>();
    private ArrayList<String> sDate = new ArrayList<>(  );
    private EventModel eventModel;



    // calendar action bar
    TextView calActionBarDateTv1, calActionBarDateTv2;
    private boolean isRotate = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_calendar, container, false );
    // calendar action bar view
        calActionBarDateTv1 = view.findViewById( R.id.calActionBarDateTv1 );
        calActionBarDateTv2 = view.findViewById( R.id.calActionBarDateTv2 );
        //floating buttons
        mainFab = view.findViewById( R.id.mainFab );
        eventFab = view.findViewById( R.id.eventFab );
        taskFab = view.findViewById( R.id.taskFab );

        taskTextView = view.findViewById( R.id.fabTaskTv );
        eventTextView = view.findViewById( R.id.fabEventTv );

//
        final SimpleDateFormat formatYear = new SimpleDateFormat( "yyyy" );
        final SimpleDateFormat formatRestDate = new SimpleDateFormat( "MMMM dd" );
        mainActivity = (MainActivity) getActivity();
        allTasksFrag = new AllTasksFrag();
        recyclerView = view.findViewById( R.id.recyclerView );


        readCalendarEvent();
        initAdapter(returnCurrentDateItemPosition());


        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        Calendar defaultActionBarDate = Calendar.getInstance();

        startDate.add( Calendar.YEAR, -1 );

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add( Calendar.YEAR, 100 );

        Calendar date = Calendar.getInstance();
        date.getTime();

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder( view, R.id.myHorizontalCalendar )
                .range( startDate, endDate )
                .datesNumberOnScreen( 7 )
                .defaultSelectedDate( date )
                .configure()
                .showTopText( false )
                .sizeBottomText( 12 )
                .sizeMiddleText( 12 )
                .end()
                .build();

        calActionBarDateTv1.setText( formatYear.format( defaultActionBarDate.getTime() ) );
        calActionBarDateTv2.setText( new SimpleDateFormat( "MMMM dd" ).format( defaultActionBarDate.getTime() ) );

        horizontalCalendar.setCalendarListener( new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                calActionBarDateTv1.setText( formatYear.format( date.getTime() ) );
                calActionBarDateTv2.setText( formatRestDate.format( date.getTime() ) );
                SimpleDateFormat format = new SimpleDateFormat( "EEEE, d MMMM" );
                String currentDate = format.format( date.getTime() );
                String listDate;
                int matchItemPosition = 0;
                for (int i = 0; i<eventModelList.size(); i++)
                {
                    listDate = eventModelList.get( i ).getStartDates();
                    if (currentDate.matches( listDate ))
                    {
                        matchItemPosition = i;
                    }

                }
                initAdapter(matchItemPosition);

            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {
            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                String myDate = String.valueOf( date );

                Toast.makeText( getContext(), "performed Long Click on " + myDate, Toast.LENGTH_SHORT ).show();
                return true;
            }
        } );

        ViewAnimation.init( taskFab );
        ViewAnimation.init( eventFab );
        ViewAnimation.init( eventTextView );
        ViewAnimation.init( taskTextView );


        mainFab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = ViewAnimation.rotateFab( v, !isRotate );
                if (isRotate) {

                    ViewAnimation.showIn( taskFab, taskTextView );
                    ViewAnimation.showIn( eventFab, eventTextView );

                } else {
                    ViewAnimation.showOut( taskFab, taskTextView );
                    ViewAnimation.showOut( eventFab, eventTextView );


                }

            }
        } );


        eventFab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarEventAddBottomSheetDialogFrag calendarEventAddBottomSheetDialogFrag =
                        CalendarEventAddBottomSheetDialogFrag.newInstance();
                calendarEventAddBottomSheetDialogFrag.show( Objects.requireNonNull( getActivity() ).getSupportFragmentManager(),
                        "BSheet" );
            }
        } );
        taskFab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString( "get_btn", "today_Clicked" );
                InputListFrag inputListFrag = new InputListFrag();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack( null );
                inputListFrag.setArguments( bundle );
                fragmentTransaction.replace( R.id.fragcontainer, inputListFrag );
                fragmentTransaction.commit();
            }
        } );


        return view;

    }


    private void initAdapter(int i) {
//        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller( getContext() )
//        {
//            @Override
//            protected int getVerticalSnapPreference() {
//                return LinearSmoothScroller.SNAP_TO_START;
//            }
//        };

        Collections.sort( eventModelList );
        adapter = new CalendarAdapter( getContext(), eventModelList );
        recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(getContext()));
        recyclerView.setAdapter( adapter );
        recyclerView.smoothScrollToPosition(i);
        adapter.notifyDataSetChanged();
    }


    public void readCalendarEvent() {

        if (mainActivity.checkPermission()) {
            Cursor cursor = getContext().getContentResolver()
                    .query(
                            Uri.parse( "content://com.android.calendar/events" ),
                            new String[]{"_id", "title", "description",
                                    "dtstart", "dtend", "eventLocation"}, new String( "(" + CalendarContract.Events.DELETED + " != 1)" ),
                            null, null );
            cursor.moveToFirst();
            // fetching calendars name
            String CNames[] = new String[cursor.getCount()];

            // fetching calendars id
//            nameOfEvent.clear();
//            startDates.clear();
//            endDates.clear();
//            descriptions.clear();
            for (int i = 0; i < CNames.length; i++) {
                eventModel = new EventModel(  );
                sDate.add( MyTimeSettingClass.getCalDateFormat( Long.parseLong( cursor.getString( 3 ) ), "EEEE, d MMMM" ) );
                eventModel.setCalendarId( cursor.getString( 0 ) );
                eventModel.setNameOfEvent( cursor.getString( 1 ) );
                eventModel.setStartDates( MyTimeSettingClass.getCalDateFormat( Long.parseLong( cursor.getString( 3 ) ), "EEEE, d MMMM" ) );
                eventModel.setDescriptions( cursor.getString( 2 ) );
                eventModel.setLocation( cursor.getString( 5 ) );
                CNames[i] = cursor.getString( 1 );
                cursor.moveToNext();
                eventModelList.add( eventModel );
            }
        }
    }

    public int returnCurrentDateItemPosition()
    {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") java.text.SimpleDateFormat format = new java.text.SimpleDateFormat( "EEEE, d MMMM" );
        String currentDate = format.format( calendar.getTime() );
        String listDate;
        int i;
        int matchItemPosition = 0;
        for ( i = 0; i<eventModelList.size(); i++)
        {
            listDate = eventModelList.get( i ).getStartDates();
            if (currentDate.matches( listDate ))
            {
                matchItemPosition = i;

            }
        }

        return matchItemPosition ;
    }

    public class LinearLayoutManagerWithSmoothScroller extends LinearLayoutManager {

        public LinearLayoutManagerWithSmoothScroller(Context context) {
            super( context, VERTICAL, false );
        }

        public LinearLayoutManagerWithSmoothScroller(Context context, int orientation, boolean reverseLayout) {
            super( context, orientation, reverseLayout );
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                           int position) {
            RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller( recyclerView.getContext() );
            smoothScroller.setTargetPosition( position );
            startSmoothScroll( smoothScroller );
        }

        private class TopSnappedSmoothScroller extends LinearSmoothScroller {
            public TopSnappedSmoothScroller(Context context) {
                super( context );

            }

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return LinearLayoutManagerWithSmoothScroller.this
                        .computeScrollVectorForPosition( targetPosition );
            }

            @Override
            protected int getVerticalSnapPreference() {
                return SNAP_TO_START;
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }





}
