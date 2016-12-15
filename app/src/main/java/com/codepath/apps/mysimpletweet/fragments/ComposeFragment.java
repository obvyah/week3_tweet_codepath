package com.codepath.apps.mysimpletweet.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweet.OnTweetSuccessListener;
import com.codepath.apps.mysimpletweet.R;
import com.codepath.apps.mysimpletweet.service.RestClient;
import com.codepath.apps.mysimpletweet.TweetApplication;
import com.codepath.apps.mysimpletweet.models.Tweet;
import com.codepath.apps.mysimpletweet.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class ComposeFragment extends DialogFragment {

    private RestClient client;
    private ImageView ivProfileAvatar;
    private TextView txName;
    private EditText etContent;
    private OnTweetSuccessListener listener;

    public static ComposeFragment newInstance(){
        ComposeFragment composeFragment = new ComposeFragment();
        return composeFragment;
    }

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TweetApplication.getRestClient();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_compose,null);

        ivProfileAvatar = (ImageView) view.findViewById(R.id.ivProfileAvatar);
        txName = (TextView) view.findViewById(R.id.txName);
        etContent = (EditText) view.findViewById(R.id.etContent);

        client.getAccountSetting(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String screenName = null;
                try {
                    screenName = response.getString("screen_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                client.getUserShowByScreenName(screenName,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        User user = new User(response);
                        txName.setText(user.getName());
                        Picasso.with(getContext()).load(user.getProfile_image_url_https()).noFade().fit().into(ivProfileAvatar);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }

        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(getString(R.string.fragment_compose_title));
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                client.postTweet(etContent.getText().toString(),new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet tweet = new Tweet(response);
                        tweet.save();
                        tweet.getUser().save();
                        if(listener!=null) {
                            listener.onCreateSuccess(tweet.getId());
                            listener = null;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    }
                });
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnTweetSuccessListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
