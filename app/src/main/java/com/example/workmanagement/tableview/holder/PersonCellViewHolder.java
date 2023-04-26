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

package com.example.workmanagement.tableview.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.example.workmanagement.R;
import com.example.workmanagement.activities.HomeActivity;
import com.example.workmanagement.tableview.TableViewModel;
import com.example.workmanagement.viewmodels.UserViewModel;

/**
 * Created by evrencoskun on 4.02.2018.
 */

public class PersonCellViewHolder extends AbstractViewHolder {
    @NonNull
    public final ImageView cell_image;
    public final TextView cell_name;
    public final RelativeLayout cell_container;
    private UserViewModel userViewModel;
    private Context mContext;
    public PersonCellViewHolder(@NonNull View itemView) {
        super(itemView);
        cell_image = itemView.findViewById(R.id.cell_image);
        cell_name = itemView.findViewById(R.id.cell_text);
        cell_container = itemView.findViewById(R.id.cell_background);
    }

    public PersonCellViewHolder(@NonNull View itemView, Context mContext) {
        super(itemView);
        this.mContext = mContext;
        cell_image = itemView.findViewById(R.id.cell_image);
        cell_name = itemView.findViewById(R.id.cell_text);
        cell_container = itemView.findViewById(R.id.cell_background);
    }

    public void setData(Object data, String name) {
        int mood = (int) data;
        int moodDrawable = mood == TableViewModel.HAPPY ? R.drawable.ic_happy : R.drawable.ic_next;

        cell_image.setImageResource(moodDrawable);
        cell_name.setText(name);
        cell_container.setBackgroundResource(R.color.blue);

//        userViewModel.getPhotoUrl().observe((LifecycleOwner) mContext, photoUrl -> Glide.with(mContext)
//                .asBitmap()
//                .load(photoUrl)
//                .into(cell_image));
//        cell_name.setText(userViewModel.getDisplayName().toString());
    }
}