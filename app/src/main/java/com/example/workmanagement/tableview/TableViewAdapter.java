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

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.sort.SortState;
import com.example.workmanagement.R;
import com.example.workmanagement.tableview.holder.CellViewHolder;
import com.example.workmanagement.tableview.holder.ColumnHeaderViewHolder;
import com.example.workmanagement.tableview.holder.PersonCellViewHolder;
import com.example.workmanagement.tableview.holder.RowHeaderViewHolder;
import com.example.workmanagement.tableview.model.Cell;
import com.example.workmanagement.tableview.model.ColumnHeader;
import com.example.workmanagement.tableview.model.RowHeader;
import com.example.workmanagement.utils.dto.TaskDetailsDTO;
import com.example.workmanagement.viewmodels.BoardViewModel;

/**
 * Created by evrencoskun on 11/06/2017.
 * <p>
 * This is a sample of custom TableView Adapter.
 */

public class TableViewAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {

    // Cell View Types by Column Position
    private static final int PERSON_CELL_TYPE = 1;
    private static final int GENDER_CELL_TYPE = 3;
    private static final int DEADLINE_CELL_TYPE = 2;
    // add new one if it necessary..

    private static final String LOG_TAG = TableViewAdapter.class.getSimpleName();

    @NonNull
    private final TableViewModel mTableViewModel;
    private Context mContext;
    private LifecycleOwner lifecycleOwner;
    private BoardViewModel boardViewModel;
    private int tablePosition;
    public TableViewAdapter(@NonNull TableViewModel tableViewModel) {
        super();
        this.mTableViewModel = tableViewModel;
    }

    public TableViewAdapter(@NonNull TableViewModel mTableViewModel, Context mContext) {
        this.mTableViewModel = mTableViewModel;
        this.mContext = mContext;
    }

    public TableViewAdapter(@NonNull TableViewModel mTableViewModel, Context mContext, BoardViewModel boardViewModel) {
        this.mTableViewModel = mTableViewModel;
        this.mContext = mContext;
        this.boardViewModel = boardViewModel;
    }

    public TableViewAdapter(@NonNull TableViewModel mTableViewModel, Context mContext, BoardViewModel boardViewModel, int pos) {
        this.mTableViewModel = mTableViewModel;
        this.mContext = mContext;
        this.boardViewModel = boardViewModel;
        this.tablePosition = pos;
    }

    public TableViewAdapter(@NonNull TableViewModel mTableViewModel, Context mContext, LifecycleOwner lifecycleOwner) {
        this.mTableViewModel = mTableViewModel;
        this.mContext = mContext;
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * This is where you create your custom Cell ViewHolder. This method is called when Cell
     * RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given type to
     * represent an item.
     *
     * @param viewType : This value comes from "getCellItemViewType" method to support different
     *                 type of viewHolder as a Cell item.
     * @see #getCellItemViewType(int);
     */
    @NonNull
    @Override
    public AbstractViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO check
        Log.e(LOG_TAG, " onCreateCellViewHolder has been called");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout;

        switch (viewType) {
            case PERSON_CELL_TYPE:
                // Get image cell layout which has ImageView on the base instead of TextView.
                layout = inflater.inflate(R.layout.table_view_image_cell_layout, parent, false);

                return new PersonCellViewHolder(layout, parent.getContext());
            case GENDER_CELL_TYPE:
                // Get image cell layout which has ImageView instead of TextView.
                layout = inflater.inflate(R.layout.table_view_image_cell_layout, parent, false);

                return new PersonCellViewHolder(layout, parent.getContext(), "LABEL");
//                layout = inflater.inflate(R.layout.table_view_cell_layout, parent, false);
//
//                return new CellViewHolder(layout);
            default:
                // For cells that display a text
                layout = inflater.inflate(R.layout.table_view_cell_layout, parent, false);

                // Create a Cell ViewHolder
                return new CellViewHolder(layout);
        }
    }

    /**
     * That is where you set Cell View Model data to your custom Cell ViewHolder. This method is
     * Called by Cell RecyclerView of the TableView to display the data at the specified position.
     * This method gives you everything you need about a cell item.
     *
     * @param holder         : This is one of your cell ViewHolders that was created on
     *                       ```onCreateCellViewHolder``` method. In this example we have created
     *                       "CellViewHolder" holder.
     * @param cellItemModel  : This is the cell view model located on this X and Y position. In this
     *                       example, the model class is "Cell".
     * @param columnPosition : This is the X (Column) position of the cell item.
     * @param rowPosition    : This is the Y (Row) position of the cell item.
     * @see #onCreateCellViewHolder(ViewGroup, int) ;
     */
    @Override
    public void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable Cell cellItemModel, int
            columnPosition, int rowPosition) {

        switch (holder.getItemViewType()) {
            case PERSON_CELL_TYPE:
                PersonCellViewHolder personViewHolder = (PersonCellViewHolder) holder;

                String temp = boardViewModel.getTables().getValue().get(tablePosition).getTasks().get(rowPosition).getStatus();

                if (temp.equals("DONE")) {
                    personViewHolder.cell_container.setBackgroundResource(R.color.done);
                } else if (temp.equals("PENDING")) {
                    personViewHolder.cell_container.setBackgroundResource(R.color.pending);
                } else if (temp.equals("STUCK")) {
                    personViewHolder.cell_container.setBackgroundResource(R.color.stuck);
                } else {
                    personViewHolder.cell_container.setBackgroundResource(R.color.primary_4);
                }
                personViewHolder.cell_name.setText(cellItemModel.getText());

                if (String.valueOf(cellItemModel.getData()).equals("default")) {
                    personViewHolder.cell_image.setImageResource(R.drawable.ic_launcher_foreground);
                } else {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(String.valueOf(cellItemModel.getData()))
                            .into(personViewHolder.cell_image);
                }
                break;
            case DEADLINE_CELL_TYPE:
                CellViewHolder viewHolder1 = (CellViewHolder) holder;
                viewHolder1.setCellDeadline(cellItemModel);
                break;
            case GENDER_CELL_TYPE:
                PersonCellViewHolder cellViewHolder = (PersonCellViewHolder) holder;
                TaskDetailsDTO task = boardViewModel.getTables().getValue().get(tablePosition).getTasks().get(rowPosition);
                if (task.getLabelAttributes().stream().filter(atr -> atr.getName().equals("label")).findFirst().isPresent()) {
                    long labelId = task.getLabelAttributes().stream().filter(atr -> atr.getName().equals("label")).findFirst().get().getLabelId();
                    String color = boardViewModel.getLabels().getValue().stream().filter(l -> l.getId() == labelId).findFirst().get().getColor();
                    cellViewHolder.cell_container.setBackgroundColor(Color.parseColor(color));
                }
                cellViewHolder.cell_name.setText(cellItemModel.getText());
                //cellViewHolder.setCell(cellItemModel);
                break;
            default:
                // Get the holder to update cell item text
                CellViewHolder viewHolder = (CellViewHolder) holder;
                viewHolder.setCell(cellItemModel);
                break;
        }
    }

    /**
     * This is where you create your custom Column Header ViewHolder. This method is called when
     * Column Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getColumnHeaderItemViewType" method to support
     *                 different type of viewHolder as a Column Header item.
     * @see #getColumnHeaderItemViewType(int);
     */
    @NonNull
    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO: check
        //Log.e(LOG_TAG, " onCreateColumnHeaderViewHolder has been called");
        // Get Column Header xml Layout
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_column_header_layout, parent, false);

        // Create a ColumnHeader ViewHolder
        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    /**
     * That is where you set Column Header View Model data to your custom Column Header ViewHolder.
     * This method is Called by ColumnHeader RecyclerView of the TableView to display the data at
     * the specified position. This method gives you everything you need about a column header
     * item.
     *
     * @param holder                : This is one of your column header ViewHolders that was created
     *                              on ```onCreateColumnHeaderViewHolder``` method. In this example
     *                              we have created "ColumnHeaderViewHolder" holder.
     * @param columnHeaderItemModel : This is the column header view model located on this X
     *                              position. In this example, the model class is "ColumnHeader".
     * @param columnPosition        : This is the X (Column) position of the column header item.
     * @see #onCreateColumnHeaderViewHolder(ViewGroup, int) ;
     */
    @Override
    public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable ColumnHeader
            columnHeaderItemModel, int columnPosition) {

        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.setColumnHeader(columnHeaderItemModel);
    }

    /**
     * This is where you create your custom Row Header ViewHolder. This method is called when
     * Row Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getRowHeaderItemViewType" method to support
     *                 different type of viewHolder as a row Header item.
     * @see #getRowHeaderItemViewType(int);
     */
    @NonNull
    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get Row Header xml Layout
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_row_header_layout, parent, false);

        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }


    /**
     * That is where you set Row Header View Model data to your custom Row Header ViewHolder. This
     * method is Called by RowHeader RecyclerView of the TableView to display the data at the
     * specified position. This method gives you everything you need about a row header item.
     *
     * @param holder             : This is one of your row header ViewHolders that was created on
     *                           ```onCreateRowHeaderViewHolder``` method. In this example we have
     *                           created "RowHeaderViewHolder" holder.
     * @param rowHeaderItemModel : This is the row header view model located on this Y position. In
     *                           this example, the model class is "RowHeader".
     * @param rowPosition        : This is the Y (row) position of the row header item.
     * @see #onCreateRowHeaderViewHolder(ViewGroup, int) ;
     */
    @Override
    public void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable RowHeader rowHeaderItemModel,
                                          int rowPosition) {

        // Get the holder to update row header item text
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(String.valueOf(rowHeaderItemModel.getData()));
    }

    @NonNull
    @Override
    public View onCreateCornerView(@NonNull ViewGroup parent) {
        // Get Corner xml layout
        View corner = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_view_corner_layout, parent, false);
        corner.setOnClickListener(view -> {
            SortState sortState = TableViewAdapter.this.getTableView()
                    .getRowHeaderSortingStatus();
            if (sortState != SortState.ASCENDING) {
                Log.d("TableViewAdapter", "Order Ascending");
                TableViewAdapter.this.getTableView().sortRowHeader(SortState.ASCENDING);
            } else {
                Log.d("TableViewAdapter", "Order Descending");
                TableViewAdapter.this.getTableView().sortRowHeader(SortState.DESCENDING);
            }
        });
        return corner;
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        // The unique ID for this type of column header item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        // The unique ID for this type of row header item
        // If you have different items for Row Header View by Y (Row) position,
        // then you should fill this method to be able create different
        // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
        return 0;
    }

    @Override
    public int getCellItemViewType(int column) {

        // The unique ID for this type of cell item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        switch (column) {
            case TableViewModel.PERSON_COLUMN_INDEX:
                return PERSON_CELL_TYPE;
            case TableViewModel.GENDER_COLUMN_INDEX:
                return GENDER_CELL_TYPE;
            default:
                // Default view type
                return 0;
        }
    }
}
