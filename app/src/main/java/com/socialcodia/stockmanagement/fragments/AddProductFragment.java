package com.socialcodia.stockmanagement.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelBrand;
import com.socialcodia.stockmanagement.models.ModelCategory;
import com.socialcodia.stockmanagement.models.ModelItem;
import com.socialcodia.stockmanagement.models.ModelLocation;
import com.socialcodia.stockmanagement.models.ModelSize;
import com.socialcodia.stockmanagement.pojos.ResponseBrand;
import com.socialcodia.stockmanagement.pojos.ResponseCategory;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;
import com.socialcodia.stockmanagement.pojos.ResponseItems;
import com.socialcodia.stockmanagement.pojos.ResponseLocation;
import com.socialcodia.stockmanagement.pojos.ResponseSize;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductFragment extends Fragment {

    //    Spinner
    private List<ModelBrand> modelBrandList;
    private List<ModelCategory> modelCategoryList;
    private List<ModelSize> modelSizeList;
    private List<ModelLocation> modelLocationList;
    private List<ModelItem> modelItemList;
    private SearchableSpinner spinnerItem, spinnerBrand, spinnerCategory, spinnerSize, spinnerLocation, spinnerManufactureMonth, spinnerManufactureYear, spinnerExpireMonth, spinnerExpireYear;
    private EditText inputProductPrice, inputProductQuantity, inputProductBarcode;
    private TextInputLayout tilBarcode;
    private String itemName, brandName, categoryName, sizeName, locationName, price, quantity, barCode, manMonthName, manYearName, expMonthName, expYearName = "";
    private Button btnAddProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        init(view);

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


        getBrands();
        getCategories();
        getSizes();
        getLocations();
        getItems();
        setSpinner();

        btnAddProduct.setOnClickListener(v->validateData());

        return view;
    }

    private int getBrandIdByBrandName(String brandName)
    {
        int brandId = 0;
        for(ModelBrand brand : modelBrandList)
        {
            if (brandName.toLowerCase().trim().equals(brand.getBrandName().toLowerCase().trim()))
            {
                brandId = brand.getBrandId();
            }
        }
        return brandId;
    }

    private int getCategoryIdByCategoryName(String categoryName)
    {
        int categoryId = 0;
        for(ModelCategory category : modelCategoryList)
        {
            if (categoryName.toLowerCase().trim().equals(category.getCategoryName().toLowerCase().trim()))
            {
                categoryId = category.getCategoryId();
            }
        }
        return categoryId;
    }

    private int getSizeIdBySizeName(String sizeName)
    {
        int sizeId = 0;
        for(ModelSize size : modelSizeList)
        {
            if (sizeName.toLowerCase().trim().equals(size.getSizeName().toLowerCase().trim()))
            {
                sizeId = size.getSizeId();
            }
        }
        return sizeId;
    }

    private int getLocationIdByLocationName(String locationName)
    {
        int locationId = 0;
        for(ModelLocation location : modelLocationList)
        {
            if (locationName.toLowerCase().trim().equals(location.getLocationName().toLowerCase().trim()))
            {
                locationId = location.getLocationId();
            }
        }
        return locationId;
    }

    private int getItemIdByItemName(String itemName)
    {
        int itemId = 0;
        for(ModelItem item : modelItemList)
        {
            Log.d("SocialCodia", "main: "+item.getItemName());
            if (itemName.toLowerCase().trim().equals(item.getItemName().toLowerCase().trim()))
            {
                Log.d("SocailCodia", "Codition: Condtion True");
                itemId = item.getItemId();
            }
        }
        return itemId;
    }

    private void scanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.setCaptureActivity(Scanner.class);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Sell Products By Scanning Bar Code");
        intentIntegrator.forSupportFragment(AddProductFragment.this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null)
                inputProductBarcode.setText(String.valueOf(result.getContents()));
            else
                TastyToast.makeText(getContext(), "Cancelled", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void init(View view) {
        modelBrandList = new ArrayList<>();
        modelCategoryList = new ArrayList<>();
        modelSizeList = new ArrayList<>();
        modelLocationList = new ArrayList<>();
        modelItemList = new ArrayList<>();
        spinnerBrand = view.findViewById(R.id.spinnerBrand);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerSize = view.findViewById(R.id.spinnerSize);
        spinnerLocation = view.findViewById(R.id.spinnerLocation);
        spinnerItem = view.findViewById(R.id.spinnerItem);
        spinnerManufactureMonth = view.findViewById(R.id.spinnerManufactureMonth);
        spinnerManufactureYear = view.findViewById(R.id.spinnerManufactureYear);
        spinnerExpireMonth = view.findViewById(R.id.spinnerExpireMonth);
        spinnerExpireYear = view.findViewById(R.id.spinnerExpireYear);
        inputProductPrice = view.findViewById(R.id.inputProductPrice);
        inputProductQuantity = view.findViewById(R.id.inputProductQuantity);
        inputProductBarcode = view.findViewById(R.id.inputProductBarcode);
        tilBarcode = view.findViewById(R.id.tilBarcode);
        btnAddProduct = view.findViewById(R.id.btnAddProduct);
    }


    private void validateData() {
        price = inputProductPrice.getText().toString().trim();
        quantity = inputProductQuantity.getText().toString().trim();
        if (brandName==null || brandName.isEmpty()) {
            TastyToast.makeText(getContext(), "Select Brand", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (categoryName==null || categoryName.isEmpty()) {
            TastyToast.makeText(getContext(), "Select Category", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (sizeName==null || sizeName.isEmpty()) {
            TastyToast.makeText(getContext(), "Select Size", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (locationName==null || locationName.isEmpty()) {
            TastyToast.makeText(getContext(), "Select Location", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (itemName==null || itemName.isEmpty()) {
            TastyToast.makeText(getContext(), "Select Product Name", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
        if (manMonthName==null || manMonthName.isEmpty()) {
            TastyToast.makeText(getContext(), "Select Manufacture Month", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (manYearName==null || manYearName.isEmpty()) {
            TastyToast.makeText(getContext(), "Select Manufacture Year", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (expMonthName==null || expMonthName.isEmpty()) {
            TastyToast.makeText(getContext(), "Select Expire Month", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (expYearName==null || expYearName.isEmpty()) {
            TastyToast.makeText(getContext(), "Select Expire Year", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
        }
        else
        {
            barCode = inputProductBarcode.getText().toString().trim();
            int brandId = getBrandIdByBrandName(brandName);
            int categoryId = getCategoryIdByCategoryName(categoryName);
            int sizeId = getSizeIdBySizeName(sizeName);
            int locationId = getLocationIdByLocationName(locationName);
            int itemId = getItemIdByItemName(itemName);
            String productManufactureDate = manYearName + getMonthNumberByName(manMonthName) +"01";
            String productExpireDate = expYearName + getMonthNumberByName(expMonthName) +"01";
            addProduct(brandId,categoryId,sizeId,locationId,itemId,price,quantity,productManufactureDate,productExpireDate,barCode);
        }
    }

    private void addProduct(int brandId, int categoryId, int sizeId, int locationId, int itemId, String price, String quantity, String productManufactureDate, String productExpireDate, String barCode) {
        btnAddProduct.setEnabled(false);
        Call<ResponseDefault> call = ApiClient.getInstance().getApi().addProduct(Helper.getToken(),itemId,brandId,categoryId,sizeId,locationId,price,quantity,productManufactureDate,productExpireDate,barCode);
        call.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                btnAddProduct.setEnabled(true);
                if (response.isSuccessful())
                {
                    ResponseDefault resp = response.body();
                    if (!resp.isError())
                    {
                        TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Helper.playVibrate();
                        Helper.playSuccess();
                        inputProductQuantity.setText("");
                    }
                    else
                    {
                        TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        Helper.playVibrate();
                        Helper.playError();
                    }
                }
                else
                    TastyToast.makeText(getContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                t.printStackTrace();
                btnAddProduct.setEnabled(true);
            }
        });
    }


    private String getMonthNumberByName(String monthName)
    {
        String monthNumber = "0";
        switch (monthName.toLowerCase().trim())
        {
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

    private void getBrands() {
        Call<ResponseBrand> call = ApiClient.getInstance().getApi().getBrands(Helper.getToken());
        call.enqueue(new Callback<ResponseBrand>() {
            @Override
            public void onResponse(Call<ResponseBrand> call, Response<ResponseBrand> response) {
                if (response.isSuccessful()) {
                    ResponseBrand resp = response.body();
                    if (!resp.isError()) {
                        modelBrandList = resp.getBrands();
                        setBrandSpinner(modelBrandList);
                    } else
                        TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
                else
                    TastyToast.makeText(getContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                        setCategorySpinner(modelCategoryList);
                    } else
                        TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
                else
                    TastyToast.makeText(getContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                        setSizeSpinner(modelSizeList);
                    } else
                        TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
                else
                    TastyToast.makeText(getContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                        setLocationSpinner(modelLocationList);
                    } else
                        TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
                else
                    TastyToast.makeText(getContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                        setItemsSpinner(modelItemList);
                    } else
                        TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
                else
                    TastyToast.makeText(getContext(), getString(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {

            }
        });
    }


    private void setBrandSpinner(List<ModelBrand> modelBrandList) {
        ArrayList<String> list = new ArrayList<>();
        for (ModelBrand brand : modelBrandList) {
            list.add(brand.getBrandName());
        }

        try {

            spinnerBrand.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

    private void setCategorySpinner(List<ModelCategory> modelCategoryList) {
        ArrayList<String> list = new ArrayList<>();
        for (ModelCategory category : modelCategoryList) {
            list.add(category.getCategoryName());
        }

        try {

            spinnerCategory.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

        try {

            spinnerSize.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

        try {
            spinnerLocation.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

        try {
            spinnerManufactureMonth.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, manMonth));
            spinnerManufactureYear.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, manYear));
            spinnerExpireMonth.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, expMonth));
            spinnerExpireYear.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, expYear));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

        try {

            spinnerItem.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

}