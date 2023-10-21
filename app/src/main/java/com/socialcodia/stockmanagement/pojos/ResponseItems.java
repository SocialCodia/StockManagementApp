package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelItem;

import java.util.List;

public class ResponseItems {
    private boolean error;
    private String message;
    private List<ModelItem> items;

    public ResponseItems(boolean error, String message, List<ModelItem> items) {
        this.error = error;
        this.message = message;
        this.items = items;
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

    public List<ModelItem> getItems() {
        return items;
    }

    public void setItems(List<ModelItem> items) {
        this.items = items;
    }
}
