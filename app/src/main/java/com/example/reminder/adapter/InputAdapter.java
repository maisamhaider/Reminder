package com.example.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.models.EditTextStringListener;
import com.example.reminder.models.InputRemiderModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InputAdapter extends RecyclerView.Adapter<InputAdapter.InputHolder> implements Filterable {


    Context context;
    private List<InputRemiderModel> inputRemiderModelList;
    private List<InputRemiderModel> inputRemiderModelListfull;

    List<EditTextStringListener>stringListenerslist=new ArrayList<>();
    private ArrayList<String> names;

    public InputAdapter(Context context, List<InputRemiderModel> inputRemiderModelList) {
        this.context = context;
        this.inputRemiderModelList = inputRemiderModelList;
        inputRemiderModelListfull = new ArrayList<>( );
        inputRemiderModelListfull.addAll( inputRemiderModelList );


    }



    @NonNull
    @Override
    public InputHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.inputremiderindividual,parent,false);
        return new InputHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InputHolder holder, final int position) {
        final String text =inputRemiderModelList.get(position).getText();
        final  int icon = inputRemiderModelList.get(position).getIcon();
        holder.setdata(text,icon);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                //  String text1=holder.setInpputET(text);
                etvString(text);

            }
        });

   /*   holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
              //  String text1=holder.setInpputET(text);
                holder.input_ET.setText(text);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return inputRemiderModelList.size();
    }



    class InputHolder extends RecyclerView.ViewHolder{
        TextView input_RV_Item_TV;
        ImageView input_RV_Item_IV;
        LinearLayout linearLayout ;
        RecyclerView recyclerView;

        public InputHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView =itemView.findViewById(R.id.inputRemiderRV);
            input_RV_Item_TV =itemView.findViewById(R.id.inputRVItemTV);
            input_RV_Item_IV =itemView.findViewById(R.id.inputRVItemIV);
            linearLayout = itemView.findViewById(R.id.inputindividualLLayout);

        }

        public void setdata(String text, int icon)
        {
            input_RV_Item_TV.setText(text);
            input_RV_Item_IV.setImageResource(icon);
        }

    }

    public void addlistener(EditTextStringListener editTextStringListener){
         stringListenerslist.add(editTextStringListener);
    }

    void etvString(String string){
        for(EditTextStringListener editTextStringListener:stringListenerslist){
            editTextStringListener.mystring(string);
        }
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<InputRemiderModel>filteredList = new ArrayList<>(  );
            if (constraint == null || constraint.length() ==0)
            {
                filteredList.addAll( inputRemiderModelListfull );
            }
            else {
                String string = constraint.toString().toLowerCase();
                for (InputRemiderModel data : inputRemiderModelListfull)
                {
                     if (data.getText().toLowerCase().startsWith( string ))
                     {
                         filteredList.add( data );
                     }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        inputRemiderModelList.clear();
        inputRemiderModelList.addAll( (Collection<? extends InputRemiderModel>) results.values );
        notifyDataSetChanged();
        }
    };

}
