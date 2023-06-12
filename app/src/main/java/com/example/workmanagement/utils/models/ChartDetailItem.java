package com.example.workmanagement.utils.models;

public class ChartDetailItem {
    private String id;
    private String name;
    private String number_tasks;

    private int color;

    public String getId() {
        return id;
    }

    public ChartDetailItem() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber_tasks() {
        return number_tasks;
    }

    public void setNumber_tasks(String number_tasks) {
        this.number_tasks = number_tasks;
    }

    public ChartDetailItem(String id, String name, String number_tasks, int color) {
        this.id = id;
        this.name = name;
        this.number_tasks = number_tasks;
        this.color = color;
    }

    public ChartDetailItem(String id, String name, String number_tasks) {
        this.id = id;
        this.name = name;
        this.number_tasks = number_tasks;
    }
}
