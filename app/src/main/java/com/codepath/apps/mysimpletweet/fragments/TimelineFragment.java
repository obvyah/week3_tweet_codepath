package com.codepath.apps.mysimpletweet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
public class TimelineFragment extends Fragment {

    /**
     * add this static method for creating new MentionFragment to use
     */
    public static Fragment newInstance(int position, String tabName){
        Fragment f = new TimelineFragment();
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
    private HomeTimelineArrayAdapter adapter;

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
        adapter = new HomeTimelineArrayAdapter(getContext(),R.layout.item_tweet, tweetArrayList);
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
                client.getHomeTimeline(Constant.REQUEST_TWEETS_COUNT, getMaxId(totalItemsCount), new JsonHttpResponseHandler() {
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

        //Todo Workaround
        if(Tweet.countItems() == 0) {
            populateTimeline(Constant.REQUEST_TWEETS_COUNT);
        }else{
            adapter.addAll(Tweet.recentItems());
        }

        return root;
    }

    private void populateTimeline(int count) {
        populateTimeline(count, null);
    }


    private void populateTimeline(int count, Long max_id){
        client.getHomeTimeline(count, max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Tweet.fromJson(response);
                adapter.addAll(Tweet.recentItems());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public void updateList(String id){
        adapter.insert(Tweet.byId(id),0);
    }

    private class HomeTimelineArrayAdapter extends ArrayAdapter<Tweet> {
        public HomeTimelineArrayAdapter(Context context, int textViewResourceId, List<Tweet> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Tweet tweet = getItem(position);
            ViewHolder holder;

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            holder = (ViewHolder) convertView.getTag();
            holder.txCreatedAt.setText(Utility.toFriendlyTimestamp(tweet.getCreated_at()));
            try {
                holder.txContent.setText(URLDecoder.decode(tweet.getText(), "utf8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e){}
            holder.txScreenName.setText(String.format("@%s", tweet.getUser().getScreen_name()));
            holder.txName.setText(tweet.getUser().getName());
            Picasso.with(getContext()).load(tweet.getUser().getProfile_image_url_https()).noFade().fit().into(holder.ivProfileImage);
            return convertView;
        }

        private class ViewHolder {
            ImageView ivProfileImage;
            TextView txName;
            TextView txScreenName;
            TextView txContent;
            TextView txCreatedAt;

            public ViewHolder(View v) {
                ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImg);
                txName = (TextView) v.findViewById(R.id.txName);
                txScreenName = (TextView) v.findViewById(R.id.txScreenName);
                txCreatedAt = (TextView) v.findViewById(R.id.txCreatedAt);
                txContent = (TextView) v.findViewById(R.id.txContent);
            }
        }
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