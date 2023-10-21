package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelAdmin;

import java.util.List;

public class ResponseAdmin {
    private boolean error;
    private String message;
    private List<ModelAdmin> admins;


    public ResponseAdmin(boolean error, String message, List<ModelAdmin> admins) {
        this.error = error;
        this.message = message;
        this.admins = admins;
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

    public List<ModelAdmin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<ModelAdmin> admins) {
        this.admins = admins;
    }
}
