package com.example.reminder.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.reminder.Activity.AboutActivity;
import com.example.reminder.Activity.CompletedTasksActivity;
import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.NotificationSoundsAdapter;
import com.example.reminder.interfaces.EditTextStringListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.reminder.Fragments.AttachmentsBottomSheet.getPath;


public class SettingsFrag extends Fragment {

    private MainActivity mainActivity;
    private LinearLayout settingsCompletedTasks_LL,settingsAbout_LL,settingsNotification_LL;

    //profile Views
    private static final int CAMERA_REQ = 1;
    private static final int PICK_IMAGE = 2;
    private static final int REQUEST_CODE =1001 ;
    private SharedPreferences myPreferences;


    private CircleImageView personImageView;
    private TextView personNameTV;
    private ImageView profileEditIv,personNameEditIv;

    private String imageFilePathCamera;
    private Calendar calendar = Calendar.getInstance();

    String notificationStringPath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mainActivity = (MainActivity)getActivity();

        personImageView = view.findViewById( R.id.Profile_CircleImage );
        personNameTV = view.findViewById( R.id.person_name_tv );
        profileEditIv = view.findViewById( R.id.profileEditIv );
        personNameEditIv = view.findViewById( R.id.personNameEditIv );

        settingsCompletedTasks_LL = view.findViewById( R.id.settingsCompletedTasks_LL );
        settingsNotification_LL = view.findViewById( R.id.settingsNotification_LL );
        settingsAbout_LL = view.findViewById( R.id.settingsAbout_LL );

        myPreferences = getActivity().getSharedPreferences( "MY_PREFERENCES", Context.MODE_PRIVATE );

        Uri imageUri = Uri.parse( myPreferences.getString( "Profile_Image", "" ) );
        personImageView.setImageURI( imageUri  );
        personNameTV.setText(myPreferences.getString( "Person_name", "" ));




        profileEditIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfilePicFun();
            }
        } );

        personNameEditIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePersonName();
            }
        } );

        settingsCompletedTasks_LL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent = new Intent( getActivity(), CompletedTasksActivity.class );startActivity( intent );
            }
        } );
        settingsNotification_LL.setOnClickListener( new View.OnClickListener() {@Override
            public void onClick(View v) { notificationSoundsFun();
            }
        } );

        settingsAbout_LL.setOnClickListener( new View.OnClickListener() {@Override
            public void onClick(View v) { Intent intent = new Intent( getActivity(), AboutActivity.class );startActivity( intent );
            }
        } );

        return  view;
    }
    public void updateProfilePicFun() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate( R.layout.profile_update_ad_corg, null );
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setCancelable( true );
        builder.setView( view );

        final AlertDialog dialog = builder.create();
        dialog.show();

        TextView profile_fromGallery = view.findViewById( R.id.profile_fromGallery );
        TextView profile_openCamera = view.findViewById( R.id.profile_openCamera );
        Button profile_ADCancelBtn = view.findViewById( R.id.profile_ADCancelBtn );

        profile_fromGallery.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryWork();
            }
        } );
        profile_openCamera.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission())
                {
                    openImageCamera();
                }
            }
        } );
        profile_ADCancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );


    }


    private void openImageCamera() {
        Intent pictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        if (pictureIntent.resolveActivity( getActivity().getPackageManager() ) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile( getActivity(), getActivity().getPackageName() + ".provider", photoFile );
                pictureIntent.putExtra( MediaStore.EXTRA_OUTPUT,
                        photoURI );
                startActivityForResult( pictureIntent,
                        CAMERA_REQ );
            }
        }

    }

    public void updatePersonName()
    {
        View view = LayoutInflater.from( getContext() ).inflate( R.layout.person_name_ad_layouyt,null );
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext()  );
        builder.setView( view ).setCancelable( true );
        final EditText editPersonName = view.findViewById( R.id.person_name_et );
        Button cancel_person_name_btn =view.findViewById( R.id.cancel_person_name_btn );
        Button save_person_name_btn =view.findViewById( R.id.save_person_name_btn );
        final AlertDialog dialog = builder.create();
        dialog.show();

        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = myPreferences.edit();

        cancel_person_name_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        } );
        save_person_name_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editPersonName.getText().toString();

                if(name.matches( "" ))
                {
                    editor.putString( "Person_name",android.os.Build.MODEL  ).apply();
                }
                else
                {
                    editor.putString( "Person_name",name).commit();
                }
                mainActivity.setSettingsBNBItem();
                dialog.dismiss();
            }
        } );
        editPersonName.setOnEditorActionListener( new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((event!=null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId== EditorInfo.IME_ACTION_DONE))
                {
                    String name = editPersonName.getText().toString();

                    if (editPersonName.length()==0)
                    {
                        editor.putString( "Person_name",android.os.Build.MODEL ).apply();

                    }
                    else{
                        editor.putString( "Person_name",name).commit();
                    }
                    mainActivity.setSettingsBNBItem();

                }
                return false;
            }
        } );

        cancel_person_name_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );

    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat( "dd",
                        Locale.getDefault() ).format( new Date() );
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir( Environment.DIRECTORY_PICTURES );
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );
        imageFilePathCamera = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = myPreferences.edit();

        String imageName = "Image"+ ".png";
        File dir = new File( Environment.getExternalStorageDirectory().getPath() + "/.Reminder/Person/" );
        String imagePath = Environment.getExternalStorageDirectory() + "/.Reminder/Person/" + imageName;

        try {
            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
                Uri selectedImageUri = data.getData();
                String filePath = getPath( getActivity().getApplicationContext(), selectedImageUri );
                editor.putString( "Profile_Image",filePath ).apply();
                mainActivity.setSettingsBNBItem();


            }
            else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK) {

                if (!dir.exists())
                {
                    dir.mkdirs();
                }
                File SourceFile = new File(imageFilePathCamera);

                File DestinationFile = new File(imagePath);

                if(SourceFile.renameTo(DestinationFile))
                {
                    Log.v("Moving", "Moving file successful.");
                }
                else
                {
                    Log.v("Moving", "Moving file failed.");
                }

                editor.putString( "Profile_Image",imagePath ).commit();
                mainActivity.setSettingsBNBItem();


            }
        } catch (Exception e) {

        }
    }

    private void galleryWork() {

        Intent photoPickerIntent = new Intent( Intent.ACTION_PICK );
        photoPickerIntent.setType( "*/*" );
        photoPickerIntent.putExtra( Intent.CATEGORY_APP_GALLERY, new String[]{"image/*"} );
        startActivityForResult( photoPickerIntent, PICK_IMAGE );
    }

    public boolean checkPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA );
        int readStoragePermission = ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE );
        int writeStoragePermission = ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE );

        if (cameraPermission == PackageManager.PERMISSION_GRANTED && readStoragePermission == PackageManager.PERMISSION_GRANTED
                && writeStoragePermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions( getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE );
            return false;
        }
    }


    private ArrayList<String> getNotificationSoundsName() {
        RingtoneManager manager = new RingtoneManager(getContext());
        manager.setType(RingtoneManager.TYPE_NOTIFICATION);
        Cursor cursor = manager.getCursor();

        ArrayList<String> listOFNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            String title = cursor.getString( RingtoneManager.TITLE_COLUMN_INDEX );
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);

            String combinedString = uri + "/" + id+"/"+title;
            String finalSoundName = combinedString.substring( combinedString.lastIndexOf( "/" )+1 );
            listOFNames.add(finalSoundName);
        }

        return listOFNames;
    }
    private  ArrayList<String> getNotificationSoundsPath()
    {

        RingtoneManager ringtoneManager = new RingtoneManager( getContext() );
        ringtoneManager.setType( RingtoneManager.TYPE_NOTIFICATION );
        Cursor cursor = ringtoneManager.getCursor();

        ArrayList<String>listOfPaths = new ArrayList<>(  );
        while (cursor.moveToNext())
        {
            String id = cursor.getString( RingtoneManager.ID_COLUMN_INDEX );
            String uri = cursor.getString( RingtoneManager.URI_COLUMN_INDEX );

            listOfPaths.add( uri +"/"+id );
        }
        return listOfPaths;
    }

    private void notificationSoundsFun()
    {
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = myPreferences.edit();

        View view = LayoutInflater.from( getContext() ).inflate( R.layout.fragment_notification_sounds_dialog,null );
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setCancelable( true ).setView( view );

        final AlertDialog dialog = builder.create();
        dialog.show();

        boolean isVibrate = false;

        RecyclerView notificationSoundRecyclerView = view.findViewById( R.id.notificationSoundRecyclerView );

        CheckBox vibrateCheckBox = view.findViewById( R.id.vibrateCheckBox );
        Button notification_sound_cancelBtn =  view.findViewById( R.id.notification_sound_cancelBtn );
        Button notification_sound_saveBtn =  view.findViewById( R.id.notification_sound_saveBtn );

        notification_sound_cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dialog.dismiss();
            }
        } );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        notificationSoundRecyclerView.setLayoutManager( linearLayoutManager );
        NotificationSoundsAdapter notificationSoundsAdapter;

        notificationSoundsAdapter = new NotificationSoundsAdapter(getContext(), getNotificationSoundsName(),getNotificationSoundsPath());
        notificationSoundRecyclerView.setAdapter( notificationSoundsAdapter );
        notificationSoundsAdapter.notifyDataSetChanged();
        notificationSoundsAdapter.getPathListener( new EditTextStringListener() {
            @Override
            public void myString(String ss) {
                notificationStringPath = ss;
            }
        } );

        if (vibrateCheckBox.isChecked())
        {
            isVibrate = true;
        }

        final boolean finalIsVibrate = isVibrate;
        notification_sound_saveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean( "Is_Vibrate", finalIsVibrate ).apply();
                editor.putString( "NotificationSoundPath",notificationStringPath ).commit();
                dialog.dismiss();
            }
        } );



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
