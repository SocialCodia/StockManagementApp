package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelLocation;

import java.util.List;

public class ResponseLocation {
    private boolean error;
    private String message;
    private List<ModelLocation> locations;

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

    public List<ModelLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<ModelLocation> locations) {
        this.locations = locations;
    }
}
