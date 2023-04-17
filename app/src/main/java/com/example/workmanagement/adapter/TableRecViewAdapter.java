package com.example.workmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.example.workmanagement.R;
import com.example.workmanagement.tableview.TableViewAdapter;
import com.example.workmanagement.tableview.TableViewListener;
import com.example.workmanagement.tableview.TableViewModel;

import java.util.ArrayList;

public class TableRecViewAdapter extends RecyclerView.Adapter<TableRecViewAdapter.ViewHolder> {

    private ArrayList<String> tables = new ArrayList<>();
    private Context context;

    public void setTables(ArrayList<String> tables) {
        this.tables = tables;
    }

    public TableRecViewAdapter(Context context) {
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container, titleBar;
        ImageView down, download, up;
        TextView tableName, addTask;
        TableView table;

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

            TableViewModel tableViewModel = new TableViewModel();
            TableViewAdapter tableViewAdapter = new TableViewAdapter(tableViewModel);

            table.setAdapter(tableViewAdapter);
            table.setTableViewListener(new TableViewListener(table));
            tableViewAdapter.setAllItems(tableViewModel.getColumnHeaderList(), tableViewModel.getRowHeaderList(), tableViewModel.getCellList());
        }
    }
}
