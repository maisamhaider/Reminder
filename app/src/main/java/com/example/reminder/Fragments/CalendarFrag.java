package com.example.reminder.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.reminder.R;
import com.example.reminder.adapter.CalendarAdapter;
import com.example.reminder.classes.ItemType;
import com.example.reminder.models.CalendarModel;
import com.kodmap.library.kmrecyclerviewstickyheader.KmHeaderItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class CalendarFrag extends Fragment {

    private CalendarAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private KmHeaderItemDecoration kmHeaderItemDecoration;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_calendar, container, false);
        recyclerView =view.findViewById(R.id.recyclerView);
        adapter = new CalendarAdapter();

        initAdapter();
        initData();

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

    public static class GoogleCalendar {

        private int event_id;
        private String title,
                dtstart,
                dtend;

        public GoogleCalendar()
        {
        }

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
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
