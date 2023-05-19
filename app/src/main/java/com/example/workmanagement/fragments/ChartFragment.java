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
import com.example.workmanagement.databinding.FragmentChartBinding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;


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
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.BoardDetailsDTO;
import com.example.workmanagement.utils.dto.ChartDTO;
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





    private LocalDate localDate;


    private UserViewModel userViewModel;







    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void GroupChart(int n1, List<Integer> d1, List<String> names1, int n2, List<Integer> d2, int n3, List<Integer> d3, List<String> names3) {
        barChart = view.findViewById(R.id.barchart);
        barChartYouSelf = view.findViewById(R.id.barchart_youself);
        pieChart = view.findViewById(R.id.piechart);

        if (n1 != 0) {
            barChart.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            barChart.setData(getDataBarChart(n1, d1, names1));
            barChart.setDescription(null);
            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    String s;
                    if(h.getY() == 1)
                        s = names1.get((int)h.getX()) +" has "+ String.valueOf((int)h.getY()) + " task.";
                    else
                        s = names1.get((int)h.getX()) +" has "+ String.valueOf((int)h.getY()) + " tasks.";


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

        if (n2 != 0) {
            barChartYouSelf.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            barChartYouSelf.setData(getDataBarChartY(n2, d2));
            barChartYouSelf.setDescription(null);
            barChartYouSelf.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    String s;
                    if(h.getX()==0){
                        if(h.getY()==0 || h.getY()==1)
                            s = "You have " + String.valueOf((int)h.getY()) + " stuck task";
                        else
                            s = "You have " + String.valueOf((int)h.getY()) + " stuck tasks";
                    }else if(h.getX()==1){
                        if(h.getY()==0 || h.getY()==1)
                            s = "You have " + String.valueOf((int)h.getY()) + " pending task";
                        else
                            s = "You have " + String.valueOf((int)h.getY()) + " pending tasks";
                    }else{
                        if(h.getY()==0 || h.getY()==1)
                            s = "You have " + String.valueOf((int)h.getY()) + " done task";
                        else
                            s = "You have " + String.valueOf((int)h.getY()) + " done tasks";
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

        if (n3 != 0) {
            pieChart.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            pieChart.setUsePercentValues(true);
            pieChart.setData(getDataPieChart(n3, d3, names3));
            pieChart.setDescription(null);

            pieChart.setHighlightPerTapEnabled(true);

            pieChart.setCenterTextColor(getActivity().getResources().getColor(R.color.white));
            pieChart.setCenterTextSize(25f);
            pieChart.setDrawEntryLabels(false);

            Legend l = pieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    String s;
                    if(h.getY() == 1)
                        s = names3.get((int)h.getX()) +" has "+ String.valueOf((int)h.getY()) + " task.";
                    else
                        s = names3.get((int)h.getX()) +" has "+ String.valueOf((int)h.getY()) + " tasks.";
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

    private BarData getDataBarChart(int number, List<Integer> d, List<String> names) {

        BarData barData = new BarData();


        for (int i = 0; i < number; i++) {
            ArrayList<BarEntry> data = new ArrayList<BarEntry>();
            data.add(new BarEntry(i, d.get(i)));

            BarDataSet DataSet;
            if(i == 0)
                DataSet = new BarDataSet(data, names.get(i));
            else
                DataSet = new BarDataSet(data, null);

            switch (i){
                case 0:
                    DataSet.setColor(getActivity().getResources().getColor(R.color.top1));
                    break;
                case 1:
                    DataSet.setColor(getActivity().getResources().getColor(R.color.top2));
                    break;
                case 2:
                    DataSet.setColor(getActivity().getResources().getColor(R.color.top3));
                    break;
                case 3:
                    DataSet.setColor(getActivity().getResources().getColor(R.color.top4));
                    break;
                case 4:
                    DataSet.setColor(getActivity().getResources().getColor(R.color.top5));
                    break;
            }

            barData.addDataSet(DataSet);
        }


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
        return pieData;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(inflater);
        view = binding.getRoot();

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

                                    GroupChart(dataAllChart.getChart_1().size(), l1, names1, dataAllChart.getChart_3().size(), l2, sizePieChart, l3, names3);

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