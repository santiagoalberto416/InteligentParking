package com.example.macbook.smartparking.graphFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.macbook.smartparking.HomeScreenAdministrator;
import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.data.sensorInfo.Sensor;
import com.example.macbook.smartparking.graph.DataWorker;
import com.example.macbook.smartparking.mainFragment.MainViewFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by skirk on 9/5/17.
 */

public class GraphFragmentGeneric  extends Fragment implements DatePickerDialog.OnDateSetListener, MainViewFragment {

    LineChartView mChart;
    FloatingActionButton pickDateButton;
    //GraphPresenter worker;
    DataWorker worker;
    RelativeLayout progress;
    TextView dateTextView;


    public GraphFragmentGeneric() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        pickDateButton = (FloatingActionButton)view.findViewById(R.id.pickDateButton);
        mChart = (LineChartView) view.findViewById(R.id.chart);
        progress = (RelativeLayout)view.findViewById(R.id.progresView);
        dateTextView = (TextView)view.findViewById(R.id.date);
        worker = new DataWorker();
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        GraphFragmentGeneric.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setOnDateSetListener(GraphFragmentGeneric.this);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

            }
        });

        /**
         * aqui se definen los labels
         */
        TextView labelLeft = (TextView)view.findViewById(R.id.labelLeft);
        TextView labelBottom = (TextView)view.findViewById(R.id.labelBottom);
        labelLeft.setText("Carros");
        labelBottom.setText("Horas (0 a 24 hrs)");

        showLoading();
        return view;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String month = "";
        if(monthOfYear < 10){
            month = "0"+(monthOfYear+1);
        }else {
            month = (monthOfYear+1)+"";
        }
        String day = "";
        if(dayOfMonth < 10){
            day = "0"+dayOfMonth;
        }else {
            day = dayOfMonth+"";
        }
        ((HomeScreenAdministrator)getActivity()).setDateFragment(year+"-"+month+"-"+day, 0);
        worker.getData(year+"-"+month+"-"+day, this);
        dateTextView.setText(year+"-"+month+"-"+day);
    }

    public void setLabelForType(String type){
        switch (type){

        }
    }

    private void setData(List<Float> datas) {
        List<PointValue> values = new ArrayList<PointValue>();
        Boolean thereValues = false;
        for (int i = 0; i < datas.size(); i++) {
            float val = datas.get(i);
            if(val > 0) {
                thereValues = true;
            }
            values.add(new PointValue(i, val));
        }

        if(!thereValues){
            getView().findViewById(R.id.noData).setVisibility(View.VISIBLE);
        }else{
            getView().findViewById(R.id.noData).setVisibility(View.GONE);
        }

        Line line = new Line(values)
                .setColor(Color.BLUE)
                .setCubic(false)
                .setHasPoints(true).setHasLabels(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);


        mChart.setLineChartData(data);
    }

    @Override
    public void onResume() {
        String date = ((HomeScreenAdministrator)getActivity()).getDateFragment(0);
        if(!date.equals("")) {
            worker.getData(date, this);
            dateTextView.setText(date);
        }else {
            ((HomeScreenAdministrator)getActivity()).setDateFragment(GraphByBlockFragment.getDateToday(), 0);
            worker.getData(GraphByBlockFragment.getDateToday(), this);
            dateTextView.setText( GraphByBlockFragment.getDateToday());
        }
        super.onResume();
    }

    @Override
    public void hideLoading() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDataFromServer(List<Sensor> response) {

    }

    @Override
    public void showDataFromServerCharset(List<Float> chartSets) {
        setData(chartSets);
    }
}
