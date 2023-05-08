package com.example.workmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.workmanagement.R;
import com.example.workmanagement.databinding.ActivityEditBoardBinding;
import com.example.workmanagement.databinding.ActivityMessageBinding;

public class MessageActivity extends AppCompatActivity {

    private ActivityMessageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.messageViewBtnBack.setOnClickListener(v -> onBackPressed());
    }
}