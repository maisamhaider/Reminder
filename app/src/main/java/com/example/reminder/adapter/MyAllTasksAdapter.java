package com.example.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.classes.HideAndShowViewClass;
import com.example.reminder.database.DataBaseHelper;
import com.example.reminder.interfaces.MyItemClickListener;
import com.example.reminder.models.MyAllTasksModel;

import java.util.List;

public class MyAllTasksAdapter extends RecyclerView.Adapter<MyAllTasksAdapter.MyHolder> {

    private final Context context;
    public static List<MyAllTasksModel> modelListForDeletion;
    private List<MyAllTasksModel> myModelList;
    DataBaseHelper dataBaseHelper;

    public MyAllTasksAdapter(Context context, List<MyAllTasksModel> myModelList, DataBaseHelper dataBaseHelper) {
        this.modelListForDeletion = myModelList;
        this.context = context;
        this.myModelList = myModelList;
        this.dataBaseHelper = dataBaseHelper;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.listindividualitenlayout, null );
        // create ViewHolder
        MyHolder myHolder = new MyHolder( itemLayoutView );
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.notes_TextView.setText( myModelList.get( position ).getNotes() );
        holder.date_textView.setText( myModelList.get( position ).getDate() );

        HideAndShowViewClass.hideView( holder.deleteItemIv );
        HideAndShowViewClass.hideView( holder.attachmentsIv );
        HideAndShowViewClass.hideView( holder.subTasksIv);


        holder.setItemClickListener( new MyItemClickListener() {
            @Override
            public void onMyClick(View view, int pos) {
                CheckBox checkBox = (CheckBox)view;

                if (checkBox.isChecked())
                {
                    HideAndShowViewClass.showView( holder.deleteItemIv );
                }
                else
                    HideAndShowViewClass.hideView( holder.deleteItemIv );
            }
        } );
        holder.deleteItemIv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.deleteOneItem(myModelList.get( position ).getId());
                myModelList.remove( position );
                notifyDataSetChanged();

            }
        } );


    }

    @Override
    public int getItemCount() {
        return myModelList.size();

    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView notes_TextView, date_textView;
        ImageView deleteItemIv,attachmentsIv,subTasksIv;
        RelativeLayout mainLayout;
        CheckBox checkBox;
        MyItemClickListener mItemClickListener;

        public MyHolder(@NonNull final View itemView) {
            super( itemView );

            notes_TextView = itemView.findViewById( R.id.noteTextView );
            date_textView  = itemView.findViewById( R.id.dateTextView );
            deleteItemIv   = itemView.findViewById( R.id.deleteSingletaskIv );
            attachmentsIv  = itemView.findViewById( R.id.AttachmentImageView );
            subTasksIv     = itemView.findViewById( R.id.subtasksImageView );
            checkBox       = itemView.findViewById( R.id.checkbox );
            checkBox.setOnClickListener( this);


            mainLayout = (RelativeLayout) itemView.findViewById( R.id.mainindivduallayout );
            mainLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText( itemView.getContext(), "Position:" + Integer.toString( getPosition() ), Toast.LENGTH_SHORT ).show();
                }
            } );

        }

        public void setItemClickListener(MyItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }


        @Override
        public void onClick(View v) {
            this.mItemClickListener.onMyClick(v,getLayoutPosition());

        }
    }


}
