package com.example.reminder.adapter;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reminder.Fragments.AttachmentsBottomSheet;
import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.models.AttachmentTaskModel;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

public class AttachmentTaskAdapter extends RecyclerView.Adapter<AttachmentTaskAdapter.MyAttachmentHolder> {

    Context context;
    List<AttachmentTaskModel> list;
    DataBaseHelper dataBaseHelper;
    FragmentManager fragmentManager;
    private MediaPlayer mediaPlayer;
    int play = -1;


    public AttachmentTaskAdapter(Context context, List<AttachmentTaskModel> list, DataBaseHelper dataBaseHelper,
                                 FragmentManager fragmentManager) {
        this.context = context;
        this.list = list;
        this.dataBaseHelper = dataBaseHelper;
        this.fragmentManager = fragmentManager;
        mediaPlayer = new MediaPlayer();

    }

    @NonNull
    @Override
    public MyAttachmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.attachmentsitemslayout, parent, false );
        return new MyAttachmentHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAttachmentHolder holder, final int position) {
        final String title = list.get( position ).getTaskAttachmentTitle();
        final String myFinalString = title.substring( title.lastIndexOf( "/" ) + 1 );

        holder.attaImageView.setImageResource( list.get( position ).getImage() );
        holder.attaCreateTimeTV.setText( list.get( position ).getCreateDate() );
        holder.attaTitleTV.setText( myFinalString );

        if (play==position)
        {
            if (title.contains( "mp3" ))
            {
                holder.attaImageView.setImageResource( R.drawable.stop_audio_foreground );
            }
            else
                {
                    holder.attaImageView.setImageResource( R.drawable.play_audio_foreground );

                }


        }



        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaController vidControl = new MediaController( context );

                String path = list.get( position ).getTaskAttachmentTitle();

                if (path.contains( ".mp4" )) {

                    final View view = LayoutInflater.from( context ).inflate( R.layout.video_view, null );
                    AlertDialog.Builder builder = new AlertDialog.Builder( context );
                    builder.setView( view );
                    builder.setCancelable( true );

                    final AlertDialog dialog = builder.create();
                    VideoView videoView = view.findViewById( R.id.videoView_vv );
                    videoView.setVideoPath( path );
                    vidControl.setAnchorView( videoView );
                    videoView.setMediaController( vidControl );
                    videoView.canPause();
                    videoView.canSeekBackward();
                    videoView.canSeekForward();
                    videoView.start();
                    dialog.show();

                } else if (path.contains( ".mp3" )) {
                    {


                        File file = new File( path );
                        if (mediaPlayer != null)
                        {

                            if (mediaPlayer.isPlaying())
                            {
                                mediaPlayer.pause();


                            }
                            else {

                                play = position;
                                mediaPlayer = MediaPlayer.create( context, Uri.fromFile( file ) );
                                mediaPlayer.start();
                                mediaPlayer.setLooping( true );

                            }

                        }
                        notifyDataSetChanged();

                    }


                } else {

                    final View view = LayoutInflater.from( context ).inflate( R.layout.view_image, null );
                    AlertDialog.Builder builder = new AlertDialog.Builder( context );
                    builder.setView( view );
                    builder.setCancelable( true );

                    final AlertDialog dialog = builder.create();

                    ImageView imageView = view.findViewById( R.id.viewImage_IV );
                    Glide.with( context ).load( path )
                            .thumbnail( 0.5f )
                            .into( imageView );

                    dialog.show();
                }


            }
        } );


        holder.attaRemoveIV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from( context ).inflate( R.layout.attachmentdeletelayout,null );
                AlertDialog.Builder builder = new AlertDialog.Builder( context );
                builder.setCancelable( true );
                builder.setView( view );
                final AlertDialog dialog = builder.create();
                dialog.show();

                Button cancelBtn,deleteBtn;
                cancelBtn = view.findViewById( R.id.attachment_cancelBtn );
                deleteBtn = view.findViewById( R.id.attachment_deleteBtn );
                cancelBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                } );
                deleteBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dataBaseHelper.deleteAttachment( list.get( position ).getId() );
                        list.remove( position );
                        deleteFile(title);
                        notifyDataSetChanged();
                        dialog.dismiss();

                    }
                } );


            }
        } );

    }

    public void stopAudioOnBackPres()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }
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


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyAttachmentHolder extends RecyclerView.ViewHolder {

        TextView attaTitleTV, attaCreateTimeTV;
        ImageView attaImageView, attaRemoveIV;


        public MyAttachmentHolder(@NonNull View itemView) {
            super( itemView );

            attaTitleTV = itemView.findViewById( R.id.attaTitleTV );
            attaCreateTimeTV = itemView.findViewById( R.id.attaCreateTimeTV );
            attaImageView = itemView.findViewById( R.id.attaImageView );
            attaRemoveIV = itemView.findViewById( R.id.attaRemoveIV );


        }
    }


}
