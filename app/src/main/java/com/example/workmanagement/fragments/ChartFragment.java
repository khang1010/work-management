package com.example.workmanagement.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.workmanagement.R;
import com.example.workmanagement.adapter.ChartDetailRecycleviewAdapter;
import com.example.workmanagement.databinding.FragmentChartBinding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.BoardDetailsDTO;
import com.example.workmanagement.utils.dto.ChartDTO;
import com.example.workmanagement.utils.models.ChartDetailItem;
import com.example.workmanagement.utils.services.impl.BoardServiceImpl;
import com.example.workmanagement.viewmodels.BoardViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.transition.MaterialSharedAxis;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChartFragment extends Fragment {

    private FragmentChartBinding binding;
    private BarChart barChart;

    private BarChart barChartYouSelf;
    private PieChart pieChart;
    private View view;

    private RecyclerView barchartRecyclerview, piechartRecyclerview;
    private ChartDetailRecycleviewAdapter barChartAdapter, pieChartAdapter;
    private List<ChartDetailItem> barChartList;
    private List<ChartDetailItem> pieChartList;

    private LocalDate localDate;

    private UserViewModel userViewModel;

    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initElements(LayoutInflater inflater) {
        barChart = view.findViewById(R.id.barchart);
        barChartYouSelf = view.findViewById(R.id.barchart_youself);
        pieChart = view.findViewById(R.id.piechart);
        barchartRecyclerview = view.findViewById(R.id.barchart_recyclerview);
        piechartRecyclerview = view.findViewById(R.id.piechart_recyclerview);

        barChartList = new ArrayList<>();
        pieChartList = new ArrayList<>();

        barchartRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        piechartRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));


        barChartAdapter = new ChartDetailRecycleviewAdapter(barChartList, inflater);
        pieChartAdapter = new ChartDetailRecycleviewAdapter(pieChartList, inflater);

        barchartRecyclerview.setAdapter(barChartAdapter);
        piechartRecyclerview.setAdapter(pieChartAdapter);


    }

    private void GroupBarChart(int n1, List<Integer> d1, List<String> names1) {
        if (n1 != 0) {
            barChart.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            barChart.setData(getDataBarChart(n1, d1, names1));
            barChart.setDescription(null);


            Legend l = barChart.getLegend();
//            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setWordWrapEnabled(true);
            l.setForm(Legend.LegendForm.SQUARE);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setDrawLabels(false);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(true);


            YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setLabelCount(8, false);
            yAxisLeft.setSpaceTop(0.3f);
            yAxisLeft.setStartAtZero(true);


            YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setDrawLabels(false);
            yAxisRight.setDrawAxisLine(true);
            yAxisRight.setDrawGridLines(false);

            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    String s;
                    if (h.getY() == 1)
                        s = names1.get((int) h.getX()) + " has " + String.valueOf((int) h.getY()) + " task.";
                    else
                        s = names1.get((int) h.getX()) + " has " + String.valueOf((int) h.getY()) + " tasks.";


                    Toast toast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
                    toast.show();


                }

                @Override
                public void onNothingSelected() {

                }
            });
            barChart.invalidate();


        } else {

        }
    }

    private void GroupBarChartYouSelf(int n2, List<Integer> d2) {
        if (d2.get(0) != 0 || d2.get(1) != 0 || d2.get(2) != 0) {
            barChartYouSelf.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            barChartYouSelf.setData(getDataBarChartY(n2, d2));
            barChartYouSelf.setDescription(null);


            Legend l = barChartYouSelf.getLegend();
            //l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            //l.setWordWrapEnabled(true);
            l.setForm(Legend.LegendForm.SQUARE);

            XAxis xAxis = barChartYouSelf.getXAxis();
            xAxis.setDrawLabels(false);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(true);


            YAxis yAxisLeft = barChartYouSelf.getAxisLeft();
            yAxisLeft.setStartAtZero(true);
            yAxisLeft.setLabelCount(5, false);
            yAxisLeft.setSpaceTop(0.3f);


            YAxis yAxisRight = barChartYouSelf.getAxisRight();
            yAxisRight.setDrawLabels(false);
            yAxisRight.setDrawAxisLine(true);
            yAxisRight.setDrawGridLines(false);
            barChartYouSelf.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    String s;
                    if (h.getX() == 0) {
                        if (h.getY() == 0 || h.getY() == 1)
                            s = "You have " + String.valueOf((int) h.getY()) + " stuck task";
                        else
                            s = "You have " + String.valueOf((int) h.getY()) + " stuck tasks";
                    } else if (h.getX() == 1) {
                        if (h.getY() == 0 || h.getY() == 1)
                            s = "You have " + String.valueOf((int) h.getY()) + " pending task";
                        else
                            s = "You have " + String.valueOf((int) h.getY()) + " pending tasks";
                    } else {
                        if (h.getY() == 0 || h.getY() == 1)
                            s = "You have " + String.valueOf((int) h.getY()) + " done task";
                        else
                            s = "You have " + String.valueOf((int) h.getY()) + " done tasks";
                    }
                    Toast toast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
                    toast.show();


                }

                @Override
                public void onNothingSelected() {

                }
            });
            barChartYouSelf.invalidate();
        } else {

        }
    }

//    private List<Integer> convertToPercentageValueList(List<Integer> list, int total){
//        List<Integer> result = new ArrayList<>();
//        for(int i = 0; i < list.size(); i++){
//            result.add(list.get(i)/total);
//        }
//        return result;
//    }

    private void GroupPieChart(int n3, List<Integer> d3, List<String> names3, int totalTaskInBoard) {
        if (n3 != 0) {
            List<Integer> percent = new ArrayList<>();
            pieChart.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            pieChart.setUsePercentValues(true);
            pieChart.setData(getDataPieChart(n3, d3, names3));
            pieChart.setDescription(null);

            pieChart.setHighlightPerTapEnabled(true);

            pieChart.setCenterTextColor(getActivity().getResources().getColor(R.color.white));
            pieChart.setCenterTextSize(25f);
            pieChart.setDrawEntryLabels(false);

            Legend l = pieChart.getLegend();
            l.setEnabled(false);

//            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//            l.setOrientation(Legend.LegendOrientation.VERTICAL);
//            l.setDrawInside(false);
//            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

//            l.setWordWrapEnabled(true);
//            l.setForm(Legend.LegendForm.CIRCLE);
//            l.setMaxSizePercent(0.8f);


            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    String s;
                    if (h.getY() == 1)
                        s = names3.get((int) h.getX()) + " has " + String.valueOf((int) h.getY()) + " task.";
                    else
                        s = names3.get((int) h.getX()) + " has " + String.valueOf((int) h.getY()) + " tasks.";
                    Toast toast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
                    toast.show();

                }

                @Override
                public void onNothingSelected() {

                }
            });
            pieChart.invalidate();

        } else {

        }
    }

    public void GroupCharts(int n1, List<Integer> d1, List<String> names1, int n2, List<Integer> d2, int n3, List<Integer> d3, List<String> names3, int totalTaskInBoard) {

        GroupBarChart(n1, d1, names1);
        GroupBarChartYouSelf(n2, d2);
        GroupPieChart(n3, d3, names3, totalTaskInBoard);

    }

    private void setDataRecyclerview(List<String> names, List<Integer> numbeTasks, List<Integer> colors, List<ChartDetailItem> list) {

        for (int i = 0; i < names.size(); i++) {
            ChartDetailItem item = new ChartDetailItem();
            item.setId(String.valueOf(i));
            item.setName(names.get(i));
            item.setNumber_tasks(String.valueOf(numbeTasks.get(i)));
            item.setColor(colors.get(i));
            list.add(item);
        }

    }

    private BarData getDataBarChart(int number, List<Integer> d, List<String> names) {

        BarData barData = new BarData();
        ArrayList<Integer> colors = new ArrayList<Integer>();


        int temp = d.get(0);

        for (int i = 0; i < number; i++) {
            ArrayList<BarEntry> data = new ArrayList<BarEntry>();
            data.add(new BarEntry(i, d.get(i)));

            BarDataSet DataSet;
            if (i == 0)
                DataSet = new BarDataSet(data, names.get(i));
            else
                DataSet = new BarDataSet(data, "");

            if (d.get(i) == temp) {
                DataSet.setColor(getActivity().getResources().getColor(R.color.top1));
                colors.add(getActivity().getResources().getColor(R.color.top1));
            }
            else
                switch (i) {
                    case 0:
                        DataSet.setColor(getActivity().getResources().getColor(R.color.top1));
                        colors.add(getActivity().getResources().getColor(R.color.top1));
                        break;
                    case 1:
                        DataSet.setColor(getActivity().getResources().getColor(R.color.top2));
                        colors.add(getActivity().getResources().getColor(R.color.top2));
                        break;
                    case 2:
                        DataSet.setColor(getActivity().getResources().getColor(R.color.top3));
                        colors.add(getActivity().getResources().getColor(R.color.top3));
                        break;
                    case 3:
                        DataSet.setColor(getActivity().getResources().getColor(R.color.top4));
                        colors.add(getActivity().getResources().getColor(R.color.top4));
                        break;
                    case 4:
                        DataSet.setColor(getActivity().getResources().getColor(R.color.top5));
                        colors.add(getActivity().getResources().getColor(R.color.top5));
                        break;
                }

            barData.addDataSet(DataSet);
        }




        setDataRecyclerview(names, d, colors, barChartList);
        barChartAdapter.notifyDataSetChanged();

        return barData;
    }

    private BarData getDataBarChartY(int number, List<Integer> dd) {

        BarData barData = new BarData();


        ArrayList<BarEntry> data1 = new ArrayList<BarEntry>();
        data1.add(new BarEntry(2, dd.get(0)));
        BarDataSet DataSet1 = new BarDataSet(data1, "Done");
        DataSet1.setColor(getActivity().getResources().getColor(R.color.done));

        ArrayList<BarEntry> data2 = new ArrayList<BarEntry>();
        data2.add(new BarEntry(1, dd.get(1)));
        BarDataSet DataSet2 = new BarDataSet(data2, "Pending");
        DataSet2.setColor(getActivity().getResources().getColor(R.color.pending));

        ArrayList<BarEntry> data3 = new ArrayList<BarEntry>();
        data3.add(new BarEntry(0, dd.get(2)));
        BarDataSet DataSet3 = new BarDataSet(data3, "Stuck");
        DataSet3.setColor(getActivity().getResources().getColor(R.color.stuck));

        barData.addDataSet(DataSet1);
        barData.addDataSet(DataSet2);
        barData.addDataSet(DataSet3);

        return barData;

    }

    private PieData getDataPieChart(int number, List<Integer> dd, List<String> names) {
        ArrayList<PieEntry> d = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        Random rand = new Random();
        int r = 0;
        int g = 0;
        int b = 0;

        for (int i = 0; i < number; i++) {


            d.add((new PieEntry(dd.get(i), names.get(i))));
            r = rand.nextInt(255);
            g = rand.nextInt(255);
            b = rand.nextInt(255);
            colors.add(Color.rgb(r, g, b));


        }
        //int[] color = new int[]{Color.BLUE, Color.CYAN, Color.GREEN, Color.RED, Color.GRAY, Color.MAGENTA};

        PieDataSet dataSet = new PieDataSet(d, "");
        dataSet.setColors(colors);

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(2f);


        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(15f);
        pieData.setValueTextColor(getActivity().getResources().getColor(R.color.white));
        setDataRecyclerview(names, dd, colors, pieChartList);
        pieChartAdapter.notifyDataSetChanged();
        return pieData;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(inflater);
        view = binding.getRoot();
        initElements(inflater);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        userViewModel.getCurrentBoardId().observe(getViewLifecycleOwner(), id ->
                BoardServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).getChartData(id)
                        .enqueue(new Callback<ChartDTO>() {
                            @Override
                            public void onResponse(Call<ChartDTO> call, Response<ChartDTO> response) {
                                if (response.isSuccessful() && response.code() == 200) {
                                    ChartDTO dataAllChart = response.body();
                                    List<Integer> l1 = new ArrayList<>();
                                    List<Integer> l2 = new ArrayList<>();
                                    List<Integer> l3 = new ArrayList<>();
                                    int sizePieChart = 0;
                                    List<String> names1 = new ArrayList<>();
                                    List<String> names3 = new ArrayList<>();
                                    int totalTaskInBoard = 0;
                                    try {
                                        for (int i = 0; i < dataAllChart.getChart_1().size(); i++) {
                                            try {
                                                l1.add(dataAllChart.getChart_1().get(i).getAmount());
                                                names1.add(dataAllChart.getChart_1().get(i).getDisplayName());
                                            } catch (Exception e) {
                                                System.out.println("Err: chart 1 cant get data");
                                            }

                                        }
                                    } catch (Exception e) {
                                        System.out.println("Err: havent data in chart 1");
                                    }

                                    try {
                                        for (int i = 0; i < dataAllChart.getChart_3().size(); i++) {
                                            try {
                                                l2.add(dataAllChart.getChart_3().get(i).getAmount());
                                            } catch (Exception e) {
                                                System.out.println("Err: chart 2 cant get data");
                                            }

                                        }
                                    } catch (Exception e) {
                                        System.out.println("Err: havent data in chart 2");
                                    }

                                    try {
                                        sizePieChart = dataAllChart.getChart_2().size();
                                    } catch (Exception e) {
                                    }
                                    try {
                                        for (int i = 0; i < dataAllChart.getChart_2().size(); i++) {
                                            try {

                                                int amount = dataAllChart.getChart_2().get(i).getAmount();
                                                if (amount != 0) {
                                                    l3.add(amount);
                                                    names3.add(dataAllChart.getChart_2().get(i).getDisplayName());
                                                    totalTaskInBoard += amount;
                                                } else {
                                                    sizePieChart--;
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Err: chart 3 cant get data");
                                            }

                                        }
                                    } catch (Exception e) {
                                        System.out.println("Err: havent data in chart 3");
                                    }

                                    Collections.reverse(l3);
                                    barChartList.clear();
                                    pieChartList.clear();
                                    barChartAdapter.notifyDataSetChanged();
                                    pieChartAdapter.notifyDataSetChanged();


                                    GroupCharts(dataAllChart.getChart_1().size(), l1, names1, dataAllChart.getChart_3().size(), l2, sizePieChart, l3, names3, totalTaskInBoard);

                                }
                            }

                            @Override
                            public void onFailure(Call<ChartDTO> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }


}