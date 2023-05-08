package com.example.workmanagement.activities;

import android.os.Bundle;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.UserDTO;
import com.example.workmanagement.utils.services.impl.AuthServiceImpl;
import com.example.workmanagement.viewmodels.BoardViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.concurrent.atomic.AtomicInteger;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInforActivity extends AppCompatActivity {
    private Button btnBack;
    private CircleImageView avatar;
    private EditText bio;
    private EditText phone;
    private EditText work;
    private ImageView imgEditBio;
    private ImageView imgEditPhone;
    private ImageView imgEditWork;
    private TextView txtNothing, txtName;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(UserInforActivity.this).get(UserViewModel.class);

        setContentView(R.layout.activity_user_infor);
        btnBack = findViewById(R.id.btn_back);
        avatar = findViewById(R.id.img_avater_user_profiler);
        bio = findViewById(R.id.edit_text_bio);
        phone=findViewById(R.id.edit_text_phone);
        work=findViewById(R.id.edit_text_work);
        imgEditBio= findViewById(R.id.img_edit_bio);
        imgEditPhone=findViewById(R.id.img_edit_phone);
        imgEditWork=findViewById(R.id.img_edit_work);
        txtNothing = findViewById(R.id.txt_nothing);
        txtName = findViewById(R.id.txt_username_user_profiler);

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
            if (!String.valueOf(account.getPhotoUrl()).equals("null"))
                Glide.with(UserInforActivity.this)
                        .asBitmap()
                        .load(String.valueOf(account.getPhotoUrl()))
                        .into(avatar);
            else
                avatar.setImageResource(R.mipmap.ic_launcher);
        }

        txtNothing.setOnClickListener(v ->
                Toast.makeText(UserInforActivity.this, "Please enter board !!!", Toast.LENGTH_SHORT).show()
        );
        imgEditBio.setOnClickListener(v -> {
            if(bio.isEnabled())
            {
                bio.setEnabled(false);
            }
            else
            {
                bio.setEnabled(true);
            }
        });

        imgEditPhone.setOnClickListener(v -> {
            if(phone.isEnabled())
            {
                phone.setEnabled(false);
            }
            else
            {
                phone.setEnabled(true);
            }
        });
        imgEditWork.setOnClickListener(v -> {
            if(work.isEnabled())
            {
                work.setEnabled(false);
            }
            else
            {
                work.setEnabled(true);
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());

        avatar.setOnClickListener(v -> {

        });

    }
}
