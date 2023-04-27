package com.example.workmanagement.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.example.workmanagement.R;

import com.example.workmanagement.activities.HomeActivity;

import com.example.workmanagement.adapter.TableRecViewAdapter;
import com.example.workmanagement.adapter.UserInvitedRecViewAdapter;
import com.example.workmanagement.adapter.UserSearchInTaskAdapter;
import com.example.workmanagement.adapter.UserSearchRecViewAdapter;
import com.example.workmanagement.databinding.FragmentTableBinding;
import com.example.workmanagement.tableview.TableViewAdapter;
import com.example.workmanagement.tableview.TableViewListener;
import com.example.workmanagement.tableview.TableViewModel;
import com.example.workmanagement.tableview.model.Cell;
import com.example.workmanagement.tableview.model.ColumnHeader;
import com.example.workmanagement.tableview.model.RowHeader;
import com.example.workmanagement.utils.dto.SearchUserResponse;
import com.example.workmanagement.utils.dto.TableDTO;
import com.example.workmanagement.utils.dto.TableDetailsDTO;
import com.example.workmanagement.utils.dto.UserInfoDTO;
import com.example.workmanagement.utils.services.TableService;
import com.example.workmanagement.utils.services.impl.TableServiceImpl;
import com.example.workmanagement.utils.services.impl.UserServiceImpl;
import com.example.workmanagement.viewmodels.BoardViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableFragment extends Fragment {

    private FragmentTableBinding binding;

    private BoardViewModel boardViewModel;

    private RecyclerView tableRecView;
    private RelativeLayout clockLayout;
    private Button btnAddTable;
    private UserViewModel userViewModel;
    public TableFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TableView mTableView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTableBinding.inflate(inflater, container, false);
//        binding.tableTestContainer.setVisibility(View.GONE);
//        TableView tableView = new TableView(getContext());
//        TableViewModel tableViewModel =new TableViewModel();
//        TableViewAdapter adapter = new TableViewAdapter(tableViewModel);
//
//        tableView.setAdapter(adapter);
//        tableView.setTableViewListener(new TableViewListener(tableView));
//        adapter.setAllItems(tableViewModel.getColumnHeaderList(), tableViewModel.getRowHeaderList(), tableViewModel.getCellList());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        mTableView = view.findViewById(R.id.tableview);
//        TableViewModel tableViewModel = new TableViewModel();
//
//        TableViewAdapter tableViewAdapter = new TableViewAdapter(tableViewModel);
//
//        mTableView.setAdapter(tableViewAdapter);
//        mTableView.setTableViewListener(new TableViewListener(mTableView));
//        tableViewAdapter.setAllItems(tableViewModel.getColumnHeaderList(), tableViewModel
//                .getRowHeaderList(), tableViewModel.getCellList());
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        boardViewModel = new ViewModelProvider(requireActivity()).get(BoardViewModel.class);
        boardViewModel.getTables().observe(getViewLifecycleOwner(), tables -> {
            new Handler().postDelayed(() -> {
                tableRecView = view.findViewById(R.id.table_rec_view);
                TableRecViewAdapter adapter = new TableRecViewAdapter(getActivity(), userViewModel, boardViewModel);
                tableRecView.setAdapter(adapter);
                tableRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter.setTables(tables);
                clockLayout = view.findViewById(R.id.clock_container);
                clockLayout.setVisibility(View.GONE);
            }, 500);
        });
        btnAddTable = view.findViewById(R.id.addTableBtn);
        btnAddTable.setOnClickListener(view1 -> {
           showCreateTableDialog();
        });
    }

    private void showCreateTableDialog() {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_table);
        EditText txtSearchUser = dialog.findViewById(R.id.editTxtSearchUserTable);
        EditText txtTableName = dialog.findViewById(R.id.editTxtCreateTableName);
        ConstraintLayout btnCreateTable = dialog.findViewById(R.id.btnCreateTable);


        UserInvitedRecViewAdapter invitedAdapter = new UserInvitedRecViewAdapter(getActivity());
        RecyclerView userInvitedRecView = dialog.findViewById(R.id.invitedUserRecView);
        userInvitedRecView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        userInvitedRecView.setAdapter(invitedAdapter);

        UserSearchRecViewAdapter adapter = new UserSearchRecViewAdapter(getActivity(), invitedAdapter);
        RecyclerView userRecView = dialog.findViewById(R.id.searchUserRecView);
        userRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userRecView.setAdapter(adapter);

        btnCreateTable.setOnClickListener(view -> {
            if (!txtTableName.getText().toString().isEmpty()) {
                List<TableDetailsDTO> tableDetailsDTOS = boardViewModel.getTables().getValue();
                TableDTO newTable = new TableDTO();
                newTable.setName(txtTableName.getText().toString());
                newTable.setBoardId(boardViewModel.getId().getValue());
//                ArrayList<Long> membersId = new ArrayList<>();
//                membersId.add(userViewModel.getId().getValue());
//                for (UserInfoDTO user: invitedAdapter.getUsers()) {
//                    membersId.add(user.getId());
//                }
                newTable.setMemberIds(invitedAdapter.getUsers().stream().map(u -> u.getId()).collect(Collectors.toList()));
                TableServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).createTable(newTable)
                        .enqueue(new Callback<TableDetailsDTO>() {
                            @Override
                            public void onResponse(Call<TableDetailsDTO> call, Response<TableDetailsDTO> response) {
                                if (response.isSuccessful() && response.code() == 201) {
                                    tableDetailsDTOS.add(response.body());
                                    boardViewModel.setTables(tableDetailsDTOS);
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<TableDetailsDTO> call, Throwable t) {
                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getActivity(), "Please fill information", Toast.LENGTH_SHORT).show();
            }
        });

        txtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    List<UserInfoDTO> users = new ArrayList<>();
                    users.addAll(boardViewModel.getMembers().getValue());
                    users.add(boardViewModel.getAdmin().getValue());
                    adapter.setUsers(users.stream()
                            .filter(m -> m.getId() != userViewModel.getId().getValue()
                                    && (m.getDisplayName().trim().toLowerCase().contains(charSequence.toString().trim())
                                    || m.getEmail().trim().toLowerCase().contains(charSequence.toString().trim()))
                            )
                            .collect(Collectors.toList()));
                }
                else adapter.setUsers(new ArrayList<>());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }
}
