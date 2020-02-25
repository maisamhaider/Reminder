package com.example.reminder.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexzh.circleimageview.CircleImageView;
import com.example.reminder.Activity.AboutActivity;
import com.example.reminder.Activity.CompletedTasksActivity;
import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.NotificationSoundsAdapter;
import com.example.reminder.classes.NotificationSounds;
import com.example.reminder.interfaces.EditTextStringListener;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class SettingsFrag extends Fragment {

    private MainActivity mainActivity;
    private LinearLayout settingsCompletedTasks_LL, settingsAbout_LL, settingsNotification_LL;
    private NotificationSounds notificationSounds;

    //profile Views
    private static final int CAMERA_REQ = 1;
    private static final int PICK_IMAGE = 2;
    private SharedPreferences myPreferences;


    private CircleImageView personImageView;
    private TextView personNameTV, appVersionTv1;
    private ImageView profileEditIv, personNameEditIv;

    private String imageFilePathCamera;
    private Calendar calendar = Calendar.getInstance();
    private boolean isVibrate = false;

    private String notificationStringPath;
    int itemPosition;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_settings, container, false );
        mainActivity = (MainActivity) getActivity();
        notificationSounds = new NotificationSounds( getActivity() );

        personImageView = view.findViewById( R.id.Profile_CircleImage );
        personNameTV = view.findViewById( R.id.person_name_tv );
        appVersionTv1 = view.findViewById( R.id.appVersionTv1 );
        profileEditIv = view.findViewById( R.id.profileEditIv );
        personNameEditIv = view.findViewById( R.id.personNameEditIv );

        settingsCompletedTasks_LL = view.findViewById( R.id.settingsCompletedTasks_LL );
        settingsNotification_LL = view.findViewById( R.id.settingsNotification_LL );
        settingsAbout_LL = view.findViewById( R.id.settingsAbout_LL );

        myPreferences = getActivity().getSharedPreferences( "MY_PREFERENCES", Context.MODE_PRIVATE );

        Uri imageUri = Uri.parse( myPreferences.getString( "Profile_Image", String.valueOf( R.drawable.user ) ) );
        personImageView.setImageURI( imageUri );


        personNameTV.setText( myPreferences.getString( "Person_name", Build.MODEL ) );
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo( Objects.requireNonNull( getContext() ).getPackageName(), 0 );
            String version = pInfo.versionName;
            appVersionTv1.setText( "To do list: " + version );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


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
            public void onClick(View v) {
                Intent intent = new Intent( getActivity(), CompletedTasksActivity.class );
                startActivity( intent );
            }
        } );
        settingsNotification_LL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationSoundsFun();
            }
        } );

        settingsAbout_LL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getActivity(), AboutActivity.class );
                startActivity( intent );
            }
        } );

        return view;
    }

    public void updateProfilePicFun() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate( R.layout.profile_update_ad_corg, null );
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setCancelable( true );
        builder.setView( view );

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );

//        TextView profile_fromGallery = view.findViewById( R.id.profile_fromGallery );
        TextView profile_openCamera = view.findViewById( R.id.profile_openCamera );
        ImageView profile_ADCancelBtn = view.findViewById( R.id.profile_ADCancelIv );

//        profile_fromGallery.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               galleryWork();
//                dialog.dismiss();
//            }
//        } );
        profile_openCamera.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.checkPermission()) {
                    openImageCamera();
                    dialog.dismiss();
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

    public void updatePersonName() {
        View view = LayoutInflater.from( getContext() ).inflate( R.layout.person_name_ad_layouyt, null );
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setView( view ).setCancelable( true );
        final EditText editPersonName = view.findViewById( R.id.person_name_et );
        ImageView cancel_person_name_btn = view.findViewById( R.id.cancel_person_name_Iv );
        ImageView save_person_name_btn = view.findViewById( R.id.save_person_name_Iv );
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );

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

                if (name.matches( "" )) {
                    editor.putString( "Person_name", android.os.Build.MODEL ).apply();
                } else {
                    editor.putString( "Person_name", name ).commit();
                }
                mainActivity.setSettingsBNBItem();
                dialog.dismiss();
            }
        } );
        editPersonName.setOnEditorActionListener( new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String name = editPersonName.getText().toString();

                    if (editPersonName.length() == 0) {
                        editor.putString( "Person_name", android.os.Build.MODEL ).commit();

                    } else {
                        editor.putString( "Person_name", name ).commit();
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
        @SuppressLint({"NewApi", "LocalSuppress"}) String timeStamp = new SimpleDateFormat( "dd",
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
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = myPreferences.edit();

        String imageName = "Image.png";
        File dir = new File( Environment.getExternalStorageDirectory().getPath() + "/.Reminder/Person/" );
        String imagePath = Environment.getExternalStorageDirectory() + "/.Reminder/Person/" + imageName;

        try {
//            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
//                Uri selectedImageUri = data.getData();
//                String filePath = getPath( getActivity().getApplicationContext(), selectedImageUri );
//                editor.putString( "Profile_Image", filePath ).commit();
//                mainActivity.setSettingsBNBItem();
//
//
//            } else
            if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK) {

                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File SourceFile = new File( imageFilePathCamera );


                File DestinationFile = new File( imagePath );

                if (SourceFile.renameTo( DestinationFile )) {
                    Log.v( "Moving", "Moving file successful." );
                } else {
                    Log.v( "Moving", "Moving file failed." );
                }

                Uri uri = Uri.fromFile( DestinationFile );
//                    personImageView.setImageURI( uri );
                editor.putString( "Profile_Image", imagePath ).commit();
                mainActivity.setSettingsBNBItem();


            }
        } catch (Exception e) {

        }
    }

//    private void galleryWork() {
//
//        Intent photoPickerIntent = new Intent( Intent.ACTION_PICK );
//        photoPickerIntent.setType( "image/*" );
//        photoPickerIntent.putExtra( Intent.CATEGORY_APP_GALLERY, new String[]{"image/*"} );
//        startActivityForResult( photoPickerIntent, PICK_IMAGE );
//    }
//



    private void notificationSoundsFun() {
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = myPreferences.edit();

        View view = LayoutInflater.from( getContext() ).inflate( R.layout.fragment_notification_sounds_dialog, null );
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setCancelable( true ).setView( view );

        final AlertDialog dialog = builder.create();
        dialog.show();


        RecyclerView notificationSoundRecyclerView = view.findViewById( R.id.notificationSoundRecyclerView );

        CheckBox vibrateCheckBox = view.findViewById( R.id.vibrateCheckBox );
        Button notification_sound_cancelBtn = view.findViewById( R.id.notification_sound_cancelBtn );
        Button notification_sound_saveBtn = view.findViewById( R.id.notification_sound_saveBtn );
        boolean isV = myPreferences.getBoolean( "Is_Vibrate", false );
        if (isV) {
            vibrateCheckBox.setChecked( true );
        } else {
            vibrateCheckBox.setChecked( false );
        }
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

        notificationSoundsAdapter = new NotificationSoundsAdapter( getContext(), notificationSounds.getNotificationSoundsName(), notificationSounds.getNotificationSoundsPath() );
        notificationSoundRecyclerView.setAdapter( notificationSoundsAdapter );
        notificationSoundsAdapter.notifyDataSetChanged();
        notificationSoundsAdapter.getPathListener( new EditTextStringListener() {
            @Override
            public void myString(String ss) {
                notificationStringPath = ss;
            }

            @Override
            public void myItemPosition(int pos) {
                itemPosition = pos;
            }
        } );

        vibrateCheckBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isVibrate = true;
                } else {
                    isVibrate = false;
                }
            }
        } );


        notification_sound_saveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean( "Is_Vibrate", isVibrate ).commit();
                editor.putString( "NotificationSoundPath", notificationStringPath ).commit();
                editor.putInt( "sound_item_position", itemPosition ).commit();
                dialog.dismiss();
            }
        } );


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
