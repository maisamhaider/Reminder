package com.example.reminder.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

public class AttachmentsBottomSheet extends BottomSheetDialogFragment {

    private static final int AUDIO_REQUEST_CODE = 2020;


    private static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQ = 2;
    private static final int RECORD_VIDEO_CR = 3;
    private static final int RECORD_AUDIO_R = 4;

    DataBaseHelper dataBaseHelper;

    private LinearLayout fromGalleryLL, takeAPictureLL, recordVideoLL, recordAudioLL;
    EditTask editTask = new EditTask();


    private String imageFilePathCamera,videoFilePathCamera;


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
//                Intent toRecordAudio new Intent(  )

            }
        } );

        return view;

    }
    private void openImageCamera() {
        Intent pictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),getActivity().getPackageName() + ".provider",photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        CAMERA_REQ);
            }
        }
    }
    private void openVideoCamera() {
        Intent pictureIntent = new Intent( MediaStore.ACTION_VIDEO_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createVideoFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),getActivity().getPackageName() + ".provider",photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        RECORD_VIDEO_CR);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String videoFileName = "VIDEO_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File video = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );
        videoFilePathCamera = video.getAbsolutePath();
        return video;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
                Uri selectedImageUri = data.getData();
                File myFile = new File( selectedImageUri.getPath() );
                final String file = myFile.getAbsolutePath();

                boolean insert = dataBaseHelper.insertFile( file, taskPosition );

                if (insert) {
                    Toast.makeText( getContext(), "insert", Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText( getContext(), "not insert", Toast.LENGTH_SHORT ).show();
                }

            } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK) {

              boolean insert = dataBaseHelper.insertFile( imageFilePathCamera,taskPosition );
              if (insert)
              {
                  Toast.makeText( getContext(), "inserted", Toast.LENGTH_SHORT ).show();
              }
              else
              {
                  Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();

              }


            } else if (requestCode == RECORD_VIDEO_CR && resultCode == Activity.RESULT_OK) {


                boolean insert = dataBaseHelper.insertFile( videoFilePathCamera,taskPosition );
                if (insert)
                {
                    Toast.makeText( getContext(), "inserted", Toast.LENGTH_SHORT ).show();
                }
                else
                {
                    Toast.makeText( getContext(), "not inserted", Toast.LENGTH_SHORT ).show();

                }
            }

        } catch (Exception e) {
            Toast.makeText( getContext(), e.getMessage(), Toast.LENGTH_SHORT ).show();
        }

    }

    private void galleryWork() {
        Intent toGallery = new Intent();
        toGallery.addCategory( Intent.CATEGORY_OPENABLE );
        toGallery.setType( "image/* video/*" );
        toGallery.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( toGallery, "Select Picture" ), PICK_IMAGE );
    }

    private boolean checkAudioPermission() {
        int audioPermission = ContextCompat.checkSelfPermission( getContext(), Manifest.permission.RECORD_AUDIO );
        int writeStoragePermission = ContextCompat.checkSelfPermission( getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE );

        if (audioPermission == PackageManager.PERMISSION_GRANTED && writeStoragePermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions( getActivity(), new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, AUDIO_REQUEST_CODE );
            return false;
        }
    }

}

