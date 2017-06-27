package com.example.macbook.smartparking.graphContainer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.macbook.smartparking.graphFragment.GraphFragment;

/**
 * Created by macbook on 26/06/17.
 */
class GraphsAdapter extends FragmentPagerAdapter {

    Context mContext;

    public GraphsAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        // Create fragment object
        Fragment fragment = new GraphFragment();

        // Attach some data to the fragment
        // that we'll use to populate our fragment layouts
        Bundle args = new Bundle();
        args.putInt("page_position", position + 1);

        // Set the arguments on the fragment
        // that will be fetched in the
        // DemoFragment@onCreateView
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + (position + 1);
    }
}
