package com.example.workmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.MessageDTO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesRecViewAdapter extends RecyclerView.Adapter<MessagesRecViewAdapter.ViewHolder> {

    private String email;

    private List<MessageDTO> messages;

    private Context mContext;

    public MessagesRecViewAdapter(Context mContext, List<MessageDTO> messages, String email) {
        this.messages = messages;
        this.mContext = mContext;
        this.email = email;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessagesRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!messages.get(position).getPhotoUrl().equals("null"))
            Glide.with(mContext)
                    .asBitmap()
                    .load(messages.get(position).getPhotoUrl())
                    .into(holder.imgAvatar);
        else
            holder.imgAvatar.setImageResource(R.mipmap.ic_launcher);
        holder.txtMessage.setText(messages.get(position).getMessage());
        holder.txtYourMessage.setText(messages.get(position).getMessage());
        if (messages.get(position).getEmail().equals(email)) {
            holder.txtMessage.setVisibility(View.GONE);
            holder.imgAvatar.setVisibility(View.GONE);
        }
        else
            holder.txtYourMessage.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgAvatar;

        private TextView txtMessage, txtYourMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgMessageAvatar);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtYourMessage = itemView.findViewById(R.id.txtYourMessage);
        }
    }
}
