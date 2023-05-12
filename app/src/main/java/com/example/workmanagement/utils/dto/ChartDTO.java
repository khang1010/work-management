package com.example.workmanagement.utils.dto;

import com.squareup.moshi.Json;

import java.util.List;

public class ChartDTO {

    @Json(name = "chart_1")
    private List<UserChartDTO> chart_1;

    @Json(name = "chart_2")
    private List<UserChartDTO> chart_2;

    @Json(name = "chart_3")
    private List<UserChartDTO> chart_3;

    public List<UserChartDTO> getChart_1() {
        return chart_1;
    }

    public void setChart_1(List<UserChartDTO> chart_1) {
        this.chart_1 = chart_1;
    }

    public List<UserChartDTO> getChart_2() {
        return chart_2;
    }

    public void setChart_2(List<UserChartDTO> chart_2) {
        this.chart_2 = chart_2;
    }

    public List<UserChartDTO> getChart_3() {
        return chart_3;
    }

    public void setChart_3(List<UserChartDTO> chart_3) {
        this.chart_3 = chart_3;
    }
}
