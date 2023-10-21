package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelProduct;

import java.util.List;

public class ResponseProducts
{
    private boolean error;
    private String message;
    private List<ModelProduct> products;

    public ResponseProducts(boolean error, String message, List<ModelProduct> products) {
        this.error = error;
        this.message = message;
        this.products = products;
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

    public List<ModelProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ModelProduct> products) {
        this.products = products;
    }
}
