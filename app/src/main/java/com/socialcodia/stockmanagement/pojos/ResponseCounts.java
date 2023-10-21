package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelCount;

public class ResponseCounts {
    private boolean error;
    private String message;
    private ModelCount counts;

    public ResponseCounts(boolean error, String message, ModelCount counts) {
        this.error = error;
        this.message = message;
        this.counts = counts;
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

    public ModelCount getCounts() {
        return counts;
    }

    public void setCounts(ModelCount counts) {
        this.counts = counts;
    }
}
