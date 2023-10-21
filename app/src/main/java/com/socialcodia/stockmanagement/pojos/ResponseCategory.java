package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelCategory;

import java.util.List;

public class ResponseCategory {
    private boolean error;
    private String message;
    private List<ModelCategory> categories;

    public ResponseCategory(boolean error, String message, List<ModelCategory> categories) {
        this.error = error;
        this.message = message;
        this.categories = categories;
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

    public List<ModelCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ModelCategory> categories) {
        this.categories = categories;
    }
}
