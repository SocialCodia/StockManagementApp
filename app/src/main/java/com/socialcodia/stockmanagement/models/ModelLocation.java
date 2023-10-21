package com.socialcodia.stockmanagement.models;

public class ModelLocation {
    private int locationId;
    private String locationName;

    public ModelLocation(int locationId, String locationName) {
        this.locationId = locationId;
        this.locationName = locationName;
    }


    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
