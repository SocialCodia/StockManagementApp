package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelSale;

public class ResponseSale {
    private boolean error;
    private String message;
    private ModelSale product;

    public ResponseSale(boolean error, String message, ModelSale product) {
        this.error = error;
        this.message = message;
        this.product = product;
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

    public ModelSale getProduct() {
        return product;
    }

    public void setProduct(ModelSale product) {
        this.product = product;
    }
}
