package com.example.workmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.workmanagement.R;
import com.example.workmanagement.adapter.UserInvitedRecViewAdapter;
import com.example.workmanagement.adapter.UserSearchRecViewAdapter;
import com.example.workmanagement.databinding.ActivityOnBoardingBinding;
import com.example.workmanagement.utils.dto.SearchUserResponse;
import com.example.workmanagement.utils.services.impl.UserServiceImpl;
import com.example.workmanagement.viewmodels.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnBoardingActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private ActivityOnBoardingBinding binding;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/userinfo.profile"))
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {
            goSignOut();
        } else {
            binding.acctName.setText(getFirstWord(account.getDisplayName().toString()));
        }
        binding.btnCreate1st.setOnClickListener(v -> showCreateBoardDialog());
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
                            Toast.makeText(OnBoardingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                else adapter.setUsers(new ArrayList<>());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnCreateBoard.setOnClickListener(v -> {
            goTohome();
        });
        dialog.show();
    }
    private String getFirstWord(String text) {
        int index = text.indexOf(' ');
        if (index > -1) { // Check if there is more than one word.
            return text.substring(0, index).trim(); // Extract first word.
        } else {
            return text; // Text is the first word itself.
        }
    }
    private void goTohome() {

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    private void goSignOut() {
        gsc.signOut().addOnSuccessListener(unused -> {
            startActivity(new Intent(OnBoardingActivity.this, LoginActivity.class));
            finish();
        });
    }

}