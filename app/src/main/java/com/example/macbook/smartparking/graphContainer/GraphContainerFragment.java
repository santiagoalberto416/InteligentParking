package com.example.macbook.smartparking.graphContainer;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.graphFragment.GraphByMonthFragment;
import com.example.macbook.smartparking.graphFragment.GraphFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphContainerFragment extends Fragment {

    GraphsAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    public static final String TAG_GRAPH_CONTAINER = "graphContainer";
    public GraphContainerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_graph_container, container, false);;
        List<String> classes = new ArrayList<>();
        classes.add(GraphFragment.class.getName());
        classes.add(GraphByMonthFragment.class.getName());
        mCustomPagerAdapter = new GraphsAdapter(getChildFragmentManager(), classes, getActivity());

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

        return view;
    }



}
