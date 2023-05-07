package com.example.workmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.UserInfoDTO;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSearchInTaskAdapter extends RecyclerView.Adapter<UserSearchInTaskAdapter.ViewHolder> {

    private List<UserInfoDTO> users;
    private UserInfoDTO user;

    private Context mContext;
    private boolean chosen;

    private UserInvitedRecViewAdapter invitedRecViewAdapter;

    public UserSearchInTaskAdapter(Context mContext, UserInvitedRecViewAdapter invitedRecViewAdapter) {
        if (users == null)
            users = new ArrayList<>();
        this.mContext = mContext;
        this.invitedRecViewAdapter = invitedRecViewAdapter;
        chosen = false;
    }

    public UserSearchInTaskAdapter(Context mContext) {
        if (users == null)
            users = new ArrayList<>();
        this.mContext = mContext;
        chosen = false;
    }

    public UserSearchInTaskAdapter(UserInfoDTO user, Context mContext) {
        if (users == null)
            users = new ArrayList<>();
        this.user = user;
        this.mContext = mContext;
        chosen = false;
    }

    public UserSearchInTaskAdapter(Context mContext, boolean chosen) {
        this.mContext = mContext;
        this.chosen = chosen;
    }

    public void setUsers(List<UserInfoDTO> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public UserInfoDTO getUser() {
        return user;
    }

    public void setUser(UserInfoDTO user) {
        this.user = user;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_search_item, parent, false);
        return new UserSearchInTaskAdapter.ViewHolder(view);
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
        if (user == null) {
            holder.cbxSelect.setChecked(false);
        } else {
            if (users != null && users.get(position).getId() == user.getId()) {
                holder.cbxSelect.setChecked(true);
            } else {
                holder.cbxSelect.setChecked(false);
            }
        }
        holder.cbxSelect.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                this.user = users.get(position);
                chosen = true;
                notifyDataSetChanged();
            } else {
                if (this.user.getId() == users.get(position).getId()) {
                    this.user = null;
                    chosen = false;
                    notifyDataSetChanged();
                }
            }
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
