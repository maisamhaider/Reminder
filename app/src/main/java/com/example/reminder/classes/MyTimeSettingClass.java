package com.example.reminder.classes;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyTimeSettingClass {


    @SuppressLint("SimpleDateFormat")
    public static String getToday() {

        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.HOUR, 2 );
        return new SimpleDateFormat( "HH:mm a" ).format( cal.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getToday(String strFormat) {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat( strFormat ).format( cal.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.set( Calendar.HOUR_OF_DAY, 9 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.add( Calendar.DATE, 1 );
        return new SimpleDateFormat( "EEE,h a" ).format( cal.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTomorrow(String strFormat) {
        Calendar cal = new GregorianCalendar();
        cal.add( Calendar.DATE, 1 );
        return new SimpleDateFormat( strFormat ).format( cal.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getUpcoming() {
        Calendar calendar = new GregorianCalendar();
        calendar.add( Calendar.DATE, 6 );
        calendar.add( Calendar.MINUTE, 0 );
        calendar.add( Calendar.SECOND, 0 );
        return new SimpleDateFormat( "EEE,h a" ).format( calendar.getTime() );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getUpcoming(String strFoarmat) {
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
    public static String upcomingPlaceDate() {
        Calendar calendar = Calendar.getInstance();
        final int setDay = calendar.get( Calendar.DAY_OF_MONTH + 5 );
        final int setMonth = calendar.get( Calendar.MONTH );
        final int setYear = calendar.get( Calendar.YEAR );
        return String.format( "%d/%d/%d", setDay, setMonth, setYear );
    }



}
