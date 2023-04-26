package com.example.workmanagement.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.workmanagement.R;
import com.example.workmanagement.databinding.FragmentChartBinding;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import com.example.workmanagement.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.Locale;

public class ChartFragment extends Fragment {

    private FragmentChartBinding binding;
    private BarChart barChart;

    private LineChart lineChart;
    private PieChart barChart3;
    private View view;
    private Button selectedChartButton;
    private String[] listItems = {"chart 1", "chart 2", "chart 3"};
    private boolean[] selectedItems = {true, false, false};

    private LocalDate localDate;

    private int dayOfWeek;

    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void GroupBarChart(){
        barChart = view.findViewById(R.id.barchartWeek);
        lineChart = view.findViewById(R.id.linechartWeek);
        barChart3 = view.findViewById(R.id.piechartWeek);

        barChart.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        barChart.setData(getDataBarChart());
        barChart.invalidate();

        lineChart.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        lineChart.setData(getDataLineChart());
        lineChart.invalidate();

        barChart3.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        barChart3.setData(getDataPieChart());
        barChart3.invalidate();


    }

    private BarData getDataBarChart(){
        ArrayList<BarEntry> data = new ArrayList<BarEntry>();

        int j = 5;
        for(int i = 0; i < 5; i++){
            data.add(new BarEntry(i,j));
            j += 2;
        }
        BarDataSet DataSet = new BarDataSet(data, "Number Task");
        DataSet.setColor(getActivity().getResources().getColor(R.color.color_barchart));
        BarData barData = new BarData();
        barData.addDataSet(DataSet);

        return barData;
    }
    private LineData getDataLineChart(){
        ArrayList<Entry> d = new ArrayList<Entry>();
        int j = 5;
        for(int i = 0; i < dayOfWeek; i++){
            d.add(new Entry(i,j));
            j += 2;
        }
        LineDataSet  lineDataSet = new LineDataSet(d,"data");
        ArrayList<ILineDataSet> data = new ArrayList<>();
        data.add(lineDataSet);
        LineData lineData = new LineData(data);

        return lineData;

    }

    private PieData getDataPieChart(){
        ArrayList<PieEntry> d = new ArrayList<PieEntry>();
        d.add(new PieEntry(100, "Mr 1"));
        d.add(new PieEntry(20, "Mr 2"));
        d.add(new PieEntry(10, "Mr 3"));
        d.add(new PieEntry(60, "Mr 4"));
        d.add(new PieEntry(40, "Mr 5"));
        d.add(new PieEntry(90, "Mr 6"));
        d.add(new PieEntry(50, "Mr 7"));
        d.add(new PieEntry(70, "Mr 8"));
        d.add(new PieEntry(10, "Mr 9"));
        d.add(new PieEntry(80, "Mr 10"));
        int[] color = new int[]{Color.BLUE, Color.CYAN, Color.GREEN, Color.RED, Color.GRAY, Color.MAGENTA, Color.DKGRAY, Color.YELLOW, Color.LTGRAY, Color.WHITE};
        PieDataSet dataSet = new PieDataSet(d, "");
        dataSet.setColors(color);
        PieData pieData = new PieData(dataSet);
        return pieData;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(inflater);
        view = binding.getRoot();

        localDate = LocalDate.now();
        dayOfWeek = localDate.getDayOfWeek().getValue();

        View bcv[] = new View[3];
        bcv[0] = view.findViewById(R.id.barchartWeek);
        bcv[1] = view.findViewById(R.id.linechartWeek);
        bcv[2] = view.findViewById(R.id.piechartWeek);

        for (int j = 0; j < 3; j++){
            if(selectedItems[j] == true){
                bcv[j].setVisibility(View.VISIBLE);

            }
            else if(selectedItems[j]==false){
                bcv[j].setVisibility(View.GONE);
            }
        }


        GroupBarChart();
        //contextmenubutton = view.findViewById(R.id.week_chart_context_menu_button);
        //registerForContextMenu(contextmenubutton);
        selectedChartButton = view.findViewById(R.id.week_chart_select_chart_button);
        selectedChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
//                mBuilder.setTitle("Select chart to show");
//                mBuilder.setMultiChoiceItems(listItems, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
//
//                    }
//                });
//                mBuilder.setCancelable(false);
//                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        for (int j = 0; j < 3; j++){
//                            if(selectedItems[j] == true){
//                                bcv[j].setVisibility(View.VISIBLE);
//
//                            }
//                            else if(selectedItems[j]==false){
//                                bcv[j].setVisibility(View.GONE);
//                            }
//                        }
//                    }
//                });
//                mBuilder.show();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialog_chart = inflater.inflate(R.layout.dialog_chart, null);
                mBuilder.setView(dialog_chart);

                Button btn_select_date_start = dialog_chart.findViewById(R.id.btn_select_date_start);
                Button btn_select_date_end = dialog_chart.findViewById(R.id.btn_select_date_end);
                btn_select_date_start.setText("Start");
                btn_select_date_end.setText("End");

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Ok", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();

            }
        });


        return view;
    }

}