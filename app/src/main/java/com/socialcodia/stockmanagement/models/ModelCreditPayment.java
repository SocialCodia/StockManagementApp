package com.socialcodia.stockmanagement.models;

public class ModelCreditPayment {
    private int paymentId,creditId;
    private String paymentDate;
    private int paymentAmount,creditorId;

    public ModelCreditPayment(int paymentId, int creditId, String paymentDate, int paymentAmount, int creditorId) {
        this.paymentId = paymentId;
        this.creditId = creditId;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.creditorId = creditorId;
    }

    public ModelCreditPayment() {
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public int getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(int creditorId) {
        this.creditorId = creditorId;
    }
}
