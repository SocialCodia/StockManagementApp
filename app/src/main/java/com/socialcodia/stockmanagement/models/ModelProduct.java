package com.socialcodia.stockmanagement.models;

public class ModelProduct {
    private int productId;
    private String productCategory,productName,productSize,productBrand;
    private int productPrice,productQuantity;
    private String productLocation,productManufacture,productExpire,barCode;


    public ModelProduct(int productId, String productCategory, String productName, String productSize, String productBrand, int productPrice, int productQuantity, String productLocation, String productManufacture, String productExpire, String barCode) {
        this.productId = productId;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productSize = productSize;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productLocation = productLocation;
        this.productManufacture = productManufacture;
        this.productExpire = productExpire;
        this.barCode = barCode;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public String getProductManufacture() {
        return productManufacture;
    }

    public void setProductManufacture(String productManufacture) {
        this.productManufacture = productManufacture;
    }

    public String getProductExpire() {
        return productExpire;
    }

    public void setProductExpire(String productExpire) {
        this.productExpire = productExpire;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
