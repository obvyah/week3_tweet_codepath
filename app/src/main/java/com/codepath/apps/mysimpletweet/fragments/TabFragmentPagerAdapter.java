package com.codepath.apps.mysimpletweet.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;


public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Home", "Mentions"};

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
                return TimelineFragment.newInstance(position, tabTitles[position]);
            case 1:
                return MentionFragment.newInstance(position, tabTitles[position]);
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