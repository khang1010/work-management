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
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.workmanagement.R;
import com.example.workmanagement.adapter.UserInvitedRecViewAdapter;
import com.example.workmanagement.adapter.UserSearchRecViewAdapter;
import com.example.workmanagement.databinding.ActivityHomeBinding;
import com.example.workmanagement.fragments.ChatFragment;
import com.example.workmanagement.fragments.HomeFragment;
import com.example.workmanagement.fragments.SettingFragment;
import com.example.workmanagement.utils.SystemConstant;
import com.example.workmanagement.utils.dto.BoardDTO;
import com.example.workmanagement.utils.dto.BoardInfo;
import com.example.workmanagement.utils.dto.MessageDTO;
import com.example.workmanagement.utils.dto.NotificationDTO;
import com.example.workmanagement.utils.dto.SearchUserResponse;
import com.example.workmanagement.utils.dto.UserDTO;
import com.example.workmanagement.utils.services.impl.AuthServiceImpl;
import com.example.workmanagement.utils.services.impl.BoardServiceImpl;
import com.example.workmanagement.utils.services.impl.UserServiceImpl;
import com.example.workmanagement.utils.services.store.BoardMessages;
import com.example.workmanagement.utils.services.store.MessageStorage;
import com.example.workmanagement.viewmodels.BoardViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.squareup.moshi.Moshi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private UserViewModel userViewModel;

    private BoardViewModel boardViewModel;

    private StompClient stompClient;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.notificationPoint.setVisibility(View.GONE);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/userinfo.profile"))
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {
            goSignOut();
        } else {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(account.getEmail());
            userDTO.setDisplayName(account.getDisplayName());
            userDTO.setFamilyName(account.getFamilyName());
            userDTO.setGivenName(account.getGivenName());
            userDTO.setPhotoUrl(String.valueOf(account.getPhotoUrl()));

            AuthServiceImpl.getInstance().getService().loginUser(userDTO).enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        userViewModel.setId(response.body().getId());
                        userViewModel.setEmail(response.body().getEmail());
                        userViewModel.setDisplayName(response.body().getDisplayName());
                        userViewModel.setPhotoUrl(response.body().getPhotoUrl());
                        userViewModel.setToken(response.body().getToken());
                        userViewModel.setBoards(response.body().getBoards());
                        userViewModel.setHasNonReadNotification(response.body().isHasNonReadNotification());

                        SubMenu subMenu = binding.navigationView.getMenu().addSubMenu("Your boards");

                        userViewModel.getBoards().observe(HomeActivity.this, boards -> {
                            subMenu.clear();
                            AtomicInteger i = new AtomicInteger();
                            boards.forEach(b -> {
                                subMenu.add(b.getName());
                                subMenu.getItem(i.getAndIncrement()).setIcon(R.drawable.space_dashboard);
                            });
                            if (i.get() > 0) {
                                subMenu.getItem(0).setChecked(true);
                                boardViewModel = new ViewModelProvider(HomeActivity.this).get(BoardViewModel.class);
                                userViewModel.setCurrentBoardId(boards.get(0).getId());
                            }
                            //initSocketConnection("");
                        });

                        userViewModel.getCurrentBoardId().observe(HomeActivity.this, id -> initSocketConnection(""));

                        //userViewModel.getId().observe(HomeActivity.this, id -> initSocketConnection(""));

                        userViewModel.getPhotoUrl().observe(HomeActivity.this, photoUrl -> {
                            if (!photoUrl.equals("null"))
                                Glide.with(HomeActivity.this)
                                        .asBitmap()
                                        .load(photoUrl)
                                        .into(binding.imgAvatar);
                            else
                                binding.imgAvatar.setImageResource(R.mipmap.ic_launcher);
                        });
                        //userViewModel.getToken().observe(HomeActivity.this, token -> initSocketConnection(token));
                        userViewModel.getHasNonReadNotification().observe(HomeActivity.this, hasNonRead -> {
                            if (hasNonRead)
                                binding.notificationPoint.setVisibility(View.VISIBLE);
                            else
                                binding.notificationPoint.setVisibility(View.GONE);
                        });
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        binding.imgSideBar.setOnClickListener(v -> binding.drawableLayout.openDrawer(GravityCompat.START));

        binding.imgAvatar.setOnClickListener(v -> startActivity(new Intent(this, UserInforActivity.class)));

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            Menu menu = binding.navigationView.getMenu().getItem(0).getSubMenu();
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setChecked(false);
            item.setChecked(true);
            long id = userViewModel.getBoards().getValue().stream().filter(b -> b.getName() == item.getTitle())
                    .findFirst().get().getId();
            userViewModel.setCurrentBoardId(id);
            return false;
        });

        binding.bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.property_1_mess));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.property_1_home));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.property_1_setting));

        binding.bottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    break;
            }
            return null;
        });

        binding.bottomNavigation.setOnShowListener(model -> {
            switch (model.getId()) {
                case 1:
                    loadFragment(new ChatFragment());
                    break;
                case 2:
                    loadFragment(new HomeFragment());
                    break;
                case 3:
                    loadFragment(new SettingFragment());
                    break;
                default:
                    break;
            }
            return null;
        });
        binding.bottomNavigation.show(2, true);
        binding.navigationView.getHeaderView(0).findViewById(R.id.btnAddNewBoard)
                .setOnClickListener(v -> showCreateBoardDialog());
        binding.imgNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putExtra("TOKEN", userViewModel.getToken().getValue());
            intent.putExtra("ID", userViewModel.getId().getValue());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadUserData();
    }

    private void reloadUserData() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(account.getEmail());
        userDTO.setDisplayName(account.getDisplayName());
        userDTO.setFamilyName(account.getFamilyName());
        userDTO.setGivenName(account.getGivenName());
        userDTO.setPhotoUrl(String.valueOf(account.getPhotoUrl()));

        AuthServiceImpl.getInstance().getService().loginUser(userDTO).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    userViewModel.setId(response.body().getId());
                    userViewModel.setEmail(response.body().getEmail());
                    userViewModel.setDisplayName(response.body().getDisplayName());
                    userViewModel.setPhotoUrl(response.body().getPhotoUrl());
                    userViewModel.setToken(response.body().getToken());
                    userViewModel.setBoards(response.body().getBoards());
                    userViewModel.setHasNonReadNotification(response.body().isHasNonReadNotification());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (stompClient != null)
            stompClient.disconnect();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        binding.notificationPoint.setVisibility(View.GONE);
    }

    private void initSocketConnection(String token) {
        if (stompClient != null)
            stompClient.disconnect();
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SystemConstant.BASE_URL + "ws/websocket");
        stompClient.connect();
        stompClient.topic("/notification/" + userViewModel.getId().getValue())
                .subscribe(message -> {
                    Moshi moshi = new Moshi.Builder().build();
                    createNotification(moshi.adapter(NotificationDTO.class).fromJson(message.getPayload()));
                    runOnUiThread(() -> binding.notificationPoint.setVisibility(View.VISIBLE));
                });
        MessageStorage.getInstance().setBoardMessages(new ArrayList<>());
        userViewModel.getBoards().getValue().forEach(b -> {
            MessageStorage.getInstance().getBoardMessages().add(new BoardMessages(b.getId(), b.getName()));
            stompClient.topic("/chatroom/" + b.getId())
                    .subscribe(message -> {
                        createNotification(new Moshi.Builder().build().adapter(MessageDTO.class).fromJson(message.getPayload()));
                        MessageStorage.getInstance().addMessage(new Moshi.Builder().build().adapter(MessageDTO.class).fromJson(message.getPayload()));
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
                .setLargeIcon(Glide.with(HomeActivity.this).asBitmap().load(message.getPhotoUrl()).submit().get())
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

    private void showCreateBoardDialog() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_board);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText txtSearchUser = dialog.findViewById(R.id.editTxtSearchUser);
        EditText txtBoardName = dialog.findViewById(R.id.editTxtCreateBoardName);
        ConstraintLayout btnCreateBoard = dialog.findViewById(R.id.btnCreateBoard);


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
                    UserServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).searchUser(1, charSequence.toString()).enqueue(new Callback<SearchUserResponse>() {
                        @Override
                        public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                            if (response.isSuccessful() && response.code() == 200)
                                adapter.setUsers(response.body().getUsers());
                        }

                        @Override
                        public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                            Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                else adapter.setUsers(new ArrayList<>());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnCreateBoard.setOnClickListener(v -> {
            if (txtBoardName.getText().toString().trim().isEmpty())
                Toasty.warning(HomeActivity.this, "Please enter board name", Toast.LENGTH_SHORT, true).show();
            else
                BoardServiceImpl.getInstance().getService(userViewModel.getToken().getValue())
                        .createBoard(new BoardDTO(txtBoardName.getText().toString(),
                                invitedAdapter.getUsers().stream().map(u -> u.getId()).collect(Collectors.toList()))
                        )
                        .enqueue(new Callback<BoardInfo>() {
                            @Override
                            public void onResponse(Call<BoardInfo> call, Response<BoardInfo> response) {
                                if (response.isSuccessful() && response.code() == 201) {
                                    List<BoardInfo> boards = userViewModel.getBoards().getValue();
                                    boards.add(0, response.body());
                                    userViewModel.setBoards(boards);
                                    Toasty.success(HomeActivity.this, "Create board success!", Toast.LENGTH_SHORT, true).show();
                                    dialog.dismiss();
                                    //binding.drawableLayout.closeDrawer(GravityCompat.START);
                                } else
                                    Toasty.error(HomeActivity.this, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                            }

                            @Override
                            public void onFailure(Call<BoardInfo> call, Throwable t) {
                                Toasty.error(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }
                        });
        });
        dialog.show();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment, null)
                .commit();
    }

    private void goSignOut() {
        gsc.signOut().addOnSuccessListener(unused -> {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit App?");
        dialog.setMessage("Do you want to exit app?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }
}