package com.example.workmanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workmanagement.R;
import com.example.workmanagement.adapter.LabelsRecViewAdapter;
import com.example.workmanagement.databinding.ActivityLabelBinding;
import com.example.workmanagement.utils.dto.LabelDTO;
import com.example.workmanagement.utils.dto.TaskDetailsDTO;
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

        long boardId = getIntent().getLongExtra("BOARD_ID", -1);
        String token = getIntent().getStringExtra("TOKEN");
        List<TaskDetailsDTO> tasks = (List<TaskDetailsDTO>) getIntent().getSerializableExtra("TASKS");

        binding.imgEditBoardBackToHome.setOnClickListener(v -> onBackPressed());
        binding.btnCreateLabel.setOnClickListener(v -> showCreateLabelDialog(boardId, token));

        adapter = new LabelsRecViewAdapter(this, token);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Toast.makeText(LabelActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                long labelId = adapter.getLabels().get(viewHolder.getAdapterPosition()).getId();
                if (tasks.stream().anyMatch(t ->
                        t.getLabelAttributes().stream().filter(atr -> atr.getName().equals("label")).findFirst().isPresent()
                        && t.getLabelAttributes().stream().filter(atr -> atr.getName().equals("label")).findFirst().get().getLabelId() == labelId)
                ) {
                    Toasty.warning(LabelActivity.this, "Please remove label from task first", Toast.LENGTH_SHORT, true).show();
                    adapter.notifyDataSetChanged();
                } else {
                    LabelServiceImpl.getInstance().getService(token).deleteLabel(labelId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful() && response.code() == 200) {
                                adapter.removeLabel(labelId);
                                Toasty.success(LabelActivity.this, "Delete label success!", Toast.LENGTH_SHORT, true).show();
                            } else
                                Toasty.error(LabelActivity.this, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toasty.error(LabelActivity.this, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                    });
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                float newDx = dX;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Get RecyclerView item from the ViewHolder
                    View itemView = viewHolder.itemView;

                    Paint p = new Paint();
                    int color = ContextCompat.getColor(LabelActivity.this, R.color.red);
                    p.setColor(color);
                    if (dX > 0) {
                        /* Set your color for positive displacement */

                        // Draw Rect with varying right side, equal to displacement dX
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom(), p);
                        Drawable drawable = ContextCompat.getDrawable(LabelActivity.this, R.drawable.trash);
                        drawable.setTint(ContextCompat.getColor(LabelActivity.this, R.color.white));
                        drawable.setBounds(itemView.getLeft() + 150, (itemView.getTop() + itemView.getBottom()) / 2 - 30,
                                200,
                                (itemView.getTop() + itemView.getBottom()) / 2 + 30);
                        drawable.draw(c);
//                        if (dX > 250) newDx = 300;
                    } else {
                        /* Set your color for negative displacement */

                        // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), p);
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.labelsRecView);

        binding.labelsRecView.setAdapter(adapter);
        binding.labelsRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        adapter.setLabels((List<LabelDTO>) getIntent().getSerializableExtra("LABELS"));
    }

    private void showCreateLabelDialog(long boardId, String token) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
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
                            adapter.addLabel(response.body());
                            Toasty.success(LabelActivity.this, "Create label success!", Toast.LENGTH_SHORT, true).show();
                            dialog.dismiss();
                        } else
                            Toasty.error(LabelActivity.this, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onFailure(Call<LabelDTO> call, Throwable t) {
                        Toasty.error(LabelActivity.this, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                });
            } else
                Toasty.warning(this, "Please fill full information", Toast.LENGTH_SHORT, true).show();
        });

        dialog.show();
    }
}