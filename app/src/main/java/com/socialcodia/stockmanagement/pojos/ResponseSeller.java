package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelSeller;

public class ResponseSeller {
    private boolean error;
    private String message;
    private ModelSeller seller;

    public ResponseSeller(boolean error, String message, ModelSeller seller) {
        this.error = error;
        this.message = message;
        this.seller = seller;
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

    public ModelSeller getSeller() {
        return seller;
    }

    public void setSeller(ModelSeller seller) {
        this.seller = seller;
    }
}
