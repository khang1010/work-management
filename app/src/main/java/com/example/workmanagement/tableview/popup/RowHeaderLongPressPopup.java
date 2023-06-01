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

package com.example.workmanagement.tableview.popup;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.example.workmanagement.R;
import com.example.workmanagement.utils.dto.TableDetailsDTO;
import com.example.workmanagement.utils.services.impl.TaskServiceImpl;
import com.example.workmanagement.viewmodels.BoardViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by evrencoskun on 21.01.2018.
 */

public class RowHeaderLongPressPopup extends PopupMenu implements PopupMenu
        .OnMenuItemClickListener {

    // Menu Item constants
    private static final int SCROLL_COLUMN = 1;
    private static final int SHOWHIDE_COLUMN = 2;
    private static final int REMOVE_ROW = 3;

    private Context context;

    private BoardViewModel boardViewModel;
    private UserViewModel userViewModel;
    @NonNull
    private final TableView mTableView;
    private final int mRowPosition;
    private int id;
    private List<TableDetailsDTO> tables = new ArrayList<>();
    private int pos = -1;

    public RowHeaderLongPressPopup(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull TableView tableView) {
        super(viewHolder.itemView.getContext(), viewHolder.itemView);

        this.mTableView = tableView;
        this.mRowPosition = viewHolder.getAdapterPosition();

        initialize();
    }

    public RowHeaderLongPressPopup(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull TableView tableView, BoardViewModel boardViewModel, UserViewModel userViewModel, int id, List<TableDetailsDTO> tables, int position, Context context) {
        super(viewHolder.itemView.getContext(), viewHolder.itemView);

        this.mTableView = tableView;
        this.mRowPosition = viewHolder.getAdapterPosition();
        this.boardViewModel = boardViewModel;
        this.userViewModel = userViewModel;
        this.tables = tables;
        this.pos = position;
        this.id = id;
        this.context = context;
        initialize();
    }


    private void initialize() {
        createMenuItem();

        this.setOnMenuItemClickListener(this);
    }

    private void createMenuItem() {
        Context context = mTableView.getContext();
//        this.getMenu().add(Menu.NONE, SCROLL_COLUMN, 0, context.getString(R.string
//                .scroll_to_column_position));
//        this.getMenu().add(Menu.NONE, SHOWHIDE_COLUMN, 1, context.getString(R.string
//                .show_hide_the_column));
        this.getMenu().add(Menu.NONE, REMOVE_ROW, 2, "Remove " + mRowPosition + " position");
        // add new one ...

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        // Note: item id is index of menu item..

        switch (menuItem.getItemId()) {
            case SCROLL_COLUMN:
                mTableView.scrollToColumnPosition(15);
                break;
            case SHOWHIDE_COLUMN:
                int column = 1;
                if (mTableView.isColumnVisible(column)) {
                    mTableView.hideColumn(column);
                } else {
                    mTableView.showColumn(column);
                }

                break;
            case REMOVE_ROW:
                List<TableDetailsDTO> tableDetailsDTOS = boardViewModel.getTables().getValue();
                TaskServiceImpl.getInstance().getService(userViewModel.getToken().getValue()).deleteTask(id).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            //mTableView.getAdapter().removeRow(mRowPosition);
                            int index = IntStream.range(0, tables.get(pos).getTasks().size())
                                    .filter(i -> id == tables.get(pos).getTasks().get(i).getId())
                                    .findFirst().orElse(-1);
                            tableDetailsDTOS.get(pos).setTasks(tables.get(pos).getTasks().stream()
                                    .filter(t -> t.getId() != id)
                                    .collect(Collectors.toList())
                            );
                            boardViewModel.setTables(tableDetailsDTOS);
                            Toasty.success(context, "Delete task success!", Toast.LENGTH_SHORT, true).show();
                        } else Toasty.error(context, response.raw().toString(), Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toasty.error(context, t.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                });
                break;
        }
        return true;
    }

}
