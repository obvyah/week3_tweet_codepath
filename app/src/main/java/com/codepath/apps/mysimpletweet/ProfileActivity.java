package com.codepath.apps.mysimpletweet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweet.fragments.FragmentViewPager;
import com.codepath.apps.mysimpletweet.fragments.UserTweetFragment;
import com.codepath.apps.mysimpletweet.models.User;
import com.codepath.apps.mysimpletweet.service.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity{

    private static final String TAG = "ActivityProfile";
    private RestClient client;
    ImageView ivProfileImg;
    TextView txName;
    TextView txScreenName;
    TextView txCreatedAt;
    TextView txContent, txFollower, txFollowing;

    FragmentViewPager viewPager;
    TabFragmentPagerAdapter adapter;
    PagerSlidingTabStrip tabsStrip;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //init extra value
        try {
            Bundle extra = getIntent().getExtras();
            user = (User) extra.getSerializable("EXTRA_USER");
        }catch (Exception e){}

        ivProfileImg = (ImageView) findViewById(R.id.ivProfileImg);
        txName = (TextView) findViewById(R.id.txName);
        txScreenName = (TextView) findViewById(R.id.txScreenName);
        txCreatedAt = (TextView) findViewById(R.id.txCreatedAt);
        txContent = (TextView) findViewById(R.id.txContent);
        txFollower = (TextView) findViewById(R.id.txFollower);
        txFollowing = (TextView) findViewById(R.id.txFollowing);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (FragmentViewPager) findViewById(R.id.viewpager);
        // Give the PagerSlidingTabStrip the ViewPager
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setShouldExpand(true);

        client  = TweetApplication.getRestClient();

        // Fetch data
        if (user==null) {
            fetchProfile();
        }else{
            setToolbarTitle(user.getName());
            reloadUserView(user);
            //get the user tweets and display
            initViewPagerAdapter();
        }

    }

    private void setToolbarTitle(String toolbarTitle){
        getSupportActionBar().setTitle(toolbarTitle);
    }

    private void initViewPagerAdapter(){
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }

    private void fetchProfile(){
        client.getAccountVerifyCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = new User(response);
                setToolbarTitle(user.getName());
                reloadUserView(user);
                //get the user tweets and display
                initViewPagerAdapter();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    private void reloadUserView(User user){
        txName.setText(user.getName());
        txScreenName.setText(user.getScreen_name());
        Picasso.with(getApplicationContext()).load(user.getProfile_image_url_https()).noFade().fit().into(ivProfileImg);
        txContent.setText(user.getDescription());
        txFollower.setText(user.getFollowers_count() + " Followers");
        txFollowing.setText("0 Followings");
    }


    public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = new String[] { "Tweets"};

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabTitles.length; }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return UserTweetFragment.newInstance(position, tabTitles[position], user.getId());
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

    }

}
