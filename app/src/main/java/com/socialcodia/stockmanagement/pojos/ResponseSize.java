package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelSize;

import java.util.List;

public class ResponseSize {

    private boolean  error;
    private String message;
    private List<ModelSize> sizes;

    public ResponseSize(boolean error, String message, List<ModelSize> sizes) {
        this.error = error;
        this.message = message;
        this.sizes = sizes;
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

    public List<ModelSize> getSizes() {
        return sizes;
    }

    public void setSizes(List<ModelSize> sizes) {
        this.sizes = sizes;
    }
}
