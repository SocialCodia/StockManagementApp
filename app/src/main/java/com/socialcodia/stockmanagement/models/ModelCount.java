package com.socialcodia.stockmanagement.models;

public class ModelCount {
    private int productsCount,productsAvailableCount,salesCount,brandsCount,productsNoticeCount,productsExpiringCount,productsExpiredCount;


    public ModelCount(int productsCount, int productsAvailableCount, int salesCount, int brandsCount, int productsNoticeCount, int productsExpiringCount, int productsExpiredCount) {
        this.productsCount = productsCount;
        this.productsAvailableCount = productsAvailableCount;
        this.salesCount = salesCount;
        this.brandsCount = brandsCount;
        this.productsNoticeCount = productsNoticeCount;
        this.productsExpiringCount = productsExpiringCount;
        this.productsExpiredCount = productsExpiredCount;
    }

    public int getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(int productsCount) {
        this.productsCount = productsCount;
    }

    public int getProductsAvailableCount() {
        return productsAvailableCount;
    }

    public void setProductsAvailableCount(int productsAvailableCount) {
        this.productsAvailableCount = productsAvailableCount;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    public int getBrandsCount() {
        return brandsCount;
    }

    public void setBrandsCount(int brandsCount) {
        this.brandsCount = brandsCount;
    }

    public int getProductsNoticeCount() {
        return productsNoticeCount;
    }

    public void setProductsNoticeCount(int productsNoticeCount) {
        this.productsNoticeCount = productsNoticeCount;
    }

    public int getProductsExpiringCount() {
        return productsExpiringCount;
    }

    public void setProductsExpiringCount(int productsExpiringCount) {
        this.productsExpiringCount = productsExpiringCount;
    }

    public int getProductsExpiredCount() {
        return productsExpiredCount;
    }

    public void setProductsExpiredCount(int productsExpiredCount) {
        this.productsExpiredCount = productsExpiredCount;
    }
}
