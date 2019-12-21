package com.example.reminder.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.reminder.Activity.MainActivity;
import com.example.reminder.R;
import com.example.reminder.adapter.InputAdapter;
import com.example.reminder.adapter.MyAdapter;
import com.example.reminder.models.EditTextStringListener;
import com.example.reminder.models.InputRemiderModel;
import com.example.reminder.models.MyModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class InputListFrag extends Fragment {

    private RecyclerView recyclerView_tomorrow;
    private RecyclerView recyclerView_upcoming;
    private RecyclerView recyclerView_someday;
    private RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;

    private InputAdapter inputAdapter;
    MyAdapter myAdapter;

    TasksFrag tasksFrag;
    private EditText input_ET;
    private Button input_Done_btn,close_btn;

    String input_from_inputRV;
    String date1 = "today",date2 = "tommorow",date3 = "upcoming",date4 = "someday";
    EditTextStringListener editTextStringListener;

    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_input_list, container, false);
        mainActivity = (MainActivity)getActivity();

        recyclerView =view.findViewById(R.id.inputRemiderRV);
        RecyclerView recyclerView_today = view.findViewById( R.id.todaytasksRecyclerView );
        recyclerView_tomorrow =view.findViewById(R.id.TomorrowtasksRecyclerView);
        recyclerView_upcoming =view.findViewById(R.id.upcomingtasksRecyclerView);
        recyclerView_someday =view.findViewById(R.id.somedaytasksRecyclerView);


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        input_ET =view.findViewById( R.id.searchET );
        close_btn=view.findViewById( R.id.search_close_btn );





        input_ET.post(new Runnable() {
            @Override
            public void run() {
                input_ET.requestFocus();
                InputMethodManager imgr = (InputMethodManager) Objects.requireNonNull( getActivity() ).getSystemService(Context.INPUT_METHOD_SERVICE);
                imgr.showSoftInput(input_ET, InputMethodManager.SHOW_FORCED);
                input_ET.setImeOptions( EditorInfo.IME_ACTION_DONE );
                input_ET.setSingleLine();

            }
        });
        input_ET.setOnEditorActionListener( new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENDCALL) || (actionId == EditorInfo.IME_ACTION_DONE))
                    goToTaskFrag();
                return false;
            }

        });


        List<InputRemiderModel>inputRemiderModelList= new ArrayList<>();


        inputRemiderModelList.add(new InputRemiderModel("Call",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Check",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Get",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Email",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Buy",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Meet/Schedule",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Clean",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Take",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Send",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Pay",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Make",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Pick",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Do",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Read",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Print",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Finish",R.drawable.ic_launcher_foreground));
        inputRemiderModelList.add(new InputRemiderModel("Study",R.drawable.ic_launcher_foreground));

        inputAdapter = new InputAdapter(getContext(),inputRemiderModelList);
        inputAdapter.addlistener( new EditTextStringListener() {
            @Override
            public void mystring(String ss) {
                input_ET.setText( ss+" "  );
                input_ET.setSelection( input_ET.getText().length() );
            }
        } );

        //on done button data should go to task fragment.

        //search
        input_ET.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                inputAdapter.getFilter().filter( s );}});


        //go to task fragment
        close_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTaskFrag();
            }
        } );


        recyclerView.setAdapter(inputAdapter);
        inputAdapter.notifyDataSetChanged();





        return view;
    }

    public void addListenerIF(EditTextStringListener editTextStringListener)
    {
        input_from_inputRV = editTextStringListener.toString();
    }
    public void sString(String s) {
        if (editTextStringListener != null)
        {
            editTextStringListener.mystring( s );
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        closeKeyboard();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()== MotionEvent.ACTION_UP &&keyCode==KeyEvent.KEYCODE_BACK) {
                    goToTaskFrag();

                    return true;
                }
                return false;
            }
        });


    }

    @Override
    public void onDetach() {
        super.onDetach();
        closeKeyboard();
    }


    public void goToTaskFrag()
    {
        MainActivity mainActivity = (MainActivity) getActivity();
        TasksFrag tasksFrag = new TasksFrag();
        mainActivity.loadmyfrag(tasksFrag);
    }


    private void closeKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

}
