package com.example.workmanagement.fragments;

import android.app.DatePickerDialog;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChartFragment extends Fragment {

    private FragmentChartBinding binding;
    private BarChart barChart;

    private LineChart lineChart;
    private PieChart barChart3;
    private View view;


    private String[] listItems = {"chart 1", "chart 2", "chart 3"};
    private boolean[] selectedItems = {true, false, false};

    private LocalDate localDate;

    private int dayOfWeek;
    private BoardViewModel boardViewModel;
    private UserViewModel userViewModel;

    private int numberBarchart;
    private int numberLinechart;
    private int numberPiechart;

    private DatePickerDialog datePickerDialog;
    private int maxPeopleInBoard;

    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void GroupBarChart() {
        barChart = view.findViewById(R.id.barchartWeek);
        lineChart = view.findViewById(R.id.linechartWeek);
        barChart3 = view.findViewById(R.id.piechartWeek);

        barChart.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        barChart.setData(getDataBarChart(numberBarchart));
        barChart.invalidate();

        lineChart.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        lineChart.setData(getDataLineChart(numberLinechart));
        lineChart.invalidate();

        barChart3.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        barChart3.setData(getDataPieChart(numberPiechart));
        barChart3.invalidate();


    }

    private BarData getDataBarChart(int number) {

        BarData barData = new BarData();

        int j = 5;
        for (int i = 0; i < number; i++) {
            ArrayList<BarEntry> data = new ArrayList<BarEntry>();
            data.add(new BarEntry(i, j));
            j += 2;
            String label = String.valueOf(i + 1);
            BarDataSet DataSet = new BarDataSet(data, label);

            DataSet.setColor(getActivity().getResources().getColor(R.color.color_barchart));
            barData.addDataSet(DataSet);
        }


        return barData;
    }

    private LineData getDataLineChart(int number) {

        ArrayList<ILineDataSet> data = new ArrayList<>();
        Random rand = new Random();
        int r = 0;
        int g = 0;
        int b = 0;
        int j = 5;
        for (int n = 0; n < number; n++) {
            ArrayList<Entry> d = new ArrayList<Entry>();


            for (int i = 0; i < dayOfWeek; i++) {
                d.add(new Entry(i, j));
                j += 2;
            }
            String label = String.valueOf(n + 1);
            LineDataSet lineDataSet = new LineDataSet(d, label);
            if (n == 0)
                lineDataSet.setColor(getActivity().getResources().getColor(R.color.color_linechart));
            else {
                r = rand.nextInt(255);
                g = rand.nextInt(255);
                b = rand.nextInt(255);
                lineDataSet.setColor(Color.rgb(r, g, b));
            }
            data.add(lineDataSet);
        }


        LineData lineData = new LineData(data);

        return lineData;

    }

    private PieData getDataPieChart(int number) {
        ArrayList<PieEntry> d = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        Random rand = new Random();
        int r = 0;
        int g = 0;
        int b = 0;
        int j = 20;
        for (int i = 0; i < number; i++) {
            String label = "Mr" + String.valueOf(i + 1);
            d.add((new PieEntry(j, label)));
            r = rand.nextInt(255);
            g = rand.nextInt(255);
            b = rand.nextInt(255);
            colors.add(Color.rgb(r, g, b));

            j += 10;
        }
        //int[] color = new int[]{Color.BLUE, Color.CYAN, Color.GREEN, Color.RED, Color.GRAY, Color.MAGENTA};

        PieDataSet dataSet = new PieDataSet(d, "");
        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);
        return pieData;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(inflater);
        view = binding.getRoot();

        maxPeopleInBoard = 20;
        numberBarchart = 5;
        numberLinechart = 1;
        numberPiechart = 10;


        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);


        localDate = LocalDate.now();
        dayOfWeek = localDate.getDayOfWeek().getValue();

        View bcv[] = new View[3];
        bcv[0] = view.findViewById(R.id.barchartWeek);
        bcv[1] = view.findViewById(R.id.linechartWeek);
        bcv[2] = view.findViewById(R.id.piechartWeek);

        for (int j = 0; j < 3; j++) {
            if (selectedItems[j] == true) {
                bcv[j].setVisibility(View.VISIBLE);

            } else if (selectedItems[j] == false) {
                bcv[j].setVisibility(View.GONE);
            }
        }


        GroupBarChart();





        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        userViewModel.getCurrentBoardId().observe(getViewLifecycleOwner(), id ->
                BoardServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).getBoardDetails(id)
                        .enqueue(new Callback<BoardDetailsDTO>() {
                            @Override
                            public void onResponse(Call<BoardDetailsDTO> call, Response<BoardDetailsDTO> response) {
                                if (response.isSuccessful() && response.code() == 200) {

                                }
                            }

                            @Override
                            public void onFailure(Call<BoardDetailsDTO> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                );
    }
}