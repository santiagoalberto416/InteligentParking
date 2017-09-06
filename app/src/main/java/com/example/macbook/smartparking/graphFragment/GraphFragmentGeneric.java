package com.example.macbook.smartparking.graphFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import java.text.SimpleDateFormat;
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

    private LineChartView mChart;
    private FloatingActionButton mPickDateButton;
    private DataWorker mWorker;
    private RelativeLayout mProgress;
    private int mTypeFragment;
    private String mLeftString = "";
    private String mBottomString = "";
    private TextView mDateTextView;
    public static final int GRAPH_BY_DAY = 1;
    public static final int GRAPH_BY_BLOCK = 2;
    public static final int GRAPH_BY_MONTH = 3;
    public static final String LEFT_STRING_KEY = "left";
    public static final String BOTTOM_STRING_KEY = "bottom";
    public static final String TYPE_KEY = "bottom";


    public static GraphFragmentGeneric newInstance(String leftString, String rightString, int typeFragment) {
        GraphFragmentGeneric myFragment = new GraphFragmentGeneric();
        Bundle args = new Bundle();
        args.putString(LEFT_STRING_KEY, leftString);
        args.putString(BOTTOM_STRING_KEY, rightString);
        args.putInt(TYPE_KEY, typeFragment);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            mLeftString = getArguments().getString(LEFT_STRING_KEY);
            mBottomString = getArguments().getString(BOTTOM_STRING_KEY);
            mTypeFragment = getArguments().getInt(TYPE_KEY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        mPickDateButton = (FloatingActionButton)view.findViewById(R.id.pickDateButton);
        mChart = (LineChartView) view.findViewById(R.id.chart);
        mProgress = (RelativeLayout)view.findViewById(R.id.progresView);
        mDateTextView = (TextView)view.findViewById(R.id.date);
        mWorker = new DataWorker();
        mPickDateButton.setOnClickListener(new View.OnClickListener() {
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

        TextView labelLeft = (TextView)view.findViewById(R.id.labelLeft);
        TextView labelBottom = (TextView)view.findViewById(R.id.labelBottom);
//        I must take these values
//        labelLeft.setText("Carros");
//        labelBottom.setText("Horas (0 a 24 hrs)");
        labelLeft.setText(mLeftString);
        labelBottom.setText(mBottomString);

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
        mWorker.getData(year+"-"+month+"-"+day, this);
        mDateTextView.setText(year+"-"+month+"-"+day);
    }

    public void setTypeFragment(int type){
        this.mTypeFragment = type;
    }

    public void setLabels(String labelLeft, String labelBottom){
        this.mLeftString = labelLeft;
        this.mBottomString = labelBottom;
    }

    public void getDataByType(String date, int typeOfFragment){
        switch (typeOfFragment){
            case GRAPH_BY_DAY:
                mWorker.getData(date, this);
                break;
            case GRAPH_BY_MONTH:
                break;
            case GRAPH_BY_BLOCK:
                break;
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
            mWorker.getData(date, this);
            getDataByType(date, mTypeFragment);
            mDateTextView.setText(date);
        }else {
            ((HomeScreenAdministrator)getActivity()).setDateFragment(getDateToday(), 0);
            getDataByType(getDateToday(), mTypeFragment);
            mDateTextView.setText( GraphByBlockFragment.getDateToday());
        }
        super.onResume();
    }

    private String getDateToday(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(cal.getTime());
    }

    @Override
    public void hideLoading() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDataFromServer(List<Sensor> response) {

    }

    @Override
    public void showDataFromServerCharset(List<Float> chartSets) {
        setData(chartSets);
    }
}
