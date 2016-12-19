package com.codepath.apps.mysimpletweet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweet.fragments.ComposeFragment;
import com.codepath.apps.mysimpletweet.fragments.FragmentViewPager;
import com.codepath.apps.mysimpletweet.fragments.TabFragmentPagerAdapter;
import com.codepath.apps.mysimpletweet.fragments.TimelineFragment;
import com.codepath.apps.mysimpletweet.models.Tweet;

public class HomeActivity extends AppCompatActivity implements OnTweetSuccessListener{

    private static final String TAG = "ActivityHome";
    private final int TAB_HOME_INDEX = 0;

    FragmentViewPager viewPager;
    TabFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (FragmentViewPager) findViewById(R.id.viewpager);
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setShouldExpand(true);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public void actionProfile(MenuItem items){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void actionCompose(MenuItem item) {
        showComposeDialog();
    }

    private void showComposeDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance();
        composeFragment.show(fragmentManager, "frag_compose");
    }

    @Override
    public void onCreateSuccess(String id) {
        Fragment fragment = viewPager.getActiveFragment(getSupportFragmentManager(), TAB_HOME_INDEX);
        if (fragment!=null){
            if (fragment instanceof TimelineFragment) {
                ((TimelineFragment) fragment).updateList(id);
            }
        }else{
            Toast.makeText(HomeActivity.this, "no fragment", Toast.LENGTH_LONG).show();
        }
    }


}
