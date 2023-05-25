/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.workmanagement.tableview;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.example.workmanagement.R;
import com.example.workmanagement.adapter.UserSearchInTaskAdapter;
import com.example.workmanagement.tableview.holder.ColumnHeaderViewHolder;
import com.example.workmanagement.tableview.model.Cell;
import com.example.workmanagement.tableview.popup.ColumnHeaderLongPressPopup;
import com.example.workmanagement.tableview.popup.RowHeaderLongPressPopup;
import com.example.workmanagement.utils.dto.DateAttributeDTO;
import com.example.workmanagement.utils.dto.TableDetailsDTO;
import com.example.workmanagement.utils.dto.TaskDTO;
import com.example.workmanagement.utils.dto.TaskDetailsDTO;
import com.example.workmanagement.utils.dto.TextAttributeDTO;
import com.example.workmanagement.utils.dto.UserInfoDTO;
import com.example.workmanagement.utils.services.impl.TaskServiceImpl;
import com.example.workmanagement.viewmodels.BoardViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by evrencoskun on 21/09/2017.
 */

public class TableViewListener implements ITableViewListener {
    @NonNull
    private final Context mContext;
    @NonNull
    private final TableView mTableView;

    private BoardViewModel boardViewModel;
    private UserViewModel userViewModel;
    private List<TableDetailsDTO> tables = new ArrayList<>();

    public void setTables(List<TableDetailsDTO> tables) {
        this.tables = tables;
    }

    private int position = -1;

    public TableViewListener(@NonNull TableView tableView) {
        this.mContext = tableView.getContext();
        this.mTableView = tableView;
    }

    public TableViewListener(@NonNull TableView mTableView, BoardViewModel boardViewModel, UserViewModel userViewModel) {
        this.mTableView = mTableView;
        this.boardViewModel = boardViewModel;
        this.userViewModel = userViewModel;
        this.mContext = mTableView.getContext();
    }

    public TableViewListener(@NonNull TableView mTableView, BoardViewModel boardViewModel, UserViewModel userViewModel, List<TableDetailsDTO> tables) {
        this.mTableView = mTableView;
        this.boardViewModel = boardViewModel;
        this.userViewModel = userViewModel;
        this.tables = tables;
        this.mContext = mTableView.getContext();
    }

    public TableViewListener(@NonNull TableView mTableView, BoardViewModel boardViewModel, UserViewModel userViewModel, List<TableDetailsDTO> tables, int position) {
        this.mTableView = mTableView;
        this.boardViewModel = boardViewModel;
        this.userViewModel = userViewModel;
        this.tables = tables;
        this.position = position;
        this.mContext = mTableView.getContext();
    }

    /**
     * Called when user click any cell item.
     *
     * @param cellView : Clicked Cell ViewHolder.
     * @param column   : X (Column) position of Clicked Cell item.
     * @param row      : Y (Row) position of Clicked Cell item.
     */
    String date_time = "";

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

        // Do what you want.
//        showToast("Cell " + column + " " + row + " has been clicked.");
        if (column == 2) {
            TextView cell_name = cellView.itemView.findViewById(R.id.cell_data);
            openDialog(cell_name, position, row);
        } else {
            showUpdateTaskDialog(position, column, row);
        }

    }

    private void openDialog(TextView cell_name, int pos, int row) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(mContext, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                System.out.println(month);
                date_time = day + "/" + month + "/" + year;
                openTimeDialog(cell_name, pos, row, day, month, year);
            }

        }, mYear, mMonth, mDay);
        dialog.show();
    }

    private void openTimeDialog(TextView cell_name, int pos, int row, int day, int month, int year) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(mContext, R.style.DialogTheme, (timePicker, hours, minutes) -> {
            //date_time = (String.format("%02d", hours) + ":" + String.format("%02d", minutes) + " " + date_time);
            //cell_name.setText(date_time);
            LocalDateTime date = LocalDateTime.of(year, month + 1, day, hours, minutes, 0, 0);
            //Toast.makeText(mContext, date.toString(), Toast.LENGTH_SHORT).show();
            long tableId = tables.get(pos).getId();
            List<TableDetailsDTO> tableDetailsDTOS = boardViewModel.getTables().getValue();
            TaskDetailsDTO task = tables.get(pos).getTasks().get(row);
            TaskDTO newTask = new TaskDTO();
            List<TextAttributeDTO> textAttributes = new ArrayList<>();
            List<DateAttributeDTO> dateAttributes = new ArrayList<>();
            TextAttributeDTO textAttribute = new TextAttributeDTO();
            textAttribute.setId(task.getTextAttributes().stream().filter(atr -> atr.getName().equals("name")).findFirst().get().getId());
            textAttribute.setName("name");
            textAttribute.setValue(task.getTextAttributes().stream().filter(atr -> atr.getName().equals("name")).findFirst().get().getValue());
            textAttributes.add(textAttribute);
            DateAttributeDTO dateAttribute = new DateAttributeDTO();
            dateAttribute.setId(task.getDateAttributes().stream().filter(atr -> atr.getName().equals("deadline")).findFirst().get().getId());
            dateAttribute.setName("deadline");
            dateAttribute.setValue(date.toString());
            dateAttributes.add(dateAttribute);
            newTask.setTextAttributes(textAttributes);
            newTask.setDateAttributes(dateAttributes);
            TaskServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).updateTask(task.getId(), newTask)
                    .enqueue(new Callback<TaskDetailsDTO>() {
                        @Override
                        public void onResponse(Call<TaskDetailsDTO> call, Response<TaskDetailsDTO> response) {
                            if (response.isSuccessful() && response.code() == 200) {
                                int index = IntStream.range(0, tables.get(pos).getTasks().size())
                                        .filter(i -> response.body().getId() == tables.get(pos).getTasks().get(i).getId())
                                        .findFirst().orElse(-1);
                                tableDetailsDTOS.stream().filter(t -> t.getId() == tableId)
                                        .findFirst().get().getTasks()
                                        .set(index, response.body());
                                boardViewModel.setTables(tableDetailsDTOS);
                                Toasty.success(mContext, "Update task success!", Toast.LENGTH_SHORT, true).show();
                            } else
                                Toasty.error(mContext, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                        }

                        @Override
                        public void onFailure(Call<TaskDetailsDTO> call, Throwable t) {
                            Toasty.error(mContext, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                    });
        }, mHour, mMinute, true);
        dialog.show();
    }

    private void showUpdateTaskDialog(int pos, int column, int row) {

        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_task);
        EditText txtSearchUser = dialog.findViewById(R.id.editTxtSearch);
        EditText txtTaskName = dialog.findViewById(R.id.editTxtCreateTaskName);
        Spinner status = dialog.findViewById(R.id.statusSpinner);
        status.setVisibility(View.VISIBLE);
        String[] items = {"DONE", "PENDING", "STUCK"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, items);
        status.setAdapter(statusAdapter);
        String taskStatusStr = tables.get(pos).getTasks().get(row).getStatus();
        if (taskStatusStr.equals("DONE"))
            status.setSelection(0);
        else if (taskStatusStr.equals("PENDING"))
            status.setSelection(1);
        else if (taskStatusStr.equals("STUCK"))
            status.setSelection(2);

        final int[] taskStatus = {0};
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                taskStatus[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ConstraintLayout btnCreateTask = dialog.findViewById(R.id.btnCreateTask);
        ImageView btnIcon = btnCreateTask.findViewById(R.id.imagePlus);
        TextView btnText = btnCreateTask.findViewById(R.id.createTxt);
        btnText.setText("Done");
        btnIcon.setImageResource(R.drawable.ic_check);

        List<List<Cell>> listCells = new ArrayList<>();
        tables.get(pos).getTasks().forEach(t -> {
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

        UserSearchInTaskAdapter adapter = new UserSearchInTaskAdapter(tables.get(pos).getTasks().get(row).getUser(), mContext);
        RecyclerView userRecView = dialog.findViewById(R.id.searchRecView);
        userRecView.setLayoutManager(new LinearLayoutManager(mContext));
        userRecView.setAdapter(adapter);
        adapter.setChosen(true);

        txtTaskName.setText(String.valueOf(listCells.get(row).get(0).getData()));
        if (String.valueOf(listCells.get(row).get(1).getText()).equals("")) {
            txtSearchUser.setText("");
            List<UserInfoDTO> userList = new ArrayList<>();
            userList.addAll(tables.get(pos).getMembers());
            adapter.setUsers(userList);
        } else {
            txtSearchUser.setText(String.valueOf(tables.get(pos).getTasks().get(row).getUser().getEmail()));
            List<UserInfoDTO> users = new ArrayList<>();
            users.addAll(tables.get(pos).getMembers());
            users.add(tables.get(pos).getCreatedBy());
            adapter.setUsers(users.stream()
                    .filter(m -> m.getDisplayName().trim().toLowerCase().contains(txtSearchUser.getText().toString().trim())
                            || m.getEmail().trim().toLowerCase().contains(txtSearchUser.getText().toString().trim())
                    )
                    .collect(Collectors.toList()));
        }

        btnCreateTask.setOnClickListener(view -> {
            if (!txtTaskName.getText().toString().isEmpty() && adapter.isChosen()) {
                long tableId = tables.get(pos).getId();
                List<TableDetailsDTO> tableDetailsDTOS = boardViewModel.getTables().getValue();
                TaskDetailsDTO task = tables.get(pos).getTasks().get(row);
                TaskDTO newTask = new TaskDTO();
                newTask.setStatus(taskStatus[0]);
                newTask.setUserId(adapter.getUser().getId());
                List<TextAttributeDTO> textAttributes = new ArrayList<>();
                List<DateAttributeDTO> dateAttributes = new ArrayList<>();
                TextAttributeDTO textAttribute = new TextAttributeDTO();
                textAttribute.setId(task.getTextAttributes().stream().filter(atr -> atr.getName().equals("name")).findFirst().get().getId());
                textAttribute.setName("name");
                textAttribute.setValue(txtTaskName.getText().toString());
                textAttributes.add(textAttribute);
                DateAttributeDTO dateAttribute = new DateAttributeDTO();
                dateAttribute.setId(task.getDateAttributes().stream().filter(atr -> atr.getName().equals("deadline")).findFirst().get().getId());
                dateAttribute.setName("deadline");
                dateAttribute.setValue(task.getDateAttributes().stream().filter(atr -> atr.getName().equals("deadline")).findFirst().get().getValue());
                dateAttributes.add(dateAttribute);
                newTask.setTextAttributes(textAttributes);
                newTask.setDateAttributes(dateAttributes);
                TaskServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).updateTask(task.getId(), newTask)
                        .enqueue(new Callback<TaskDetailsDTO>() {
                            @Override
                            public void onResponse(Call<TaskDetailsDTO> call, Response<TaskDetailsDTO> response) {
                                if (response.isSuccessful() && response.code() == 200) {
                                    int index = IntStream.range(0, tables.get(pos).getTasks().size())
                                            .filter(i -> response.body().getId() == tables.get(pos).getTasks().get(i).getId())
                                            .findFirst().orElse(-1);
                                    tableDetailsDTOS.stream().filter(t -> t.getId() == tableId)
                                            .findFirst().get().getTasks()
                                            .set(index, response.body());
                                    boardViewModel.setTables(tableDetailsDTOS);
                                    Toasty.success(mContext, "Update task success!", Toast.LENGTH_SHORT, true).show();
                                    dialog.dismiss();
                                } else Toasty.error(mContext, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                            }

                            @Override
                            public void onFailure(Call<TaskDetailsDTO> call, Throwable t) {
                                Toasty.error(mContext, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }
                        });
            } else
                Toast.makeText(mContext, "Please fill full information", Toast.LENGTH_SHORT).show();

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

    /**
     * Called when user double click any cell item.
     *
     * @param cellView : Clicked Cell ViewHolder.
     * @param column   : X (Column) position of Clicked Cell item.
     * @param row      : Y (Row) position of Clicked Cell item.
     */
    @Override
    public void onCellDoubleClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
        // Do what you want.
        showToast("Cell " + column + " " + row + " has been double clicked.");
    }

    /**
     * Called when user long press any cell item.
     *
     * @param cellView : Long Pressed Cell ViewHolder.
     * @param column   : X (Column) position of Long Pressed Cell item.
     * @param row      : Y (Row) position of Long Pressed Cell item.
     */
    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, final int column,
                                  int row) {
        // Do What you want
        showToast("Cell " + column + " " + row + " has been long pressed.");
    }

    /**
     * Called when user click any column header item.
     *
     * @param columnHeaderView : Clicked Column Header ViewHolder.
     * @param column           : X (Column) position of Clicked Column Header item.
     */
    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column) {
        // Do what you want.
        showToast("Column header  " + column + " has been clicked.");
    }

    /**
     * Called when user double click any column header item.
     *
     * @param columnHeaderView : Clicked Column Header ViewHolder.
     * @param column           : X (Column) position of Clicked Column Header item.
     */
    @Override
    public void onColumnHeaderDoubleClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
        // Do what you want.
        showToast("Column header  " + column + " has been double clicked.");
    }

    /**
     * Called when user long press any column header item.
     *
     * @param columnHeaderView : Long Pressed Column Header ViewHolder.
     * @param column           : X (Column) position of Long Pressed Column Header item.
     */
    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column) {

        if (columnHeaderView instanceof ColumnHeaderViewHolder) {
            // Create Long Press Popup
            ColumnHeaderLongPressPopup popup = new ColumnHeaderLongPressPopup(
                    (ColumnHeaderViewHolder) columnHeaderView, mTableView);
            // Show
            popup.show();
        }
    }

    /**
     * Called when user click any Row Header item.
     *
     * @param rowHeaderView : Clicked Row Header ViewHolder.
     * @param row           : Y (Row) position of Clicked Row Header item.
     */
    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
        // Do whatever you want.
        showToast("Row header " + row + " has been clicked.");
    }

    /**
     * Called when user double click any Row Header item.
     *
     * @param rowHeaderView : Clicked Row Header ViewHolder.
     * @param row           : Y (Row) position of Clicked Row Header item.
     */
    @Override
    public void onRowHeaderDoubleClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
        // Do whatever you want.
        showToast("Row header " + row + " has been double clicked.");
    }

    /**
     * Called when user long press any row header item.
     *
     * @param rowHeaderView : Long Pressed Row Header ViewHolder.
     * @param row           : Y (Row) position of Long Pressed Row Header item.
     */
    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

        // Create Long Press Popup
        RowHeaderLongPressPopup popup = new RowHeaderLongPressPopup(rowHeaderView, mTableView, boardViewModel, userViewModel, (int) tables.get(position).getTasks().get(row).getId(), tables, position, mContext);
        // Show
        popup.show();
    }


    private void showToast(String p_strMessage) {
        Toast.makeText(mContext, p_strMessage, Toast.LENGTH_SHORT).show();
    }
}
