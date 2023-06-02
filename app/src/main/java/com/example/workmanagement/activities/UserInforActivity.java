package com.example.workmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.adapter.ListBoardRecViewAdapter;
import com.example.workmanagement.utils.dto.BoardDTO;
import com.example.workmanagement.utils.dto.BoardDetailsDTO;
import com.example.workmanagement.utils.dto.TaskDetailsDTO;
import com.example.workmanagement.utils.dto.UserDTO;
import com.example.workmanagement.utils.models.BoardItemList;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInforActivity extends AppCompatActivity {
    private Button btnBack;
    private CircleImageView avatar;
    private TextView  txtName, txtEmail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_user_infor);
        btnBack = findViewById(R.id.btn_back);

        avatar = findViewById(R.id.img_avater_user_profiler);





        txtName = findViewById(R.id.txt_username_user_profiler);
        txtEmail = findViewById(R.id.txt_email_user_profiler);


//


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {

        } else {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(account.getEmail());
            userDTO.setDisplayName(account.getDisplayName());
            userDTO.setFamilyName(account.getFamilyName());
            userDTO.setGivenName(account.getGivenName());
            userDTO.setPhotoUrl(String.valueOf(account.getPhotoUrl()));

            txtName.setText(account.getDisplayName());
            txtEmail.setText(account.getEmail());

            if (!String.valueOf(account.getPhotoUrl()).equals("null"))
                Glide.with(UserInforActivity.this)
                        .asBitmap()
                        .load(String.valueOf(account.getPhotoUrl()))
                        .into(avatar);
            else
                avatar.setImageResource(R.mipmap.ic_launcher);





        }









        btnBack.setOnClickListener(v -> onBackPressed());




        avatar.setOnClickListener(v -> {

        });

    }
}
