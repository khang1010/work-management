package com.example.workmanagement.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.ArrayList;
import java.util.List;
import com.example.workmanagement.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekChartFragment extends Fragment {

    private BarChart barChart;
    private View view;

    public WeekChartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void GroupBarChart(){
        barChart = view.findViewById(R.id.barchartWeek);
        barChart.setDrawBarShadow(false);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        // empty labels so that the names are spread evenly

        List<String> labels = new ArrayList<>();
        labels.add("");
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("");
        //String[] labels = {"", "Name1", "Name2", "Name3", "Name4", "Name5", ""};

        XAxis xAxis = barChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextColor(getActivity().getApplication().getResources().getColor(R.color.black));
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(getActivity().getApplication().getResources().getColor(R.color.white));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(8, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        float[] valOne = {10, 20, 30, 40, 50};
        float[] valTwo = {60, 50, 40, 30, 20};
        float[] valThree = {50, 60, 20, 10, 30};

        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        ArrayList<BarEntry> barThree = new ArrayList<>();
        for (int i = 0; i < valOne.length; i++) {
            barOne.add(new BarEntry(i, valOne[i]));
            barTwo.add(new BarEntry(i, valTwo[i]));
            barThree.add(new BarEntry(i, valThree[i]));
        }

        BarDataSet set1 = new BarDataSet(barOne, "barOne");
        set1.setColor(getActivity().getApplication().getResources().getColor(R.color.blue));
        BarDataSet set2 = new BarDataSet(barTwo, "barTwo");
        set2.setColor(getActivity().getApplication().getResources().getColor(R.color.red));
        BarDataSet set3 = new BarDataSet(barThree, "barTwo");
        set3.setColor(getActivity().getApplication().getResources().getColor(R.color.yellow));

        set1.setHighlightEnabled(false);
        set2.setHighlightEnabled(false);
        set3.setHighlightEnabled(false);
        set1.setDrawValues(false);
        set2.setDrawValues(false);
        set3.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        BarData data = new BarData(dataSets);
        float groupSpace = 0.4f;
        float barSpace = 0f;
        float barWidth = 0.3f;
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.setBarWidth(barWidth);
        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(labels.size() - 1.1f);
        barChart.setData(data);
        barChart.setScaleEnabled(true);
        barChart.setVisibleXRangeMaximum(6f);
        barChart.groupBars(1f, groupSpace, barSpace);
        barChart.invalidate();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_week_chart, container, false);
        GroupBarChart();
        return view;
    }
}