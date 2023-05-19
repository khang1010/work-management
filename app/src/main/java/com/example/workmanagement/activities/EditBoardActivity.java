package com.example.workmanagement.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.adapter.BoardMembersRecViewAdapter;
import com.example.workmanagement.adapter.EditBoardMembersRecAdapter;
import com.example.workmanagement.adapter.UserInvitedRecViewAdapter;
import com.example.workmanagement.adapter.UserSearchRecViewAdapter;
import com.example.workmanagement.databinding.ActivityEditBoardBinding;
import com.example.workmanagement.utils.SystemConstant;
import com.example.workmanagement.utils.dto.BoardDTO;
import com.example.workmanagement.utils.dto.BoardDetailsDTO;
import com.example.workmanagement.utils.dto.MessageDTO;
import com.example.workmanagement.utils.dto.NotificationDTO;
import com.example.workmanagement.utils.dto.SearchUserResponse;
import com.example.workmanagement.utils.dto.UserInfoDTO;
import com.example.workmanagement.utils.services.impl.BoardServiceImpl;
import com.example.workmanagement.utils.services.impl.UserServiceImpl;
import com.squareup.moshi.Moshi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class EditBoardActivity extends AppCompatActivity {

    private StompClient stompClient;

    private ActivityEditBoardBinding binding;

    private BoardMembersRecViewAdapter membersAdapter;

    private EditBoardMembersRecAdapter editMembersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String token = getIntent().getStringExtra("TOKEN");
        long boardId = getIntent().getLongExtra("BOARD_ID", -1);
        List<UserInfoDTO> members = (List<UserInfoDTO>) getIntent().getSerializableExtra("BOARD_MEMBERS");
        List<Long> memberIds = (List<Long>) getIntent().getSerializableExtra("IDS"); // members already in any table
        List<Long> boardIds = (List<Long>) getIntent().getSerializableExtra("BOARD_IDS");
        long userId = getIntent().getLongExtra("USER_ID", -1);
        initSocketConnection(userId, boardIds);

        membersAdapter = new BoardMembersRecViewAdapter(this, members);
        binding.membersRecView.setAdapter(membersAdapter);
        binding.membersRecView.setLayoutManager(new LinearLayoutManager(this));

        editMembersAdapter = new EditBoardMembersRecAdapter(this, members);
        binding.editMembersRecView.setAdapter(editMembersAdapter);
        binding.editMembersRecView.setLayoutManager(new LinearLayoutManager(this));
        binding.editMembersRecView.setVisibility(View.GONE);

        binding.txtBoardName.setText(getIntent().getStringExtra("BOARD_NAME"));
        UserInfoDTO admin = (UserInfoDTO) getIntent().getSerializableExtra("BOARD_ADMIN");
        Glide.with(this).asBitmap().load(admin.getPhotoUrl()).into(binding.imgAdminAvatar);
        binding.txtAdminName.setText(admin.getDisplayName());
        binding.imgEditBoardBackToHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));

        if (userId != admin.getId())
            binding.imgEditBoard.setVisibility(View.GONE);

        binding.imgEditBoard.setOnClickListener(v -> {
            binding.txtBoardName.setVisibility(View.GONE);
            binding.imgEditBoard.setVisibility(View.GONE);
            binding.btnDoneEditBoard.setVisibility(View.VISIBLE);
            binding.editTxtEditBoardName.setVisibility(View.VISIBLE);
            binding.btnDeleteBoard.setVisibility(View.VISIBLE);
            binding.editTxtEditBoardName.setText(binding.txtBoardName.getText());
            binding.membersRecView.setVisibility(View.GONE);
            binding.editMembersRecView.setVisibility(View.VISIBLE);
            binding.btnInviteMember.setVisibility(View.VISIBLE);
        });

        binding.btnDoneEditBoard.setOnClickListener(v -> {

            List<Long> ids = editMembersAdapter.getMemberIds(); // id of members after remove someone

            if (binding.txtBoardName.getText().toString().equals(binding.editTxtEditBoardName.getText().toString())
                    && ids.size() == members.size()) {
                binding.txtBoardName.setVisibility(View.VISIBLE);
                binding.imgEditBoard.setVisibility(View.VISIBLE);
                binding.btnDoneEditBoard.setVisibility(View.GONE);
                binding.editTxtEditBoardName.setVisibility(View.GONE);
                binding.btnDeleteBoard.setVisibility(View.GONE);
                binding.editMembersRecView.setVisibility(View.GONE);
                binding.btnInviteMember.setVisibility(View.GONE);
                binding.membersRecView.setVisibility(View.VISIBLE);
                membersAdapter.setMembers(members);
                editMembersAdapter.setMembers(members);
                return;
            }

            BoardDTO dto = new BoardDTO();
            if (!binding.editTxtEditBoardName.getText().toString().trim().isEmpty()
                    && !binding.txtBoardName.getText().toString().equals(binding.editTxtEditBoardName.getText().toString()))
                dto.setName(binding.editTxtEditBoardName.getText().toString());
            if (ids.size() < memberIds.size())
                if (members.stream().filter(m -> ids.stream().noneMatch(i -> i == m.getId()))
                        .collect(Collectors.toList()) // get removed members
                        .stream().anyMatch(m -> memberIds.stream().anyMatch(id -> id == m.getId())) // check if we remove any user in table
                ) {
                    Toast.makeText(this, "Please remove user from table first!", Toast.LENGTH_SHORT).show();
                    binding.txtBoardName.setVisibility(View.VISIBLE);
                    binding.imgEditBoard.setVisibility(View.VISIBLE);
                    binding.btnDoneEditBoard.setVisibility(View.GONE);
                    binding.editTxtEditBoardName.setVisibility(View.GONE);
                    binding.btnDeleteBoard.setVisibility(View.GONE);
                    binding.editMembersRecView.setVisibility(View.GONE);
                    binding.btnInviteMember.setVisibility(View.GONE);
                    binding.membersRecView.setVisibility(View.VISIBLE);
                    membersAdapter.setMembers(members);
                    editMembersAdapter.setMembers(members);
                    return;
                } else dto.setMembersIds(ids);

            BoardServiceImpl.getInstance().getService(token).updateBoard(boardId, dto)
                    .enqueue(new Callback<BoardDetailsDTO>() {
                        @Override
                        public void onResponse(Call<BoardDetailsDTO> call, Response<BoardDetailsDTO> response) {
                            if (response.isSuccessful() && response.code() == 200) {
                                binding.txtBoardName.setText(response.body().getName());
                                binding.txtBoardName.setVisibility(View.VISIBLE);
                                binding.imgEditBoard.setVisibility(View.VISIBLE);
                                binding.btnDoneEditBoard.setVisibility(View.GONE);
                                binding.editTxtEditBoardName.setVisibility(View.GONE);
                                binding.btnDeleteBoard.setVisibility(View.GONE);
                                binding.editMembersRecView.setVisibility(View.GONE);
                                binding.btnInviteMember.setVisibility(View.GONE);
                                binding.membersRecView.setVisibility(View.VISIBLE);
                                membersAdapter.setMembers(members.stream()
                                        .filter(m -> ids.stream().anyMatch(i -> i == m.getId())).collect(Collectors.toList())
                                );
                                editMembersAdapter.setMembers(members.stream()
                                        .filter(m -> ids.stream().anyMatch(i -> i == m.getId())).collect(Collectors.toList())
                                );
                            }
                        }

                        @Override
                        public void onFailure(Call<BoardDetailsDTO> call, Throwable t) {
                            Toast.makeText(EditBoardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        binding.btnDeleteBoard.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this board ?")
                    .setPositiveButton("Yes", (dialogInterface, i) ->
                            BoardServiceImpl.getInstance().getService(token).deleteBoard(boardId)
                                    .enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.isSuccessful() && response.code() == 200)
                                                startActivity(new Intent(EditBoardActivity.this, HomeActivity.class));
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(EditBoardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                    )
                    .setNegativeButton("No", (dialogInterface, i) -> {

                    });
            builder.create().show();
        });

        binding.btnInviteMember.setOnClickListener(v -> showInviteUserDialog(token, boardId, userId, binding.txtBoardName.getText().toString()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stompClient.disconnect();
    }

    private void initSocketConnection(long userId, List<Long> ids) {
        if (stompClient == null || !stompClient.isConnected()) {
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SystemConstant.BASE_URL + "ws/websocket");
            stompClient.connect();
            stompClient.topic("/notification/" + userId)
                    .subscribe(message -> {
                        Moshi moshi = new Moshi.Builder().build();
                        createNotification(moshi.adapter(NotificationDTO.class).fromJson(message.getPayload()));
                    });
            ids.forEach(i ->
                    stompClient.topic("/chatroom/" + i)
                            .subscribe(message ->
                                    createNotification(new Moshi.Builder().build().adapter(MessageDTO.class).fromJson(message.getPayload()))
                            )
            );
        }
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
                .setLargeIcon(Glide.with(EditBoardActivity.this).asBitmap().load(message.getPhotoUrl()).submit().get())
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

    private void showInviteUserDialog(String token, long boardId, long userId, String boardName) {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_board);
        EditText txtSearchUser = dialog.findViewById(R.id.editTxtSearchUser);
        EditText txtBoardName = dialog.findViewById(R.id.editTxtCreateBoardName);
        txtBoardName.setText(boardName);
        ConstraintLayout btnCreateBoard = dialog.findViewById(R.id.btnCreateBoard);
        TextView txtBtn = btnCreateBoard.findViewById(R.id.textView2);
        txtBtn.setText("Invite");

        UserInvitedRecViewAdapter invitedAdapter = new UserInvitedRecViewAdapter(this);
        RecyclerView userInvitedRecView = dialog.findViewById(R.id.invitedUserRecView);
        userInvitedRecView.setLayoutManager(new GridLayoutManager(this, 5));
        userInvitedRecView.setAdapter(invitedAdapter);

        UserSearchRecViewAdapter adapter = new UserSearchRecViewAdapter(this, invitedAdapter);
        RecyclerView userRecView = dialog.findViewById(R.id.searchUserRecView);
        userRecView.setLayoutManager(new LinearLayoutManager(this));
        userRecView.setAdapter(adapter);

        txtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty())
                    UserServiceImpl.getInstance().getService(token).searchUser(1, charSequence.toString()).enqueue(new Callback<SearchUserResponse>() {
                        @Override
                        public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                            if (response.isSuccessful() && response.code() == 200)
                                adapter.setUsers(response.body().getUsers()
                                        .stream().filter(u ->
                                                membersAdapter.getMembers().stream().noneMatch(m -> m.getId() == u.getId())
                                                        && u.getId() != userId
                                        ).collect(Collectors.toList())
                                );
                        }

                        @Override
                        public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                            Toast.makeText(EditBoardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                else adapter.setUsers(new ArrayList<>());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnCreateBoard.setOnClickListener(v -> {
            BoardDTO dto = new BoardDTO();
            dto.setMembersIds(invitedAdapter.getUsers().stream().map(u -> u.getId()).collect(Collectors.toList()));
            BoardServiceImpl.getInstance().getService(token)
                    .updateBoard(boardId, dto)
                    .enqueue(new Callback<BoardDetailsDTO>() {
                        @Override
                        public void onResponse(Call<BoardDetailsDTO> call, Response<BoardDetailsDTO> response) {
                            if (response.isSuccessful() && response.code() == 200)
                                Toast.makeText(EditBoardActivity.this, "Invite successful", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(EditBoardActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<BoardDetailsDTO> call, Throwable t) {
                            Toast.makeText(EditBoardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
        });
        dialog.show();
    }
}