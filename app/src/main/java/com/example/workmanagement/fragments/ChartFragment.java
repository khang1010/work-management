package com.example.workmanagement.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.workmanagement.R;
import com.example.workmanagement.databinding.FragmentChartBinding;

public class ChartFragment extends Fragment {

    private FragmentChartBinding binding;

    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(inflater);
        View view = binding.getRoot();
        loadFragment(new WeekChartFragment());
        binding.weekButton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.white));
        binding.weekButton.setBackgroundResource(R.drawable.shape_chart_nav_active);
        // Inflate the layout for this fragment
        binding.weekButton.setOnClickListener(view1 -> {
            loadFragment(new WeekChartFragment());
            binding.weekButton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.white));
            binding.monthButton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.black));
            binding.yearButton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.black));
            binding.weekButton.setBackgroundResource(R.drawable.shape_chart_nav_active);
            binding.monthButton.setBackgroundResource(R.drawable.shape_chart_nav_inactive);
            binding.yearButton.setBackgroundResource(R.drawable.shape_chart_nav_inactive);
        });
        binding.monthButton.setOnClickListener(view1 -> {
            loadFragment(new MonthChartFragment());
            binding.weekButton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.black));
            binding.monthButton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.white));
            binding.yearButton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.black));
            binding.weekButton.setBackgroundResource(R.drawable.shape_chart_nav_inactive);
            binding.monthButton.setBackgroundResource(R.drawable.shape_chart_nav_active);
            binding.yearButton.setBackgroundResource(R.drawable.shape_chart_nav_inactive);
        });
        binding.yearButton.setOnClickListener(view1 -> {
            loadFragment(new YearChartFragment());
            binding.weekButton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.black));
            binding.monthButton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.black));
            binding.yearButton.setTextColor(getActivity().getApplication().getResources().getColor(R.color.white));
            binding.weekButton.setBackgroundResource(R.drawable.shape_chart_nav_inactive);
            binding.monthButton.setBackgroundResource(R.drawable.shape_chart_nav_inactive);
            binding.yearButton.setBackgroundResource(R.drawable.shape_chart_nav_active);
        });
        return view;
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.chart_frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}