package com.example.workmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.workmanagement.adapter.NotificationRecViewAdapter;
import com.example.workmanagement.databinding.ActivityNotificationBinding;
import com.example.workmanagement.utils.SystemConstant;
import com.example.workmanagement.utils.dto.NotificationDTO;
import com.example.workmanagement.utils.services.impl.NotificationServiceImpl;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;

    private NotificationRecViewAdapter adapter;

    private StompClient stompClient;

    private Moshi moshi = new Moshi.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String token = getIntent().getStringExtra("TOKEN");
        long id = getIntent().getLongExtra("ID", -1);
        initSocketConnection(token, id);
        adapter = new NotificationRecViewAdapter(this, token);
        binding.imgNotificationsBackToHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        binding.notificationsRecView.setLayoutManager(new LinearLayoutManager(this));
        binding.notificationsRecView.setAdapter(adapter);
        NotificationServiceImpl.getInstance().getService(token).getNotifications()
                .enqueue(new Callback<List<NotificationDTO>>() {
                    @Override
                    public void onResponse(Call<List<NotificationDTO>> call, Response<List<NotificationDTO>> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            NotificationServiceImpl.getInstance().getService(token).updateNotifications(
                                    response.body().stream()
                                            .filter(n -> !n.isRead())
                                            .map(n -> n.getId())
                                            .collect(Collectors.toList())).enqueue(new Callback<List<NotificationDTO>>() {
                                @Override
                                public void onResponse(Call<List<NotificationDTO>> call, Response<List<NotificationDTO>> response) {

                                }

                                @Override
                                public void onFailure(Call<List<NotificationDTO>> call, Throwable t) {
                                    Toast.makeText(NotificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            adapter.setNotifications(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NotificationDTO>> call, Throwable t) {
                        Toast.makeText(NotificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stompClient.disconnect();
    }

    private void initSocketConnection(String token, long id) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SystemConstant.BASE_URL + "ws/websocket");
        stompClient.connect();
        stompClient.topic("/notification/" + id)
                .subscribe(message ->
                        runOnUiThread(() -> {
                                    try {
                                        adapter.addNotification(moshi.adapter(NotificationDTO.class).fromJson(message.getPayload()));
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        )
                );
    }
}