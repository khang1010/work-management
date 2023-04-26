package com.example.workmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.MessageDTO;

import java.util.ArrayList;
import java.util.List;

public class BoardMessageRecViewAdapter extends RecyclerView.Adapter<BoardMessageRecViewAdapter.ViewHolder> {

    private ArrayList<MessageDTO> messageDTOS = new ArrayList<>();

    private Context context;

    public BoardMessageRecViewAdapter() {
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_message_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.displayName.setText(messageDTOS.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return messageDTOS.size();
    }

    public void setMessageDTOS(ArrayList<MessageDTO> messageDTOS) {
        this.messageDTOS = messageDTOS;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView displayName;

        public ViewHolder (View itemView) {
            super (itemView);
            displayName = itemView.findViewById(R.id.displayName);

        }

    }
}
