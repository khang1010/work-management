package com.example.workmanagement.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.example.workmanagement.R;
import com.example.workmanagement.tableview.TableViewAdapter;
import com.example.workmanagement.tableview.TableViewListener;
import com.example.workmanagement.tableview.TableViewModel;
import com.example.workmanagement.tableview.model.Cell;
import com.example.workmanagement.utils.SystemConstant;
import com.example.workmanagement.utils.dto.DateAttributeDTO;
import com.example.workmanagement.utils.dto.TableDTO;
import com.example.workmanagement.utils.dto.TableDetailsDTO;
import com.example.workmanagement.utils.dto.TaskDTO;
import com.example.workmanagement.utils.dto.TaskDetailsDTO;
import com.example.workmanagement.utils.dto.TextAttributeDTO;
import com.example.workmanagement.utils.dto.UserInfoDTO;
import com.example.workmanagement.utils.services.impl.TableServiceImpl;
import com.example.workmanagement.utils.services.impl.TaskServiceImpl;
import com.example.workmanagement.viewmodels.BoardViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;

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

public class TableRecViewAdapter extends RecyclerView.Adapter<TableRecViewAdapter.ViewHolder> {

    private List<TableDetailsDTO> tables = new ArrayList<>();
    private Context context;
    private UserViewModel userViewModel;
    private BoardViewModel boardViewModel;

    public void setTables(List<TableDetailsDTO> tables) {
        this.tables = tables;
        notifyDataSetChanged();
    }

    public TableRecViewAdapter(Context context, UserViewModel userViewModel, BoardViewModel boardViewModel) {
        this.context = context;
        this.userViewModel = userViewModel;
        this.boardViewModel = boardViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_table, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tableName.setText(tables.get(position).getName());
        holder.editTable.setText(holder.tableName.getText().toString());

        TableViewModel tableViewModel = new TableViewModel();

        List<List<Cell>> listCells = new ArrayList<>();
        tables.get(position).getTasks().forEach(t -> {
            List<Cell> list = new ArrayList<>();
            list.add(new Cell("1", t.getTextAttributes().stream().filter(atr -> atr.getName().equals("name")).findFirst().get().getValue()));
            list.add(new Cell("2", t.getUser().getPhotoUrl().equals("null") ? "default" : t.getUser().getPhotoUrl(), t.getUser().getDisplayName()));
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(t.getDateAttributes().stream().filter(atr -> atr.getName().equals("deadline")).findFirst().get().getValue());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            list.add(new Cell("3", new SimpleDateFormat("HH:mm dd/MM/yyyy").format(date)));
            listCells.add(list);
        });

        tableViewModel.setRow(listCells.size());

        TableViewAdapter tableViewAdapter = new TableViewAdapter(tableViewModel, context, boardViewModel, position);
        holder.table.setAdapter(tableViewAdapter);
        holder.table.setTableViewListener(new TableViewListener(holder.table, boardViewModel, userViewModel, tables, position));
        tableViewAdapter.setAllItems(tableViewModel.getColumnHeaderList(), tableViewModel.getRowHeaderList(), tableViewModel.getCellList());
        tableViewAdapter.setCellItems(listCells);
        if (listCells.size() == 0) {
            holder.table.setVisibility(View.GONE);
        }
        holder.down.setOnClickListener(view -> {
            holder.down.setVisibility(View.GONE);
            holder.up.setVisibility(View.VISIBLE);
            if (listCells.size() == 0) {
                holder.table.setVisibility(View.GONE);
            } else {
                holder.table.setVisibility(View.VISIBLE);
            }
            holder.addTask.setVisibility(View.VISIBLE);
        });
        holder.up.setOnClickListener(view -> {
            holder.down.setVisibility(View.VISIBLE);
            holder.up.setVisibility(View.GONE);
            holder.table.setVisibility(View.GONE);
            holder.addTask.setVisibility(View.GONE);
        });
        holder.addTask.setOnClickListener(view -> showCreateTaskDialog(position));

        holder.container.setOnClickListener(view -> showUpdateTableDialog(position));

        if (tables.get(position).getCreatedBy().getEmail().equals(userViewModel.getEmail().getValue()))
            holder.tableName.setOnClickListener(view -> {
                holder.editTable.setVisibility(View.VISIBLE);
                holder.accept.setVisibility(View.VISIBLE);
                holder.tableName.setVisibility(View.GONE);
            });
        else
            holder.delete.setVisibility(View.GONE);

        holder.delete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to delete this table ?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        TableServiceImpl.getInstance().getService(userViewModel.getToken().getValue())
                                .deleteTable(tables.get(position).getId())
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful() && response.code() == 200) {
                                            long id = tables.get(holder.getAdapterPosition()).getId();
                                            boardViewModel.setTables(tables.stream().filter(t -> t.getId() != id).collect(Collectors.toList()));
                                            Toasty.success(context, "Delete table success!", Toast.LENGTH_SHORT, true).show();
                                        } else Toasty.error(context, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toasty.error(context, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {

                    });
            builder.create().show();
        });


        holder.accept.setOnClickListener(view -> {
            if (holder.editTable.getText().toString().isEmpty())
                Toast.makeText(context, "Please fill information", Toast.LENGTH_SHORT).show();
            else {
                TableDTO dto = new TableDTO();
                dto.setName(holder.editTable.getText().toString());
                TableServiceImpl.getInstance().getService(userViewModel.getToken().getValue())
                        .updateTable(tables.get(position).getId(), dto)
                        .enqueue(new Callback<TableDetailsDTO>() {
                            @Override
                            public void onResponse(Call<TableDetailsDTO> call, Response<TableDetailsDTO> response) {
                                if (response.isSuccessful() && response.code() == 200) {
                                    List<TableDetailsDTO> tableDetailsDTOS = boardViewModel.getTables().getValue();
                                    tableDetailsDTOS.set(holder.getAdapterPosition(), response.body());
                                    holder.tableName.setVisibility(View.VISIBLE);
                                    holder.editTable.setVisibility(View.GONE);
                                    holder.accept.setVisibility(View.GONE);
                                    boardViewModel.setTables(tableDetailsDTOS);
                                    Toasty.success(context, "Update table success!", Toast.LENGTH_SHORT, true).show();
                                } else
                                    Toasty.error(context, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                            }

                            @Override
                            public void onFailure(Call<TableDetailsDTO> call, Throwable t) {
                                Toasty.error(context, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    private void showCreateTaskDialog(int pos) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_task);
        EditText txtSearchUser = dialog.findViewById(R.id.editTxtSearch);
        EditText txtTaskName = dialog.findViewById(R.id.editTxtCreateTaskName);
        EditText txtTaskDesc = dialog.findViewById(R.id.editTxtCreateTaskDesc);
        ConstraintLayout btnCreateTask = dialog.findViewById(R.id.btnCreateTask);

        UserSearchInTaskAdapter adapter = new UserSearchInTaskAdapter(context);
        RecyclerView userRecView = dialog.findViewById(R.id.searchRecView);
        userRecView.setLayoutManager(new LinearLayoutManager(context));
        userRecView.setAdapter(adapter);

        btnCreateTask.setOnClickListener(view -> {
            if (!txtTaskName.getText().toString().trim().isEmpty()
                    && !txtTaskDesc.getText().toString().trim().isEmpty() && adapter.isChosen()) {
                long tableId = tables.get(pos).getId();
                List<TableDetailsDTO> tableDetailsDTOS = boardViewModel.getTables().getValue();
                TaskDTO newTask = new TaskDTO();
                newTask.setDescription(txtTaskDesc.getText().toString());
                newTask.setStatus(SystemConstant.PENDING_STATUS);
                newTask.setUserId(adapter.getUser().getId());
                newTask.setTableId(tableId);
                List<TextAttributeDTO> textAttributes = new ArrayList<>();
                List<DateAttributeDTO> dateAttributes = new ArrayList<>();
                TextAttributeDTO textAttribute = new TextAttributeDTO();
                textAttribute.setName("name");
                textAttribute.setValue(txtTaskName.getText().toString());
                textAttributes.add(textAttribute);
                DateAttributeDTO dateAttribute = new DateAttributeDTO();
                dateAttribute.setName("deadline");
                dateAttribute.setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()));
                dateAttributes.add(dateAttribute);
                newTask.setTextAttributes(textAttributes);
                newTask.setDateAttributes(dateAttributes);
                TaskServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).createTask(newTask)
                        .enqueue(new Callback<TaskDetailsDTO>() {
                            @Override
                            public void onResponse(Call<TaskDetailsDTO> call, Response<TaskDetailsDTO> response) {
                                if (response.isSuccessful() && response.code() == 201) {
                                    tableDetailsDTOS.stream().filter(t -> t.getId() == tableId)
                                            .findFirst().get().getTasks().add(response.body());
                                    boardViewModel.setTables(tableDetailsDTOS);
                                    Toasty.success(context, "Create task success!", Toast.LENGTH_SHORT, true).show();
                                    dialog.dismiss();
                                } else
                                    Toasty.error(context, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                            }

                            @Override
                            public void onFailure(Call<TaskDetailsDTO> call, Throwable t) {
                                Toasty.error(context, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }
                        });
                notifyDataSetChanged();
            } else Toasty.warning(context, "Please fill full information", Toast.LENGTH_SHORT, true).show();
        });

        txtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    List<UserInfoDTO> users = new ArrayList<>();
                    users.addAll(tables.get(pos).getMembers());
                    users.add(tables.get(pos).getCreatedBy());
                    adapter.setUsers(users.stream()
                            .filter(m -> m.getDisplayName().trim().toLowerCase().contains(charSequence.toString().trim())
                                    || m.getEmail().trim().toLowerCase().contains(charSequence.toString().trim())
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

    private void showUpdateTableDialog(int pos) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_table);
        TextView txtUpdate = dialog.findViewById(R.id.createTxt);
        EditText txtSearchUser = dialog.findViewById(R.id.editTxtSearchUserTable);
        EditText txtTableName = dialog.findViewById(R.id.editTxtCreateTableName);
        EditText txtTableDesc = dialog.findViewById(R.id.editTxtCreateTableDesc);
        ConstraintLayout btnCreateTable = dialog.findViewById(R.id.btnCreateTable);

        txtUpdate.setText("Update");

        UserInvitedRecViewAdapter invitedAdapter = new UserInvitedRecViewAdapter(context);
        RecyclerView userInvitedRecView = dialog.findViewById(R.id.invitedUserRecView);
        userInvitedRecView.setLayoutManager(new GridLayoutManager(context, 5));
        userInvitedRecView.setAdapter(invitedAdapter);
        invitedAdapter.setUsers(tables.get(pos).getMembers());

        UserSearchRecViewAdapter adapter = new UserSearchRecViewAdapter(context, invitedAdapter);
        RecyclerView userRecView = dialog.findViewById(R.id.searchUserRecView);
        userRecView.setLayoutManager(new LinearLayoutManager(context));
        userRecView.setAdapter(adapter);

        txtTableName.setText(boardViewModel.getTables().getValue().get(pos).getName());
        txtTableDesc.setText(boardViewModel.getTables().getValue().get(pos).getDescription());

        List<UserInfoDTO> users = new ArrayList<>();
        users.addAll(tables.get(pos).getMembers());
        adapter.setUsers(users);

        if (!userViewModel.getEmail().getValue().equals(tables.get(pos).getCreatedBy().getEmail())) {
            txtSearchUser.setVisibility(View.GONE);
            userRecView.setVisibility(View.GONE);
            btnCreateTable.setVisibility(View.GONE);
        }

        btnCreateTable.setOnClickListener(view -> {
            List<TableDetailsDTO> tableDetailsDTOS = boardViewModel.getTables().getValue();
            TableDTO newTable = new TableDTO();

            if (!txtTableName.getText().toString().trim().isEmpty()
                    && !txtTableName.getText().toString().equals(tables.get(pos).getName()))
                newTable.setName(txtTableName.getText().toString());

            if (!txtTableDesc.getText().toString().trim().isEmpty()
                    && !txtTableDesc.getText().toString().equals(tables.get(pos).getDescription()))
                newTable.setDescription(txtTableDesc.getText().toString());

            List<UserInfoDTO> removedUsers = tables.get(pos).getMembers().stream()
                    .filter(m -> invitedAdapter.getUsers().stream().noneMatch(u -> u.getId() == m.getId()))
                    .collect(Collectors.toList());
            if (removedUsers.stream().anyMatch(u -> tables.get(pos).getTasks().stream().anyMatch(t -> t.getUser().getId() == u.getId()))) {
                Toast.makeText(context, "Please remove user from task first", Toast.LENGTH_SHORT).show();
                return;
            }
            newTable.setMemberIds(invitedAdapter.getUsers().stream().map(u -> u.getId()).collect(Collectors.toList()));

            TableServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).updateTable(tables.get(pos).getId(), newTable)
                    .enqueue(new Callback<TableDetailsDTO>() {
                        @Override
                        public void onResponse(Call<TableDetailsDTO> call, Response<TableDetailsDTO> response) {
                            if (response.isSuccessful() && response.code() == 200) {
                                tableDetailsDTOS.set(pos, response.body());
                                boardViewModel.setTables(tableDetailsDTOS);
                                dialog.dismiss();
                                Toasty.success(context, "Update table success!", Toast.LENGTH_SHORT, true).show();
                            } else Toasty.error(context, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                        }

                        @Override
                        public void onFailure(Call<TableDetailsDTO> call, Throwable t) {
                            Toasty.error(context, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                    });
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
                } else {
                    List<UserInfoDTO> userList = new ArrayList<>();
                    userList.addAll(tables.get(pos).getMembers());
                    adapter.setUsers(userList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container, titleBar;
        ImageView down, download, up, accept, delete;
        TextView tableName, addTask;
        TableView table;
        EditText editTable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.fragment_container);
            titleBar = itemView.findViewById(R.id.titleBar);
            down = itemView.findViewById(R.id.iconDown);
            up = itemView.findViewById(R.id.iconUp);
            download = itemView.findViewById(R.id.iconDownload);
            tableName = itemView.findViewById(R.id.tableName);
            table = itemView.findViewById(R.id.table_container);
            addTask = itemView.findViewById(R.id.addTaskBtn);
            editTable = itemView.findViewById(R.id.editTableName);
            accept = itemView.findViewById(R.id.acceptBtn);
            delete = itemView.findViewById(R.id.iconDelete);

        }
    }
}
