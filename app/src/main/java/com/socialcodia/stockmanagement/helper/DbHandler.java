package com.socialcodia.stockmanagement.helper;

import com.socialcodia.stockmanagement.SocialCodia;
import com.socialcodia.stockmanagement.models.ModelProduct;

import java.util.List;

public class DbHandler extends SocialCodia {

    public static int productId;
    private static List<ModelProduct> modelProductList;

    public static List<ModelProduct> getModelProductList() {
        return modelProductList;
    }

    public static void setModelProductList(List<ModelProduct> modelProductList) {
        DbHandler.modelProductList = modelProductList;
    }

//
//    public static List<ModelSale> getModelSalesList() {
//        return modelSales;
//    }

    public static void setProductId(int pid) {
        productId = pid;
    }

    public static int getProductId() {
        return productId;
    }

    public static void removeProductId()
    {
        productId = 0;
    }

    //No in use
//    public static void sellProduct(int productId) {
//        Call<ResponseSale> call = ApiClient.getInstance().getApi().sellProductById(Helper.getToken(), productId);
//        call.enqueue(new Callback<ResponseSale>() {
//            @Override
//            public void onResponse(Call<ResponseSale> call, Response<ResponseSale> response) {
//                if (response.isSuccessful()) {
//                    ResponseSale resp = response.body();
//                    if (!resp.isError()) {
//                        ModelSale sale = resp.getProduct();
//                        DbHandler.addDataToModelSale(sale);
//                    } else
//                        Log.d("TAG", "onResponse: thgis sis demo" + resp.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseSale> call, Throwable t) {
//
//            }
//        });
//    }
}
