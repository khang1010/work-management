package com.example.workmanagement.adapter;

import static android.app.ProgressDialog.show;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.activities.MessageActivity;
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
        if (messageDTOS != null && messageDTOS.size() > position) {
            holder.displayName.setText(messageDTOS.get(position).getEmail());
            holder.displayMessage.setText(messageDTOS.get(position).getMessage());
        }
        if (context != null) {
            Glide.with(context)
                    .load(messageDTOS.get(position).getPhotoUrl())
                    .into(holder.imageAvatar);
        }

        holder.boardBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context != null) {
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    //intent.putExtra("displayName", messageDTOS.get(position).getEmail());
                    //intent.putExtra("displayMessage", messageDTOS.get(position).getMessage());
                    view.getContext().startActivity(intent);
                } else
                    Toast.makeText(view.getContext(), "bi gi a ma", Toast.LENGTH_SHORT).show();

            }
        });

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView displayName;
        private TextView displayMessage;
        private ImageView imageAvatar;
        private RelativeLayout boardBox;

        private ImageView notifyPoint;

        public ViewHolder(View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.displayName);
            displayMessage = itemView.findViewById(R.id.displayMessage);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
            notifyPoint = itemView.findViewById(R.id.notifyPoint);
            boardBox = itemView.findViewById(R.id.boardBox);

        }

    }
}
