package com.example.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.models.AttachmentTaskModel;

import java.util.List;

public class AttachmentTaskAdapter extends RecyclerView.Adapter<AttachmentTaskAdapter.MyAttachmentHolder> {

    Context context;
    List<AttachmentTaskModel> list;
    DataBaseHelper dataBaseHelper;

    public AttachmentTaskAdapter(Context context, List<AttachmentTaskModel> list, DataBaseHelper dataBaseHelper) {
        this.context = context;
        this.list = list;
        this.dataBaseHelper = dataBaseHelper;
    }

    @NonNull
    @Override
    public MyAttachmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.attachmentsitemslayout,parent,false );
        return new MyAttachmentHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAttachmentHolder holder, final int position) {
        holder.attaImageView.setImageResource( list.get( position ).getImage() );
        holder.attaTitleTV.setText( list.get( position ).getSubTaskTitle() );



        holder.attaRemoveIV.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.deleteAttachment( list.get( position ).getId());
                list.remove( position );
                notifyDataSetChanged();
            }
        } );



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
