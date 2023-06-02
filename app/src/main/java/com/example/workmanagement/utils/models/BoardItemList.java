package com.example.workmanagement.utils.models;

import android.graphics.Bitmap;

public class BoardItemList {
    String name;
    String num;
    Bitmap img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public BoardItemList(String name, String num, Bitmap img) {
        this.name = name;
        this.num = num;
        this.img = img;
    }
}
