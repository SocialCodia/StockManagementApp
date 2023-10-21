package com.socialcodia.stockmanagement.models;

public class ModelPayment {
    private int paymentId;
    private String invoiceNumber,paymentDate;
    private int paymentAmount,sellerId;


    public ModelPayment(int paymentId, String invoiceNumber, String paymentDate, int paymentAmount, int sellerId) {
        this.paymentId = paymentId;
        this.invoiceNumber = invoiceNumber;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.sellerId = sellerId;
    }

    public ModelPayment() {
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }
}
