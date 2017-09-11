package com.example.macbook.smartparking.container;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.macbook.smartparking.graphs.GraphFragmentGeneric;

import java.util.List;

/**
 * Created by macbook on 26/06/17.
 */
class GraphsAdapter extends FragmentPagerAdapter {

    Context mContext;
    private String[] mTitles;
    private List<GraphFragmentGeneric> mFragmentsA;

    public GraphsAdapter(FragmentManager fm, List<GraphFragmentGeneric> fragments, Context context, String [] titles) {
        super(fm);
        mFragmentsA = fragments;
        mContext = context;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentsA.get(position);
    }


    @Override
    public int getCount() {
        // return CONTENT.length;
        return mFragmentsA.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
