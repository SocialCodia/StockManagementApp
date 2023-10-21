package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelSale;

import java.util.List;

public class ResponseSales {
    private boolean error;
    private String message;
    private List<ModelSale> sales;

    public ResponseSales(boolean error, String message, List<ModelSale> sales) {
        this.error = error;
        this.message = message;
        this.sales = sales;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ModelSale> getSales() {
        return sales;
    }

    public void setSales(List<ModelSale> sales) {
        this.sales = sales;
    }
}
