package com.example.workmanagement.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.databinding.ActivityUserInforBinding;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInforBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityUserInforBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String token = getIntent().getStringExtra("TOKEN");
        String photoUrl = getIntent().getStringExtra("PHOTO_URL");
        String name = getIntent().getStringExtra("NAME");
        String email = getIntent().getStringExtra("EMAIL");

        binding.btnBack.setOnClickListener(v -> onBackPressed());

        if (photoUrl.equals("null"))
            binding.imgAvaterUserProfiler.setImageResource(R.drawable.user_default);
        else
            Glide.with(this).asBitmap().load(photoUrl).into(binding.imgAvaterUserProfiler);

        binding.txtEmailUserProfiler.setText(email);
        binding.txtUsernameUserProfiler.setText(name);
    }
}
