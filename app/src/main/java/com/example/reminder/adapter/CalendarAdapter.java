package com.example.reminder.adapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.EventViewHolder> {

    Context context;
    ArrayList<String> eventTitleList;
    ArrayList<String> eventDateList;
    ArrayList<String> eventLocationList;
    ArrayList<String> eventDescriptionList;
    ArrayList<String> eventCalendarId;

    public CalendarAdapter(Context context, ArrayList<String> eventTitleList,
                           ArrayList<String> eventDateList,    ArrayList<String> eventLocationList,
                           ArrayList<String> eventDescriptionList,ArrayList<String> eventCalendarId) {
        this.context = context;
        this.eventTitleList = eventTitleList;
        this.eventDateList = eventDateList;
        this.eventLocationList = eventLocationList;
        this.eventDescriptionList = eventDescriptionList;
        this.eventCalendarId = eventCalendarId;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.cal_event_list_item, parent, false );
        return new EventViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, final int position) {
        holder.eventNameTv.setText( eventTitleList.get( position ) );
        holder.event_date.setText( eventDateList.get( position ) );
        holder.event_descriptionTv.setText( eventDescriptionList.get( position ) );
        holder.event_locationTv.setText( eventLocationList.get( position ) );
        holder.event_calendarIdTv.setText( eventCalendarId.get( position ) );

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri eventsUri;
                int osVersion = android.os.Build.VERSION.SDK_INT;
                if (osVersion <= 7) { //up-to Android 2.1
                    eventsUri = Uri.parse("content://calendar/events");
                } else { //8 is Android 2.2 (Froyo) (http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)
                    eventsUri = Uri.parse("content://com.android.calendar/events");
                }
                ContentResolver resolver = context.getContentResolver();
                deleteEvent(resolver, eventsUri, Integer.parseInt( eventCalendarId.get( position ) ) );
             eventTitleList.remove( position );
             eventDateList.remove( position );
             eventLocationList.remove( position );
             eventDescriptionList.remove( position );
             eventCalendarId.remove( position );
             notifyDataSetChanged();
            }
        } );
    }

    @Override
    public int getItemCount() {
        return eventDateList.size();
    }


    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventNameTv;
        TextView event_date;
        TextView event_descriptionTv;
        TextView event_locationTv;
        TextView event_calendarIdTv;
        CheckBox eventCheckBox;
        ImageView deleteEventIV;

        public EventViewHolder(@NonNull View itemView) {
            super( itemView );
            eventNameTv = itemView.findViewById( R.id.eventNameTv );
            event_date = itemView.findViewById( R.id.event_date );
            event_descriptionTv = itemView.findViewById( R.id.event_descriptionTv );
            event_locationTv = itemView.findViewById( R.id.event_locationTv );
            deleteEventIV = itemView.findViewById( R.id.deleteEventIV );
            event_calendarIdTv = itemView.findViewById( R.id.event_calendarIdTv );
//            eventCheckBox = itemView.findViewById( R.id.eventCheckBox );


    }

    }
    private void deleteEvent(ContentResolver resolver, Uri eventsUri, int calendarId) {
        Cursor cursor;
        if (android.os.Build.VERSION.SDK_INT <= 7) { //up-to Android 2.1
            cursor = resolver.query( eventsUri, new String[]{"_id"}, "Calendars._id=" + calendarId, null, null );
        } else { //8 is Android 2.2 (Froyo) (http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)
            cursor = resolver.query( eventsUri, new String[]{"_id"}, "calendar_id=" + calendarId, null, null );
        }
        while (cursor.moveToNext()) {
            long eventId = cursor.getLong( cursor.getColumnIndex( "_id" ) );
            resolver.delete( ContentUris.withAppendedId( eventsUri, eventId ), null, null );
        }
        cursor.close();

    }

}