package com.example.workmanagement.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.NotificationDTO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationRecViewAdapter extends RecyclerView.Adapter<NotificationRecViewAdapter.ViewHolder> {

    private List<NotificationDTO> notifications;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout replyBtnLayout, acceptLayout, rejectLayout;

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
