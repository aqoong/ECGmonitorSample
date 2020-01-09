package com.aqoong.lib.ecgmonitor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * Created by andy on 2020-01-07.
 * <p>
 * email : cooldnjsdn@gmail.com
 **/
public class MonitorView extends RelativeLayout {
    private final String TAG = getClass().getSimpleName();

    private int mBackgroundID;
    private int mBackgroundColor    = -1;

    private int mLineColor      = -1;
    private int mTraceLineColor = -1;
    private float mTraceSpace;
    private float mLineWidth;
    private int mChartMaxSize;

    private LineChart vChart;

    private ArrayList<Entry> chartEntries;
    private ArrayList<Entry> chartEntriesTrace;



    public MonitorView(Context context) {
        this(context, null);
    }

    public MonitorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        try{
            TypedArray ta   = context.obtainStyledAttributes(attrs, R.styleable.MonitorView);

            mBackgroundID   = ta.getResourceId(R.styleable.MonitorView_monitorBackground, 0);
            mBackgroundColor= Color.parseColor(ta.getString(R.styleable.MonitorView_backgroundColor));
            if(mBackgroundColor == -1)
                mBackgroundColor = ta.getColor(R.styleable.MonitorView_backgroundColor, ContextCompat.getColor(getContext(), android.R.color.white));

            mLineColor      = Color.parseColor(ta.getString(R.styleable.MonitorView_lineColor));
            if(mLineColor == -1)
                mLineColor = ta.getColor(R.styleable.MonitorView_lineColor, ContextCompat.getColor(getContext(), android.R.color.black));

            mTraceLineColor = Color.parseColor(ta.getString(R.styleable.MonitorView_traceLineColor));
            if( mTraceLineColor == -1)
                mTraceLineColor = ta.getColor(R.styleable.MonitorView_traceLineColor, Color.parseColor("#C8C8C8"));
            mTraceSpace      = ta.getFloat(R.styleable.MonitorView_traceSpace, 30);
            mLineWidth      = ta.getFloat(R.styleable.MonitorView_lineWidth, 0.5f);
            mChartMaxSize   = ta.getInt(R.styleable.MonitorView_maxSize, 300);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setup();
        }
    }

    private void setup(){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.monitor_layout, this);

        vChart = findViewById(R.id.chart);
        setupChart();


    }

    private void setupChart(){
        vChart.setAutoScaleMinMaxEnabled(false);

        //set description
        Description description = new Description();
        description.setEnabled(false);
        vChart.setDescription(description);

        chartEntries = new ArrayList<>();
        chartEntriesTrace = new ArrayList<>();

        for(int i = 0 ; i < mChartMaxSize ; i++){
            chartEntries.add(new Entry(i, 0));
        }


        vChart.getLegend().setEnabled(false);

        //x축
        vChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        vChart.getXAxis().setLabelCount(10, true);
        vChart.getXAxis().setDrawLabels(false);

        //y축
        vChart.getAxisLeft().setLabelCount(7, true);
        vChart.getAxisLeft().setAxisMinimum(-3f);
        vChart.getAxisLeft().setAxisMaximum(3f);

        vChart.getAxisRight().setEnabled(false);

        vChart.setPinchZoom(false);
        vChart.setTouchEnabled(false);

        //data
        LineData lineData = new LineData();
        lineData.addDataSet(setupLineDataSet(chartEntries, false));
        lineData.addDataSet(setupLineDataSet(chartEntriesTrace, true));
        vChart.setData(lineData);
    }

    private LineDataSet setupLineDataSet(ArrayList<Entry> entries, boolean isTrace){
        LineDataSet lineDataSet = new LineDataSet(entries, "");

        lineDataSet.setColor(isTrace ? mTraceLineColor : mLineColor);
        lineDataSet.setLineWidth(mLineWidth);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        return lineDataSet;
    }

    private int count = 0;
    public void updateData(float x, float y){
        if(count >= mChartMaxSize){

            count = 0;
            initEntries();
        }

        chartEntries.set(count, new Entry(count, x));
        try{
            for(int i = count+1 ; i < mChartMaxSize ; i++){
                chartEntries.set(i, new Entry(i, 4));
            }
            for(int i = 0 ; i < count + mTraceSpace ; i++){
                chartEntriesTrace.set(i, new Entry(i, 4));
            }

        }catch (IndexOutOfBoundsException e){}

        count++;

        vChart.notifyDataSetChanged();
        vChart.invalidate();
    }

    private void initEntries(){
        chartEntriesTrace.clear();
        chartEntriesTrace.addAll(chartEntries);
        chartEntries.clear();
        for(int i = 0 ; i < mChartMaxSize ; i++){
            chartEntries.add(new Entry(i, 0));
        }
    }
}
