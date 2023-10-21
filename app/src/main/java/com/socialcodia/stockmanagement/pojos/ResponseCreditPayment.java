package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelCreditPayment;

import java.util.List;

public class ResponseCreditPayment {
    private boolean error;
    private String message;
    private List<ModelCreditPayment> payments;


    public ResponseCreditPayment(boolean error, String message, List<ModelCreditPayment> payments) {
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

    public List<ModelCreditPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<ModelCreditPayment> payments) {
        this.payments = payments;
    }
}
