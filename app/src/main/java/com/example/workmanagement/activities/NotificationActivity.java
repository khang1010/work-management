package com.example.workmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.workmanagement.R;
import com.example.workmanagement.adapter.NotificationRecViewAdapter;
import com.example.workmanagement.databinding.ActivityNotificationBinding;
import com.example.workmanagement.utils.dto.NotificationDTO;
import com.example.workmanagement.utils.services.impl.NotificationServiceImpl;
import com.example.workmanagement.viewmodels.BoardViewModel;
import com.example.workmanagement.viewmodels.NotificationViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;

    private NotificationRecViewAdapter adapter;

    private NotificationViewModel notificationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        adapter = new NotificationRecViewAdapter(this);
        binding.imgNotificationsBackToHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        binding.notificationsRecView.setLayoutManager(new LinearLayoutManager(this));
        binding.notificationsRecView.setAdapter(adapter);
        String token = getIntent().getStringExtra("TOKEN");
        NotificationServiceImpl.getInstance().getService(token).getNotifications()
                .enqueue(new Callback<List<NotificationDTO>>() {
                    @Override
                    public void onResponse(Call<List<NotificationDTO>> call, Response<List<NotificationDTO>> response) {
                        if (response.isSuccessful() && response.code() == 200)
                            notificationViewModel.setNotifications(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<NotificationDTO>> call, Throwable t) {
                        Toast.makeText(NotificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        notificationViewModel.getNotifications().observe(this, notifications -> adapter.setNotifications(notifications));
    }
}