package com.example.reminder.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.interfaces.RecyclerCallBack;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AttachmentsBottomSheet extends BottomSheetDialogFragment {

    private static final int AUDIO_REQUEST_CODE = 2020;


    private static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQ = 2;
    private static final int RECORD_VIDEO_CR = 3;
    private static final int RECORD_AUDIO_R = 4;

    DataBaseHelper dataBaseHelper;

    private LinearLayout fromGalleryLL, takeAPictureLL, recordVideoLL, recordAudioLL;
    private RecyclerCallBack mRecyclerCallBack;


    private String imageFilePathCamera, videoFilePathCamera;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat createdDateFormat = new SimpleDateFormat( "dd MMM yyyy" );

    // audio related
    String audioSavePath = "";
    MediaRecorder mediaRecorder;
    boolean startAudio = false;
    private int counter;
    private CountDownTimer countDownTimer;
    private long totalSeconds = 3600, intervalSeconds = 1;
    private ImageView audio_Record_StartStopIV;
    TextView audioTv;
    private Button audioCancelBtn, audioAddBtn;

    String taskPosition;


    public static AttachmentsBottomSheet getAttachInstance() {
        return new AttachmentsBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.attachmentsbottomsheet, container, false );

        taskPosition = getArguments().getString( "Position" );
        dataBaseHelper = new DataBaseHelper( getContext() );

        fromGalleryLL = view.findViewById( R.id.fromGalleryLL );
        takeAPictureLL = view.findViewById( R.id.takeAPictureLL );
        recordVideoLL = view.findViewById( R.id.recordVideoLL );
        recordAudioLL = view.findViewById( R.id.recordAudioLL );


        fromGalleryLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryWork();
            }
        } );
        takeAPictureLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageCamera();
            }
        } );
        recordVideoLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoCamera();

            }
        } );
        recordAudioLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAudioPermission()) {
                    audioFun();
                } else {

                }
            }
        } );

        return view;

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

    private void openVideoCamera() {
        Intent pictureIntent = new Intent( MediaStore.ACTION_VIDEO_CAPTURE );
        if (pictureIntent.resolveActivity( getActivity().getPackageManager() ) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createVideoFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile( getActivity(), getActivity().getPackageName() + ".provider", photoFile );
                pictureIntent.putExtra( MediaStore.EXTRA_OUTPUT, photoURI );
                startActivityForResult( pictureIntent, RECORD_VIDEO_CR );
            }
        }
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

    private File createVideoFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat( "dd",
                        Locale.getDefault() ).format( new Date() );
        String videoFileName = "VIDEO_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir( Environment.DIRECTORY_PICTURES );
        File video = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",  /* suffix */
                storageDir     /* directory*/
        );
        videoFilePathCamera = video.getAbsolutePath();
        return video;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String imageName = "Image" + new SimpleDateFormat( "ddhmmss" ).format( calendar.getTime() ) + ".png";
        File dir = new File( Environment.getExternalStorageDirectory().getPath() + "/.Reminder/" );
        String imagePath = Environment.getExternalStorageDirectory() + "/.Reminder/" + imageName;


        String videoName = "Video" + new SimpleDateFormat( "ddhmmss" ).format( calendar.getTime() ) + ".mp4";
        String videoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.Reminder/" + videoName;


        try {

            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

                Uri selectedImageUri = data.getData();
                String filePath = getPath( getActivity().getApplicationContext(), selectedImageUri );
                Log.d( "Picture Path", filePath );

                boolean insert = dataBaseHelper.insertFile( filePath, createdDateFormat.format( calendar.getTime() ) ,taskPosition   );

                if (insert) {
                    Toast.makeText( getContext(), "insert", Toast.LENGTH_SHORT ).show();
                    mRecyclerCallBack.mCallBack();
                } else {
                    Toast.makeText( getContext(), "not insert", Toast.LENGTH_SHORT ).show();
                }

            } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK) {

                if (!dir.exists()) {
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

                boolean insert = dataBaseHelper.insertFile( imagePath,createdDateFormat.format( calendar.getTime()), taskPosition );
                if (insert) {
                    mRecyclerCallBack.mCallBack();
                    Toast.makeText( getContext(), "inserted", Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
                }

            } else if (requestCode == RECORD_VIDEO_CR && resultCode == Activity.RESULT_OK) {

                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File SourceFile = new File(videoFilePathCamera);

                File DestinationFile = new File(videoPath);

                if(SourceFile.renameTo(DestinationFile))
                {
                    Log.v("Moving", "Moving file successful.");
                }
                else
                {
                    Log.v("Moving", "Moving file failed.");
                }

                boolean insert = dataBaseHelper.insertFile( videoPath,createdDateFormat.format( calendar.getTime()), taskPosition );
                if (insert) {
                    Toast.makeText( getContext(), "inserted", Toast.LENGTH_SHORT ).show();
                    mRecyclerCallBack.mCallBack();

                } else {
                    Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText( getContext(), e.getMessage(), Toast.LENGTH_SHORT ).show();
        }

    }

    private void galleryWork() {

        Intent photoPickerIntent = new Intent( Intent.ACTION_PICK );
        photoPickerIntent.setType( "*/*" );
        photoPickerIntent.putExtra( Intent.CATEGORY_APP_GALLERY, new String[]{"image/*", "video/*"} );
        startActivityForResult( photoPickerIntent, PICK_IMAGE );
    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query( uri, proj, null, null, null );
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }


    public void audioFun() {


        View view = LayoutInflater.from( getContext() ).inflate( R.layout.recordaudiolayout, null );
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setView( view );

        final AlertDialog dialog = builder.create();
        dialog.show();


        audio_Record_StartStopIV = view.findViewById( R.id.audio_StartStop_IV );
        audioTv = view.findViewById( R.id.audioTV );
        audioCancelBtn = view.findViewById( R.id.audio_CancelBtn );
        audioAddBtn = view.findViewById( R.id.audio_AddBtn );
        audioAddBtn.setEnabled( false );

        audio_Record_StartStopIV.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {


                    if (!startAudio) {
                        //  on audio stared
                        startAudio = true;
                        audio_Record_StartStopIV.setImageResource( R.drawable.stop_audio_foreground );
                        String audioName = "Audio" + new SimpleDateFormat( "ddhmmss" ).format( calendar.getTime() ) + ".mp3";
                        File dir = new File( Environment.getExternalStorageDirectory().getPath() + "/.Reminder/" );
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        audioSavePath = Environment.getExternalStorageDirectory() + "/.Reminder/" + audioName;
                        mediaRecorderFun();

                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                        } catch (Exception e) {
                            // error
                        }

                        final long[] secCounter = {totalSeconds};
                        countDownTimer = new CountDownTimer( totalSeconds * 1000, intervalSeconds * 1000 ) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long recordedTime = totalSeconds-secCounter[0];
                                secCounter[0]=secCounter[0]-1;
                                audioTv.setText( String.valueOf( recordedTime ) );
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onFinish() {
                                mediaRecorder.stop();
                                audioTv.setText( " Time Reached " );
                                audioAddBtn.setEnabled( true );
                                audio_Record_StartStopIV.setImageResource( R.drawable.record_audio_foreground );
                            }
                        };
                        countDownTimer.start();
                    }
                    // on audio stopped
                    else {
                        startAudio = false;
                        audioAddBtn.setEnabled( true );
                        audioTv.setText( " Press to Record" );
                        mediaRecorder.stop();
                        countDownTimer.cancel();

                }
            }
        } );

        audioAddBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean insert = dataBaseHelper.insertFile( audioSavePath,createdDateFormat.format( calendar.getTime()), taskPosition );
                if (insert) {
                    if (mRecyclerCallBack != null)
                    {
                        mRecyclerCallBack.mCallBack();
                    }


                    dialog.dismiss();
                    Toast.makeText( getContext(), "insert", Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText( getContext(), "not insert", Toast.LENGTH_SHORT ).show();
                }
            }
        } );

        audioCancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );

    }

    private void mediaRecorderFun() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource( MediaRecorder.AudioSource.MIC );
        mediaRecorder.setOutputFormat( MediaRecorder.OutputFormat.THREE_GPP );
        mediaRecorder.setAudioEncoder( MediaRecorder.OutputFormat.AMR_NB );
        mediaRecorder.setOutputFile( audioSavePath );

    }


    public void setRecyclerCallBack(RecyclerCallBack mRecyclerCallBack) {
        this.mRecyclerCallBack = mRecyclerCallBack;
    }



    private boolean checkAudioPermission() {
        int audioPermission = ContextCompat.checkSelfPermission( getContext(), Manifest.permission.RECORD_AUDIO );

        if (audioPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions( getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_REQUEST_CODE );
            return false;
        }
    }

}

