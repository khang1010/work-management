package com.example.workmanagement.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workmanagement.R;
import com.example.workmanagement.activities.MessageActivity;
import com.example.workmanagement.adapter.BoardMessageRecViewAdapter;
import com.example.workmanagement.databinding.FragmentChartBinding;
import com.example.workmanagement.databinding.FragmentChatBinding;
import com.example.workmanagement.databinding.FragmentTableBinding;
import com.example.workmanagement.utils.dto.MessageDTO;
import com.example.workmanagement.utils.services.store.BoardMessages;
import com.example.workmanagement.utils.services.store.MessageStorage;
import com.example.workmanagement.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;

    private BoardMessageRecViewAdapter adapter;

    private UserViewModel userViewModel;

    public ChatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        adapter = new BoardMessageRecViewAdapter(getActivity(), userViewModel);
        binding.boardMessBoxRecView.setAdapter(adapter);
        binding.boardMessBoxRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userViewModel.getBoards().observe(getViewLifecycleOwner(), boards ->
                adapter.setBoards(MessageStorage.getInstance().getBoardMessages())
        );
        binding.editTxtSearchBoardMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty())
                    adapter.setBoards(userViewModel.getBoards().getValue()
                            .stream().filter(b -> b.getName().contains(charSequence))
                            .map(b -> new BoardMessages(b.getId(), b.getName()))
                            .collect(Collectors.toList())
                    );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
