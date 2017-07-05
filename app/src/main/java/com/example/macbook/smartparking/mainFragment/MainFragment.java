package com.example.macbook.smartparking.mainFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.macbook.smartparking.HomeScreenAdministrator;
import com.example.macbook.smartparking.OnGraphButtonListener;
import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.data.sensorInfo.MainAdminResponse;
import com.example.macbook.smartparking.data.sensorInfo.Sensor;
import com.example.macbook.smartparking.maps.MapActivityMain;

import java.util.List;


public class MainFragment extends Fragment implements MainViewFragment, OnClickedItem {

    public static final String TAG_MAIN = "mainfragment";
    private FloatingActionButton goToGraphsButton;
    private OnGraphButtonListener listener;
    private MainFragmentPresenter presenter;
    private RelativeLayout progressContainer;
    private LinearLayout emptyStateContainer;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MainFragment() {
        // Required empty public constructor
    }

    public void setListener(OnGraphButtonListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity){
            a=(Activity) context;
            if (a instanceof OnGraphButtonListener) {
                listener = ((OnGraphButtonListener) a);
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
        goToGraphsButton = (FloatingActionButton) rootView.findViewById(R.id.graphButton);
        progressContainer = (RelativeLayout)rootView.findViewById(R.id.progresView);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        emptyStateContainer = (LinearLayout)rootView.findViewById(R.id.emptyState);
        goToGraphsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onClickGraph();
                }else {
                    listener = (OnGraphButtonListener)getActivity();
                    listener.onClickGraph();
                }
            }
        });
        presenter = new MainFragmentPresenter(this);
        presenter.getData(getActivity());
        return rootView;
    }

    @Override
    public void hideLoading() {
        progressContainer.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDataFromServer(List<Sensor> response) {
        hideLoading();
        if(response.size()>0) {
            emptyStateContainer.setVisibility(View.GONE);
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new MainFragmentAdapter(response, this);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            emptyStateContainer.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showDataFromServerCharset(List<Float> chartSets) {

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt("someVarA", someVarA);
//        outState.putString("someVarB", someVarB);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        someVarA = savedInstanceState.getInt("someVarA");
//        someVarB = savedInstanceState.getString("someVarB");
    }

    @Override
    public void onClickPosition(int id) {
        Intent goToMap = new Intent(getActivity(), MapActivityMain.class);
        goToMap.putExtra("id", id);
        getActivity().startActivity(goToMap);
    }
}
