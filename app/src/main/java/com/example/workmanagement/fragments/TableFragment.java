package com.example.workmanagement.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.TableView;
import com.example.workmanagement.R;
import com.example.workmanagement.activities.Cell;
import com.example.workmanagement.activities.ColumnHeader;
import com.example.workmanagement.activities.HomeActivity;
import com.example.workmanagement.activities.MyTableViewAdapter;
import com.example.workmanagement.activities.MyTableViewListener;
import com.example.workmanagement.activities.RowHeader;
import com.example.workmanagement.databinding.FragmentTableBinding;
import com.example.workmanagement.viewmodels.BoardViewModel;

import java.util.List;

public class TableFragment extends Fragment {

    private FragmentTableBinding binding;

    private BoardViewModel boardViewModel;
    private List<RowHeader> mRowHeaderList;
    private List<ColumnHeader> mColumnHeaderList;
    private List<List<Cell>> mCellList;
    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTableBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        TableView tableView = new TableView(getContext());
        MyTableViewAdapter adapter = new MyTableViewAdapter(getContext());
        tableView.setAdapter(adapter);
        adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);
        tableView.setTableViewListener(new MyTableViewListener());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        boardViewModel = new ViewModelProvider(requireActivity()).get(BoardViewModel.class);
//        boardViewModel.getId().observe(getViewLifecycleOwner(), id -> binding.tableFragmentText.setText("Board with id: " + id));
    }
}