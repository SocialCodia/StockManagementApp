package com.socialcodia.stockmanagement.models;

public class ModelCreditor {
    private int creditorId;
    private String creditorName,creditorMobile,creditorAddress;

    public ModelCreditor(int creditorId, String creditorName, String creditorMobile, String creditorAddress) {
        this.creditorId = creditorId;
        this.creditorName = creditorName;
        this.creditorMobile = creditorMobile;
        this.creditorAddress = creditorAddress;
    }

    public int getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(int creditorId) {
        this.creditorId = creditorId;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorMobile() {
        return creditorMobile;
    }

    public void setCreditorMobile(String creditorMobile) {
        this.creditorMobile = creditorMobile;
    }

    public String getCreditorAddress() {
        return creditorAddress;
    }

    public void setCreditorAddress(String creditorAddress) {
        this.creditorAddress = creditorAddress;
    }
}
