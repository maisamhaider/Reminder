package com.example.reminder.classes;

import android.view.View;

public class HideViewClass {

    public static void HideView(View view)
    {
        view.setVisibility( View.INVISIBLE );
    }
    public static void showView(View view)
    {
        view.setVisibility( View.VISIBLE );
    }

}
