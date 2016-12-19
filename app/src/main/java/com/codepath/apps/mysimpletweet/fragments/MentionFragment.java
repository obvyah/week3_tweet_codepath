package com.codepath.apps.mysimpletweet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweet.Constant;
import com.codepath.apps.mysimpletweet.EndlessScrollListener;
import com.codepath.apps.mysimpletweet.R;
import com.codepath.apps.mysimpletweet.TweetApplication;
import com.codepath.apps.mysimpletweet.Utility;
import com.codepath.apps.mysimpletweet.adapters.TweetListAdapter;
import com.codepath.apps.mysimpletweet.models.Tweet;
import com.codepath.apps.mysimpletweet.service.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

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


    private static final String TAG = "ActivityHomeTimeline";
    private RestClient client;
    private ListView lvHomeTimeline;
    private ArrayList<Tweet> tweetArrayList;
    private TweetListAdapter adapter;

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

        client  = TweetApplication.getRestClient();
        lvHomeTimeline = (ListView) root.findViewById(R.id.lvTimeline);
        tweetArrayList = new ArrayList<Tweet>();
        adapter = new TweetListAdapter(getContext(),R.layout.item_tweet, tweetArrayList);
        lvHomeTimeline.setAdapter(adapter);
        lvHomeTimeline.setOnScrollListener(new EndlessScrollListener() {

            private long getMaxId(int totalItemsCount) {
                try {
                    return Long.parseLong(adapter.getItem(totalItemsCount - 1).getId()) - 1;
                } catch (NullPointerException e) {
                    return 0;
                }
            }

            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                client.getMentionTimeline(Constant.REQUEST_TWEETS_COUNT, getMaxId(totalItemsCount), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        adapter.addAll(Tweet.fromJson(response));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    }
                });
                return true;
            }
        });

        //TODO(Workaround)
        populateTimeline(Constant.REQUEST_TWEETS_COUNT);

        return root;
    }

    private void populateTimeline(int count) {
        populateTimeline(count, null);
    }


    private void populateTimeline(int count, Long max_id){
        client.getMentionTimeline(count, max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                adapter.addAll(Tweet.fromJson(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public void updateList(String id){
        adapter.insert(Tweet.byId(id),0);
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