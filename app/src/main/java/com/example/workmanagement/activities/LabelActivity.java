package com.example.workmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workmanagement.R;
import com.example.workmanagement.adapter.LabelsRecViewAdapter;
import com.example.workmanagement.databinding.ActivityLabelBinding;
import com.example.workmanagement.utils.dto.LabelDTO;
import com.example.workmanagement.utils.services.LabelService;
import com.example.workmanagement.utils.services.impl.LabelServiceImpl;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LabelActivity extends AppCompatActivity {

    private ActivityLabelBinding binding;

    private LabelsRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLabelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new LabelsRecViewAdapter(this);
        binding.labelsRecView.setAdapter(adapter);
        binding.labelsRecView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setLabels((List<LabelDTO>) getIntent().getSerializableExtra("LABELS"));
        long boardId = getIntent().getLongExtra("BOARD_ID", -1);
        String token = getIntent().getStringExtra("TOKEN");

        binding.imgEditBoardBackToHome.setOnClickListener(v -> onBackPressed());
        binding.btnCreateLabel.setOnClickListener(v -> showCreateLabelDialog(boardId, token));
    }

    private void showCreateLabelDialog(long boardId, String token) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_label);
        Window window = dialog.getWindow();
        if (window == null)
            return;
        //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LabelDTO dto = new LabelDTO();
        dto.setBoardId(boardId);
        dto.setColor("#00FF29");

        EditText txtLabelName = dialog.findViewById(R.id.editTxtCreateLabelName);
        TextView color = dialog.findViewById(R.id.labelColorPicker);
        color.setOnClickListener(view -> {
            new ColorPickerDialog.Builder(this)
                    .setTitle("ColorPicker Dialog")
                    .setPreferenceName("MyColorPickerDialog")
                    .setPositiveButton(getString(R.string.confirm), (ColorEnvelopeListener) (envelope, fromUser) -> {
                        dto.setColor("#" + envelope.getHexCode());
                        color.setBackgroundColor(envelope.getColor());
                    })
                    .show();
        });
        Button btnCreate = dialog.findViewById(R.id.btnCreateLabel);
        btnCreate.setOnClickListener(v -> {
            if (!txtLabelName.getText().toString().trim().isEmpty()) {
                dto.setName(txtLabelName.getText().toString());
                LabelServiceImpl.getInstance().getService(token).createLabel(dto).enqueue(new Callback<LabelDTO>() {
                    @Override
                    public void onResponse(Call<LabelDTO> call, Response<LabelDTO> response) {
                        if (response.isSuccessful() && response.code() == 201) {
                            adapter.addLabel(response.          body());
                            Toasty.success(LabelActivity.this, "Create label success!", Toast.LENGTH_SHORT, true).show();
                            dialog.dismiss();
                        } else Toasty.error(LabelActivity.this, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onFailure(Call<LabelDTO> call, Throwable t) {
                        Toasty.error(LabelActivity.this, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                });
            } else Toasty.warning(this, "Please fill full information", Toast.LENGTH_SHORT, true).show();
        });

        dialog.show();
    }
}