package com.example.workmanagement.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.workmanagement.adapter.BoardMembersRecViewAdapter;
import com.example.workmanagement.adapter.EditBoardMembersRecAdapter;
import com.example.workmanagement.databinding.ActivityEditBoardBinding;
import com.example.workmanagement.utils.dto.UserInfoDTO;

import java.util.List;
import java.util.stream.Collectors;

public class EditBoardActivity extends AppCompatActivity {

    private ActivityEditBoardBinding binding;

    private BoardMembersRecViewAdapter membersAdapter;

    private EditBoardMembersRecAdapter editMembersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<UserInfoDTO> members = (List<UserInfoDTO>) getIntent().getSerializableExtra("BOARD_MEMBERS");
        List<Long> memberIds = (List<Long>) getIntent().getSerializableExtra("IDS"); // members already in any table

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
        binding.imgEditBoardBackToHome.setOnClickListener(v -> onBackPressed());

        if (getIntent().getLongExtra("USER_ID", -1) != admin.getId())
            binding.imgEditBoard.setVisibility(View.GONE);

        binding.imgEditBoard.setOnClickListener(v -> {
            binding.txtBoardName.setVisibility(View.GONE);
            binding.imgEditBoard.setVisibility(View.GONE);
            binding.btnDoneEditBoard.setVisibility(View.VISIBLE);
            binding.editTxtEditBoardName.setVisibility(View.VISIBLE);
            binding.editTxtEditBoardName.setText(binding.txtBoardName.getText());
            binding.membersRecView.setVisibility(View.GONE);
            binding.editMembersRecView.setVisibility(View.VISIBLE);
            binding.btnInviteMember.setVisibility(View.VISIBLE);
        });

        binding.btnDoneEditBoard.setOnClickListener(v -> {
            binding.txtBoardName.setVisibility(View.VISIBLE);
            binding.imgEditBoard.setVisibility(View.VISIBLE);
            binding.btnDoneEditBoard.setVisibility(View.GONE);
            binding.editTxtEditBoardName.setVisibility(View.GONE);
            List<Long> ids = editMembersAdapter.getMemberIds(); // id of members after remove someone
            binding.editMembersRecView.setVisibility(View.GONE);
            binding.btnInviteMember.setVisibility(View.GONE);
            binding.membersRecView.setVisibility(View.VISIBLE);
            if (ids.size() != members.size())
                if (members.stream().filter(m -> ids.stream().noneMatch(i -> i == m.getId()))
                        .collect(Collectors.toList()) // get removed members
                        .stream().anyMatch(m -> memberIds.stream().anyMatch(id -> id == m.getId())) // check if we remove any user in table
                ) {
                    Toast.makeText(this, "Please remove user from table first!", Toast.LENGTH_SHORT).show();
                    membersAdapter.setMembers(members);
                    editMembersAdapter.setMembers(members);
                } else {
                    membersAdapter.setMembers(members.stream()
                            .filter(m -> ids.stream().anyMatch(i -> i == m.getId())).collect(Collectors.toList())
                    );
                    editMembersAdapter.setMembers(members.stream()
                            .filter(m -> ids.stream().anyMatch(i -> i == m.getId())).collect(Collectors.toList())
                    );
                }
        });
    }
}