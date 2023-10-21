package com.socialcodia.stockmanagement.pojos;

import com.socialcodia.stockmanagement.models.ModelBrand;

import java.util.List;

public class ResponseBrand {
    private boolean error;
    private String message;
    private List<ModelBrand> brands;


    public ResponseBrand(boolean error, String message, List<ModelBrand> brands) {
        this.error = error;
        this.message = message;
        this.brands = brands;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ModelBrand> getBrands() {
        return brands;
    }

    public void setBrands(List<ModelBrand> brands) {
        this.brands = brands;
    }
}
