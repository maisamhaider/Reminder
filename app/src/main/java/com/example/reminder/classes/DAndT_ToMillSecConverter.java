package com.example.reminder.classes;

import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Date;

public class DAndT_ToMillSecConverter {

    public static long dateToMilliseconds(String date)
    {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try
        {
            Date mDate = sdf.parse(date);
            return mDate.getTime();
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    public static long timeToMilliseconds(String time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "h:mm a" );
        try {
            Date mTime= sdf.parse( time );
            return mTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }




}
