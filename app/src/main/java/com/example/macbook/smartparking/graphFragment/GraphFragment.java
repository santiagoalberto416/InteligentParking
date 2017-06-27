package com.example.macbook.smartparking.graphFragment;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.macbook.smartparking.R;
import com.example.macbook.smartparking.data.sensorInfo.Sensor;
import com.example.macbook.smartparking.mainFragment.MainViewFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment implements DatePickerDialog.OnDateSetListener, MainViewFragment {

    LineChartView mChart;
    Button pickDateButton;
    GraphPresenter presenter;
    public GraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        pickDateButton = (Button)view.findViewById(R.id.pickDateButton);
        mChart = (LineChartView) view.findViewById(R.id.chart);
        presenter = new GraphPresenter(this);
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        GraphFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        presenter.getData(getActivity());
        return view;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    private void setData(List<Float> datas) {
        List<PointValue> values = new ArrayList<PointValue>();

        for (int i = 0; i < datas.size(); i++) {

            float val = datas.get(i);;
            values.add(new PointValue(i, val));
        }

        Line line = new Line(values)
                .setColor(Color.BLUE)
                .setCubic(false)
                .setHasPoints(true).setHasLabels(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

//        List<AxisValue> axisValuesForX = new ArrayList<>();
//        List<AxisValue> axisValuesForY = new ArrayList<>();
//        AxisValue tempAxisValue;
//        for (float i = 0; i <= 360.0f; i += 30.0f){
//            tempAxisValue = new AxisValue(i);
//            tempAxisValue.setLabel(i+"\u00b0");
//            axisValuesForX.add(tempAxisValue);
//        }
//
//        for (float i = 0.0f; i <= 1.00f; i += 0.25f){
//            tempAxisValue = new AxisValue(i);
//            tempAxisValue.setLabel(""+i);
//            axisValuesForY.add(tempAxisValue);
//        }
//
//        Axis xAxis = new Axis(axisValuesForX);
//        Axis yAxis = new Axis(axisValuesForY);
//        data.setAxisXBottom(xAxis);
//        data.setAxisYLeft(yAxis);


        mChart.setLineChartData(data);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showDataFromServer(List<Sensor> response) {

    }

    @Override
    public void showDataFromServerCharset(List<Float> chartSets) {
            setData(chartSets);
    }
}
