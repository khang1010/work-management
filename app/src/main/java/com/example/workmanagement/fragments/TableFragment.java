package com.example.workmanagement.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.example.workmanagement.R;
import com.example.workmanagement.adapter.TableRecViewAdapter;
import com.example.workmanagement.databinding.FragmentTableBinding;
import com.example.workmanagement.utils.dto.TaskDetailsDTO;
import com.example.workmanagement.utils.dto.UserDTO;
import com.example.workmanagement.utils.services.impl.AuthServiceImpl;
import com.example.workmanagement.viewmodels.BoardViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableFragment extends Fragment {

    private FragmentTableBinding binding;

    private BoardViewModel boardViewModel;

    private RecyclerView tableRecView;
    private RelativeLayout clockLayout;
    private Button btnAddTable;
    private UserViewModel userViewModel;

    public TableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TableView mTableView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTableBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        boardViewModel = new ViewModelProvider(requireActivity()).get(BoardViewModel.class);
        boardViewModel.getTables().observe(getViewLifecycleOwner(), tables -> {
            List<List<TaskDetailsDTO>> tasks = tables.stream().map(t -> t.getTasks()).collect(Collectors.toList());
            List<TaskDetailsDTO> myTasks = new ArrayList<>();
            tasks.forEach(task -> myTasks.addAll(task.stream().filter(t -> t.getUser().getId() == userViewModel.getId().getValue()).collect(Collectors.toList())));
            int count = 0;
            Date today = new Date();
            count = (int) myTasks.stream().filter(t -> {
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(t.getDateAttributes().stream().filter(atr -> atr.getName().equals("deadline")).findFirst().get().getValue());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (date.getYear() == today.getYear() && date.getMonth() == today.getMonth() && date.getDate() == today.getDate())
                    return true;
                return false;
            }).count();
            binding.txtClockImage.setText("You have " + count + " task need to complete today");
            new Handler().postDelayed(() -> {
                tableRecView = view.findViewById(R.id.table_rec_view);
                TableRecViewAdapter adapter = new TableRecViewAdapter(getActivity(), userViewModel, boardViewModel);
                tableRecView.setAdapter(adapter);
                tableRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter.setTables(tables);
                clockLayout = view.findViewById(R.id.clock_container);
                clockLayout.setVisibility(View.GONE);
            }, 500);
        });
        binding.refreshLayout.setOnRefreshListener(() -> reloadUserData());
    }

    private void reloadUserData() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
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
                binding.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toasty.error(getContext(), t.getMessage(), Toast.LENGTH_SHORT, true).show();
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }
}