package com.example.workmanagement.activities;

import androidx.annotation.Nullable;

public class Cell {
    @Nullable
    private Object mData;

    public Cell(@Nullable Object data) {
        this.mData = data;
    }

    @Nullable
    public Object getData() {
        return mData;
    }
}
