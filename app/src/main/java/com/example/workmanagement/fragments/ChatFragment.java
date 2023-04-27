package com.example.workmanagement.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workmanagement.R;
import com.example.workmanagement.activities.MessageActivity;
import com.example.workmanagement.adapter.BoardMessageRecViewAdapter;
import com.example.workmanagement.utils.dto.MessageDTO;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    public ChatFragment() {
        // Required empty public constructor
    }

    private RecyclerView boardMessBoxRecView;
    private ArrayList<String> messBox = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //boardMessBoxRecView = boardMessBoxRecView.findViewById(R.id.boardMessBoxRecView);



    }
    BoardMessageRecViewAdapter adapter = new BoardMessageRecViewAdapter();
    ArrayList<MessageDTO> messageDTOS = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        boardMessBoxRecView = rootView.findViewById(R.id.boardMessBoxRecView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        boardMessBoxRecView.setLayoutManager(layoutManager);

        //ArrayList<MessageDTO> messageDTOS = new ArrayList<>();
        messageDTOS.add(new MessageDTO("ZangChuChe@gmail.com", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t39.30808-6/341694474_496163519267495_2965195482123986755_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=CVH65JLugRkAX_yXeAC&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfA-t7f-Fu-h-KQQVxQFB8CCkMbsm4bIbXlh_HacakFiTA&oe=644EDA5E", "Chao mung tro lai voi chanel cua minh"));
        messageDTOS.add(new MessageDTO("DuyDan@gmail.com", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t39.30808-6/341694474_496163519267495_2965195482123986755_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=CVH65JLugRkAX_yXeAC&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfA-t7f-Fu-h-KQQVxQFB8CCkMbsm4bIbXlh_HacakFiTA&oe=644EDA5E", "Toi la chu be dan"));
        messageDTOS.add(new MessageDTO("KhangDan@gmail.com", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t39.30808-6/341694474_496163519267495_2965195482123986755_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=CVH65JLugRkAX_yXeAC&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfA-t7f-Fu-h-KQQVxQFB8CCkMbsm4bIbXlh_HacakFiTA&oe=644EDA5E", "Zang cute qua"));
        messageDTOS.add(new MessageDTO("ZkKhai@gmail.com", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t39.30808-6/341694474_496163519267495_2965195482123986755_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=CVH65JLugRkAX_yXeAC&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfA-t7f-Fu-h-KQQVxQFB8CCkMbsm4bIbXlh_HacakFiTA&oe=644EDA5E", "iu ck"));
        messageDTOS.add(new MessageDTO("DuongDan@gmail.com", "https://scontent.fsgn5-2.fna.fbcdn.net/v/t39.30808-6/341694474_496163519267495_2965195482123986755_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=CVH65JLugRkAX_yXeAC&_nc_ht=scontent.fsgn5-2.fna&oh=00_AfA-t7f-Fu-h-KQQVxQFB8CCkMbsm4bIbXlh_HacakFiTA&oe=644EDA5E", "Toi khong suy"));

        //BoardMessageRecViewAdapter adapter = new BoardMessageRecViewAdapter();
        adapter.setMessageDTOS(messageDTOS);

        boardMessBoxRecView.setAdapter(adapter);
        boardMessBoxRecView.setLayoutManager(new LinearLayoutManager(getActivity()));


//        Intent intent = new Intent(getActivity(), MessageActivity.class);
//
//        // Add any necessary data to the Intent
//        intent.putExtra("message", "Messsage Screen");
//
//        // Start the MessageActivity
//        startActivity(intent);

        return rootView;
    }

    protected void loadData() {
        // Perform network request to get data
        // ...

        // Update adapter with data and context
        if (getContext() != null) {
            adapter.setMessageDTOS(messageDTOS, getContext());
        }
    }
}