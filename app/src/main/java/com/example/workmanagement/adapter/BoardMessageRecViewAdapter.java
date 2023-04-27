package com.example.workmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.MessageDTO;

import java.util.ArrayList;
import java.util.List;

public class BoardMessageRecViewAdapter extends RecyclerView.Adapter<BoardMessageRecViewAdapter.ViewHolder> {

    private ArrayList<MessageDTO> messageDTOS = new ArrayList<>();

    private Context context;
    public Context getContext() {
        return context;
    }


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
        holder.displayMessage.setText(messageDTOS.get(position).getMessage());
        if (context != null) {
        Glide.with(context)
                .load(messageDTOS.get(position).getPhotoUrl())
                .into(holder.imageAvatar);}
    }

    @Override
    public int getItemCount() {
        return messageDTOS.size();
    }

    public void setMessageDTOS(ArrayList<MessageDTO> messageDTOS) {
        this.messageDTOS = messageDTOS;
        this.context = context;
        notifyDataSetChanged();
    }

    public void setMessageDTOS(ArrayList<MessageDTO> messageDTOS, Context context) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView displayName;
        private TextView displayMessage;
        private ImageView imageAvatar;


        public ViewHolder (View itemView) {
            super (itemView);
            displayName = itemView.findViewById(R.id.displayName);
            displayMessage = itemView.findViewById(R.id.displayMessage);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);

        }

    }
}
