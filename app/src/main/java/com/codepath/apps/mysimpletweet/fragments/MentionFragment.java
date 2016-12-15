package com.codepath.apps.mysimpletweet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweet.R;

/**
 * Created by obvyah on 16/12/4.
 */
public class MentionFragment extends Fragment {

    /**
     * add this static method for creating new MentionFragment to use
     */
    public static Fragment newInstance(int position, String tabName){
        Fragment f = new MentionFragment();
        Bundle extras = new Bundle();
        extras.putInt("POSITION", position);
        extras.putString("TAB_NAME", tabName);
        f.setArguments(extras);
        return f;
    }

    /** the extra data of this fragment */
    private int position = 0;
    private String tabName = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get argument
        Bundle extras = getArguments();
        this.position = extras.getInt("POSITION");
        this.tabName = extras.getString("TAB_NAME");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_timeline, container, false);


        return root;
    }




    /** added for demo lifecycle of the activity and fragment */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

}