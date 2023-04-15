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

import com.example.workmanagement.activities.HomeActivity;

import com.example.workmanagement.databinding.FragmentTableBinding;
import com.example.workmanagement.tableview.TableViewAdapter;
import com.example.workmanagement.tableview.TableViewListener;
import com.example.workmanagement.tableview.TableViewModel;
import com.example.workmanagement.tableview.model.Cell;
import com.example.workmanagement.tableview.model.ColumnHeader;
import com.example.workmanagement.tableview.model.RowHeader;
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

    TableView mTableView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTableBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
//        TableView tableView = new TableView(getContext());
//        TableViewModel tableViewModel =new TableViewModel();
//        TableViewAdapter adapter = new TableViewAdapter(tableViewModel);
//
//        tableView.setAdapter(adapter);
//        tableView.setTableViewListener(new TableViewListener(tableView));
//        adapter.setAllItems(tableViewModel.getColumnHeaderList(), tableViewModel.getRowHeaderList(), tableViewModel.getCellList());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTableView = view.findViewById(R.id.tableview);
        TableViewModel tableViewModel = new TableViewModel();

        TableViewAdapter tableViewAdapter = new TableViewAdapter(tableViewModel);

        mTableView.setAdapter(tableViewAdapter);
        mTableView.setTableViewListener(new TableViewListener(mTableView));
        tableViewAdapter.setAllItems(tableViewModel.getColumnHeaderList(), tableViewModel
                .getRowHeaderList(), tableViewModel.getCellList());
        boardViewModel = new ViewModelProvider(requireActivity()).get(BoardViewModel.class);
//        boardViewModel.getId().observe(getViewLifecycleOwner(), id -> binding.tableFragmentText.setText("Board with id: " + id));
    }
}