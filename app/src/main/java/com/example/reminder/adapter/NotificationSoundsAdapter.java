package com.example.reminder.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.interfaces.EditTextStringListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationSoundsAdapter extends RecyclerView.Adapter<NotificationSoundsAdapter.NotificationSoundViewHolder> {

   private Context context;
   private ArrayList<String> list;
   ArrayList<String>hiddenList;
    private int clickedPosition = -1 ;
    private MediaPlayer mediaPlayer;

    private SharedPreferences sharedPreferences;

    private EditTextStringListener editTextStringListener;

    public NotificationSoundsAdapter(Context context, ArrayList<String> list, ArrayList<String> hiddenList) {
        this.context = context;
        this.list = list;
        this.hiddenList = hiddenList;
    }

    @NonNull
    @Override
    public NotificationSoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.notification_sounds_item_layout,parent,false );

        return new NotificationSoundViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationSoundViewHolder holder, final int position) {
        holder.notification_sound_holder_Tv.setText(list.get(position));
        holder.hidden_notification_sound_holder_Tv.setText( hiddenList.get( position ) );

        if (clickedPosition==position)
        {
            holder.notification_sound_select_Rb.setChecked( true );
        }
        else
            {
                holder.notification_sound_select_Rb.setChecked( false );

            }

        holder.notificationItemLL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer = MediaPlayer.create( context, Uri.parse(  hiddenList.get( position ) ));
                if (mediaPlayer!=null)
                {
                    if (mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                    else
                        mediaPlayer.start();
                }
                holder.notification_sound_select_Rb.setChecked( true );
                clickedPosition = position;
                setPath(hiddenList.get( position ));
                notifyDataSetChanged();

            }
        } );


    }

    public void getPathListener(EditTextStringListener editTextStringListener)
    {
        this.editTextStringListener = editTextStringListener;
    }

    void setPath(String s)
    {
        editTextStringListener.myString( s );
    }



    void setPash(String s)
    {
        String s1 = s;

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotificationSoundViewHolder extends RecyclerView.ViewHolder {

        RadioButton notification_sound_select_Rb;
        TextView notification_sound_holder_Tv,hidden_notification_sound_holder_Tv;
        LinearLayout notificationItemLL ;

        public NotificationSoundViewHolder(@NonNull View itemView) {
            super( itemView );
            notification_sound_select_Rb = itemView.findViewById( R.id.notification_sound_select_Rb );
            notification_sound_holder_Tv = itemView.findViewById( R.id.notification_sound_holder_Tv );
            hidden_notification_sound_holder_Tv = itemView.findViewById( R.id.hidden_notification_sound_holder_Tv );
            notificationItemLL = itemView.findViewById( R.id.notificationItemLL );
        }
    }
}
