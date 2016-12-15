package com.codepath.apps.mysimpletweet.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * Created by s331058 on 16/12/15.
 */
public class FragmentViewPager extends ViewPager{
    public FragmentViewPager(Context context) {
        super(context);
    }

    public FragmentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Fragment getActiveFragment(FragmentManager fragmentManager, int position) {
        final String name = makeFragmentName(getId(), position);
        final Fragment fragmentByTag = fragmentManager.findFragmentByTag(name);
        return fragmentByTag;
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }
}
