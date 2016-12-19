package com.codepath.apps.mysimpletweet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.mysimpletweet.ProfileActivity;
import com.codepath.apps.mysimpletweet.R;
import com.codepath.apps.mysimpletweet.Utility;
import com.codepath.apps.mysimpletweet.models.Tweet;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class TweetListAdapter extends ArrayAdapter<Tweet> {
    public TweetListAdapter(Context context, int textViewResourceId, List<Tweet> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
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
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewAdapterListener!=null){
                    listViewAdapterListener.onClickItemImage(position, tweet);
                }
            }
        });
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewAdapterListener!=null){
                    listViewAdapterListener.onClickItem(position, tweet);
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        RelativeLayout rootView;
        ImageView ivProfileImage;
        TextView txName;
        TextView txScreenName;
        TextView txContent;
        TextView txCreatedAt;

        public ViewHolder(View v) {
            rootView = (RelativeLayout) v.findViewById(R.id.rootView);
            ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImg);
            txName = (TextView) v.findViewById(R.id.txName);
            txScreenName = (TextView) v.findViewById(R.id.txScreenName);
            txCreatedAt = (TextView) v.findViewById(R.id.txCreatedAt);
            txContent = (TextView) v.findViewById(R.id.txContent);
        }
    }

    /* Click Listener */
    private ListViewAdapterListener listViewAdapterListener;

    public interface ListViewAdapterListener {
        void onClickItem(int position, Tweet tweet);
        void onClickItemImage(int position, Tweet tweet);
    }

    public void setListViewAdapterListener(ListViewAdapterListener listViewAdapterListener) {
        this.listViewAdapterListener = listViewAdapterListener;
    }
}