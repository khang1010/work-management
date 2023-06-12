package com.example.workmanagement.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.adapter.MessagesRecViewAdapter;
import com.example.workmanagement.databinding.ActivityMessageBinding;
import com.example.workmanagement.utils.SystemConstant;
import com.example.workmanagement.utils.dto.MessageDTO;
import com.example.workmanagement.utils.dto.NotificationDTO;
import com.example.workmanagement.utils.TextToImageHelper;
import com.example.workmanagement.utils.services.store.MessageStorage;
import com.squareup.moshi.Moshi;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class MessageActivity extends AppCompatActivity {

    private StompClient stompClient;

    private ActivityMessageBinding binding;

    private MessagesRecViewAdapter adapter;
    private CircleImageView imgBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        long userId = getIntent().getLongExtra("USER_ID", -1);
        String email = getIntent().getStringExtra("USER_EMAIL");
        String displayName = getIntent().getStringExtra("USER_NAME");
        String photoUrl = getIntent().getStringExtra("PHOTO_URL");
        String boardName = getIntent().getStringExtra("BOARD_NAME");
        long boardId = getIntent().getLongExtra("BOARD_ID", -1);
        List<Long> ids = (List<Long>) getIntent().getSerializableExtra("IDS");

        String first = String.valueOf(boardName.charAt(0));

        TextToImageHelper t = new TextToImageHelper();
        binding.imgBoard.setImageBitmap(t.generateImage(first, getIntent().getIntExtra("TEXT_COLOR", 255), getIntent().getIntExtra("BACKGROUND_COLOR", 255)));

        adapter = new MessagesRecViewAdapter(
                this,
                MessageStorage.getInstance().getBoardMessages()
                        .stream().filter(b -> b.getId() == boardId)
                        .findFirst().get().getMessages(),
                email);
        binding.messagesRecView.setAdapter(adapter);
        binding.messagesRecView.setLayoutManager(new LinearLayoutManager(this));
        binding.messageViewBoardName.setText(boardName);
        binding.messageViewBtnBack.setOnClickListener(v -> onBackPressed());
        binding.inputMessage.setOnClickListener(view -> {
            binding.messagesRecView.scrollToPosition(adapter.getItemCount() - 1);
        });

        initSocketConnection(userId, boardId, ids);

        binding.stickerIconMessageBoxBtn.setOnClickListener(v -> {
            if (!binding.inputMessage.getText().toString().isEmpty()) {
                MessageDTO message = new MessageDTO(boardId, boardName, email, displayName, photoUrl, binding.inputMessage.getText().toString());
                binding.inputMessage.setText("");
                stompClient.send("/app/message/" + boardId, new Moshi.Builder().build().adapter(MessageDTO.class).toJson(message)).subscribe();
                binding.messagesRecView.scrollToPosition(adapter.getItemCount() - 1);
            } else {
                MessageDTO message = new MessageDTO(boardId, boardName, email, displayName, photoUrl, "\uD83D\uDE03");
                binding.inputMessage.setText("");
                stompClient.send("/app/message/" + boardId, new Moshi.Builder().build().adapter(MessageDTO.class).toJson(message)).subscribe();
                binding.messagesRecView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
        binding.inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.inputMessage.length() == 0)
                    binding.stickerIconMessageBoxBtn.setBackground(getDrawable(R.drawable.ic_happy));
                else
                    binding.stickerIconMessageBoxBtn.setBackground(getDrawable(R.drawable.icon_send));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (stompClient != null)
            stompClient.disconnect();
    }

    private void initSocketConnection(long userId, long boardId, List<Long> ids) {
        if (stompClient != null)
            stompClient.disconnect();
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SystemConstant.BASE_URL + "ws/websocket");
        stompClient.connect();
        stompClient.topic("/notification/" + userId)
                .subscribe(message -> {
                    Moshi moshi = new Moshi.Builder().build();
                    createNotification(moshi.adapter(NotificationDTO.class).fromJson(message.getPayload()));
                });
        ids.forEach(i -> {
            //MessageStorage.getInstance().getBoardMessages().add(new BoardMessages(b.getId(), b.getName()));
            stompClient.topic("/chatroom/" + i)
                    .subscribe(message -> {
                        if (i != boardId)
                            createNotification(new Moshi.Builder().build().adapter(MessageDTO.class).fromJson(message.getPayload()));
                        MessageStorage.getInstance().addMessage(new Moshi.Builder().build().adapter(MessageDTO.class).fromJson(message.getPayload()));
                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                    });
        });
    }

    private void createNotification(MessageDTO message) throws ExecutionException, InterruptedException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MY_NOTIFICATION",
                    "My Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(this, BlankActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MY_NOTIFICATION")
                .setSmallIcon(R.mipmap.ic_logo)
                .setLargeIcon(Glide.with(MessageActivity.this).asBitmap().load(message.getPhotoUrl()).submit().get())
                .setStyle(new NotificationCompat.BigPictureStyle())
                .setContentTitle(message.getBoardName())
                .setContentText(message.getDisplayName() + ": " + message.getMessage())
                .setFullScreenIntent(null, true)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setTicker("Notification");
        builder.setContentIntent(contentIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(new Random().nextInt(), builder.build());
    }

    private void createNotification(NotificationDTO notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MY_NOTIFICATION",
                    "My Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(this, BlankActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MY_NOTIFICATION")
                .setSmallIcon(R.mipmap.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo3x))
                .setStyle(new NotificationCompat.BigPictureStyle())
                .setContentTitle("Notification")
                .setContentText(notification.getMessage())
                .setFullScreenIntent(null, true)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setTicker("Notification");
        builder.setContentIntent(contentIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(new Random().nextInt(), builder.build());
    }

}