package com.example.workmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.NotificationDTO;
import com.example.workmanagement.utils.services.impl.NotificationServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRecViewAdapter extends RecyclerView.Adapter<NotificationRecViewAdapter.ViewHolder> {

    private Context mContext;

    private String token;

    private List<NotificationDTO> notifications;

    public NotificationRecViewAdapter(Context mContext, String token) {
        if (notifications == null)
            notifications = new ArrayList<>();
        this.mContext = mContext;
        this.token = token;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public void addNotification(NotificationDTO notification) {
        this.notifications.add(0, notification);
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
                holder.acceptLayout.setVisibility(View.GONE);
                holder.rejectLayout.setVisibility(View.GONE);
            } else if (notifications.get(position).isAccept()) {
                holder.replyBtnLayout.setVisibility(View.GONE);
                holder.rejectLayout.setVisibility(View.GONE);
            } else if (notifications.get(position).isReject()) {
                holder.replyBtnLayout.setVisibility(View.GONE);
                holder.acceptLayout.setVisibility(View.GONE);
            }
        }
        holder.btnAccept.setOnClickListener(v -> NotificationServiceImpl.getInstance().getService(token)
                .acceptInvitation(notifications.get(position).getId())
                .enqueue(new Callback<NotificationDTO>() {
                    @Override
                    public void onResponse(Call<NotificationDTO> call, Response<NotificationDTO> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            int index = IntStream.range(0, notifications.size())
                                    .filter(i -> notifications.get(i).getId() == response.body().getId())
                                    .findFirst().orElse(-1);
                            notifications.set(index, response.body());
                            notifyItemChanged(index);
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationDTO> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
        holder.btnReject.setOnClickListener(v -> NotificationServiceImpl.getInstance().getService(token)
                .rejectInvitation(notifications.get(position).getId())
                .enqueue(new Callback<NotificationDTO>() {
                    @Override
                    public void onResponse(Call<NotificationDTO> call, Response<NotificationDTO> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            int index = IntStream.range(0, notifications.size())
                                    .filter(i -> notifications.get(i).getId() == response.body().getId())
                                    .findFirst().orElse(-1);
                            notifications.set(index, response.body());
                            notifyItemChanged(index);
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationDTO> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
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
