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
import com.example.workmanagement.utils.services.store.BoardMessages;
import com.example.workmanagement.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardMessageRecViewAdapter extends RecyclerView.Adapter<BoardMessageRecViewAdapter.ViewHolder> {

    private List<BoardMessages> boards;

    private Context context;

    private UserViewModel userViewModel;


    public BoardMessageRecViewAdapter(Context context, UserViewModel userViewModel) {
        if (boards == null)
            boards = new ArrayList<>();
        this.context = context;
        this.userViewModel = userViewModel;
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
        Glide.with(context).asBitmap().load("https://scontent.fsgn5-14.fna.fbcdn.net/v/t39.30808-6/340746646_137283612616338_5424412554452308706_n.jpg?_nc_cat=101&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=We-EPxOzK3EAX-V5kmM&_nc_ht=scontent.fsgn5-14.fna&oh=00_AfB4daIlHoCLxyVbVcJUXKstp7HdJHRqyAG3_--8k1Vl1g&oe=645F7678")
                .into(holder.imageAvatar);
        holder.boardBox.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MessageActivity.class);
            intent.putExtra("USER_ID", userViewModel.getId().getValue());
            intent.putExtra("USER_EMAIL", userViewModel.getEmail().getValue());
            intent.putExtra("USER_NAME", userViewModel.getDisplayName().getValue());
            intent.putExtra("PHOTO_URL", userViewModel.getPhotoUrl().getValue());
            intent.putExtra("BOARD_ID", boards.get(position).getId());
            intent.putExtra("BOARD_NAME", boards.get(position).getName());
            intent.putExtra("IDS", (ArrayList) userViewModel.getBoards().getValue().stream().map(b -> b.getId()).collect(Collectors.toList()));
            view.getContext().startActivity(intent);
        });
        if (boards.get(position).getMessages().size() > 0)
            holder.displayMessage.setText(boards.get(position).getLatestMessage().getMessage());
    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

    public void setBoards(List<BoardMessages> boards) {
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
