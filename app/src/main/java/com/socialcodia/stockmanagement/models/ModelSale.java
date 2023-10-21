package com.socialcodia.stockmanagement.models;

public class ModelSale {
    private int productId,saleId;
    private String productCategory,productName,productSize,productBrand;
    private int productPrice;
    private String productLocation;
    private int productQuantity;
    private String productManufacture,productExpire;
    private int saleQuantity,saleDiscount,salePrice;
    private String createdAt;

    public int getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(int productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    private int productTotalPrice;

    public ModelSale(int productId, int saleId, String productCategory, String productName, String productSize, String productBrand, int productPrice, String productLocation, int productQuantity, String productManufacture, String productExpire, int saleQuantity, int saleDiscount, int salePrice, String createdAt) {
        this.productId = productId;
        this.saleId = saleId;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productSize = productSize;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.productLocation = productLocation;
        this.productQuantity = productQuantity;
        this.productManufacture = productManufacture;
        this.productExpire = productExpire;
        this.saleQuantity = saleQuantity;
        this.saleDiscount = saleDiscount;
        this.salePrice = salePrice;
        this.createdAt = createdAt;
    }

    public ModelSale(int productId, int saleId, String productCategory, String productName, String productSize, String productBrand, int productPrice, String productLocation, int productQuantity, String productManufacture, String productExpire) {
        this.productId = productId;
        this.saleId = saleId;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productSize = productSize;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.productLocation = productLocation;
        this.productQuantity = productQuantity;
        this.productManufacture = productManufacture;
        this.productExpire = productExpire;
    }

    public ModelSale(int productId, String productCategory, String productName, String productSize, String productBrand, int productPrice, String productManufacture, String productExpire, int saleQuantity, int saleDiscount, int salePrice) {
        this.productId = productId;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productSize = productSize;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.productManufacture = productManufacture;
        this.productExpire = productExpire;
        this.saleQuantity = saleQuantity;
        this.saleDiscount = saleDiscount;
        this.salePrice = salePrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
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

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
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

    public int getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(int saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    public int getSaleDiscount() {
        return saleDiscount;
    }

    public void setSaleDiscount(int saleDiscount) {
        this.saleDiscount = saleDiscount;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
