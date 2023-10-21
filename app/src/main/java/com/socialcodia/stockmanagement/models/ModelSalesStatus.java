package com.socialcodia.stockmanagement.models;

public class ModelSalesStatus {
    private String month,totalSales;

    public ModelSalesStatus(String month, String totalSales) {
        this.month = month;
        this.totalSales = totalSales;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(String totalSales) {
        this.totalSales = totalSales;
    }
}
