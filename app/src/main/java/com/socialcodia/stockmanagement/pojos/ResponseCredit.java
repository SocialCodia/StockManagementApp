package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelCredit;

public class ResponseCredit {
    private boolean error;
    private String message;
    private ModelCredit credit;

    public ResponseCredit(boolean error, String message, ModelCredit credit) {
        this.error = error;
        this.message = message;
        this.credit = credit;
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

    public ModelCredit getCredit() {
        return credit;
    }

    public void setCredit(ModelCredit credit) {
        this.credit = credit;
    }
}
