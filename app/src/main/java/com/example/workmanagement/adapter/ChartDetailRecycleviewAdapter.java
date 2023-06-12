package com.example.workmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workmanagement.R;
import com.example.workmanagement.utils.models.ChartDetailItem;

import java.util.List;

public class ChartDetailRecycleviewAdapter extends RecyclerView.Adapter<ChartDetailRecycleviewAdapter.ViewHolder> {

    private List<ChartDetailItem> mData;
    private LayoutInflater mInflater;

    public ChartDetailRecycleviewAdapter(List<ChartDetailItem> mData, LayoutInflater mInflater) {
        this.mData = mData;
        this.mInflater = mInflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.chart_detail_recycleview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id.setText(mData.get(position).getId());
        holder.name.setText(mData.get(position).getName());
        holder.number.setText(mData.get(position).getNumber_tasks());

        holder.id.setTextColor(mData.get(position).getColor());
        holder.name.setTextColor(mData.get(position).getColor());
        holder.number.setTextColor(mData.get(position).getColor());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id;
        TextView name;
        TextView number;


        ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id_chart_detail);
            name = itemView.findViewById(R.id.user_name_chart_detail);
            number = itemView.findViewById(R.id.number_task_chart_detail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}

