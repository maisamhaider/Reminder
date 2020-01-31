package com.example.reminder.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.AttachmentTaskAdapter;
import com.example.reminder.adapter.SubTaskAdapter;
import com.example.reminder.classes.AlarmSettingClass;
import com.example.reminder.classes.MyTimeSettingClass;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.interfaces.RecyclerCallBack;
import com.example.reminder.models.AttachmentTaskModel;
import com.example.reminder.models.MySubTaskModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class EditTask extends BottomSheetDialogFragment {

    MainActivity mainActivity;
    private static final int REQUEST_CODE = 1001;
    private MyTimeSettingClass myTimeSettingClass;

    // Edit task Views
    private CardView setTimeCV, tomorrow9AmCV, daily_WeeklyCV, etcCv, shareTaskCv;
    private TextView edittaskTitle, createdTextView, notesHolderTv, editSetTimeTv, editDeleteUpperTv, edit_uploadTv;
    private EditText addSubTasksET;
    private LinearLayout editSetTimeLL, addNewSubTaskLL, tabToAddAttachmentsLL, edit_MarkAsDoneLL, edit_deleteTaskLL;
    private Switch editReminderOnOffSwitch;
    private RecyclerView subTaskRecyclerView, attachmentRecyclerView;
    private SubTaskAdapter subTaskAdapter;
    private AttachmentTaskAdapter attachmentTaskAdapter;


    private String taskPosition, taskTitle, repeatValue, task_reminderDate, task_placeDate, subTasks, taskNotes, taskCreatedDate, attachments;
    private String reminder_date = "", date_to_place_task = "";
    private String checkRepeat = "daily";
    boolean isRepeatllHidden = false;

    private Calendar calendar = Calendar.getInstance();
    private int checkYear, currentYear, isTomorrow, mTomorrow, isToday, mToday;


    // Edit Notes AlerDialog Views
    Button myNotesSaveBtn;
    ImageView myNotesBackIv;
    EditText myNotesET;

    // addReminder views
    private Button oneTimeBtn, repeatBtn, locationBtn;
    private LinearLayout repeatLL, editTagsLL, editLocationLL, editRemindTagsLL, edit_thisEveningLL, edit_later_todayLL,
            edit_tomorrowLL, edit_nextWeekLL, edit_somedayLL, edit_customLL, repeat_Daily_LL, repeat_Weekly_LL, repeat_Monthly_LL,
            repeat_Yearly_LL;
    private TextView editLaterTodayTimeTv, editThisEvening, edit_remindMeOrNoReminderTv, edit_addReminderShowTimeTv,
            editTimeTomorrowTv, ediTimeNextweekTv, repeatTimeHolderTv;

    private ImageView edit_addReminderDeleteTimeIV;
    private Button edit_task_SetBtn, edit_task_cancelBtn;
    ConstraintLayout edit_showReminderTimeCL;
    private Switch edit_oneTimeAddReminderSwitch;

    private boolean isOneButton = true;


    DataBaseHelper dataBaseHelper;

    private SimpleDateFormat sformat;
    String notesHolderStg;
    String whichOnIsClick = "", repeatString = "";
    private AlarmSettingClass alarmSettingClass;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.edit_task, container, false );
        alarmSettingClass = new AlarmSettingClass( getActivity() );



        mainActivity = (MainActivity) getActivity();
        dataBaseHelper = new DataBaseHelper( getContext() );
        myTimeSettingClass = new MyTimeSettingClass();
        subTaskRecyclerView = view.findViewById( R.id.subTasksRecyclerView );
        attachmentRecyclerView = view.findViewById( R.id.AttachmentRv );

        shareTaskCv = view.findViewById( R.id.shareTaskCv );

        setTimeCV = view.findViewById( R.id.setTaskTimeCV );
        tomorrow9AmCV = view.findViewById( R.id.tomorrow9amCv );
        daily_WeeklyCV = view.findViewById( R.id.setTaskRepeatCv );
        edittaskTitle = view.findViewById( R.id.taskTitle_tv );
        createdTextView = view.findViewById( R.id.createdDateTv );
        editSetTimeTv = view.findViewById( R.id.editSetTimeTv );
        editReminderOnOffSwitch = view.findViewById( R.id.editReminderOnOffSwitch );
        editDeleteUpperTv = view.findViewById( R.id.editDeleteUpperTv );

        addNewSubTaskLL = view.findViewById( R.id.addNewSubTaskLL );
        editRemindTagsLL = view.findViewById( R.id.editRemindTagsLL );
        editSetTimeLL = view.findViewById( R.id.editSetTimeLL );

        addSubTasksET = view.findViewById( R.id.addSubTasksET );
        notesHolderTv = view.findViewById( R.id.holdNotesTv );

        tabToAddAttachmentsLL = view.findViewById( R.id.tabToAddAttachmentsLL );
        edit_uploadTv = view.findViewById( R.id.edit_uploadTv );
        edit_MarkAsDoneLL = view.findViewById( R.id.edit_MarkAsDoneLL );
        edit_deleteTaskLL = view.findViewById( R.id.edit_deleteTaskLL );

        editSetTimeLL.setVisibility( View.GONE );


        launchReminderDialogFun();
        launchNotesDialogFragFun();
        getDataInBSHDF();
        subTaskFun();
        attachmentFun();


        shareTaskCv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTaskDataFun();
            }
        } );

        editDeleteUpperTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isOneButton = true;
                mainActivity.setTaskFragDefaultBNBItem();
                showEditRemindTagsLLAndStuffs();
                editSetTimeTv.setText( "" );
                dataBaseHelper.update( "", MyTimeSettingClass.todayPlaceDate(), "","0", taskPosition );
            }
        } );


        editReminderOnOffSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                } else {

                }
            }
        } );

        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        subTaskRecyclerView.setLayoutManager( linearLayoutManager );

        edit_MarkAsDoneLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdate = dataBaseHelper.upDate( taskPosition, "yes","0" );
                if (isUpdate) {
                    alarmSettingClass.setAllAlarm();

                    Toast.makeText( getContext(), "updated", Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText( getContext(), "not updated", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
        edit_deleteTaskLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder( getContext() )
                        .setCancelable( true ).setTitle( "Delete Alert" ).setMessage( "Are you sure to delete the task ?" )
                        .setPositiveButton( "yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Cursor cursor = dataBaseHelper.getAttachment( taskPosition );
                                if (cursor.getCount()==0)
                                {}
                                while (cursor.moveToNext())
                                {
                                    String file = cursor.getString( 0 );
                                    deleteFile( file );
                                }
                                dataBaseHelper.deleteOneTask( taskPosition );
                                dataBaseHelper.deleteAllAttachments( taskPosition );
                                dataBaseHelper.deleteSubTasks( taskPosition );
                                dialog.dismiss();
                            }
                        } ).setNegativeButton( "Keep Editing", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        } );
                AlertDialog dialog = builder.create();
                dialog.show();



            }
        } );




        return view;
    }

    private void deleteFile(String inputPath) {
        try {
            // delete the original file
            new File(inputPath).delete();
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    private void shareTaskDataFun() {
        ArrayList<String> subTasks = new ArrayList<>();
        ArrayList<Uri> attachment = new ArrayList<>();
        Cursor AC = dataBaseHelper.getAttachment( taskPosition );
        Cursor STC = dataBaseHelper.getSubTasks( taskPosition );
        if (STC.getCount() == 0) {
        }
        while (STC.moveToNext()) {
            subTasks.add( STC.getString( 1 ) );
        }
        if (AC.getCount() == 0) {//do nothing
        }
        while (AC.moveToNext()) {

            Uri uri = Uri.parse( AC.getString( 2 ) );
            attachment.add( uri );
        }

        Intent intent = new Intent();
        intent.setAction( Intent.ACTION_SEND );
        intent.putExtra( Intent.EXTRA_EMAIL, "*/*" );
        intent.putExtra( Intent.EXTRA_TITLE, " Task Title :" + taskTitle );
        intent.putExtra( Intent.EXTRA_TEXT, "SubTasks : " + subTasks + "\nTask notes :" + taskNotes + "\nTask Reminder Time : " + reminder_date + "\nTask Created date: " + taskCreatedDate );
        intent.putParcelableArrayListExtra( Intent.EXTRA_STREAM, attachment );
        intent.setType( "*/*" );

        Intent shareNow = Intent.createChooser( intent, "Share" + " \"" + taskTitle + " \"  " );

        startActivity( shareNow );

    }

    public static EditTask editTaskInstence() {
        return new EditTask();
    }


    private void launchReminderDialogFun() {

        setTimeCV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editAddReminderAlertDialog();
            }
        } );
        tomorrow9AmCV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditRemindTagsLLAndStuffs();
                editSetTimeTv.setText( MyTimeSettingClass.getTomorrowMorning() );
                dataBaseHelper.update( editSetTimeTv.getText().toString(), MyTimeSettingClass.tomorrowPlaceDate(), "","1", taskPosition );

            }
        } );
        daily_WeeklyCV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddReminderAlertDialog();
            }
        } );

        editSetTimeLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddReminderAlertDialog();

            }

        } );
    }




//    private DatePickerDialog.OnDateSetListener datePickerDialog1 = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//            calendar.set( Calendar.YEAR, year );
//            calendar.set( Calendar.MONTH, month );
//            calendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );
//
//            Calendar calendar1 = Calendar.getInstance();
//
//            checkYear = calendar.get( Calendar.YEAR );
//            currentYear = calendar1.get( Calendar.YEAR );
//            isTomorrow = calendar.get( Calendar.DAY_OF_MONTH );
//            mTomorrow = calendar1.get( Calendar.DAY_OF_MONTH ) + 1;
//            isToday = calendar.get( Calendar.DAY_OF_MONTH );
//            mToday = calendar1.get( Calendar.DAY_OF_MONTH );
//
//            myTimeSettingClass.setCustomPlaceDate( dayOfMonth, month, year );
//            date_to_place_task = MyTimeSettingClass.getCustomPlaceDate();
//
//
//        }
//    };
//    private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @Override
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//            calendar.set( Calendar.HOUR_OF_DAY, hourOfDay );
//            calendar.set( Calendar.MINUTE, minute );
//
//            if (minute == 0 && checkYear > currentYear && isTomorrow != mTomorrow && isToday != mToday) {
//                sformat = new SimpleDateFormat( "dd MMM yyyy, h a" );
//            } else if (minute != 0 && checkYear > currentYear && isTomorrow != mTomorrow && isToday != mToday) {
//                sformat = new SimpleDateFormat( "d MMM yyyy, h:mm a" );
//            }
//
//            if (minute == 0 && checkYear == currentYear && isTomorrow != mTomorrow) {
//                sformat = new SimpleDateFormat( "d MMM, h a" );
//            } else if (minute != 0 && checkYear == currentYear && isTomorrow != mTomorrow) {
//                sformat = new SimpleDateFormat( "d MMM, h:mm a" );
//            }
//
//            if (minute == 0 && isTomorrow == mTomorrow && isToday != mToday) {
//                sformat = new SimpleDateFormat( "EEE, h a " );
//            } else if (minute != 0 && isTomorrow == mTomorrow && isToday != mToday) {
//                sformat = new SimpleDateFormat( "EEE, h:mm a" );
//            }
//
//            if (minute == 0 && isToday == mToday && isTomorrow != mTomorrow) {
//                sformat = new SimpleDateFormat( "h a" );
//            } else if (minute != 0 && isToday == mToday && isTomorrow != mTomorrow) {
//                sformat = new SimpleDateFormat( "h:mm a" );
//            }
//            reminder_date = sformat.format( calendar.getTime() );
//
//            Log.i( "formated date", reminder_date );
//            dataBaseHelper.update( reminder_date, date_to_place_task, taskPosition );
//            editSetTimeTv.setText( reminder_date );
//
//        }
//    };


    private void launchNotesDialogFragFun() {

        notesHolderTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myNotesAlertDialog();

            }
        } );
    }


    private void hideEditRemindTagsLLAndStuffs() {
        editSetTimeLL.setVisibility( View.VISIBLE );
        editRemindTagsLL.setVisibility( View.GONE );
        editDeleteUpperTv.setVisibility( View.VISIBLE );
        editReminderOnOffSwitch.setChecked( true );

    }

    private void showEditRemindTagsLLAndStuffs() {
        editSetTimeLL.setVisibility( View.GONE );
        editRemindTagsLL.setVisibility( View.VISIBLE );
        editDeleteUpperTv.setVisibility( View.GONE );
        editReminderOnOffSwitch.setChecked( false );
    }

    private void getDataInBSHDF() {

        taskPosition = getArguments().getString( "task_position" );
        taskTitle = getArguments().getString( "Task_Title" );
        repeatValue = getArguments().getString( "Repeat_Value" );
        subTasks = getArguments().getString( "Sub_Tasks" );
        reminder_date = getArguments().getString( "Reminder_date" );
        taskNotes = getArguments().getString( "Task_Note" );
        taskCreatedDate = getArguments().getString( "Task_Created_Date" );
        attachments = getArguments().getString( "Attachment" );

        edittaskTitle.setText( taskTitle );
        createdTextView.setText( taskCreatedDate );


        if (reminder_date.matches( "" )) {
            showEditRemindTagsLLAndStuffs();

        } else {
            hideEditRemindTagsLLAndStuffs();
            if (repeatValue.matches( "" )) {
                editSetTimeTv.setText( reminder_date );
            } else {
                if (repeatValue.matches( "daily" )) {
                    editSetTimeTv.setText( "Once a day " + reminder_date );
                } else if (repeatValue.matches( "weekly" )) {
                    editSetTimeTv.setText( "Once a week " + reminder_date );
                } else if (repeatValue.matches( "monthly" )) {
                    editSetTimeTv.setText( "Once a month " + reminder_date );
                } else if (repeatValue.matches( "yearly" )) {
                    editSetTimeTv.setText( "every year " + reminder_date );
                }
            }

        }

        if (taskNotes.matches( "" )) {
            notesHolderTv.setText( "Tap to add Notes" );
        } else {
            notesHolderTv.setText( taskNotes );
        }
        if (subTasks == null) {
            subTaskRecyclerView.setVisibility( View.GONE );
            Toast.makeText( getContext(), "null", Toast.LENGTH_SHORT ).show();
            addSubTasksET.setVisibility( View.VISIBLE );
            addNewSubTaskLL.setVisibility( View.GONE );
        } else {

            subTaskRecyclerView.setVisibility( View.VISIBLE );
            addSubTasksET.setVisibility( View.GONE );
            addNewSubTaskLL.setVisibility( View.VISIBLE );
            getSubTaskFromDb();
        }
        if (attachments == null) {
            edit_uploadTv.setVisibility( View.GONE );
            tabToAddAttachmentsLL.setVisibility( View.VISIBLE );
            attachmentRecyclerView.setVisibility( View.GONE );

        } else {
            edit_uploadTv.setVisibility( View.VISIBLE );
            tabToAddAttachmentsLL.setVisibility( View.GONE );
            attachmentRecyclerView.setVisibility( View.VISIBLE );
            getAttachmentFromDB();
        }


    }

    //add reminder AlertDialog work
    private void editAddReminderAlertDialog() {
        Calendar c = Calendar.getInstance();
        final int timeOfDay = c.get( Calendar.HOUR_OF_DAY );

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate( R.layout.editaddreminderdialogfrag, null );

        edit_oneTimeAddReminderSwitch = view.findViewById( R.id.edit_oneTimeAddReminderSwitch );
        edit_oneTimeAddReminderSwitch.setChecked( false );

        edit_task_SetBtn = view.findViewById( R.id.edit_task_SetBtn );
        edit_task_SetBtn.setEnabled( false );
        edit_task_cancelBtn = view.findViewById( R.id.edit_task_cancelBtn );
        edit_showReminderTimeCL = view.findViewById( R.id.edit_showReminderTimeCL );
        edit_showReminderTimeCL.setVisibility( View.GONE );
        edit_remindMeOrNoReminderTv = view.findViewById( R.id.edit_remindMeOrNoReminderTv );
        edit_addReminderShowTimeTv = view.findViewById( R.id.edit_addReminderShowTimeTv );
        edit_addReminderDeleteTimeIV = view.findViewById( R.id.edit_addReminderDeleteTimeIV );

        editTagsLL = view.findViewById( R.id.editTagsLL );
        oneTimeBtn = view.findViewById( R.id.oneTime_button );


        repeatBtn = view.findViewById( R.id.repeat_button );
        repeat_Daily_LL = view.findViewById( R.id.edit_dailyLL );
        repeat_Weekly_LL = view.findViewById( R.id.edit_weeklyLL );
        repeat_Monthly_LL = view.findViewById( R.id.edit_monthlyLL );
        repeat_Yearly_LL = view.findViewById( R.id.edit_yearlyLL );

        repeatTimeHolderTv = view.findViewById( R.id.repeatTimeHolderTv );

        locationBtn = view.findViewById( R.id.location_button );

        repeatLL = view.findViewById( R.id.repeatLL );
        editTagsLL = view.findViewById( R.id.editTagsLL );
        editLocationLL = view.findViewById( R.id.editLocationLL );

        edit_later_todayLL = view.findViewById( R.id.edit_later_todayLL );
        edit_thisEveningLL = view.findViewById( R.id.edit_thisEveningLL );
        edit_tomorrowLL = view.findViewById( R.id.edit_tomorrowLL );
        edit_nextWeekLL = view.findViewById( R.id.edit_nextWeekLL );
        edit_somedayLL = view.findViewById( R.id.edit_somedayLL );
        edit_customLL = view.findViewById( R.id.edit_customLL );

        editLaterTodayTimeTv = view.findViewById( R.id.edit_latertodayTimeTv );
        editThisEvening = view.findViewById( R.id.editThis_EveningTv );
        editTimeTomorrowTv = view.findViewById( R.id.editTimeTomorrowTv );
        ediTimeNextweekTv = view.findViewById( R.id.ediTimeNextweekTv );


        final AlertDialog.Builder builder = new AlertDialog.Builder( getContext() ).setView( view ).setCancelable( true );
        String s = editSetTimeTv.getText().toString();
        final AlertDialog dialog = builder.create();
        if (isOneButton) {

            if (editSetTimeTv.length() == 0) {
                changeButtonBg( oneTimeBtn, repeatBtn, locationBtn );
                repeatLL.setVisibility( View.GONE );
                edit_task_SetBtn.setEnabled( false );
                edit_oneTimeAddReminderSwitch.setChecked( false );
                editTagsLL.setVisibility( View.VISIBLE );
                edit_showReminderTimeCL.setVisibility( View.GONE );
            } else if (s.contains( "Once" )) {
                repeatLayoutSomeStuffHiding();
                dialog.show();

            } else if (s.contains( "every" )) {
                repeatLayoutSomeStuffHiding();
            } else {
                changeButtonBg( oneTimeBtn, repeatBtn, locationBtn );
                repeatLL.setVisibility( View.GONE );
                edit_task_SetBtn.setEnabled( false );
                edit_oneTimeAddReminderSwitch.setChecked( false );
                editTagsLL.setVisibility( View.GONE );
                edit_remindMeOrNoReminderTv.setText( "Remind Me" );
                edit_showReminderTimeCL.setVisibility( View.VISIBLE );
                edit_addReminderShowTimeTv.setText( editSetTimeTv.getText().toString() );
            }
            dialog.show();
        } else {
            repeatLayoutSomeStuffHiding();
            dialog.show();
        }

        if (timeOfDay > 0 && timeOfDay < 18) {
            edit_thisEveningLL.setVisibility( View.VISIBLE );
            edit_later_todayLL.setVisibility( View.VISIBLE );
        } else {
            edit_thisEveningLL.setVisibility( View.GONE );
            edit_later_todayLL.setVisibility( View.GONE );
        }
        editLocationLL.setVisibility( View.GONE );


        edit_oneTimeAddReminderSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                } else {
                }
            }
        } );


        edit_later_todayLL.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                edit_oneTimeAddReminderSwitch.setChecked( true );
                edit_task_SetBtn.setEnabled( true );
                editTagsLL.setVisibility( View.GONE );
                edit_showReminderTimeCL.setVisibility( View.VISIBLE );
                reminder_date = MyTimeSettingClass.getLaterToday();
                date_to_place_task = MyTimeSettingClass.todayPlaceDate();
                edit_remindMeOrNoReminderTv.setText( "Remind Me" );
                edit_addReminderShowTimeTv.setText( reminder_date );

            }
        } );

        edit_thisEveningLL.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                edit_oneTimeAddReminderSwitch.setChecked( true );
                edit_task_SetBtn.setEnabled( true );
                editTagsLL.setVisibility( View.GONE );
                edit_showReminderTimeCL.setVisibility( View.VISIBLE );
                reminder_date = MyTimeSettingClass.getToday6pm();
                date_to_place_task = MyTimeSettingClass.todayPlaceDate();
                edit_remindMeOrNoReminderTv.setText( "Remind Me" );
                edit_addReminderShowTimeTv.setText( reminder_date );


            }
        } );
        edit_tomorrowLL.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                edit_oneTimeAddReminderSwitch.setChecked( true );
                edit_task_SetBtn.setEnabled( true );
                editTagsLL.setVisibility( View.GONE );
                edit_showReminderTimeCL.setVisibility( View.VISIBLE );
                reminder_date = MyTimeSettingClass.getTomorrowMorning();
                date_to_place_task = MyTimeSettingClass.tomorrowPlaceDate();
                edit_remindMeOrNoReminderTv.setText( "Remind Me" );
                edit_addReminderShowTimeTv.setText( reminder_date );

            }
        } );
        edit_nextWeekLL.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                edit_oneTimeAddReminderSwitch.setChecked( true );
                edit_task_SetBtn.setEnabled( true );
                editTagsLL.setVisibility( View.GONE );
                edit_showReminderTimeCL.setVisibility( View.VISIBLE );
                reminder_date = MyTimeSettingClass.getNextWeek();
                date_to_place_task = MyTimeSettingClass.nextWeekPlaceDate();
                edit_remindMeOrNoReminderTv.setText( "Remind Me" );
                edit_addReminderShowTimeTv.setText( reminder_date );



            }
        } );
        edit_somedayLL.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                edit_oneTimeAddReminderSwitch.setChecked( true );
                edit_task_SetBtn.setEnabled( true );
                editTagsLL.setVisibility( View.GONE );
                edit_showReminderTimeCL.setVisibility( View.VISIBLE );
                reminder_date = "";
                date_to_place_task = "";
                edit_addReminderDeleteTimeIV.setVisibility( View.GONE );
                edit_remindMeOrNoReminderTv.setText( "Someday" );
                edit_addReminderShowTimeTv.setText( "Tap to change" );


            }
        } );


        edit_customLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get( Calendar.YEAR );
                final int month = calendar.get( Calendar.MONTH );
                final int day = calendar.get( Calendar.DAY_OF_MONTH );

                DatePickerDialog dP = new DatePickerDialog( Objects.requireNonNull( getContext() ), datePickerDialog, year, month, day );
                dP.getDatePicker().setMinDate( System.currentTimeMillis() - 1000 );
                dP.show();


            }
        } );


        edit_task_SetBtn.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {





                if (isOneButton) {
                    if (repeatValue.matches( "" )) {

                    } else {

                    }
                    mainActivity.setTaskFragDefaultBNBItem();
                    Toast.makeText( getContext(), "i am Clicked", Toast.LENGTH_SHORT ).show();
                    hideEditRemindTagsLLAndStuffs();
                    edit_addReminderShowTimeTv.setText( reminder_date );
                    editSetTimeTv.setText( reminder_date );
                    dataBaseHelper.update( reminder_date, date_to_place_task, "","1", taskPosition );
                    alarmSettingClass.setAllAlarm();
                    dialog.dismiss();

                } else {
                    if (!isRepeatllHidden) {
                        isRepeatllHidden = true;
                        repeatLL.setVisibility( View.GONE );
                        edit_remindMeOrNoReminderTv.setText( "Remind me" );
                        edit_showReminderTimeCL.setVisibility( View.VISIBLE );

                        if (checkRepeat.matches( "daily" )) {
                            edit_showReminderTimeCL.setVisibility( View.VISIBLE );
                            repeatString = ("Once a day " + reminder_date);
                            whichOnIsClick = "Once a day ";


                        } else if (checkRepeat.matches( "weekly" )) {
                            edit_showReminderTimeCL.setVisibility( View.VISIBLE );
                            repeatString = ("Once a week " + reminder_date);
                            whichOnIsClick = "Once a week ";


                        } else if (checkRepeat.matches( "monthly" )) {
                            edit_showReminderTimeCL.setVisibility( View.VISIBLE );
                            repeatString = ("Once a month " + reminder_date);
                            whichOnIsClick = "Once a month ";


                        } else if (checkRepeat.matches( "yearly" )) {
                            edit_showReminderTimeCL.setVisibility( View.VISIBLE );
                            repeatLL.setVisibility( View.GONE );
                            repeatString = ("every year " + reminder_date);
                            whichOnIsClick = "every year ";

                        } else {
                            edit_showReminderTimeCL.setVisibility( View.VISIBLE );
                            repeatString = ("Once a day " + reminder_date);
                            whichOnIsClick = "Once a day ";
                        }
                        edit_addReminderShowTimeTv.setText( whichOnIsClick + reminder_date );
                        if (repeatValue.matches( "" )) {
//                            RepeatToolTip(v);

                        } else {
                        }
                        edit_task_SetBtn.setText( "save" );

                    } else {
                        if (date_to_place_task.matches( "" )) {
                            editSetTimeTv.setText( whichOnIsClick + MyTimeSettingClass.getTomorrow() );
                            edit_addReminderShowTimeTv.setText( whichOnIsClick + MyTimeSettingClass.getTomorrow() );
                            dataBaseHelper.update( MyTimeSettingClass.getTomorrow(), MyTimeSettingClass.todayPlaceDate(),
                                    checkRepeat,"1", taskPosition );
                            alarmSettingClass.setAllAlarm();


                        } else {
                            Cursor cursor = dataBaseHelper.getDateToPlaceSingleRowValue( taskPosition );
                            if (cursor.getCount() == 0) {

                            }
                            while (cursor.moveToNext()) {

                                date_to_place_task = cursor.getString( 0 );
                            }
                            editSetTimeTv.setText( whichOnIsClick + reminder_date );
                            edit_addReminderShowTimeTv.setText( whichOnIsClick + reminder_date );
                            dataBaseHelper.update( reminder_date, date_to_place_task, checkRepeat,"1", taskPosition );
                            alarmSettingClass.setAllAlarm();

                        }
                        mainActivity.setTaskFragDefaultBNBItem();
                        dialog.dismiss();

                    }
                }
            }
        } );

        edit_task_cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );


        edit_addReminderDeleteTimeIV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_task_SetBtn.setEnabled( false );
                edit_oneTimeAddReminderSwitch.setChecked( false );
                edit_remindMeOrNoReminderTv.setText( "No reminder set" );
                edit_addReminderShowTimeTv.setText( "Tap To add" );
            }
        } );

        edit_addReminderShowTimeTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder( getContext() );
                builder1.setCancelable( true ).setTitle( "EDIT REMINDER DETAILS" ).setMessage( "Would you like to change the following detail" );
                builder1.setPositiveButton( "yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        edit_showReminderTimeCL.setVisibility( View.GONE );
                        repeatLL.setVisibility( View.VISIBLE );
                        dialog.dismiss();
                    }
                } ).setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } );

                AlertDialog dialog1 = builder1.create();


                String text = edit_addReminderShowTimeTv.getText().toString();
                if (text.matches( "Tap To add" )) {

                    if (isOneButton) {
                        edit_showReminderTimeCL.setVisibility( View.GONE );
                        editTagsLL.setVisibility( View.VISIBLE );
                    } else {

                        edit_showReminderTimeCL.setVisibility( View.GONE );
                        repeatLL.setVisibility( View.VISIBLE );

                    }

                } else {

                    if (isOneButton) {
                        Calendar calendar = Calendar.getInstance();
                        final int year = calendar.get( Calendar.YEAR );
                        final int month = calendar.get( Calendar.MONTH );
                        final int day = calendar.get( Calendar.DAY_OF_MONTH );


                        DatePickerDialog dP = new DatePickerDialog( Objects.requireNonNull( getContext() ), datePickerDialog, year, month, day );
                        dP.getDatePicker().setMinDate( System.currentTimeMillis() - 1000 );
                        dP.show();
                    } else {
                        dialog1.show();
                    }

                }

            }
        } );


        editLaterTodayTimeTv.setText( MyTimeSettingClass.getLaterToday() );
        editThisEvening.setText( MyTimeSettingClass.getToday6pm() );
        editTimeTomorrowTv.setText( MyTimeSettingClass.getTomorrowMorning() );
        ediTimeNextweekTv.setText( MyTimeSettingClass.getNextWeek() );


        oneTimeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_task_SetBtn.setText( "set" );
                isOneButton = true;
                edit_showReminderTimeCL.setVisibility( View.GONE );
                editTagsLL.setVisibility( View.VISIBLE );
                repeatLL.setVisibility( View.GONE );
                editLocationLL.setVisibility( View.GONE );
                changeButtonBg( oneTimeBtn, repeatBtn, locationBtn );
            }
        } );

        if (reminder_date.matches( "" )) {
            repeatTimeHolderTv.setText( MyTimeSettingClass.getTomorrow() );
        } else {
            repeatTimeHolderTv.setText( reminder_date );
        }

        repeatBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOneButton = false;
                edit_showReminderTimeCL.setVisibility( View.GONE );
                changeButtonBg( repeatBtn, oneTimeBtn, locationBtn );
                editTagsLL.setVisibility( View.GONE );
                repeatLL.setVisibility( View.VISIBLE );
                editLocationLL.setVisibility( View.GONE );

            }
        } );
        repeat_Daily_LL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRepeat = "daily";
                edit_task_SetBtn.setEnabled( true );

            }
        } );
        repeat_Weekly_LL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRepeat = "weekly";
                edit_task_SetBtn.setEnabled( true );

            }
        } );
        repeat_Monthly_LL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRepeat = "monthly";
                edit_task_SetBtn.setEnabled( true );

            }
        } );
        repeat_Yearly_LL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRepeat = "yearly";
                edit_task_SetBtn.setEnabled( true );

            }
        } );
        repeatTimeHolderTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get( Calendar.YEAR );
                final int month = calendar.get( Calendar.MONTH );
                final int day = calendar.get( Calendar.DAY_OF_MONTH );

                DatePickerDialog dP = new DatePickerDialog( Objects.requireNonNull( getContext() ), datePickerDialog, year, month, day );
                dP.getDatePicker().setMinDate( System.currentTimeMillis() - 1000 );
                dP.show();


            }
        } );

        locationBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_task_SetBtn.setText( "set" );

                edit_showReminderTimeCL.setVisibility( View.GONE );
                changeButtonBg( locationBtn, oneTimeBtn, repeatBtn );
                editTagsLL.setVisibility( View.GONE );
                repeatLL.setVisibility( View.GONE );
                editLocationLL.setVisibility( View.VISIBLE );
            }
        } );


    }

    public void repeatLayoutSomeStuffHiding() {
        changeButtonBg( repeatBtn, oneTimeBtn, locationBtn );
        isOneButton = false;
        repeatBtn.setSelected( true );
        edit_task_SetBtn.setEnabled( true );
        repeatLL.setVisibility( View.GONE );
        editTagsLL.setVisibility( View.GONE );
        edit_showReminderTimeCL.setVisibility( View.VISIBLE );
        edit_remindMeOrNoReminderTv.setText( "Remind Me" );
        edit_addReminderShowTimeTv.setText( editSetTimeTv.getText().toString() );
    }




    private DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            calendar.set( Calendar.YEAR, year );
            calendar.set( Calendar.MONTH, month );
            calendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );

            Calendar calendar1 = Calendar.getInstance();

            checkYear = calendar.get( Calendar.YEAR );
            currentYear = calendar1.get( Calendar.YEAR );
            isTomorrow = calendar.get( Calendar.DAY_OF_MONTH );
            mTomorrow = calendar1.get( Calendar.DAY_OF_MONTH ) + 1;
            isToday = calendar.get( Calendar.DAY_OF_MONTH );
            mToday = calendar1.get( Calendar.DAY_OF_MONTH );

            myTimeSettingClass.setCustomPlaceDate( dayOfMonth, month, year );
            date_to_place_task = MyTimeSettingClass.getCustomPlaceDate();

            final int hour = calendar.get( Calendar.HOUR_OF_DAY );
            final int minutes = calendar.get( Calendar.MINUTE );

            TimePickerDialog tP = new TimePickerDialog( getContext(), timePickerListener, hour, minutes, false );
            tP.show();


        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            calendar.set( Calendar.HOUR_OF_DAY, hourOfDay );
            calendar.set( Calendar.MINUTE, minute );

            if (minute == 0 && checkYear > currentYear && isTomorrow != mTomorrow && isToday != mToday) {
                sformat = new SimpleDateFormat( "dd MMM yyyy, h a" );
            } else if (minute != 0 && checkYear > currentYear && isTomorrow != mTomorrow && isToday != mToday) {
                sformat = new SimpleDateFormat( "d MMM yyyy, h:mm a" );
            }

            if (minute == 0 && checkYear == currentYear && isTomorrow != mTomorrow) {
                sformat = new SimpleDateFormat( "d MMM, h a" );
            } else if (minute != 0 && checkYear == currentYear && isTomorrow != mTomorrow) {
                sformat = new SimpleDateFormat( "d MMM, h:mm a" );
            }

            if (minute == 0 && isTomorrow == mTomorrow && isToday != mToday) {
                sformat = new SimpleDateFormat( "EEE, h a " );
            } else if (minute != 0 && isTomorrow == mTomorrow && isToday != mToday) {
                sformat = new SimpleDateFormat( "EEE, h:mm a" );
            }

            if (minute == 0 && isToday == mToday && isTomorrow != mTomorrow) {
                sformat = new SimpleDateFormat( "h a" );
            } else if (minute != 0 && isToday == mToday && isTomorrow != mTomorrow) {
                sformat = new SimpleDateFormat( "h:mm a" );
            }

            if (isOneButton) {
                reminder_date = sformat.format( calendar.getTime() );
                edit_task_SetBtn.setEnabled( true );
                editTagsLL.setVisibility( View.GONE );
                edit_oneTimeAddReminderSwitch.setChecked( true );
                edit_addReminderDeleteTimeIV.setVisibility( View.VISIBLE );
                edit_remindMeOrNoReminderTv.setText( "Remind me" );
                edit_addReminderShowTimeTv.setText( reminder_date );
                edit_showReminderTimeCL.setVisibility( View.VISIBLE );
            } else {
                edit_remindMeOrNoReminderTv.setText( "Remind me" );
                reminder_date = sformat.format( calendar.getTime() );
                repeatTimeHolderTv.setText( reminder_date );
            }


            Log.i( "formated date", reminder_date );

        }
    };



    @SuppressLint("ResourceAsColor")
    public void changeButtonBg(Button setBg, Button removeBg1, Button removeBg2) {
        setBg.setBackground( getResources().getDrawable( R.drawable.threebuttonsafterclickbg ) );
        setBg.setTextColor( Color.WHITE );
        setBg.setTypeface( setBg.getTypeface(), Typeface.BOLD );
        removeBg1.setBackground( getResources().getDrawable( R.drawable.threebuttonsbeforeclickbg ) );
        removeBg1.setTextColor( Color.BLACK );
        removeBg1.setTypeface( removeBg1.getTypeface(), Typeface.NORMAL );
        removeBg2.setBackground( getResources().getDrawable( R.drawable.threebuttonsbeforeclickbg ) );
        removeBg2.setTextColor( Color.BLACK );
        removeBg2.setTypeface( removeBg2.getTypeface(), Typeface.NORMAL );


    }

    private void myNotesAlertDialog() {
        LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate( R.layout.edit_task_bshnotes_ad, null );

        myNotesBackIv = view.findViewById( R.id.editAddNotesBackIV );
        myNotesET = view.findViewById( R.id.editAddNotesET );
        myNotesSaveBtn = view.findViewById( R.id.editNotesSaveBTn );

        final AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setView( view );
        builder.setCancelable( true );

        final AlertDialog myDialog = builder.create();
        myDialog.show();

        if (taskNotes.matches( "" )) {
            myNotesET.setText( "" );
        } else {
            myNotesET.setText( taskNotes );
        }

        myNotesSaveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myNotesET.length() == 0) {

                    boolean isupdate = dataBaseHelper.updateNotesColumn( "", taskPosition );
                    if (isupdate) {
                        Toast.makeText( getContext(), "updated", Toast.LENGTH_SHORT ).show();
                    } else {
                        Toast.makeText( getContext(), "updated", Toast.LENGTH_SHORT ).show();
                    }
                    notesHolderTv.setText( "tap To add notes" );
                } else {

                    boolean isUpdate = dataBaseHelper.updateNotesColumn( myNotesET.getText().toString(), taskPosition );
                    if (isUpdate) {
                        Toast.makeText( getContext(), "updated", Toast.LENGTH_SHORT ).show();
                    } else {
                        Toast.makeText( getContext(), "updated", Toast.LENGTH_SHORT ).show();
                    }
                    notesHolderTv.setText( myNotesET.getText().toString() );
                }

                myDialog.dismiss();
            }
        } );

        myNotesBackIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.dismiss();
            }
        } );

    }

    private void subTaskFun() {
        addSubTasksET.setImeOptions( EditorInfo.IME_ACTION_DONE );
        addSubTasksET.setSingleLine();
        addSubTasksET.setOnEditorActionListener( new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    String ST = addSubTasksET.getText().toString(); // SB = sub task
                    if (ST.matches( "" )) {
                    } else {
                        boolean isInsert = dataBaseHelper.insertSubTask( ST, taskPosition );

                        if (isInsert) {
                            getSubTaskFromDb();
                            Toast.makeText( getContext(), "Ok", Toast.LENGTH_SHORT ).show();
                        } else {
                            Toast.makeText( getContext(), "No", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    addSubTasksET.setVisibility( View.GONE );
                    addNewSubTaskLL.setVisibility( View.VISIBLE );
                    subTaskRecyclerView.setVisibility( View.VISIBLE );
                    InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull( getActivity() ).getSystemService( Context.INPUT_METHOD_SERVICE );
                    inputMethodManager.hideSoftInputFromWindow( addSubTasksET.getWindowToken(), 0 );
                    addSubTasksET.setText( "" );

                }
                return false;
            }
        } );

        addNewSubTaskLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubTasksET.setVisibility( View.VISIBLE );
                addSubTasksET.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull( getActivity() ).getSystemService( Context.INPUT_METHOD_SERVICE );
                inputMethodManager.showSoftInput( addSubTasksET, InputMethodManager.SHOW_FORCED );


            }
        } );
    }

    public void getSubTaskFromDb() {
        Cursor cursor = dataBaseHelper.getSubTasks( taskPosition );
        List<MySubTaskModel> subTaskModeList = new ArrayList<>();


        if (cursor.getCount() == 0) {
        }
        while (cursor.moveToNext()) {
            subTaskModeList.add( new MySubTaskModel( cursor.getString( 0 ), cursor.getString( 1 ) ) );
        }
        subTaskAdapter = new SubTaskAdapter( getContext(), subTaskModeList, dataBaseHelper );
        subTaskRecyclerView.setAdapter( subTaskAdapter );
        subTaskAdapter.notifyDataSetChanged();
    }

    private void attachmentFun() {


        tabToAddAttachmentsLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                if (checkPermission()) {
                    Bundle bundle = new Bundle();
                    bundle.putString( "Position", taskPosition );
                    AttachmentsBottomSheet attachmentsBottomSheet = AttachmentsBottomSheet.getAttachInstance();
                    attachmentsBottomSheet.show( getActivity().getSupportFragmentManager(), "attachment BSHeet" );
                    attachmentsBottomSheet.setArguments( bundle );
                }
            }
        } );

        edit_uploadTv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                if (checkPermission()) {
                    Bundle bundle = new Bundle();
                    bundle.putString( "Position", taskPosition );
                    AttachmentsBottomSheet attachmentsBottomSheet = AttachmentsBottomSheet.getAttachInstance();
                    attachmentsBottomSheet.show( getActivity().getSupportFragmentManager(), "attachment BSHeet" );
                    attachmentsBottomSheet.setArguments( bundle );
                    attachmentsBottomSheet.setRecyclerCallBack( new RecyclerCallBack() {
                        @Override
                        public void mCallBack() {
                            getAttachmentFromDB();
                        }
                    } );
                }
            }
        } );
    }

    public void getAttachmentFromDB() {
        Cursor AC = dataBaseHelper.getAttachment( taskPosition ); // AC = ATTACHMENTS CURSOR
        List<AttachmentTaskModel> attachmentTaskModelList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager( getContext() ) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        attachmentRecyclerView.setLayoutManager( linearLayoutManager );

        if (AC.getCount() == 0) {//do nothing
        }
        while (AC.moveToNext()) {
            String string = AC.getString( 2 );
            if (string.contains( ".png" )) {
                attachmentTaskModelList.add( new AttachmentTaskModel( AC.getString( 0 ), R.drawable.image_foreground, AC.getString( 2 ), AC.getString( 3 ) ) );
            } else if (string.contains( ".jpg" )) {
                attachmentTaskModelList.add( new AttachmentTaskModel( AC.getString( 0 ), R.drawable.image_foreground, AC.getString( 2 ), AC.getString( 3 ) ) );
            } else if (string.contains( ".jpeg" )) {
                attachmentTaskModelList.add( new AttachmentTaskModel( AC.getString( 0 ), R.drawable.image_foreground, AC.getString( 2 ), AC.getString( 3 ) ) );
            } else if (string.contains( ".mp4" )) {
                attachmentTaskModelList.add( new AttachmentTaskModel( AC.getString( 0 ), R.drawable.record_video_foreground, AC.getString( 2 ), AC.getString( 3 ) ) );
            } else if (string.contains( ".mp3" )) {
                attachmentTaskModelList.add( new AttachmentTaskModel( AC.getString( 0 ), R.drawable.play_audio_foreground, AC.getString( 2 ), AC.getString( 3 ) ) );
            } else {
                attachmentTaskModelList.add( new AttachmentTaskModel( AC.getString( 0 ), R.drawable.image_foreground, AC.getString( 2 ), AC.getString( 3 ) ) );
            }
        }
        FragmentManager fragmentManager = getFragmentManager();
        attachmentTaskAdapter = new AttachmentTaskAdapter( getContext(), attachmentTaskModelList, dataBaseHelper, fragmentManager );
        attachmentRecyclerView.setAdapter( attachmentTaskAdapter );
        attachmentTaskAdapter.notifyDataSetChanged();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach( context );

    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull( getView() ).setFocusableInTouchMode( true );
        getView().requestFocus();
        getView().setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == MotionEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {

                    InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull( getActivity() ).getSystemService( Context.INPUT_METHOD_SERVICE );
                    inputMethodManager.hideSoftInputFromWindow( addSubTasksET.getWindowToken(), 0 );

                }
                return false;
            }
        } );
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public boolean checkPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission( getContext(), Manifest.permission.CAMERA );
        int readStoragePermission = ContextCompat.checkSelfPermission( getContext(), Manifest.permission.READ_EXTERNAL_STORAGE );
        int writeStoragePermission = ContextCompat.checkSelfPermission( getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE );

        if (cameraPermission == PackageManager.PERMISSION_GRANTED && readStoragePermission == PackageManager.PERMISSION_GRANTED
                && writeStoragePermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions( getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE );
            return false;
        }
    }


}
