package com.example.workmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.activities.HomeActivity;
import com.example.workmanagement.utils.dto.UserInfoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSearchRecViewAdapter extends RecyclerView.Adapter<UserSearchRecViewAdapter.ViewHolder> {

    private List<UserInfoDTO> users;

    private Context mContext;

    private UserInvitedRecViewAdapter invitedRecViewAdapter;

    public UserSearchRecViewAdapter(Context mContext, UserInvitedRecViewAdapter invitedRecViewAdapter) {
        if (users == null)
            users = new ArrayList<>();
        this.mContext = mContext;
        this.invitedRecViewAdapter = invitedRecViewAdapter;
    }

    public void setUsers(List<UserInfoDTO> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_search_item, parent, false);
        return new UserSearchRecViewAdapter.ViewHolder(view);
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
        holder.txtEmail.setText(users.get(position).getEmail());
        if (invitedRecViewAdapter.getUsers().stream().anyMatch(u -> u.getId() == users.get(position).getId()))
            holder.cbxSelect.setChecked(true);
        else
            holder.cbxSelect.setChecked(false);
        holder.cbxSelect.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                invitedRecViewAdapter.addUser(users.get(position));
            else
                invitedRecViewAdapter.removeUser(users.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgAvatar;

        private TextView txtEmail;

        private CheckBox cbxSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatarSearchUser);
            txtEmail = itemView.findViewById(R.id.txtEmailSearchUser);
            cbxSelect = itemView.findViewById(R.id.cbxSearchUser);
        }
    }

}
