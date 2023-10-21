package com.socialcodia.stockmanagement.models;

public class ModelSeller {
    private int sellerId;
    private String sellerName,sellerEmail,sellerContactNumber,sellerContactNumber1,sellerImage,sellerAddress;


    public ModelSeller(int sellerId, String sellerName, String sellerEmail, String sellerContactNumber, String sellerContactNumber1, String sellerImage, String sellerAddress) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.sellerContactNumber = sellerContactNumber;
        this.sellerContactNumber1 = sellerContactNumber1;
        this.sellerImage = sellerImage;
        this.sellerAddress = sellerAddress;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
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

    public String getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(String sellerImage) {
        this.sellerImage = sellerImage;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }
}
