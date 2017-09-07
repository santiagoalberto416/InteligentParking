package com.example.macbook.smartparking.container;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.graphs.GraphFragmentGeneric;

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
        View view = inflater.inflate(R.layout.fragment_graph_container, container, false);;
        List<GraphFragmentGeneric> fragments = new ArrayList<>();
        GraphFragmentGeneric blocksByDay = GraphFragmentGeneric.newInstance("Carros", "Horas (0 a 24 hrs)", GraphFragmentGeneric.GRAPH_BY_DAY);
        blocksByDay.setTypeFragment(GraphFragmentGeneric.GRAPH_BY_DAY);
        blocksByDay.setLabels("Carros", "Horas (0 a 24 hrs)");
        GraphFragmentGeneric blocksByMonth = GraphFragmentGeneric.newInstance("Carros", "Horas (0 a 24 hrs)", GraphFragmentGeneric.GRAPH_BY_MONTH);
        blocksByMonth.setTypeFragment(GraphFragmentGeneric.GRAPH_BY_MONTH);
        blocksByMonth.setLabels("Carros", "Horas (0 a 24 hrs)");
        GraphFragmentGeneric blocksByBlock = GraphFragmentGeneric.newInstance("Carros", "Horas (0 a 24 hrs)", GraphFragmentGeneric.GRAPH_BY_BLOCK);
        blocksByBlock.setTypeFragment(GraphFragmentGeneric.GRAPH_BY_BLOCK);
        blocksByBlock.setLabels("Carros por mes", "Bloques");
        fragments.add(blocksByDay);
        fragments.add(blocksByMonth);
        fragments.add(blocksByBlock);
        String[] titles = {"Actividad por dia", "Actividad por mes", "Actividad por bloques"};
        mCustomPagerAdapter = new GraphsAdapter(getChildFragmentManager(), fragments, getActivity(), titles);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

        return view;
    }

//    labelLeft.setText("Carros por mes");
//        labelBottom.setText("Bloques");


}
