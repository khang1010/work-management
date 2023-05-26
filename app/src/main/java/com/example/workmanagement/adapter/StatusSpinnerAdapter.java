package com.example.workmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.workmanagement.R;

import java.util.List;

public class StatusSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<String> status;

    public StatusSpinnerAdapter(Context context, List<String> status) {
        this.context = context;
        this.status = status;
    }

    @Override
    public int getCount() {
        return status.size();
    }

    @Override
    public Object getItem(int i) {
        return status.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.status_spinner_item, viewGroup, false);
        TextView item = view.findViewById(R.id.statusSpinnerItem);
        item.setText(status.get(i).charAt(0) + status.get(i).substring(1).toLowerCase());
        switch (status.get(i)) {
            case "DONE":
                item.setBackground(view.getResources().getDrawable(R.drawable.done_status_border));
                break;
            case "PENDING":
                item.setBackground(view.getResources().getDrawable(R.drawable.pending_status_border));
                break;
            case "STUCK":
                item.setBackground(view.getResources().getDrawable(R.drawable.stuck_status_border));
                break;
        }
        return view;
    }
}
