package com.example.workmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workmanagement.R;
import com.example.workmanagement.utils.models.BoardItemList;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListBoardRecViewAdapter extends RecyclerView.Adapter<ListBoardRecViewAdapter.ViewHolder> {

    List<BoardItemList> l;
    Context context;

    public ListBoardRecViewAdapter(List<BoardItemList> l, Context context) {
        this.l = l;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_board, parent, false);
        ListBoardRecViewAdapter.ViewHolder holder = new ListBoardRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgBoard.setImageBitmap(l.get(position).getImg());
        holder.txtNameBoard.setText(l.get(position).getName());
        holder.txtNumberBoard.setText(l.get(position).getNum());
    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgBoard;

        private TextView txtNameBoard, txtNumberBoard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBoard = itemView.findViewById(R.id.imgBoard1);
            txtNameBoard = itemView.findViewById(R.id.txtBoardName);
            txtNumberBoard = itemView.findViewById(R.id.txtBoardNumber);
        }
    }
}

