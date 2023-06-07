package com.example.workmanagement.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.workmanagement.R;
import com.example.workmanagement.activities.LoginActivity;
import com.example.workmanagement.activities.UserInforActivity;
import com.example.workmanagement.databinding.FragmentSettingBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SettingFragment extends Fragment {

    private Button btn_logout, btn_send_require, btn_share;

    private String MAIL_ADDRESS[];
    private String MAIL_TITLE;
    private GoogleSignInClient gsc;
    private GoogleSignInOptions gso;
    private View view;
    private FragmentSettingBinding biding;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setting, container, false);
        btn_logout = view.findViewById(R.id.btn_logout);
        btn_send_require = view.findViewById(R.id.btn_send_require);
        btn_share = view.findViewById(R.id.btn_share);
        MAIL_TITLE = getResources().getString(R.string.email_title);
        MAIL_ADDRESS = new String[1];
        MAIL_ADDRESS[0] = getActivity().getResources().getString(R.string.email_address);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/userinfo.profile"))
                .build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);
        btn_logout.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            signOut();
            getActivity().finish();

        });
        btn_send_require.setOnClickListener(v->{sendMail();});
        btn_share.setOnClickListener(v->{shareApp();});


        return view;
    }

    private void signOut() {
        gsc.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }

    private void sendMail(){
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, MAIL_ADDRESS);
            intent.putExtra(Intent.EXTRA_SUBJECT, MAIL_TITLE);

            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.send_email_error), Toast.LENGTH_SHORT).show();
            System.out.println("Err: cant create intent to send mail.");
        }

    }

    private void shareApp(){
        try{
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Download Work Management app");
            intent.putExtra(Intent.EXTRA_TEXT, "Download Work Management app\n\n https://workmanagement-app-web.vercel.app/");
            startActivity(Intent.createChooser(intent, "Share with"));
        }catch (Exception e){
            Toast.makeText(getActivity(), "Can't share app", Toast.LENGTH_SHORT).show();
        }
    }

}