package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelProduct;

public class ResponseProduct {
    private boolean error;
    private String message;
    ModelProduct products;

    public ResponseProduct(boolean error, String message, ModelProduct products) {
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

    public ModelProduct getProducts() {
        return products;
    }

    public void setProducts(ModelProduct products) {
        this.products = products;
    }
}
