package com.example.workmanagement.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.LabelDTO;

import java.util.List;

public class LabelSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<LabelDTO> labels;

    public LabelSpinnerAdapter(Context context, List<LabelDTO> labels) {
        this.context = context;
        this.labels = labels;
    }

    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Object getItem(int i) {
        return labels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return labels.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.label_spinner_item, viewGroup, false);
        TextView item = view.findViewById(R.id.labelSpinnerItem);
        item.setText(labels.get(i).getName());
        item.setBackgroundColor(Color.parseColor(labels.get(i).getColor()));
        return view;
    }
}
