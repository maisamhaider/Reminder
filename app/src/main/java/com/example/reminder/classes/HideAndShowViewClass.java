package com.example.reminder.classes;

import android.view.View;

public class HideAndShowViewClass {

    public static void hideView(View view)
    {
        view.setVisibility( View.INVISIBLE );
    }
    public static void showView(View view)
    {
        view.setVisibility( View.VISIBLE );
    }

}
