package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelPayment;

import java.util.List;

public class ResponsePayments {
    private boolean error;
    private String message;
    private List<ModelPayment> payments;

    public ResponsePayments(boolean error, String message, List<ModelPayment> payments) {
        this.error = error;
        this.message = message;
        this.payments = payments;
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

    public List<ModelPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<ModelPayment> payments) {
        this.payments = payments;
    }
}
