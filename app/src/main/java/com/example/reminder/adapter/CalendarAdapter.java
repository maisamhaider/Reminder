package com.example.reminder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.classes.ItemType;
import com.example.reminder.models.CalendarModel;
import com.kodmap.library.kmrecyclerviewstickyheader.KmStickyListener;

import java.util.List;

public class CalendarAdapter extends ListAdapter<CalendarModel, RecyclerView.ViewHolder> implements KmStickyListener {


    public CalendarAdapter() {
        super( CalendarModelDiffUtilCallback );
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;
        if (viewType == ItemType.EVENT_DATE) {
            itemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.list_header, parent, false );
            return new HeaderViewHolder( itemView );
        } else{
            itemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.list_item, parent, false );
            return new ChildViewHolder( itemView );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType( position ) == ItemType.EVENT_DATE) {
            ((HeaderViewHolder) holder).bind( getItem( position ) );
        } else
            if (getItemViewType( position )==ItemType.EVENT){
            ((ChildViewHolder) holder).bind( getItem( position ) );
        }
    }

    @Override
    public Integer getHeaderPositionForItem(Integer itemPosition) {
        Integer headerPosition = 0;
        for (Integer i = itemPosition; i > 0; i--) {
            if (isHeader( i )) {
                headerPosition = i;
                return headerPosition;
            }
        }
        return headerPosition;
    }

    @Override
    public Integer getHeaderLayout(Integer headerPosition) {
        return R.layout.list_header;
    }

    @Override
    public void bindHeaderData(View header, Integer headerPosition) {

        TextView myHeader = header.findViewById( R.id.headerTv );
        myHeader.setText( getItem( headerPosition ).haeder_or_child );
    }

    @Override
    public Boolean isHeader(Integer itemPosition) {
        return getItem( itemPosition ).type.equals( ItemType.EVENT_DATE );
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView header_tv;

        public HeaderViewHolder(@NonNull View itemView) {
            super( itemView );
            header_tv = itemView.findViewById( R.id.headerTv );
        }

        public void bind(CalendarModel calendarModel) {
            header_tv.setText( calendarModel.getHaeder_or_child() );
        }
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView child_tv;

        public ChildViewHolder(@NonNull View itemView) {
            super( itemView );
            child_tv = itemView.findViewById( R.id.child_tv );

        }

        public void bind(CalendarModel calendarModel) {
            child_tv.setText( calendarModel.haeder_or_child );
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem( position ).type;
    }

    public static final DiffUtil.ItemCallback<CalendarModel> CalendarModelDiffUtilCallback =
            new DiffUtil.ItemCallback<CalendarModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull CalendarModel model, @NonNull CalendarModel t1) {
                    return model.haeder_or_child.equals( t1.haeder_or_child );
                }

                @Override
                public boolean areContentsTheSame(@NonNull CalendarModel model, @NonNull CalendarModel t1) {
                    return model.haeder_or_child.equals( t1 );
                }
            };
}