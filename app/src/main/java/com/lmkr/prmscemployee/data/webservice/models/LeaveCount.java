package com.lmkr.prmscemployee.data.webservice.models;

public class LeaveCount {
    private int id;
    private String type;
    private int total;

    public LeaveCount(int id, String type, int total, float remaining) {
        this.id = id;
        this.type = type;
        this.total = total;
        this.remaining = remaining;
    }

    private float remaining;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public float getRemaining() {
        return remaining;
    }

    public void setRemaining(float remaining) {
        this.remaining = remaining;
    }
}
