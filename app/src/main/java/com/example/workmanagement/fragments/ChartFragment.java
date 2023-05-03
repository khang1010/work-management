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
    private Button selectedChartButton;

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

                CheckBox checkBox_barchart = dialog_chart.findViewById(R.id.checkbox_barchart);
                CheckBox checkBox_linechart = dialog_chart.findViewById(R.id.checkbox_linechart);
                CheckBox checkBox_piechart = dialog_chart.findViewById(R.id.checkbox_piechart);

                checkBox_barchart.setChecked(selectedItems[0]);
                checkBox_linechart.setChecked(selectedItems[1]);
                checkBox_piechart.setChecked(selectedItems[2]);

                Button btn_select_date_start_barchart = dialog_chart.findViewById(R.id.btn_select_date_start_barchart);
                Button btn_select_date_end_barchart = dialog_chart.findViewById(R.id.btn_select_date_end_barchart);
                Button btn_select_date_start_linechart = dialog_chart.findViewById(R.id.btn_select_date_start_linechart);
                Button btn_select_date_end_linechart = dialog_chart.findViewById(R.id.btn_select_date_end_linechart);
                Button btn_select_date_start_piechart = dialog_chart.findViewById(R.id.btn_select_date_start_piechart);
                Button btn_select_date_end_piechart = dialog_chart.findViewById(R.id.btn_select_date_end_piechart);

                SeekBar seekbar_number_barchart = dialog_chart.findViewById(R.id.seekbar_number_barchart);
                SeekBar seekbar_number_linechart = dialog_chart.findViewById(R.id.seekbar_number_linechart);
                SeekBar seekbar_number_piechart = dialog_chart.findViewById(R.id.seekbar_number_piechart);

                TextView txtbarchart = dialog_chart.findViewById(R.id.txt_barchart);
                TextView txtlinechart = dialog_chart.findViewById(R.id.txt_linechart);
                TextView txtpiechart = dialog_chart.findViewById(R.id.txt_piechart);

                txtbarchart.setText(numberBarchart+"/"+maxPeopleInBoard);
                txtlinechart.setText(numberLinechart+"/"+maxPeopleInBoard);
                txtpiechart.setText(numberPiechart+"/"+maxPeopleInBoard);

                Spinner spinner_unit_barchart = dialog_chart.findViewById(R.id.spinner_unit_barchart);
                Spinner spinner_unit_linechart = dialog_chart.findViewById(R.id.spinner_unit_linechart);
                Spinner spinner_unit_piechart = dialog_chart.findViewById(R.id.spinner_unit_piechart);

                //set max for seekbar

                seekbar_number_barchart.setMax(maxPeopleInBoard);
                seekbar_number_linechart.setMax(maxPeopleInBoard);
                seekbar_number_piechart.setMax(maxPeopleInBoard);

                //set process for seekbar
                seekbar_number_barchart.setProgress(numberBarchart);
                seekbar_number_linechart.setProgress(numberLinechart);
                seekbar_number_piechart.setProgress(numberPiechart);

                seekbar_number_barchart.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        txtbarchart.setText(seekBar.getProgress()+"/"+maxPeopleInBoard);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                seekbar_number_linechart.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        txtlinechart.setText(seekBar.getProgress()+"/"+maxPeopleInBoard);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                seekbar_number_piechart.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        txtpiechart.setText(seekBar.getProgress()+"/"+maxPeopleInBoard);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.chart_unit, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_unit_barchart.setAdapter(adapter);
                spinner_unit_linechart.setAdapter(adapter);
                spinner_unit_piechart.setAdapter(adapter);


                btn_select_date_start_barchart.setText(day + "/" + (month + 1) + "/" + year);
                btn_select_date_end_barchart.setText(day + "/" + (month + 1) + "/" + year);
                btn_select_date_start_linechart.setText(day + "/" + (month + 1) + "/" + year);
                btn_select_date_end_linechart.setText(day + "/" + (month + 1) + "/" + year);
                btn_select_date_start_piechart.setText(day + "/" + (month + 1) + "/" + year);
                btn_select_date_end_piechart.setText(day + "/" + (month + 1) + "/" + year);


                btn_select_date_start_barchart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar c = Calendar.getInstance();
                        DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                btn_select_date_start_barchart.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                        dpd.show();

                    }
                });
                btn_select_date_end_barchart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar c = Calendar.getInstance();
                        DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                btn_select_date_end_barchart.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                        dpd.show();

                    }
                });
                btn_select_date_start_linechart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar c = Calendar.getInstance();
                        DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                btn_select_date_start_linechart.setText(day + " " + (month + 1) + " " + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                        dpd.show();

                    }
                });
                btn_select_date_end_linechart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar c = Calendar.getInstance();
                        DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                btn_select_date_end_linechart.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                        dpd.show();

                    }
                });
                btn_select_date_start_piechart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar c = Calendar.getInstance();
                        DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                btn_select_date_start_piechart.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                        dpd.show();

                    }
                });
                btn_select_date_end_piechart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar c = Calendar.getInstance();
                        DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                btn_select_date_end_piechart.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                        dpd.show();

                    }
                });

                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedItems[0] = checkBox_barchart.isChecked();
                        selectedItems[1] = checkBox_linechart.isChecked();
                        selectedItems[2] = checkBox_piechart.isChecked();

                        numberBarchart = seekbar_number_barchart.getProgress();
                        numberLinechart = seekbar_number_linechart.getProgress();
                        numberPiechart = seekbar_number_piechart.getProgress();
                        int[] n = new int[]{numberBarchart, numberLinechart, numberPiechart};

                        GroupBarChart();

                        for (int j = 0; j < 3; j++) {
                            if (selectedItems[j] == true) {
                                bcv[j].setVisibility(View.VISIBLE);

                            } else {
                                bcv[j].setVisibility(View.GONE);
                            }
                        }
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boardViewModel = new ViewModelProvider(getActivity()).get(BoardViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        userViewModel.getCurrentBoardId().observe(getViewLifecycleOwner(), id -> {
            if (id > 0)
                BoardServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).getBoardDetails(id).enqueue(new Callback<BoardDetailsDTO>() {
                    @Override
                    public void onResponse(Call<BoardDetailsDTO> call, Response<BoardDetailsDTO> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            boardViewModel.setId(response.body().getId());
                            boardViewModel.setName(response.body().getName());
                            boardViewModel.setAdmin(response.body().getAdmin());
                            boardViewModel.setMembers(response.body().getMembers());
                            boardViewModel.setTables(response.body().getTables());
                        }
                    }

                    @Override
                    public void onFailure(Call<BoardDetailsDTO> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        });
    }
}