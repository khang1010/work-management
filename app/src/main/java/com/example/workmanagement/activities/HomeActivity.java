package com.example.workmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.workmanagement.R;
import com.example.workmanagement.databinding.ActivityHomeBinding;
import com.example.workmanagement.fragments.ChatFragment;
import com.example.workmanagement.fragments.HomeFragment;
import com.example.workmanagement.fragments.SettingFragment;
import com.example.workmanagement.utils.dto.UserDTO;
import com.example.workmanagement.utils.services.impl.AuthServiceImpl;
import com.example.workmanagement.viewmodels.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private User user;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                    if(response.isSuccessful() && response.code() == 200) {
                        Toast.makeText(HomeActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        user = new ViewModelProvider(HomeActivity.this).get(User.class);
                        user.setId(response.body().getId());
                        user.setEmail(response.body().getEmail());
                        user.setDisplayName(response.body().getDisplayName());
                        user.setPhotoUrl(response.body().getPhotoUrl());
                        user.setToken(response.body().getToken());
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        binding.logoutBtn.setOnClickListener(view -> {
            goSignOut();
        });

        binding.bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.property_1_mess));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.property_1_home));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.property_1_setting));

        binding.bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
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
            }
        });

        binding.bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {

            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment fragment = null;
                switch (model.getId()) {
                    case 1:
                        fragment = new ChatFragment();
                        break;
                    case 2:
                        fragment = new HomeFragment();
                        break;
                    case 3:
                        fragment = new SettingFragment();
                        break;
                    default:
                        break;
                }
                loadFragment(fragment);
                return null;
            }
        });
        binding.bottomNavigation.show(2, true);
        
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment, null)
                .commit();
    }
    private void goSignOut() {
        gsc.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();

            }
        });
    }
}