package com.socialcodia.stockmanagement.models;

public class ModelCredits {
    private int creditId,creditTotalAmount,creditPaidAmount,creditRemainingAmount;
    private String creditStatus,creditDescription,creditDate;
    private ModelCreditor creditor;

    public ModelCredits(int creditId, int creditTotalAmount, int creditPaidAmount, int creditRemainingAmount, String creditStatus, String creditDescription, String creditDate, ModelCreditor creditor) {
        this.creditId = creditId;
        this.creditTotalAmount = creditTotalAmount;
        this.creditPaidAmount = creditPaidAmount;
        this.creditRemainingAmount = creditRemainingAmount;
        this.creditStatus = creditStatus;
        this.creditDescription = creditDescription;
        this.creditDate = creditDate;
        this.creditor = creditor;
    }

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    public int getCreditTotalAmount() {
        return creditTotalAmount;
    }

    public void setCreditTotalAmount(int creditTotalAmount) {
        this.creditTotalAmount = creditTotalAmount;
    }

    public int getCreditPaidAmount() {
        return creditPaidAmount;
    }

    public void setCreditPaidAmount(int creditPaidAmount) {
        this.creditPaidAmount = creditPaidAmount;
    }

    public int getCreditRemainingAmount() {
        return creditRemainingAmount;
    }

    public void setCreditRemainingAmount(int creditRemainingAmount) {
        this.creditRemainingAmount = creditRemainingAmount;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    public String getCreditDescription() {
        return creditDescription;
    }

    public void setCreditDescription(String creditDescription) {
        this.creditDescription = creditDescription;
    }

    public String getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(String creditDate) {
        this.creditDate = creditDate;
    }

    public ModelCreditor getCreditor() {
        return creditor;
    }

    public void setCreditor(ModelCreditor creditor) {
        this.creditor = creditor;
    }
}
