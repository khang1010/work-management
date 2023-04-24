package com.example.workmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.NotificationDTO;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationRecViewAdapter extends RecyclerView.Adapter<NotificationRecViewAdapter.ViewHolder> {

    private Context mContext;

    private List<NotificationDTO> notifications;

    public NotificationRecViewAdapter(Context mContext) {
        if (notifications == null)
            notifications = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!notifications.get(position).getThumbnail().equals("null"))
            Glide.with(mContext)
                    .asBitmap()
                    .load(notifications.get(position).getThumbnail())
                    .into(holder.thumbnail);
        else
            holder.thumbnail.setImageResource(R.mipmap.ic_launcher);
        holder.txtNotificationMessage.setText(notifications.get(position).getMessage());
        if (notifications.get(position).getType().equals("INFORMATION")) {
            holder.replyBtnLayout.setVisibility(View.GONE);
            holder.acceptLayout.setVisibility(View.GONE);
            holder.rejectLayout.setVisibility(View.GONE);
        } else {
            if (!notifications.get(position).isAccept() && !notifications.get(position).isReject()) {
                System.out.println("btn");
                holder.acceptLayout.setVisibility(View.GONE);
                holder.rejectLayout.setVisibility(View.GONE);
            } else if (notifications.get(position).isAccept()) {
                System.out.println("accept");
                holder.replyBtnLayout.setVisibility(View.GONE);
                holder.rejectLayout.setVisibility(View.GONE);
            } else if (notifications.get(position).isReject()) {
                System.out.println("reject");
                holder.replyBtnLayout.setVisibility(View.GONE);
                holder.acceptLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout replyBtnLayout, acceptLayout, rejectLayout;

        private CircleImageView thumbnail;

        private TextView txtNotificationMessage, btnAccept, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            replyBtnLayout = itemView.findViewById(R.id.notificationReplyBtnLayout);
            acceptLayout = itemView.findViewById(R.id.acceptInvitationLayout);
            rejectLayout = itemView.findViewById(R.id.rejectInvitationLayout);
            thumbnail = itemView.findViewById(R.id.imgNotificationThumbnail);
            txtNotificationMessage = itemView.findViewById(R.id.txtNotificationMessage);
            btnAccept = itemView.findViewById(R.id.btnAcceptInvitation);
            btnReject = itemView.findViewById(R.id.btnRejectInvitation);
        }
    }
}
