package com.example.macbook.smartparking.graphFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.macbook.smartparking.HomeScreenAdministrator;
import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.data.sensorInfo.Sensor;
import com.example.macbook.smartparking.mainFragment.MainViewFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by macbook on 05/08/17.
 */

public class GraphByBlockFragment extends Fragment implements DatePickerDialog.OnDateSetListener, MainViewFragment {

    LineChartView mChart;
    Button pickDateButton;
    GraphByBlockPresenter presenter;
    RelativeLayout progress;
    TextView dateTextView;

    public GraphByBlockFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        pickDateButton = (Button)view.findViewById(R.id.pickDateButton);
        mChart = (LineChartView) view.findViewById(R.id.chart);
        presenter = new GraphByBlockPresenter(this);
        progress = (RelativeLayout)view.findViewById(R.id.progresView);
        dateTextView = (TextView)view.findViewById(R.id.date);
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        GraphByBlockFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setOnDateSetListener(GraphByBlockFragment.this);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        String date = ((HomeScreenAdministrator)getActivity()).getDateFragment(2);
        if(!date.equals("")) {
            presenter.getData(getActivity(), getString(R.string.graph_by_day), date);
            dateTextView.setText(date);
        }else {
            String dateSelected = GraphByBlockFragment.getDateToday();
            ((HomeScreenAdministrator)getActivity()).setDateFragment(dateSelected, 2);
            presenter.getData(getActivity(), getString(R.string.graph_by_day), dateSelected);
            dateTextView.setText(dateSelected);
        }
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
        Log.d("dateselected", year+"-"+month+"-"+day);
        String fullDate = year+"-"+month+"-"+day;
        ((HomeScreenAdministrator)getActivity()).setDateFragment(fullDate, 2);
        presenter.getData(getActivity(), getString(R.string.graph_by_day), fullDate);
        dateTextView.setText(fullDate);
    }

    public static String getDateToday(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(cal.getTime());
    }

    private void setData(List<Float> datas) {
        List<PointValue> values = new ArrayList<PointValue>();

        for (int i = 0; i < datas.size(); i++) {
            values.add(new PointValue(i, datas.get(i)));
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
        hideLoading();
    }

}
