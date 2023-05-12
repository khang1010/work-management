package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

public class UserChartDTO extends UserInfoDTO {

    @Json(name = "amount")
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
