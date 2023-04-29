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

public class EditBoardMembersRecAdapter extends RecyclerView.Adapter<EditBoardMembersRecAdapter.ViewHolder> {

    private Context mContext;

    private List<UserInfoDTO> members;

    private List<Boolean> isSelect;

    public void setMembers(List<UserInfoDTO> members) {
        this.members = members;
        isSelect = new ArrayList<>(members.size());
        members.forEach(m -> isSelect.add(true));
        notifyDataSetChanged();
    }

    public EditBoardMembersRecAdapter(Context mContext, List<UserInfoDTO> members) {
        this.mContext = mContext;
        this.members = members;
        isSelect = new ArrayList<>();
        members.forEach(m -> isSelect.add(true));
    }

    public List<Long> getMemberIds() {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < members.size(); i++)
            if (isSelect.get(i))
                ids.add(members.get(i).getId());
        return ids;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_search_item, parent, false);
        return new EditBoardMembersRecAdapter.ViewHolder(view);
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
        holder.txtEmail.setText(members.get(position).getDisplayName());
        holder.cbxSelect.setChecked(true);
        holder.cbxSelect.setOnCheckedChangeListener((compoundButton, b) -> isSelect.set(position, b));
    }

    @Override
    public int getItemCount() {
        return members.size();
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
