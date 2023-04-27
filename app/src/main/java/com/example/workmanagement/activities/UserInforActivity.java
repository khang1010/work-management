package com.example.workmanagement.activities;

import android.os.Bundle;
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

import com.example.workmanagement.R;
import com.example.workmanagement.viewmodels.UserViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInforActivity extends AppCompatActivity {
    private Button btnBack;
    private CircleImageView avatar;
    private EditText bio;
    private EditText phone;
    private EditText work;
    private ImageView imgEditBio;
    private ImageView imgEditPhone;
    private ImageView imgEditWork;
    private TextView txtNothing;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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
