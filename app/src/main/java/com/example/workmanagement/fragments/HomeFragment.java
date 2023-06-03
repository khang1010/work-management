package com.example.workmanagement.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workmanagement.R;
import com.example.workmanagement.activities.EditBoardActivity;
import com.example.workmanagement.activities.LabelActivity;
import com.example.workmanagement.activities.LoginActivity;
import com.example.workmanagement.adapter.UserInvitedRecViewAdapter;
import com.example.workmanagement.adapter.UserSearchRecViewAdapter;
import com.example.workmanagement.databinding.FragmentHomeBinding;
import com.example.workmanagement.utils.dto.BoardDetailsDTO;
import com.example.workmanagement.utils.dto.TableDTO;
import com.example.workmanagement.utils.dto.TableDetailsDTO;
import com.example.workmanagement.utils.dto.TaskDetailsDTO;
import com.example.workmanagement.utils.dto.UserInfoDTO;
import com.example.workmanagement.utils.services.impl.BoardServiceImpl;
import com.example.workmanagement.utils.services.impl.TableServiceImpl;
import com.example.workmanagement.viewmodels.BoardViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private BoardViewModel boardViewModel;

    private UserViewModel userViewModel;

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

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        if (account == null) goSignOut();
        binding.navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_table:
                    loadFragment(new TableFragment());
                    return true;
                case R.id.navigation_chart:
                    loadFragment(new ChartFragment());
                    return true;
            }
            return false;
        });
        binding.actionAddTable.setOnClickListener(v -> showCreateTableDialog());
        binding.actionEditBoard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditBoardActivity.class);
            intent.putExtra("BOARD_IDS", (ArrayList) userViewModel.getBoards().getValue()
                    .stream().map(b -> b.getId()).collect(Collectors.toList()));
            intent.putExtra("BOARD_ID", boardViewModel.getId().getValue());
            intent.putExtra("BOARD_NAME", boardViewModel.getName().getValue());
            intent.putExtra("BOARD_ADMIN", boardViewModel.getAdmin().getValue());
            intent.putExtra("BOARD_MEMBERS", (ArrayList) boardViewModel.getMembers().getValue());
            intent.putExtra("USER_ID", userViewModel.getId().getValue());
            intent.putExtra("TOKEN", userViewModel.getToken().getValue());
            List<Long> ids = new ArrayList<>();
            boardViewModel.getTables().getValue().forEach(t ->
                    ids.addAll(t.getMembers()
                            .stream().map(m -> m.getId())
                            .collect(Collectors.toList()))
            );
            intent.putExtra("IDS", (ArrayList) ids.stream().distinct().collect(Collectors.toList()));
            startActivity(intent);
        });
        binding.actionLabel.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LabelActivity.class);
            intent.putExtra("LABELS", (ArrayList) boardViewModel.getLabels().getValue());
            intent.putExtra("BOARD_ID", boardViewModel.getId().getValue());
            intent.putExtra("TOKEN", userViewModel.getToken().getValue());
            List<TaskDetailsDTO> tasks = new ArrayList<>();
            boardViewModel.getTables().getValue().forEach(t -> tasks.addAll(t.getTasks()));
            intent.putExtra("TASKS", (ArrayList) tasks);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boardViewModel = new ViewModelProvider(getActivity()).get(BoardViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        userViewModel.getCurrentBoardId().observe(getViewLifecycleOwner(), id -> {
            if (id > 0)
                BoardServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).getBoardDetails(id).enqueue(new Callback<BoardDetailsDTO>() {
                    @Override
                    public void onResponse(Call<BoardDetailsDTO> call, Response<BoardDetailsDTO> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            boardViewModel.setId(response.body().getId());
                            boardViewModel.setName(response.body().getName());
                            boardViewModel.setAdmin(response.body().getAdmin());
                            boardViewModel.setMembers(response.body().getMembers());
                            boardViewModel.setTables(response.body().getTables());
                            boardViewModel.setLabels(response.body().getLabels());
                        }
                    }

                    @Override
                    public void onFailure(Call<BoardDetailsDTO> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        });
    }

    private void showCreateTableDialog() {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_table);
        Window window = dialog.getWindow();
        if (window == null)
            return;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText txtSearchUser = dialog.findViewById(R.id.editTxtSearchUserTable);
        EditText txtTableName = dialog.findViewById(R.id.editTxtCreateTableName);
        EditText txtTableDesc = dialog.findViewById(R.id.editTxtCreateTableDesc);
        ConstraintLayout btnCreateTable = dialog.findViewById(R.id.btnCreateTable);

        UserInvitedRecViewAdapter invitedAdapter = new UserInvitedRecViewAdapter(getActivity());
        RecyclerView userInvitedRecView = dialog.findViewById(R.id.invitedUserRecView);
        userInvitedRecView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        userInvitedRecView.setAdapter(invitedAdapter);

        UserSearchRecViewAdapter adapter = new UserSearchRecViewAdapter(getActivity(), invitedAdapter);
        RecyclerView userRecView = dialog.findViewById(R.id.searchUserRecView);
        userRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userRecView.setAdapter(adapter);

        btnCreateTable.setOnClickListener(view -> {
            if (!txtTableName.getText().toString().trim().isEmpty() && !txtTableDesc.getText().toString().trim().isEmpty()) {
                List<TableDetailsDTO> tableDetailsDTOS = boardViewModel.getTables().getValue();
                TableDTO newTable = new TableDTO();
                newTable.setName(txtTableName.getText().toString());
                newTable.setDescription(txtTableDesc.getText().toString());
                newTable.setBoardId(boardViewModel.getId().getValue());
                newTable.setMemberIds(invitedAdapter.getUsers().stream().map(u -> u.getId()).collect(Collectors.toList()));
                if (userViewModel.getId().getValue() != boardViewModel.getAdmin().getValue().getId())
                    newTable.getMemberIds().add(boardViewModel.getAdmin().getValue().getId());
                TableServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).createTable(newTable)
                        .enqueue(new Callback<TableDetailsDTO>() {
                            @Override
                            public void onResponse(Call<TableDetailsDTO> call, Response<TableDetailsDTO> response) {
                                if (response.isSuccessful() && response.code() == 201) {
                                    tableDetailsDTOS.add(response.body());
                                    boardViewModel.setTables(tableDetailsDTOS);
                                    Toasty.success(getContext(), "Create table success!", Toast.LENGTH_SHORT, true).show();
                                    dialog.dismiss();
                                } else
                                    Toasty.error(getContext(), response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                            }

                            @Override
                            public void onFailure(Call<TableDetailsDTO> call, Throwable t) {
                                Toasty.error(getContext(), t.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }
                        });
            } else
                Toasty.warning(getContext(), "Please fill full information", Toast.LENGTH_SHORT, true).show();
        });

        txtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    List<UserInfoDTO> users = boardViewModel.getMembers().getValue();
                    adapter.setUsers(users.stream()
                            .filter(m -> m.getId() != userViewModel.getId().getValue()
                                    && (m.getDisplayName().trim().toLowerCase().contains(charSequence.toString().trim())
                                    || m.getEmail().trim().toLowerCase().contains(charSequence.toString().trim()))
                            )
                            .collect(Collectors.toList()));
                } else adapter.setUsers(new ArrayList<>());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void goSignOut() {
        gsc.signOut().addOnSuccessListener(unused -> startActivity(new Intent(getActivity(), LoginActivity.class)));
    }
}