package com.example.workmanagement.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.workmanagement.R;
import com.example.workmanagement.activities.HomeActivity;
import com.example.workmanagement.activities.LoginActivity;
import com.example.workmanagement.databinding.ActivityHomeBinding;
import com.example.workmanagement.databinding.FragmentHomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFragment(new TableFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        View view = binding.getRoot();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(getActivity(), gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

        if (account == null) {
            goSignOut();
        }
        binding.logoutBtn.setOnClickListener(view1 -> {
            goSignOut();
        });
        binding.logoutBtn.setVisibility(View.GONE);

        binding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_table:
                        loadFragment(new TableFragment());
                        return true;
                    case R.id.navigation_chart:
                        loadFragment(new ChartFragment());
                        return true;
                }
                return false;
            }
        });

//        return inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void goSignOut() {
        gsc.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }
}