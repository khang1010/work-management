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
import com.example.workmanagement.utils.dto.UserInfoDTO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BoardMembersRecViewAdapter extends RecyclerView.Adapter<BoardMembersRecViewAdapter.ViewHolder> {

    private Context mContext;

    private List<UserInfoDTO> members;

    public BoardMembersRecViewAdapter(Context mContext, List<UserInfoDTO> members) {
        this.mContext = mContext;
        this.members = members;
    }

    public void setMembers(List<UserInfoDTO> members) {
        this.members = members;
        notifyDataSetChanged();
    }

    public List<UserInfoDTO> getMembers() {
        return members;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_member_item, parent, false);
        return new BoardMembersRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!members.get(position).getPhotoUrl().equals("null"))
            Glide.with(mContext)
                    .asBitmap()
                    .load(members.get(position).getPhotoUrl())
                    .into(holder.imgAvatar);
        else
            holder.imgAvatar.setImageResource(R.mipmap.ic_launcher);
        holder.txtMemberName.setText(members.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgAvatar;

        private TextView txtMemberName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatarBoardMember);
            txtMemberName = itemView.findViewById(R.id.txtBoardMemberName);
        }
    }

}
