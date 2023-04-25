package com.example.workmanagement.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.workmanagement.utils.dto.SearchUserResponse;
import com.example.workmanagement.utils.dto.UserDTO;
import com.example.workmanagement.utils.services.impl.AuthServiceImpl;
import com.example.workmanagement.utils.services.impl.UserServiceImpl;
import com.example.workmanagement.viewmodels.BoardViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
        binding.logoutBtn.setVisibility(View.GONE);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
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
                        });

                        userViewModel.getPhotoUrl().observe(HomeActivity.this, photoUrl -> Glide.with(HomeActivity.this)
                                .asBitmap()
                                .load(photoUrl)
                                .into(binding.imgAvatar)
                        );
                        userViewModel.getToken().observe(HomeActivity.this, token -> initSocketConnection(token));
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        binding.logoutBtn.setOnClickListener(view -> goSignOut());

        binding.imgSideBar.setOnClickListener(v -> binding.drawableLayout.openDrawer(GravityCompat.START));

        binding.imgAvatar.setOnClickListener(v ->
            startActivity(new Intent(this, UserInforActivity.class))
        );

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            Menu menu = binding.navigationView.getMenu().getItem(0).getSubMenu();
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setChecked(false);
            item.setChecked(true);
            long id = userViewModel.getBoards().getValue().stream().filter(b -> b.getName() == item.getTitle()).findFirst().get().getId();
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
    protected void onStop() {
        super.onStop();
        try {
            stompClient.disconnect();
        }catch (Exception e){

        }

    }

    private void initSocketConnection(String token) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SystemConstant.BASE_URL + "ws/websocket");
        stompClient.connect();
        stompClient.topic("/notification/" + userViewModel.getId().getValue())
                .subscribe(message ->
                        runOnUiThread(() -> Toast.makeText(this, message.getPayload(), Toast.LENGTH_SHORT).show())
                );
    }

    private void showCreateBoardDialog() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_board);
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
//            if (txtBoardName.getText().toString().isEmpty())
//                Toast.makeText(this, "Please enter board name", Toast.LENGTH_SHORT).show();
//            else
//                BoardServiceImpl.getInstance().getService(userViewModel.getToken().getValue())
//                        .createBoard(new BoardDTO(txtBoardName.getText().toString(),
//                                invitedAdapter.getUsers().stream().map(u -> u.getId()).collect(Collectors.toList()))
//                        )
//                        .enqueue(new Callback<BoardInfo>() {
//                            @Override
//                            public void onResponse(Call<BoardInfo> call, Response<BoardInfo> response) {
//                                if (response.isSuccessful() && response.code() == 201) {
//                                    List<BoardInfo> boards = userViewModel.getBoards().getValue();
//                                    boards.add(0, response.body());
//                                    userViewModel.setBoards(boards);
//                                    dialog.dismiss();
//                                    //binding.drawableLayout.closeDrawer(GravityCompat.START);
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<BoardInfo> call, Throwable t) {
//                                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
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
}