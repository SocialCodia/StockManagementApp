package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelCredits;

import java.util.List;

public class ResponseCredits {
    private boolean error;
    private String message;
    private List<ModelCredits> credits;

    public ResponseCredits(boolean error, String message, List<ModelCredits> credits) {
        this.error = error;
        this.message = message;
        this.credits = credits;
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

    public List<ModelCredits> getCredits() {
        return credits;
    }

    public void setCredits(List<ModelCredits> credits) {
        this.credits = credits;
    }
}
