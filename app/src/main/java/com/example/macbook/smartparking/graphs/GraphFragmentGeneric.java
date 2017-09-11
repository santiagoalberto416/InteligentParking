package com.example.macbook.smartparking.graphs;

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
import com.example.macbook.smartparking.worker.DataWorker;
import com.example.macbook.smartparking.main.MainViewFragment;
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
    public static final int GRAPH_BY_DAY = 0;
    public static final int GRAPH_BY_MONTH = 1;
    public static final int GRAPH_BY_BLOCK = 2;
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
        TextView labelLeft = (TextView)view.findViewById(R.id.labelLeft);
        TextView labelBottom = (TextView)view.findViewById(R.id.labelBottom);
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
        String date = year+"-"+month+"-"+day;
        ((HomeScreenAdministrator)getActivity()).setDateFragment(date, mTypeFragment);
        getDataByType(date,mTypeFragment);
        mDateTextView.setText(date);
        setDateOfModal(((HomeScreenAdministrator)getActivity()).getCalendarFragment(mTypeFragment));
    }

    public void setTypeFragment(int type){
        this.mTypeFragment = type;
    }

    public void setLabels(String labelLeft, String labelBottom){
        this.mLeftString = labelLeft;
        this.mBottomString = labelBottom;
    }

    private void setDateOfModal(final Calendar calendar){
        mPickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        GraphFragmentGeneric.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setOnDateSetListener(GraphFragmentGeneric.this);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    public void getDataByType(String date, int typeOfFragment){
        switch (typeOfFragment){
            case GRAPH_BY_DAY:
                mWorker.getData(date, this);
                break;
            case GRAPH_BY_MONTH:
                mWorker.getDataByMonth(date, this);
                break;
            case GRAPH_BY_BLOCK:
                mWorker.getDataByBlock(date, this);
                break;
        }
    }

    private void setData(List<Float> datas) {
        if(getView()==null){
            return;
        }
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
        super.onResume();
        String date = ((HomeScreenAdministrator)getActivity()).getDateFragment(mTypeFragment);
        Calendar calendar = ((HomeScreenAdministrator)getActivity()).getCalendarFragment(mTypeFragment);
        if(!date.equals("") && calendar!=null) {
            mWorker.getData(date, this);
            getDataByType(date, mTypeFragment);
            mDateTextView.setText(date);
            setDateOfModal(calendar);
        }else {
            ((HomeScreenAdministrator)getActivity()).setDateFragment(getDateToday(), mTypeFragment);
            getDataByType(getDateToday(), mTypeFragment);
            mDateTextView.setText(getDateToday());
            setDateOfModal(Calendar.getInstance());
        }
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
