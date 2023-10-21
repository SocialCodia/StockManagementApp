package com.socialcodia.stockmanagement.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.etc.Scanner;
import com.socialcodia.stockmanagement.etc.SearchableSpinner;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelBrand;
import com.socialcodia.stockmanagement.models.ModelCategory;
import com.socialcodia.stockmanagement.models.ModelItem;
import com.socialcodia.stockmanagement.models.ModelLocation;
import com.socialcodia.stockmanagement.models.ModelProduct;
import com.socialcodia.stockmanagement.models.ModelSize;
import com.socialcodia.stockmanagement.pojos.ResponseBrand;
import com.socialcodia.stockmanagement.pojos.ResponseCategory;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;
import com.socialcodia.stockmanagement.pojos.ResponseItems;
import com.socialcodia.stockmanagement.pojos.ResponseLocation;
import com.socialcodia.stockmanagement.pojos.ResponseProduct;
import com.socialcodia.stockmanagement.pojos.ResponseSize;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductActivity extends AppCompatActivity {
    private List<ModelBrand> modelBrandList;
    private List<ModelCategory> modelCategoryList;
    private List<ModelSize> modelSizeList;
    private List<ModelLocation> modelLocationList;
    private List<ModelItem> modelItemList;
    private List<ModelProduct> modelProductList;
    private SearchableSpinner spinnerItem, spinnerBrand, spinnerCategory, spinnerSize, spinnerLocation, spinnerManufactureMonth, spinnerManufactureYear, spinnerExpireMonth, spinnerExpireYear;
    private EditText inputProductPrice, inputProductQuantity, inputProductBarcode;
    private TextInputLayout tilBarcode;
    private String itemName, brandName, categoryName, sizeName, locationName, price, quantity, barCode, manMonthName, manYearName, expMonthName, expYearName = "";
    private Button btnUpdateProduct;
    private Intent intent;
    private int productId;
    private ActionBar actionBar;
    private ModelProduct product;
    private String productName, productBrand;
    private boolean fBrand, fCategory, fSize, fLocation, fItem, fProduct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        init();
        actionBar.setTitle("Update Product");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        if (intent.hasExtra("intentProductId")) {
            productId = Integer.parseInt(intent.getStringExtra("intentProductId"));
        } else
            onBackPressed();


        spinnerBrand.setTitle("Select Brand");
        spinnerCategory.setTitle("Select Category");
        spinnerSize.setTitle("Select Size");
        spinnerLocation.setTitle("Select Location");
        spinnerItem.setTitle("Select Item");
        spinnerManufactureMonth.setTitle("Select Manufacture Month");
        spinnerManufactureYear.setTitle("Select Manufacture Year");
        spinnerExpireMonth.setTitle("Select Expire Month");
        spinnerExpireYear.setTitle("Select Expire Year");

        tilBarcode.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

        getProductById();
        getBrands();
        getCategories();
        getSizes();
        getLocations();
        getItems();
        btnUpdateProduct.setOnClickListener(v -> validateData());

    }

    private void getProductById() {
        Call<ResponseProduct> call = ApiClient.getInstance().getApi().getProductById(Helper.getToken(), productId);
        call.enqueue(new Callback<ResponseProduct>() {
            @Override
            public void onResponse(Call<ResponseProduct> call, Response<ResponseProduct> response) {
                if (response.isSuccessful()) {
                    ResponseProduct resp = response.body();
                    if (!resp.isError()) {
                        product = resp.getProducts();
                        productName = product.getProductName();
                        productBrand = product.getProductBrand();
                        modelProductList.add(product);
                        fProduct = true;
                        spinnerSetKar();
                        inputProductPrice.setText(String.valueOf(product.getProductPrice()));
                        inputProductQuantity.setText(String.valueOf(product.getProductQuantity()));
                        inputProductBarcode.setText(String.valueOf(product.getBarCode()));
                    } else {
                        TastyToast.makeText(getApplicationContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        onBackPressed();
                    }
                } else {
                    TastyToast.makeText(getApplicationContext(), "Something Went Wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseProduct> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void scanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(EditProductActivity.this);
        intentIntegrator.setCaptureActivity(Scanner.class);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Sell Products By Scanning Bar Code");
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null)
                inputProductBarcode.setText(String.valueOf(result.getContents()));
            else
                TastyToast.makeText(getApplicationContext(), "Cancelled", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void validateData() {
        price = inputProductPrice.getText().toString().trim();
        quantity = inputProductQuantity.getText().toString().trim();
        if (brandName == null || brandName.isEmpty()) {
            TastyToast.makeText(getApplicationContext(), "Select Brand", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (categoryName == null || categoryName.isEmpty()) {
            TastyToast.makeText(getApplicationContext(), "Select Category", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (sizeName == null || sizeName.isEmpty()) {
            TastyToast.makeText(getApplicationContext(), "Select Size", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (locationName == null || locationName.isEmpty()) {
            TastyToast.makeText(getApplicationContext(), "Select Location", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (itemName == null || itemName.isEmpty()) {
            TastyToast.makeText(getApplicationContext(), "Select Product Name", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (price.isEmpty()) {
            inputProductPrice.setError("Enter Price");
            inputProductPrice.requestFocus();
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (quantity.isEmpty()) {
            inputProductQuantity.setError("Enter Quantity");
            inputProductQuantity.requestFocus();
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (manMonthName == null || manMonthName.isEmpty()) {
            TastyToast.makeText(getApplicationContext(), "Select Manufacture Month", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (manYearName == null || manYearName.isEmpty()) {
            TastyToast.makeText(getApplicationContext(), "Select Manufacture Year", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (expMonthName == null || expMonthName.isEmpty()) {
            TastyToast.makeText(getApplicationContext(), "Select Expire Month", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (expYearName == null || expYearName.isEmpty()) {
            TastyToast.makeText(getApplicationContext(), "Select Expire Year", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
        } else {
            barCode = inputProductBarcode.getText().toString().trim();
            int brandId = getBrandIdByBrandName(brandName);
            int categoryId = getCategoryIdByCategoryName(categoryName);
            int sizeId = getSizeIdBySizeName(sizeName);
            int locationId = getLocationIdByLocationName(locationName);
            int itemId = getItemIdByItemName(itemName);
            String productManufactureDate = manYearName + getMonthNumberByName(manMonthName) + "01";
            String productExpireDate = expYearName + getMonthNumberByName(expMonthName) + "01";
            updateProduct(brandId, categoryId, sizeId, locationId, itemId, price, quantity, productManufactureDate, productExpireDate, barCode);
        }
    }

    private void updateProduct(int brandId, int categoryId, int sizeId, int locationId, int itemId, String price, String quantity, String productManufactureDate, String productExpireDate, String barCode) {
        btnUpdateProduct.setEnabled(false);
        Call<ResponseDefault> call = ApiClient.getInstance().getApi().updateProduct(Helper.getToken(), productId, itemId, brandId, categoryId, sizeId, locationId, price, quantity, productManufactureDate, productExpireDate, barCode);
        call.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                btnUpdateProduct.setEnabled(true);
                if (response.isSuccessful()) {
                    ResponseDefault resp = response.body();
                    if (!resp.isError()) {
                        TastyToast.makeText(getApplicationContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Helper.playVibrate();
                        Helper.playSuccess();
                        inputProductQuantity.setText("");
                    } else {
                        TastyToast.makeText(getApplicationContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        Helper.playVibrate();
                        Helper.playError();
                    }
                } else
                    TastyToast.makeText(getApplicationContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                t.printStackTrace();
                btnUpdateProduct.setEnabled(true);
            }
        });
    }

    private int getBrandIdByBrandName(String brandName) {
        int brandId = 0;
        for (ModelBrand brand : modelBrandList) {
            if (brandName.toLowerCase().trim().equals(brand.getBrandName().toLowerCase().trim())) {
                brandId = brand.getBrandId();
            }
        }
        return brandId;
    }

    private int getCategoryIdByCategoryName(String categoryName) {
        int categoryId = 0;
        for (ModelCategory category : modelCategoryList) {
            if (categoryName.toLowerCase().trim().equals(category.getCategoryName().toLowerCase().trim())) {
                categoryId = category.getCategoryId();
            }
        }
        return categoryId;
    }

    private int getSizeIdBySizeName(String sizeName) {
        int sizeId = 0;
        for (ModelSize size : modelSizeList) {
            if (sizeName.toLowerCase().trim().equals(size.getSizeName().toLowerCase().trim())) {
                sizeId = size.getSizeId();
            }
        }
        return sizeId;
    }

    private int getLocationIdByLocationName(String locationName) {
        int locationId = 0;
        for (ModelLocation location : modelLocationList) {
            if (locationName.toLowerCase().trim().equals(location.getLocationName().toLowerCase().trim())) {
                locationId = location.getLocationId();
            }
        }
        return locationId;
    }

    private int getItemIdByItemName(String itemName) {
        int itemId = 0;
        for (ModelItem item : modelItemList) {
            Log.d("SocialCodia", "main: " + item.getItemName());
            if (itemName.toLowerCase().trim().equals(item.getItemName().toLowerCase().trim())) {
                Log.d("SocailCodia", "Codition: Condtion True");
                itemId = item.getItemId();
            }
        }
        return itemId;
    }

    private void getBrands() {
        Log.d("TAG", "getBrands: ");
        Call<ResponseBrand> call = ApiClient.getInstance().getApi().getBrands(Helper.getToken());
        call.enqueue(new Callback<ResponseBrand>() {
            @Override
            public void onResponse(Call<ResponseBrand> call, Response<ResponseBrand> response) {
                if (response.isSuccessful()) {
                    ResponseBrand resp = response.body();
                    if (!resp.isError()) {
                        modelBrandList = resp.getBrands();
                        fBrand = true;
                        spinnerSetKar();
                    } else
                        TastyToast.makeText(getApplicationContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else
                    TastyToast.makeText(getApplicationContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFailure(Call<ResponseBrand> call, Throwable t) {

            }
        });
    }

    private void getCategories() {
        Call<ResponseCategory> call = ApiClient.getInstance().getApi().getCategories(Helper.getToken());
        call.enqueue(new Callback<ResponseCategory>() {
            @Override
            public void onResponse(Call<ResponseCategory> call, Response<ResponseCategory> response) {
                if (response.isSuccessful()) {
                    ResponseCategory resp = response.body();
                    if (!resp.isError()) {
                        modelCategoryList = resp.getCategories();
                        fCategory = true;
                        spinnerSetKar();
                    } else
                        TastyToast.makeText(getApplicationContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else
                    TastyToast.makeText(getApplicationContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFailure(Call<ResponseCategory> call, Throwable t) {

            }
        });
    }

    private void getSizes() {
        Call<ResponseSize> call = ApiClient.getInstance().getApi().getSizes(Helper.getToken());
        call.enqueue(new Callback<ResponseSize>() {
            @Override
            public void onResponse(Call<ResponseSize> call, Response<ResponseSize> response) {
                if (response.isSuccessful()) {
                    ResponseSize resp = response.body();
                    if (!resp.isError()) {
                        modelSizeList = resp.getSizes();
                        fSize = true;
                        spinnerSetKar();
                    } else
                        TastyToast.makeText(getApplicationContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else
                    TastyToast.makeText(getApplicationContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFailure(Call<ResponseSize> call, Throwable t) {

            }
        });
    }

    private void getLocations() {
        Call<ResponseLocation> call = ApiClient.getInstance().getApi().getLocations(Helper.getToken());
        call.enqueue(new Callback<ResponseLocation>() {
            @Override
            public void onResponse(Call<ResponseLocation> call, Response<ResponseLocation> response) {
                if (response.isSuccessful()) {
                    ResponseLocation resp = response.body();
                    if (!resp.isError()) {
                        modelLocationList = resp.getLocations();
                        fLocation = true;
                        spinnerSetKar();
                    } else
                        TastyToast.makeText(getApplicationContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else
                    TastyToast.makeText(getApplicationContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFailure(Call<ResponseLocation> call, Throwable t) {

            }
        });
    }

    private void getItems() {
        Call<ResponseItems> call = ApiClient.getInstance().getApi().getItems(Helper.getToken());
        call.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                if (response.isSuccessful()) {
                    ResponseItems resp = response.body();
                    if (!resp.isError()) {
                        modelItemList = resp.getItems();
                        fItem = true;
                        spinnerSetKar();
                    } else
                        TastyToast.makeText(getApplicationContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else
                    TastyToast.makeText(getApplicationContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {

            }
        });
    }

    private String getMonthNameByNumber(String number) {
        String monthName = "";
        switch (number) {
            case "01":
                monthName = "January";
                break;
            case "02":
                monthName = "February";
                break;
            case "03":
                monthName = "March";
                break;
            case "04":
                monthName = "April";
                break;
            case "05":
                monthName = "May";
                break;
            case "06":
                monthName = "June";
                break;
            case "07":
                monthName = "July";
                break;
            case "08":
                monthName = "August";
                break;
            case "09":
                monthName = "September";
                break;
            case "10":
                monthName = "October";
                break;
            case "11":
                monthName = "November";
                break;
            case "12":
                monthName = "December";
                break;

        }
        return monthName;
    }

    private void setBrandSpinner(List<ModelBrand> modelBrandList) {
        ArrayList<String> list = new ArrayList<>();
        for (ModelBrand brand : modelBrandList) {
            list.add(brand.getBrandName());
        }

        spinnerBrand.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list));
        int index = list.indexOf(productBrand);
        Log.d("TAG", "setBrandSpinner: index is" + index + "product brand is " + productBrand);
        spinnerBrand.setSelectionM(index);
        spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                brandName = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void spinnerSetKar() {
        Log.d("TAG", "spinnerSetKar: Condition False" + fBrand + fCategory + fSize + fLocation + fItem + fProduct);
        if (fBrand && fCategory && fSize && fLocation && fItem && fProduct) {
            Log.d("TAG", "spinnerSetKar: Condition True");
            setBrandSpinner(modelBrandList);
            setCategorySpinner(modelCategoryList);
            setSizeSpinner(modelSizeList);
            setLocationSpinner(modelLocationList);
            setItemsSpinner(modelItemList);
            setSpinner();
        }
    }

    private void setCategorySpinner(List<ModelCategory> modelCategoryList) {
        ArrayList<String> list = new ArrayList<>();
        for (ModelCategory category : modelCategoryList) {
            list.add(category.getCategoryName());
        }

        spinnerCategory.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list));
        int index = list.indexOf(product.getProductCategory());
        spinnerCategory.setSelectionM(index);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryName = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSizeSpinner(List<ModelSize> modelSizeList) {
        ArrayList<String> list = new ArrayList<>();
        for (ModelSize size : modelSizeList) {
            list.add(size.getSizeName());
        }

        spinnerSize.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list));
        int index = list.indexOf(product.getProductSize());
        spinnerSize.setSelectionM(index);
        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sizeName = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setLocationSpinner(List<ModelLocation> modelLocationList) {
        ArrayList<String> list = new ArrayList<>();
        for (ModelLocation location : modelLocationList) {
            list.add(location.getLocationName());
        }

        spinnerLocation.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list));
        int index = list.indexOf(product.getProductLocation());
        spinnerLocation.setSelectionM(index);
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationName = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinner() {
        ArrayList<String> manMonth = new ArrayList<>();
        ArrayList<String> manYear = new ArrayList<>();
        ArrayList<String> expMonth = new ArrayList<>();
        ArrayList<String> expYear = new ArrayList<>();

        manMonth.add("January");
        manMonth.add("February");
        manMonth.add("March");
        manMonth.add("April");
        manMonth.add("May");
        manMonth.add("June");
        manMonth.add("July");
        manMonth.add("August");
        manMonth.add("September");
        manMonth.add("October");
        manMonth.add("November");
        manMonth.add("December");

        manYear.add("2024");
        manYear.add("2023");
        manYear.add("2022");
        manYear.add("2021");
        manYear.add("2020");
        manYear.add("2019");
        manYear.add("2018");
        manYear.add("2017");
        manYear.add("2016");
        manYear.add("2015");
        manYear.add("2014");
        manYear.add("2013");
        manYear.add("2012");

        expMonth.add("January");
        expMonth.add("February");
        expMonth.add("March");
        expMonth.add("April");
        expMonth.add("May");
        expMonth.add("June");
        expMonth.add("July");
        expMonth.add("August");
        expMonth.add("September");
        expMonth.add("October");
        expMonth.add("November");
        expMonth.add("December");


        expYear.add("2027");
        expYear.add("2026");
        expYear.add("2025");
        expYear.add("2024");
        expYear.add("2023");
        expYear.add("2022");
        expYear.add("2021");
        expYear.add("2020");
        expYear.add("2019");
        expYear.add("2018");
        expYear.add("2017");
        expYear.add("2016");
        expYear.add("2015");
        expYear.add("2014");
        expYear.add("2013");
        expYear.add("2012");


        String my = product.getProductManufacture().substring(0, 4);
        String mm = getMonthNameByNumber(product.getProductManufacture().substring(5, 7));
        String ey = product.getProductExpire().substring(0, 4);
        String em = getMonthNameByNumber(product.getProductExpire().substring(5, 7));

        spinnerManufactureMonth.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manMonth));
        spinnerManufactureYear.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manYear));
        spinnerExpireMonth.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, expMonth));
        spinnerExpireYear.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, expYear));

        int imm = manMonth.indexOf(mm);
        int imy = manYear.indexOf(my);
        int iem = expMonth.indexOf(em);
        int iey = expYear.indexOf(ey);

        spinnerManufactureMonth.setSelectionM(imm);
        spinnerManufactureYear.setSelectionM(imy);
        spinnerExpireMonth.setSelectionM(iem);
        spinnerExpireYear.setSelectionM(iey);

        spinnerManufactureMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                manMonthName = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerManufactureYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                manYearName = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerExpireMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                expMonthName = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerExpireYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                expYearName = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setItemsSpinner(List<ModelItem> modelItemList) {
        ArrayList<String> list = new ArrayList<>();
        for (ModelItem item : modelItemList) {
            list.add(item.getItemName());
        }

        spinnerItem.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list));
        int index = list.indexOf(product.getProductName());
        spinnerItem.setSelectionM(index);
        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemName = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String getMonthNumberByName(String monthName) {
        String monthNumber = "0";
        switch (monthName.toLowerCase().trim()) {
            case "january":
                monthNumber = "01";
                break;
            case "february":
                monthNumber = "02";
                break;
            case "march":
                monthNumber = "03";
                break;
            case "april":
                monthNumber = "04";
                break;
            case "may":
                monthNumber = "05";
                break;
            case "june":
                monthNumber = "06";
                break;
            case "july":
                monthNumber = "07";
                break;
            case "august":
                monthNumber = "08";
                break;
            case "september":
                monthNumber = "09";
                break;
            case "october":
                monthNumber = "10";
                break;
            case "november":
                monthNumber = "11";
                break;
            case "december":
                monthNumber = "12";
                break;
            default:
                monthNumber = "0";
        }
        return monthNumber;
    }

    private void init() {
        modelBrandList = new ArrayList<>();
        modelCategoryList = new ArrayList<>();
        modelSizeList = new ArrayList<>();
        modelLocationList = new ArrayList<>();
        modelItemList = new ArrayList<>();
        modelProductList = new ArrayList<>();
        spinnerBrand = findViewById(R.id.spinnerBrand);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSize = findViewById(R.id.spinnerSize);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        spinnerItem = findViewById(R.id.spinnerItem);
        spinnerManufactureMonth = findViewById(R.id.spinnerManufactureMonth);
        spinnerManufactureYear = findViewById(R.id.spinnerManufactureYear);
        spinnerExpireMonth = findViewById(R.id.spinnerExpireMonth);
        spinnerExpireYear = findViewById(R.id.spinnerExpireYear);
        inputProductPrice = findViewById(R.id.inputProductPrice);
        inputProductQuantity = findViewById(R.id.inputProductQuantity);
        inputProductBarcode = findViewById(R.id.inputProductBarcode);
        tilBarcode = findViewById(R.id.tilBarcode);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        actionBar = getSupportActionBar();
    }

}