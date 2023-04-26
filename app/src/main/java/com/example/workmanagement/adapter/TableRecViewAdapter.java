package com.example.workmanagement.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.example.workmanagement.R;
import com.example.workmanagement.activities.HomeActivity;
import com.example.workmanagement.tableview.TableViewAdapter;
import com.example.workmanagement.tableview.TableViewListener;
import com.example.workmanagement.tableview.TableViewModel;
import com.example.workmanagement.tableview.model.Cell;
import com.example.workmanagement.utils.dto.SearchUserResponse;
import com.example.workmanagement.utils.services.impl.UserServiceImpl;
import com.example.workmanagement.viewmodels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableRecViewAdapter extends RecyclerView.Adapter<TableRecViewAdapter.ViewHolder> {

    private ArrayList<String> tables = new ArrayList<>();
    private Context context;
    UserViewModel userViewModel;
    private String photoUrl;
    private String name;
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTables(ArrayList<String> tables) {
        this.tables = tables;
    }

    public TableRecViewAdapter(Context context) {
        this.context = context;
    }

    public TableRecViewAdapter(Context context, UserViewModel userViewModel) {
        this.context = context;
        this.userViewModel = userViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_table, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tableName.setText("Table " + tables.get(position));
        holder.editTable.setText(holder.tableName.getText().toString());

        TableViewModel tableViewModel = new TableViewModel();
        Cell cell = new Cell("1", "New task");
        Object url = "";
        System.out.println(userViewModel.getPhotoUrl().getValue());
        if (!userViewModel.getPhotoUrl().getValue().equals("null")) {
            url = userViewModel.getPhotoUrl().getValue();
        } else {
            url = "default";
        }
        List<List<Cell>> listCells = new ArrayList<>();
        Cell cell1 = new Cell("2", url, userViewModel.getDisplayName().getValue());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date date = new Date();
        String text = String.valueOf(dateFormat.format(date));
        Cell cell2 = new Cell("3", text);
        List<Cell> list = new ArrayList<>();
        list.add(cell);
        list.add(cell1);
        list.add(cell2);
        listCells.add(list);
        tableViewModel.setRow(listCells.size());
        TableViewAdapter tableViewAdapter = new TableViewAdapter(tableViewModel, context);

        holder.table.setAdapter(tableViewAdapter);
        holder.table.setTableViewListener(new TableViewListener(holder.table));
        tableViewAdapter.setAllItems(tableViewModel.getColumnHeaderList(), tableViewModel.getRowHeaderList(), tableViewModel.getCellList());
        tableViewAdapter.setCellItems(listCells);
        holder.addTask.setOnClickListener(view -> showCreateTaskDialog(tableViewAdapter, listCells));
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    private void showCreateTaskDialog(TableViewAdapter TableAdapter, List<List<Cell>> listCells) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_task);
        EditText txtSearchUser = dialog.findViewById(R.id.editTxtSearch);
        EditText txtTaskName = dialog.findViewById(R.id.editTxtCreateTaskName);
//        TextView txtEmail = dialog.findViewById(R.id.txtEmail);
//        TextView txtPhoto = dialog.findViewById(R.id.txtPhotoUrl);
        ConstraintLayout btnCreateTask = dialog.findViewById(R.id.btnCreateTask);

        UserSearchInTaskAdapter adapter = new UserSearchInTaskAdapter(context);
        RecyclerView userRecView = dialog.findViewById(R.id.searchRecView);
        userRecView.setLayoutManager(new LinearLayoutManager(context));
        userRecView.setAdapter(adapter);

        btnCreateTask.setOnClickListener(view -> {
            if (!txtTaskName.getText().toString().equals("") && adapter.isChosen()) {
                Cell cell1 = new Cell("1", txtTaskName);
                Cell cell2 = new Cell("2", adapter.getUser().getPhotoUrl(), adapter.getUser().getEmail());
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                Date date = new Date();
                String text = String.valueOf(dateFormat.format(date));
                Cell cell3 = new Cell("3", text);
                List<Cell> list = new ArrayList<>();
                list.add(cell1);
                list.add(cell2);
                list.add(cell3);
                listCells.add(list);
                TableAdapter.setCellItems(listCells);
                TableAdapter.notifyDataSetChanged();
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Please fill full information", Toast.LENGTH_SHORT).show();
            }

        });

        txtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty())
                    UserServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).searchUser(1, charSequence.toString()).enqueue(new Callback<SearchUserResponse>() {
                        @Override
                        public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                            if (response.isSuccessful() && response.code() == 200)
                                adapter.setUsers(response.body().getUsers());
                        }

                        @Override
                        public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                else adapter.setUsers(new ArrayList<>());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container, titleBar;
        ImageView down, download, up, accept;
        TextView tableName, addTask;
        TableView table;
        EditText editTable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.fragment_container);
            titleBar = itemView.findViewById(R.id.titleBar);
            down = itemView.findViewById(R.id.iconDown);
            up = itemView.findViewById(R.id.iconUp);
            download = itemView.findViewById(R.id.iconDownload);
            tableName = itemView.findViewById(R.id.tableName);
            table = itemView.findViewById(R.id.table_container);
            addTask = itemView.findViewById(R.id.addTaskBtn);
            editTable = itemView.findViewById(R.id.editTableName);
            accept = itemView.findViewById(R.id.acceptBtn);

            down.setOnClickListener(view -> {
                down.setVisibility(View.GONE);
                up.setVisibility(View.VISIBLE);
                table.setVisibility(View.VISIBLE);
                addTask.setVisibility(View.VISIBLE);
            });
            up.setOnClickListener(view -> {
                down.setVisibility(View.VISIBLE);
                up.setVisibility(View.GONE);
                table.setVisibility(View.GONE);
                addTask.setVisibility(View.GONE);
            });
            tableName.setOnClickListener(view -> {
                editTable.setVisibility(View.VISIBLE);
                accept.setVisibility(View.VISIBLE);
                tableName.setVisibility(View.GONE);
            });
            accept.setOnClickListener(view -> {
                if (editTable.getText().toString().equals("")) {
                    Toast.makeText(context, "Please fill information", Toast.LENGTH_SHORT).show();
                } else {
                    tableName.setText(editTable.getText().toString());
                    tableName.setVisibility(View.VISIBLE);
                    editTable.setVisibility(View.GONE);
                    accept.setVisibility(View.GONE);
                }
            });

        }
    }
}
