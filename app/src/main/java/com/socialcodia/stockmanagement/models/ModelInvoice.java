package com.socialcodia.stockmanagement.models;

public class ModelInvoice {

    private int invoiceId;
    private String invoiceNumber,invoiceDate;
    private int invoiceAmount,invoicePaidAmount,invoiceRemainingAmount;
    private String invoiceStatus,sellerName,sellerImage;
    private int sellerId;
    private String sellerContactNumber,sellerContactNumber1,sellerAddress,invoiceUrl;

    public ModelInvoice(int invoiceId, String invoiceNumber, String invoiceDate, int invoiceAmount, int invoicePaidAmount, int invoiceRemainingAmount, String invoiceStatus, String sellerName, String sellerImage, int sellerId, String sellerContactNumber, String sellerContactNumber1, String sellerAddress, String invoiceUrl) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.invoiceAmount = invoiceAmount;
        this.invoicePaidAmount = invoicePaidAmount;
        this.invoiceRemainingAmount = invoiceRemainingAmount;
        this.invoiceStatus = invoiceStatus;
        this.sellerName = sellerName;
        this.sellerImage = sellerImage;
        this.sellerId = sellerId;
        this.sellerContactNumber = sellerContactNumber;
        this.sellerContactNumber1 = sellerContactNumber1;
        this.sellerAddress = sellerAddress;
        this.invoiceUrl = invoiceUrl;
    }

    public ModelInvoice(int invoiceId, String invoiceNumber, String invoiceDate, int invoiceAmount, int invoicePaidAmount, int invoiceRemainingAmount, String invoiceStatus, String sellerName, String sellerImage, int sellerId, String sellerContactNumber, String sellerContactNumber1, String sellerAddress) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.invoiceAmount = invoiceAmount;
        this.invoicePaidAmount = invoicePaidAmount;
        this.invoiceRemainingAmount = invoiceRemainingAmount;
        this.invoiceStatus = invoiceStatus;
        this.sellerName = sellerName;
        this.sellerImage = sellerImage;
        this.sellerId = sellerId;
        this.sellerContactNumber = sellerContactNumber;
        this.sellerContactNumber1 = sellerContactNumber1;
        this.sellerAddress = sellerAddress;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(int invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public int getInvoicePaidAmount() {
        return invoicePaidAmount;
    }

    public void setInvoicePaidAmount(int invoicePaidAmount) {
        this.invoicePaidAmount = invoicePaidAmount;
    }

    public int getInvoiceRemainingAmount() {
        return invoiceRemainingAmount;
    }

    public void setInvoiceRemainingAmount(int invoiceRemainingAmount) {
        this.invoiceRemainingAmount = invoiceRemainingAmount;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(String sellerImage) {
        this.sellerImage = sellerImage;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerContactNumber() {
        return sellerContactNumber;
    }

    public void setSellerContactNumber(String sellerContactNumber) {
        this.sellerContactNumber = sellerContactNumber;
    }

    public String getSellerContactNumber1() {
        return sellerContactNumber1;
    }

    public void setSellerContactNumber1(String sellerContactNumber1) {
        this.sellerContactNumber1 = sellerContactNumber1;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }
}
