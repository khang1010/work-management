package com.example.workmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.UserInfoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInvitedRecViewAdapter extends RecyclerView.Adapter<UserInvitedRecViewAdapter.ViewHolder> {

    private List<UserInfoDTO> users;

    private Context mContext;

    public UserInvitedRecViewAdapter(Context mContext) {
        if (users == null)
            users = new ArrayList<>();
        this.mContext = mContext;
    }

    public List<UserInfoDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoDTO> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void addUser(UserInfoDTO user) {
        if (users.stream().noneMatch(u -> u.getId() == user.getId()))
            this.users.add(user);
        notifyDataSetChanged();
    }

    public void removeUser(UserInfoDTO user) {
        this.users = this.users.stream().filter(u -> u.getId() != user.getId()).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_invited_item, parent, false);
        return new UserInvitedRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!users.get(position).getPhotoUrl().equals("null"))
            Glide.with(mContext)
                    .asBitmap()
                    .load(users.get(position).getPhotoUrl())
                    .into(holder.imgAvatar);
        else
            holder.imgAvatar.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatarInvitedUser);
        }
    }
}
