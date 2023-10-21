package com.socialcodia.stockmanagement.apis;

import com.socialcodia.stockmanagement.pojos.ResponseAdmin;
import com.socialcodia.stockmanagement.pojos.ResponseBrand;
import com.socialcodia.stockmanagement.pojos.ResponseCategory;
import com.socialcodia.stockmanagement.pojos.ResponseCounts;
import com.socialcodia.stockmanagement.pojos.ResponseCredit;
import com.socialcodia.stockmanagement.pojos.ResponseCreditPayment;
import com.socialcodia.stockmanagement.pojos.ResponseCredits;
import com.socialcodia.stockmanagement.pojos.ResponseDailySalesStatus;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;
import com.socialcodia.stockmanagement.pojos.ResponseInvoiceSingle;
import com.socialcodia.stockmanagement.pojos.ResponseInvoices;
import com.socialcodia.stockmanagement.pojos.ResponseItems;
import com.socialcodia.stockmanagement.pojos.ResponseLocation;
import com.socialcodia.stockmanagement.pojos.ResponseLogin;
import com.socialcodia.stockmanagement.pojos.ResponsePayments;
import com.socialcodia.stockmanagement.pojos.ResponseProduct;
import com.socialcodia.stockmanagement.pojos.ResponseProducts;
import com.socialcodia.stockmanagement.pojos.ResponseSale;
import com.socialcodia.stockmanagement.pojos.ResponseSales;
import com.socialcodia.stockmanagement.pojos.ResponseSeller;
import com.socialcodia.stockmanagement.pojos.ResponseSellers;
import com.socialcodia.stockmanagement.pojos.ResponseSize;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface Api {

    @FormUrlEncoded
    @POST("login")
        Call<ResponseLogin> login(
                @Field("email") String email,
                @Field("password") String password
    );

    @FormUrlEncoded
    @POST("product/add")
    Call<ResponseDefault> addProduct(
      @Header("token") String token,
      @Field("productName") int itemId,
      @Field("productBrand") int brandId,
      @Field("productCategory") int categoryId,
      @Field("productSize") int sizeId,
      @Field("productLocation") int locationId,
      @Field("productPrice") String productPrice,
      @Field("productQuantity") String productQuantity,
      @Field("productManufactureDate") String productManufactureDate,
      @Field("productExpireDate") String productExpireDate,
      @Field("productBarCode") String  productBarCode
    );

    @FormUrlEncoded
    @POST("product/update")
    Call<ResponseDefault> updateProduct(
            @Header("token") String token,
            @Field("productId") int productId,
            @Field("productName") int itemId,
            @Field("productBrand") int brandId,
            @Field("productCategory") int categoryId,
            @Field("productSize") int sizeId,
            @Field("productLocation") int locationId,
            @Field("productPrice") String productPrice,
            @Field("productQuantity") String productQuantity,
            @Field("productManufactureDate") String productManufactureDate,
            @Field("productExpireDate") String productExpireDate,
            @Field("productBarCode") String  productBarCode
    );


    @FormUrlEncoded
    @POST("product/sell/update")
    Call<ResponseDefault> updateSale(
      @Header("token") String token,
      @Field("saleId") int saleId,
      @Field("productQuantity") int productQuantity,
      @Field("productSellDiscount") int productSellDiscount,
      @Field("productSellPrice") int productSellPrice
    );

    @GET("products")
    Call<ResponseProducts> getAllProducts(
            @Header("token") String token
    );

    @GET("products/available")
    Call<ResponseProducts> getAvailableProducts(
            @Header("token") String token
    );

    @GET("admins")
    Call<ResponseAdmin> getAdmin(
            @Header("token") String token
    );

    @GET("products/expired")
    Call<ResponseProducts> getExpiredProducts(
      @Header("token") String token
    );

    @GET("products/expiring")
    Call<ResponseProducts> getExpiringProducts(
            @Header("token") String token
    );

    @GET("products/records")
    Call<ResponseProducts> getProductsRecord(
            @Header("token") String token
    );

    @GET("counts")
    Call<ResponseCounts> getCounts(
            @Header("token") String token
    );

    @GET("products/notice")
    Call<ResponseProducts> getNoticeProducts(
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("product/sell/barcode")
    Call<ResponseSale> sellProductByBarCode(
            @Header("token") String token,
      @Field("barCode") String barCode
    );

    @FormUrlEncoded
    @POST("seller/product/sell/barcode")
    Call<ResponseSale> sellSellerProductByBarCode(
            @Header("token") String token,
            @Field("barCode") String barCode,
            @Field("invoiceNumber") String invoiceNumber
    );

    @FormUrlEncoded
    @POST("product/sell")
    Call<ResponseSale> sellProductById(
            @Header("token") String token,
            @Field("productId") int productId
    );

    @FormUrlEncoded
    @POST("seller/product/sell")
    Call<ResponseSale> sellSellerProductById(
            @Header("token") String token,
            @Field("productId") int productId,
            @Field("invoiceNumber") String invoiceNumber
    );

    @FormUrlEncoded
    @POST("seller/product/sell/update")
    Call<ResponseDefault> updateSellerSale(
            @Header("token") String token,
            @Field("saleId") int saleId,
            @Field("productQuantity") int productQuantity,
            @Field("sellDiscount") int productSellDiscount,
            @Field("productSellPrice") int productSellPrice
    );

    @GET("credits")
    Call<ResponseCredits> getCredits(
            @Header("token") String token
    );

    @GET("credit/{creditId}")
    Call<ResponseCredit> getCreditById(
            @Header("token") String token,
            @Path("creditId") int creditId
    );

    @GET("product/{productId}")
    Call<ResponseProduct> getProductById(
      @Header("token") String token,
      @Path("productId") int productId
    );

    @FormUrlEncoded
    @POST("credit/payment/add")
    Call<ResponseDefault> addCreditPayment(
            @Header("token") String token,
            @Field("paymentAmount") int paymentAmount,
            @Field("creditId") int creditId
    );

    @GET("credit/{creditId}/payments")
    Call<ResponseCreditPayment> getCreditPayments(
      @Header("token") String token,
      @Path("creditId") int creditId
    );


    @GET("sales/today")
    Call<ResponseSales> getTodaySales(
            @Header("token") String token
    );

    @GET("sales/all")
    Call<ResponseSales> getAllSales(
            @Header("token") String token
    );

    @GET("sales/status/days")
    Call<ResponseDailySalesStatus> getDailySalesStatus(
            @Header("token") String token
    );

//    @GET("sales/status/monthly")

    @GET("sellers")
    Call<ResponseSellers> getAllSellers(
      @Header("token") String token
    );

    @GET("invoice/{invoiceNumber}")
    Call<ResponseInvoiceSingle> getInvoiceByInvoiceNumber(
            @Header("token") String token,
            @Path("invoiceNumber") String invoiceNumber
    );

    @GET("invoice/{invoiceNumber}/payments")
    Call<ResponsePayments> getInvoicePaymentsByInvoiceNumber(
            @Header("token") String token,
            @Path("invoiceNumber") String invoiceNumber
    );

    @GET("seller/{sellerId}")
    Call<ResponseSeller> getSellerById(
            @Header("token") String token,
            @Path("sellerId") int sellerId
    );

    @GET("seller/{sellerId}/invoice")
    Call<ResponseInvoices> getInvoiceBySellerId(
            @Header("token") String token,
            @Path("sellerId") int sellerId
    );

    @FormUrlEncoded
    @POST("payment/add")
    Call<ResponseDefault> addPayment(
            @Header("token") String token,
            @Field("sellerId") int sellerId,
            @Field("invoiceNumber") String invoiceNumber,
            @Field("paymentAmount") int paymentAmount
    );

    @GET("brands")
    Call<ResponseBrand> getBrands(
            @Header("token") String token
    );

    @GET("categories")
    Call<ResponseCategory> getCategories(
            @Header("token") String token
    );

    @GET("sizes")
    Call<ResponseSize> getSizes(
            @Header("token") String token
    );

    @GET("locations")
    Call<ResponseLocation> getLocations(
            @Header("token") String token
    );

    @GET("items")
    Call<ResponseItems> getItems(
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("seller/add")
    Call<ResponseDefault> addSeller(
            @Header("token") String token,
            @Field("sellerName") String sellerName,
            @Field("sellerEmail") String sellerEmail,
            @Field("sellerContactNumber") String mobileNumber,
            @Field("sellerContactNumber1") String mobileNumber1,
            @Field("sellerAddress") String sellerAddress
    );

    @Multipart
    @POST("seller/add")
    Call<ResponseDefault> addSellerWithImage(
            @Header("token") String token,
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("admin/add")
    Call<ResponseDefault> addAdmin(
            @Header("token") String token,
            @Field("adminName") String adminName,
            @Field("adminEmail") String adminEmail,
            @Field("adminPassword") String adminPassword,
            @Field("adminPosition") String adminPosition,
            @Field("currentAdminPassword") String currentAdminPassword
    );

    @FormUrlEncoded
    @POST("admin/update/self")
    Call<ResponseDefault> updateAdminSelf(
            @Header("token") String token,
            @Field("adminName") String adminName
    );

    @FormUrlEncoded
    @POST("admin/update/self")
    Call<ResponseDefault> updateAdminSelfWithImage(
      @Header("token") String token,
      @PartMap Map<String, RequestBody> map ,
      @Part MultipartBody.Part image
    );

    @Multipart
    @POST("admin/add")
    Call<ResponseDefault> addAdminWithImage(
            @Header("token") String token,
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("item/add")
    Call<ResponseDefault> addItem(
            @Header("token") String token,
            @Field("itemName") String itemName,
            @Field("itemDescription") String itemDescription
    );

    @FormUrlEncoded
    @POST("brand/add")
    Call<ResponseDefault> addBrand(
            @Header("token") String token,
            @Field("brandName") String brandName
    );

    @FormUrlEncoded
    @POST("category/add")
    Call<ResponseDefault> addCategory(
            @Header("token") String token,
            @Field("categoryName") String categoryName
    );

    @FormUrlEncoded
    @POST("size/add")
    Call<ResponseDefault> addSize(
            @Header("token") String token,
            @Field("sizeName") String sizeName
    );

    @FormUrlEncoded
    @POST("location/add")
    Call<ResponseDefault> addLocation(
            @Header("token") String token,
            @Field("locationName") String locationName
    );

    @GET("invoices")
    Call<ResponseInvoices> getAllInvoice(
      @Header("token") String token
    );

    @FormUrlEncoded
    @POST("invoice/add")
    Call<ResponseInvoiceSingle> addInvoice(
            @Header("token") String token,
            @Field("sellerId") int sellerId
    );

    @FormUrlEncoded
    @POST("invoice/delete")
    Call<ResponseDefault> deleteInvoice(
            @Header("token") String token,
            @Field("invoiceNumber") String invoiceNumber
    );

    @FormUrlEncoded
    @POST("product/sell/delete")
    Call<ResponseDefault> deleteSoldProduct(
            @Field("sellId") int sellId,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("seller/product/sell/delete")
    Call<ResponseDefault> deleteSellerSoldProduct(
            @Field("sellId") int sellId,
            @Header("token") String token
    );

    @GET("invoice/{invoiceNumber}/pdf")
    Call<ResponseInvoiceSingle> getInvoiceUrlByInvoiceNumber(
            @Header("token") String token,
            @Path("invoiceNumber") String  invoiceNumber
    );

    @FormUrlEncoded
    @POST("firebaseToken/update/android")
    Call<ResponseDefault> updateFirebaseToken(
      @Header("token") String token,
      @Field("androidToken") String androidToken
    );

    @FormUrlEncoded
    @POST("password/update")
    Call<ResponseDefault> updatePassword(
            @Header("token") String token,
            @Field("password") String password,
            @Field("newPassword") String newPassword
    );

    @FormUrlEncoded
    @POST("product/sell/credit")
    Call<ResponseDefault> addCreditRecord(
      @Header("token") String token,
      @Field("creditorName") String creditorName,
      @Field("creditorMobile") String creditorMobile,
      @Field("creditorAddress") String creditorAddress,
      @Field("paidAmount") String paidAmount,
      @Field("creditDescription") String creditDescription,
      @Field("salesId") String salesId
    );

}
