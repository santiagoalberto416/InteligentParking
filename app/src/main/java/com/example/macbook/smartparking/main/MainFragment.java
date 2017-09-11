package com.example.macbook.smartparking.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.macbook.smartparking.OnGraphButtonListener;
import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.data.sensorInfo.Sensor;
import com.example.macbook.smartparking.maps.MapActivityMain;
import com.example.macbook.smartparking.worker.DataWorker;

import java.util.List;


public class MainFragment extends Fragment implements MainViewFragment, OnClickedItem {

    private FloatingActionButton mGoToGraphsButton;
    private OnGraphButtonListener mListener;
    private DataWorker mWorker;
    private RelativeLayout mProgressContainer;
    private LinearLayout mEmptyStateContainer;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MainFragment() {
    }

    public void setmListener(OnGraphButtonListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            if (a instanceof OnGraphButtonListener) {
                mListener = ((OnGraphButtonListener) a);
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGoToGraphsButton = (FloatingActionButton) rootView.findViewById(R.id.graphButton);
        mProgressContainer = (RelativeLayout) rootView.findViewById(R.id.progresView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mEmptyStateContainer = (LinearLayout) rootView.findViewById(R.id.emptyState);
        mGoToGraphsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickGraph();
                } else {
                    mListener = (OnGraphButtonListener) getActivity();
                    mListener.onClickGraph();
                }
            }
        });
        mWorker = new DataWorker();
        mWorker.getDataDelays(this);
        return rootView;
    }

    @Override
    public void hideLoading() {
        mProgressContainer.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        mProgressContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDataFromServer(List<Sensor> response) {
        hideLoading();
        if (response.size() > 0) {
            mEmptyStateContainer.setVisibility(View.GONE);
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new MainFragmentAdapter(response, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mEmptyStateContainer.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showDataFromServerCharset(List<Float> chartSets) {
    }


    @Override
    public void onClickPosition(int id) {
        Intent goToMap = new Intent(getActivity(), MapActivityMain.class);
        goToMap.putExtra("id", id);
        getActivity().startActivity(goToMap);
    }
}
