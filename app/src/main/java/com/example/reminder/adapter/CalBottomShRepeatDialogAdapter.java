package com.example.reminder.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.interfaces.EditTextStringListener;

import java.util.List;

public class CalBottomShRepeatDialogAdapter extends RecyclerView.Adapter<CalBottomShRepeatDialogAdapter.CalBottomShRepeatDialogViewHolder> {
    int isVisible = -1;

    Context context;
    List<String> list;
    AlertDialog dialog;

    private EditTextStringListener editTextStringListener;

    public CalBottomShRepeatDialogAdapter(Context context, List<String> list, AlertDialog dialog) {
        this.context = context;
        this.list = list;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public CalBottomShRepeatDialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.bsh_alarm_list_layout,parent,false );
        return new CalBottomShRepeatDialogViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final CalBottomShRepeatDialogViewHolder holder, final int position) {
        final String s =list.get( position );
        holder.setData( s );

        if (isVisible==position) {
            holder.imageView.setVisibility( View.VISIBLE );
        }
        else
        {
            holder.imageView.setVisibility( View.INVISIBLE );
        }

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isVisible==position)
                {
                    holder.imageView.setVisibility( View.INVISIBLE );
                }
                else
                {
                    holder.imageView.setVisibility( View.VISIBLE );
                }
                isVisible=position;
                setString(s);
                dialog.dismiss();
                notifyDataSetChanged();

            }
        } );

    }

    public void getRepeatStringListener(EditTextStringListener editTextStringListener)
    {
        this.editTextStringListener = editTextStringListener;
    }
    void setString(String s)
    {
        editTextStringListener.myString( s );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public  class CalBottomShRepeatDialogViewHolder extends RecyclerView.ViewHolder
    {

        TextView textView ;
        ImageView imageView;
        LinearLayout linearLayout;
        public CalBottomShRepeatDialogViewHolder(@NonNull View itemView) {
            super( itemView );
            textView =itemView.findViewById( R.id.listalarm_tv );
            imageView =itemView.findViewById( R.id.alarm_select_Iv );
            linearLayout =itemView.findViewById( R.id.bsh_alarm_list_LL );
        }
        public void setData(String s)
        {
            textView.setText( s );
        }
    }

}
