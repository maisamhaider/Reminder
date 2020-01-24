package com.example.reminder.classes;

import android.annotation.SuppressLint;

import com.example.reminder.Fragments.CalendarFrag;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyTimeSettingClass {


    public static int day, month, year, hour, minutes;

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTimeLongFormat(){
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat( " dd MMM yyyy h:mm a" ).format( calendar.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getToday9am() {
        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.HOUR_OF_DAY, 9 );
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        return new SimpleDateFormat( "h:mm a" ).format( calendar.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getToday3pm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.HOUR_OF_DAY, 15 );
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        return new SimpleDateFormat( "h:mm a" ).format( calendar.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getToday6pm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.HOUR_OF_DAY, 18 );
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        return new SimpleDateFormat( "h:mm a" ).format( calendar.getTime() );
    }


    @SuppressLint("SimpleDateFormat")
    public static String getLaterToday() {
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.HOUR, 2 );
        return new SimpleDateFormat( "h:mm a" ).format( cal.getTime() );
    }


    @SuppressLint("SimpleDateFormat")
    public static String getLaterToday(String strFormat) {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat( strFormat ).format( cal.getTime() );
    }


    @SuppressLint("SimpleDateFormat")
    public static String getTomorrowMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set( Calendar.HOUR_OF_DAY, 9 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.DATE, 1 );
        return new SimpleDateFormat( "EEE,h a" ).format( cal.getTime() );

    }

    @SuppressLint("SimpleDateFormat")
    public static String getTomorrow3pm() {
        Calendar cal = new GregorianCalendar();
        cal.set( Calendar.HOUR_OF_DAY, 15 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.DATE, 1 );
        return new SimpleDateFormat( "EEE,h a" ).format( cal.getTime() );

    }

    @SuppressLint("SimpleDateFormat")
    public static String getTomorrow6pm() {
        Calendar cal = new GregorianCalendar();
        cal.set( Calendar.HOUR_OF_DAY, 18 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.DATE, 1 );
        return new SimpleDateFormat( "EEE,h a" ).format( cal.getTime() );

    }

    @SuppressLint("SimpleDateFormat")
    public static String getTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.set( Calendar.DATE, 1 );
        return new SimpleDateFormat( "EEE,h a" ).format( cal.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTomorrow(String strFormat) {
        Calendar cal = new GregorianCalendar();
        cal.set( Calendar.DATE, 1 );
        return new SimpleDateFormat( strFormat ).format( cal.getTime() );
    }


    @SuppressLint("SimpleDateFormat")
    public static String getNextWeek9am() {
        Calendar calendar = new GregorianCalendar();
        calendar.set( Calendar.DATE, 6 );
        calendar.set( Calendar.HOUR_OF_DAY, 9 );
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        return new SimpleDateFormat( "EEE,h a" ).format( calendar.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getNextWeek3pm() {
        Calendar calendar = new GregorianCalendar();
        calendar.set( Calendar.DATE, 6 );
        calendar.set( Calendar.HOUR_OF_DAY, 15 );
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        return new SimpleDateFormat( "EEE,h a" ).format( calendar.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getNextWeek6pm() {
        Calendar calendar = new GregorianCalendar();
        calendar.set( Calendar.DATE, 6 );
        calendar.set( Calendar.HOUR_OF_DAY, 18 );
        calendar.set(Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        return new SimpleDateFormat( "EEE,h a" ).format( calendar.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getNextWeek() {
        Calendar calendar = new GregorianCalendar();
        calendar.set( Calendar.DATE,6);
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        return new SimpleDateFormat( "EEE,h a" ).format( calendar.getTime() );
    }
    @SuppressLint("SimpleDateFormat")
    public static String getNextWeekWithYear() {
        Calendar calendar = new GregorianCalendar();
        calendar.set( Calendar.DATE,6);
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        return new SimpleDateFormat( "dd MMM yyy" ).format( calendar.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getNextWeek(String strFoarmat) {
        Calendar cal = new GregorianCalendar();
        return new SimpleDateFormat( strFoarmat ).format( cal.getTime() );
    }


    @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
    public static String todayPlaceDate() {
        Calendar calender = Calendar.getInstance();
        final int setDay = calender.get( Calendar.DAY_OF_MONTH );
        final int setMonth = calender.get( Calendar.MONTH );
        final int setYear = calender.get( Calendar.YEAR );

        return String.format( "%d/%d/%d", setDay, setMonth, setYear );

    }

    @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
    public static String tomorrowPlaceDate() {
        Calendar calendar = Calendar.getInstance();
        final int setDay = calendar.get( Calendar.DAY_OF_MONTH ) + 1;
        final int setMonth = calendar.get( Calendar.MONTH );
        final int setYear = calendar.get( Calendar.YEAR );

        return String.format( "%d/%d/%d", setDay, setMonth, setYear );
    }

    @SuppressLint("DefaultLocale")
    public static String nextWeekPlaceDate() {
        Calendar calendar = Calendar.getInstance();
        final int setDay = calendar.get( Calendar.DAY_OF_MONTH )+ 6;
        final int setMonth = calendar.get( Calendar.MONTH );
        final int setYear = calendar.get( Calendar.YEAR );
        return String.format( "%d/%d/%d", setDay, setMonth, setYear );
    }




    @SuppressLint("DefaultLocale")
    public void setCustomPlaceDate(int setDay, int setMonth, int setYear) {
        day   = setDay;
        month = setMonth;
        year  = setYear;
    }

    @SuppressLint("DefaultLocale")
    public static String getCustomPlaceDate() {
        return String.format( "%d/%d/%d", day, month, year );
    }


    public void setCustomTime(int hour, int minutes) {
        MyTimeSettingClass.hour = hour;
        MyTimeSettingClass.minutes = minutes;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCustomTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.HOUR_OF_DAY,hour );
        calendar.set( Calendar.MINUTE,month );

        return new SimpleDateFormat("EEE,h a").format( calendar.getTime() );
    }


}
