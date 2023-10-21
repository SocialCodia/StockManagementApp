package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelSeller;

import java.util.List;

public class ResponseSellers {
    private boolean error;
    private String message;
    private List<ModelSeller> sellers;

    public ResponseSellers(boolean error, String message, List<ModelSeller> sellers) {
        this.error = error;
        this.message = message;
        this.sellers = sellers;
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

    public List<ModelSeller> getSellers() {
        return sellers;
    }

    public void setSellers(List<ModelSeller> sellers) {
        this.sellers = sellers;
    }
}
