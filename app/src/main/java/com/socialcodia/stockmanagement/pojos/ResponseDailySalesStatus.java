package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelDailySalesStatus;

import java.util.List;

public class ResponseDailySalesStatus {
    private boolean error;
    private String message;
    private List<ModelDailySalesStatus> status;

    public ResponseDailySalesStatus(boolean error, String message, List<ModelDailySalesStatus> status) {
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

    public List<ModelDailySalesStatus> getStatus() {
        return status;
    }

    public void setStatus(List<ModelDailySalesStatus> status) {
        this.status = status;
    }
}
