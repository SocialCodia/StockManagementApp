package com.socialcodia.stockmanagement.models;

public class ModelDailySalesStatus {
    private String day,totalSales;

    public ModelDailySalesStatus(String day, String totalSales) {
        this.day = day;
        this.totalSales = totalSales;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(String totalSales) {
        this.totalSales = totalSales;
    }
}
