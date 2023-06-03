package com.example.workmanagement.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanagement.R;
import com.example.workmanagement.activities.LabelActivity;
import com.example.workmanagement.utils.dto.LabelDTO;
import com.example.workmanagement.utils.services.impl.LabelServiceImpl;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LabelsRecViewAdapter extends RecyclerView.Adapter<LabelsRecViewAdapter.ViewHolder> {

    private Context context;
    private List<LabelDTO> labels;

    private String token;

    public LabelsRecViewAdapter(Context context, String token) {
        this.context = context;
        this.token = token;
        if (labels == null) labels = new ArrayList<>();
    }

    public List<LabelDTO> getLabels() {
        return labels;
    }

    public void addLabel(LabelDTO dto) {
        labels.add(dto);
        notifyDataSetChanged();
    }

    public void removeLabel(long id) {
        labels = labels.stream().filter(l -> l.getId() != id).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public void setLabels(List<LabelDTO> labels) {
        this.labels = labels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_item, parent, false);
        return new LabelsRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.color.setBackgroundColor(Color.parseColor(labels.get(position).getColor()));
        holder.name.setText(labels.get(position).getName());
        holder.container.setOnClickListener(v -> showUpdateLabelDialog(position));
    }

    private void showUpdateLabelDialog(int pos) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_label);
        Window window = dialog.getWindow();
        if (window == null)
            return;
        //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LabelDTO dto = new LabelDTO();
        dto.setColor(labels.get(pos).getColor());

        EditText txtLabelName = dialog.findViewById(R.id.editTxtCreateLabelName);
        txtLabelName.setText(labels.get(pos).getName());

        TextView color = dialog.findViewById(R.id.labelColorPicker);
        color.setBackgroundColor(Color.parseColor(labels.get(pos).getColor()));

        color.setOnClickListener(view -> {
            new ColorPickerDialog.Builder(context)
                    .setTitle("ColorPicker Dialog")
                    .setPreferenceName("MyColorPickerDialog")
                    .setPositiveButton(context.getString(R.string.confirm), (ColorEnvelopeListener) (envelope, fromUser) -> {
                        dto.setColor("#" + envelope.getHexCode());
                        color.setBackgroundColor(envelope.getColor());
                    })
                    .show();
        });
        Button btnCreate = dialog.findViewById(R.id.btnCreateLabel);
        btnCreate.setText("Done");

        btnCreate.setOnClickListener(v -> {
            if (!txtLabelName.getText().toString().trim().isEmpty() && !txtLabelName.getText().toString().equals(labels.get(pos).getName()))
                dto.setName(txtLabelName.getText().toString());
                LabelServiceImpl.getInstance().getService(token).updateLabel(labels.get(pos).getId(), dto).enqueue(new Callback<LabelDTO>() {
                    @Override
                    public void onResponse(Call<LabelDTO> call, Response<LabelDTO> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            labels.set(pos, response.body());
                            notifyItemChanged(pos);
                            Toasty.success(context, "Update label success!", Toast.LENGTH_SHORT, true).show();
                            dialog.dismiss();
                        } else
                            Toasty.error(context, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onFailure(Call<LabelDTO> call, Throwable t) {
                        Toasty.error(context, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                });
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout container;
        private CircleImageView color;

        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.labelContainer);
            color = itemView.findViewById(R.id.labelColor);
            name = itemView.findViewById(R.id.labelName);
        }
    }

}
