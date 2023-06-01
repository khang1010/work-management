package com.example.workmanagement.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.LabelDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class LabelsRecViewAdapter extends RecyclerView.Adapter<LabelsRecViewAdapter.ViewHolder> {

    private Context context;
    private List<LabelDTO> labels;

    public LabelsRecViewAdapter(Context context) {
        this.context = context;
        if (labels == null) labels = new ArrayList<>();
    }

    public void addLabel(LabelDTO dto) {
        labels.add(dto);
        notifyDataSetChanged();
    }

    public void removeLabel(long id) {
        labels = labels.stream().filter(l -> l.getId() != id).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public void setLabels(List<LabelDTO> labels) {
        this.labels = labels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_item, parent, false);
        return new LabelsRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.color.setBackgroundColor(Color.parseColor(labels.get(position).getColor()));
        holder.name.setText(labels.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView color;

        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.labelColor);
            name = itemView.findViewById(R.id.labelName);
        }
    }


}
