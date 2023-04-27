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
import com.example.workmanagement.utils.dto.BoardInfo;
import com.example.workmanagement.utils.dto.MessageDTO;

import java.util.ArrayList;
import java.util.List;

public class BoardMessageRecViewAdapter extends RecyclerView.Adapter<BoardMessageRecViewAdapter.ViewHolder> {

    private List<BoardInfo> boards;

    private Context context;

    public Context getContext() {
        return context;
    }


    public BoardMessageRecViewAdapter(Context context) {
        if (boards == null)
            boards = new ArrayList<>();
        this.context = context;
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
        holder.displayName.setText(boards.get(position).getName());
        Glide.with(context).asBitmap().load("https://scontent.fsgn5-2.fna.fbcdn.net/v/t39.30808-6/341694474_496163519267495_2965195482123986755_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=CVH65JLugRkAX_yXeAC&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfA-t7f-Fu-h-KQQVxQFB8CCkMbsm4bIbXlh_HacakFiTA&oe=644EDA5E")
                        .into(holder.imageAvatar);
        holder.boardBox.setOnClickListener(view ->
                view.getContext().startActivity(new Intent(view.getContext(), MessageActivity.class)));
    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

    public void setBoards(List<BoardInfo> boards) {
        this.boards = boards;
        notifyDataSetChanged();
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
