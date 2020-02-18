package com.example.reminder.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.CalendarAdapter;
import com.example.reminder.classes.MyTimeSettingClass;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class CalendarFrag extends Fragment  {

    private static final int REQUEST_PERMISSION = 1;
    MainActivity mainActivity;
    AllTasksFrag allTasksFrag;
    private CalendarAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    BoomMenuButton bmb;
    BottomSheetBehavior bottomSheetBehavior;

    private ArrayList<String> calendarId = new ArrayList<>();
    private ArrayList<String> nameOfEvent = new ArrayList<>();
    private ArrayList<String> startDates = new ArrayList<>();
    private ArrayList<String> endDates = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> location = new ArrayList<>();
    SimpleDateFormat formatRestDate,formatYear;

    View bottomsheet;

    // calendar action bar
    TextView calActionBarDateTv1,calActionBarDateTv2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_calendar, container, false );
        checkPermission();
        // calendar action bar view
        calActionBarDateTv1 = view.findViewById( R.id.calActionBarDateTv1 );
        calActionBarDateTv2 = view.findViewById( R.id.calActionBarDateTv2 );
//
        final SimpleDateFormat formatYear = new SimpleDateFormat( "yyyy" );
        final SimpleDateFormat formatRestDate= new SimpleDateFormat( "MMMM dd" );
        mainActivity = (MainActivity) getActivity();
        allTasksFrag = new AllTasksFrag();
        recyclerView = view.findViewById( R.id.recyclerView );
        adapter = new CalendarAdapter( getContext(),nameOfEvent,startDates,location,descriptions,calendarId);
        bmb = view.findViewById( R.id.bmb );

        readCalendarEvent();
        initAdapter();
        initAdapter();

        setBMB();


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
                .showTopText(false)
                .sizeBottomText( 10 )
                .sizeMiddleText( 15 )
                .end()
                .build();

        calActionBarDateTv1.setText(formatYear.format( defaultActionBarDate.getTime() ));
        calActionBarDateTv2.setText( new SimpleDateFormat("MMMM dd").format( defaultActionBarDate.getTime() ) );

        horizontalCalendar.setCalendarListener( new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                calActionBarDateTv1.setText( formatYear.format( date.getTime() ) );
                calActionBarDateTv2.setText( formatRestDate.format( date.getTime() ) );

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



        return view;

    }



    public void setBMB() {
        bmb.setUnableColor( Color.WHITE );
        bmb.setUse3DTransformAnimation( true );
        bmb.setButtonEnum( ButtonEnum.Ham );
        bmb.setPiecePlaceEnum( PiecePlaceEnum.HAM_2 );
        bmb.setButtonPlaceEnum( ButtonPlaceEnum.HAM_2 );
        int[] images = new int[]{R.drawable.task_foreground, R.drawable.event_foreground};
        String[] names = new String[]{"Task", "Event"};
        String[] subBmbText = new String[]{"Add your task", "Add your event"};

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes( images[i] ).subNormalText( subBmbText[i] )
                    .normalText( names[i] ).listener( new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {

                            if (index == 1) {

                                CalendarEventAddBottomSheetDialogFrag calendarEventAddBottomSheetDialogFrag =
                                        CalendarEventAddBottomSheetDialogFrag.newInstance();
                                calendarEventAddBottomSheetDialogFrag.show( Objects.requireNonNull( getActivity() ).getSupportFragmentManager(),
                                        "BSheet" );
                            } else {

                                Bundle bundle = new Bundle(  );
                                bundle.putString( "get_btn","today_Clicked" );
                                InputListFrag inputListFrag = new InputListFrag();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.addToBackStack(null);
                                inputListFrag.setArguments( bundle );
                                fragmentTransaction.replace(R.id.fragcontainer, inputListFrag);
                                fragmentTransaction.commit();
                            }
                        }

                    } );
            bmb.addBuilder( builder );
        }
    }


    private void initAdapter() {

        layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( adapter );
        adapter.notifyDataSetChanged();
    }

    public void readCalendarEvent() {

    if (checkPermission()) {
            Cursor cursor = getContext().getContentResolver()
                    .query(
                            Uri.parse( "content://com.android.calendar/events" ),
                            new String[]{"_id", "title", "description",
                                    "dtstart", "dtend", "eventLocation"},  new String("("+ CalendarContract.Events.DELETED +" != 1)"),
                           null, null );
            cursor.moveToFirst();
            // fetching calendars name
            String CNames[] = new String[cursor.getCount()];

            // fetching calendars id
            nameOfEvent.clear();
            startDates.clear();
            endDates.clear();
            descriptions.clear();
            for (int i = 0; i < CNames.length; i++) {

                calendarId.add( cursor.getString( 0 ) );
                nameOfEvent.add( cursor.getString( 1 ) );
                startDates.add( MyTimeSettingClass.getCalDateFormat( Long.parseLong( cursor.getString( 3 ) ),"EEE, d MMM" ) );
                descriptions.add( cursor.getString( 2 ) );
                location.add( cursor.getString( 5 ) );
                CNames[i] = cursor.getString( 1 );
                cursor.moveToNext();
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    private boolean checkPermission() {
        int calendarWritePermission = ContextCompat.checkSelfPermission( getContext(), Manifest.permission.WRITE_CALENDAR );
        int calendarReaDPermission = ContextCompat.checkSelfPermission( getContext(), Manifest.permission.READ_CALENDAR );

        if (calendarWritePermission == PackageManager.PERMISSION_GRANTED && calendarReaDPermission == PackageManager.PERMISSION_GRANTED) {
            Log.i( "Calendar READ/WRITE permssion", "Granted" );
            return true;
        } else {
            ActivityCompat.requestPermissions( getActivity(), new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}
                    , REQUEST_PERMISSION );
            return false;
        }
    }


}
