package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelSalesStatus;

import java.util.List;

public class ResponseSalesStatus
{
    private boolean error;
    private String message;
    private List<ModelSalesStatus> status;

    public ResponseSalesStatus(boolean error, String message, List<ModelSalesStatus> status) {
        this.error = error;
        this.message = message;
        this.status = status;
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

    public List<ModelSalesStatus> getStatus() {
        return status;
    }

    public void setStatus(List<ModelSalesStatus> status) {
        this.status = status;
    }
}
