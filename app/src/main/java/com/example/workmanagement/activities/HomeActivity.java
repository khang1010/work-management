package com.example.workmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.workmanagement.R;
import com.example.workmanagement.databinding.ActivityHomeBinding;
import com.example.workmanagement.fragments.ChatFragment;
import com.example.workmanagement.fragments.HomeFragment;
import com.example.workmanagement.fragments.SettingFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

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