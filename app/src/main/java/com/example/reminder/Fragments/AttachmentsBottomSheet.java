package com.example.reminder.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.interfaces.RecyclerCallBack;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AttachmentsBottomSheet extends BottomSheetDialogFragment {



    private static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQ = 2;
    private static final int RECORD_VIDEO_CR = 3;


    DataBaseHelper dataBaseHelper;


    private LinearLayout fromGalleryLL, takeAPictureLL, recordVideoLL, recordAudioLL;
    private ConstraintLayout audio_btns_cl;
    private RecyclerCallBack mRecyclerCallBack;


    private String imageFilePathCamera, videoFilePathCamera;
    Calendar calendar = Calendar.getInstance();
    @SuppressLint("NewApi")
    SimpleDateFormat createdDateFormat = new SimpleDateFormat( "dd MMM yyyy" );

    // audio related
    String audioSavePath = "";
    MediaRecorder mediaRecorder;
    boolean startAudio = false;
    private int counter;
    private CountDownTimer countDownTimer;
    private long totalSeconds = 3600, intervalSeconds = 1;
    TextView audioTv;
    private ImageView audio_CancelIV, audioAddIv;

    LottieAnimationView audio_StartLottieAnimationView, audio_StopLottieAnimationView;

    String taskPosition;
    private MainActivity mainActivity;


    public static AttachmentsBottomSheet getAttachInstance() {
        return new AttachmentsBottomSheet();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setStyle( BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme );

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.attachmentsbottomsheet, container, false );
        mediaRecorder = new MediaRecorder();
        mainActivity = (MainActivity)getActivity();

        taskPosition = getArguments().getString( "Position" );
        dataBaseHelper = new DataBaseHelper( getContext() );

        fromGalleryLL = view.findViewById( R.id.fromGalleryLL );
        takeAPictureLL = view.findViewById( R.id.takeAPictureLL );
        recordVideoLL = view.findViewById( R.id.recordVideoLL );
        recordAudioLL = view.findViewById( R.id.recordAudioLL );


        fromGalleryLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {galleryWork();
//                dismiss();
            }
        } );
        takeAPictureLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { openImageCamera();
//                dismiss();
            }
        } );
        recordVideoLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoCamera();
//                dismiss();

            }
        } );
        recordAudioLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mainActivity.checkPermission();
                if (mainActivity.checkPermission()) {
                    audioFun();
                } else { } }} );

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
        @SuppressLint({"NewApi", "LocalSuppress"}) String timeStamp =
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
        @SuppressLint({"NewApi", "LocalSuppress"}) String timeStamp =
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
        @SuppressLint({"NewApi", "LocalSuppress"}) String imageName = "Image" + new SimpleDateFormat( "ddhmmss" ).format( calendar.getTime() ) + ".png";
        File dir = new File( Environment.getExternalStorageDirectory().getPath() + "/.Reminder/" );
        String imagePath = Environment.getExternalStorageDirectory() + "/.Reminder/" + imageName;


        @SuppressLint({"NewApi", "LocalSuppress"}) String videoName = "Video" + new SimpleDateFormat( "ddhmmss" ).format( calendar.getTime() ) + ".mp4";
        String videoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.Reminder/" + videoName;


        try {

            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

                Uri selectedImageUri = data.getData();
                String filePath = getPath( getActivity().getApplicationContext(), selectedImageUri );
                Log.d( "Picture Path", filePath );

                @SuppressLint({"NewApi", "LocalSuppress"}) boolean insert = dataBaseHelper.insertFile( filePath, createdDateFormat.format( calendar.getTime() ), taskPosition );

                if (insert) {
                    Toast.makeText( getContext(), "insert", Toast.LENGTH_SHORT ).show();
                    if (mRecyclerCallBack!=null)
                    {
                        mRecyclerCallBack.mCallBack();
                    }
                } else {
                    Toast.makeText( getContext(), "not insert", Toast.LENGTH_SHORT ).show();
                }

            } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK) {

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

                @SuppressLint({"NewApi", "LocalSuppress"}) boolean insert = dataBaseHelper.insertFile( imagePath, createdDateFormat.format( calendar.getTime() ), taskPosition );
                if (insert) {
                    if (mRecyclerCallBack!=null)
                    {
                        mRecyclerCallBack.mCallBack();
                    }
                } else {
                }

            } else if (requestCode == RECORD_VIDEO_CR && resultCode == Activity.RESULT_OK) {

                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File SourceFile = new File( videoFilePathCamera );

                File DestinationFile = new File( videoPath );

                if (SourceFile.renameTo( DestinationFile )) {
                    Log.v( "Moving", "Moving file successful." );
                } else {
                    Log.v( "Moving", "Moving file failed." );
                }

                @SuppressLint({"NewApi", "LocalSuppress"}) boolean insert = dataBaseHelper.insertFile( videoPath, createdDateFormat.format( calendar.getTime() ), taskPosition );
                if (insert) {
                    if (mRecyclerCallBack!=null)
                    {
                        mRecyclerCallBack.mCallBack();
                    }
                } else {
                }
            }

        } catch (Exception e) {
            Toast.makeText( getContext(), e.getMessage(), Toast.LENGTH_SHORT ).show();
        }

    }

    private void galleryWork() {

        Intent photoPickerIntent = new Intent( Intent.ACTION_PICK );
        photoPickerIntent.setType( "*/*" );
        photoPickerIntent.putExtra( Intent.CATEGORY_APP_GALLERY, new String[]{"image/*","video/*"} );
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
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        dialog.show();


        audio_btns_cl = view.findViewById( R.id.audio_btns_Cl );
        audio_StartLottieAnimationView = view.findViewById( R.id.audio_StartLottieAnimationView );
        audio_StopLottieAnimationView = view.findViewById( R.id.audio_StopLottieAnimationView );
        audioTv = view.findViewById( R.id.audioTV );
        audio_CancelIV = view.findViewById( R.id.audio_CancelIV );
        audioAddIv = view.findViewById( R.id.audio_AddIV );
        audioAddIv.setEnabled( false );
        audio_StopLottieAnimationView.setVisibility( View.GONE );
        audio_btns_cl.setVisibility( View.GONE );
        audio_StartLottieAnimationView.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                //  on audio stared
                audio_btns_cl.setVisibility( View.VISIBLE );

                startAudio = true;
                audio_StopLottieAnimationView.setVisibility( View.VISIBLE );
                audio_StartLottieAnimationView.setVisibility( View.GONE );
                @SuppressLint({"NewApi", "LocalSuppress"}) String audioName = "Audio" + new SimpleDateFormat( "ddhmmss" ).format( calendar.getTime() ) + ".mp3";
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
                        long recordedTime = totalSeconds - secCounter[0];
                        secCounter[0] = secCounter[0] - 1;
                        audioTv.setText( String.valueOf( recordedTime ) );
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFinish() {
                        mediaRecorder.stop();
                        audioTv.setText( " Time Reached " );
                        audioAddIv.setEnabled( true );
                        audio_btns_cl.setVisibility( View.VISIBLE );
                        audio_StartLottieAnimationView.setVisibility( View.VISIBLE );
                        audio_StopLottieAnimationView.setVisibility( View.GONE );
                    }
                };
                countDownTimer.start();
            }


        } );
// on audio stopped
        audio_StopLottieAnimationView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudio = false;
                audioAddIv.setEnabled( true );
                audioTv.setText( " Press to Record" );
                mediaRecorder.stop();
                countDownTimer.cancel();
                audio_btns_cl.setVisibility( View.VISIBLE );
                audio_StopLottieAnimationView.setVisibility( View.GONE );
                audio_StartLottieAnimationView.setVisibility( View.VISIBLE );

            }
        } );


        audioAddIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint({"NewApi", "LocalSuppress"}) boolean insert = dataBaseHelper.insertFile( audioSavePath, createdDateFormat.format( calendar.getTime() ), taskPosition );
                if (insert) {

                        if (mRecyclerCallBack!=null)
                        {
                        mRecyclerCallBack.mCallBack();
                        }


//                    Toast.makeText( getContext(), "insert", Toast.LENGTH_SHORT ).show();
                    audio_btns_cl.setVisibility( View.GONE );
                    dialog.dismiss();

                } else {
                    Toast.makeText( getContext(), "not insert", Toast.LENGTH_SHORT ).show();
                }
            }
        } );

        audio_CancelIV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );
        dialog.setOnDismissListener( new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (startAudio) {
                    mediaRecorder.stop();
                    countDownTimer.cancel();
                }

            }
        } );

    }

    private void mediaRecorderFun() {
        mediaRecorder.setAudioSource( MediaRecorder.AudioSource.MIC );
        mediaRecorder.setOutputFormat( MediaRecorder.OutputFormat.THREE_GPP );
        mediaRecorder.setAudioEncoder( MediaRecorder.OutputFormat.AMR_NB );
        mediaRecorder.setOutputFile( audioSavePath );

    }


    public void setRecyclerCallBack(RecyclerCallBack mRecyclerCallBack) {
        this.mRecyclerCallBack = mRecyclerCallBack;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (startAudio) {
            mediaRecorder.stop();
            countDownTimer.cancel();

        } else {
        }
    }



}

