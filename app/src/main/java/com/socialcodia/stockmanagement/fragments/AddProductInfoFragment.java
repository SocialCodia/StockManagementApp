package com.socialcodia.stockmanagement.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductInfoFragment extends Fragment implements View.OnClickListener {

    private EditText inputItemName, inputItemDescription, inputBrandName, inputCategory, inputSize, inputLocation;
    private Button btnAddItem, btnAddBrand, btnAddCategory, btnAddSize, btnAddLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product_info, container, false);
        init(view);


        btnAddItem.setOnClickListener(this);
        btnAddBrand.setOnClickListener(this);
        btnAddCategory.setOnClickListener(this);
        btnAddSize.setOnClickListener(this);
        btnAddLocation.setOnClickListener(this);

        return view;
    }

    private void init(View view) {
        inputItemName = view.findViewById(R.id.inputItemName);
        inputItemDescription = view.findViewById(R.id.inputItemDescription);
        inputBrandName = view.findViewById(R.id.inputBrandName);
        inputCategory = view.findViewById(R.id.inputCategory);
        inputSize = view.findViewById(R.id.inputSize);
        inputLocation = view.findViewById(R.id.inputLocation);
        btnAddItem = view.findViewById(R.id.btnAddItem);
        btnAddBrand = view.findViewById(R.id.btnAddBrand);
        btnAddCategory = view.findViewById(R.id.btnAddCategory);
        btnAddSize = view.findViewById(R.id.btnAddSize);
        btnAddLocation = view.findViewById(R.id.btnAddLocation);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnAddItem:
                validateItem();
                break;
            case R.id.btnAddBrand:
                validateBrand();
                break;
            case R.id.btnAddCategory:
                validateCategory();
                break;
            case R.id.btnAddSize:
                validateSize();
                break;
            case R.id.btnAddLocation:
                validateLocation();
                break;
        }
    }

    private void validateItem() {
        String itemName = inputItemName.getText().toString().toUpperCase().trim();
        String itemDesc = inputItemDescription.getText().toString().trim();

        if (itemName.isEmpty()) {
            inputItemName.setError("Enter Item Name");
            inputItemName.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
            return;
        }
        if (itemName.length() < 3) {
            inputItemName.setError("Item Name too Short");
            inputItemName.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
        } else
            addItem(itemName, itemDesc);

    }

    private void addItem(String itemName, String itemDesc) {
        if (Helper.isNetworkAvailable()) {
            btnAddItem.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().addItem(Helper.getToken(), itemName, itemDesc);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnAddItem.setEnabled(true);
                    if (response.isSuccessful()) {
                        ResponseDefault resp = response.body();
                        if (!resp.isError()) {
                            inputItemName.setText("");
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Helper.playSuccess();
                        } else {
                            Helper.playError();
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } else {
                        TastyToast.makeText(getContext(), "Response Not Success", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        Helper.playError();
                    }
                    Helper.playVibrate();
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnAddItem.setEnabled(true);
                }
            });
        }
    }

    private void validateBrand() {
        String brandName = inputBrandName.getText().toString().toUpperCase().trim();
        if (brandName.isEmpty()) {
            inputBrandName.setError("Enter Brand Name");
            inputBrandName.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
            return;
        }
        if (brandName.length() < 3) {
            inputBrandName.setError("Brand Name too Short");
            inputBrandName.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
        } else
            addBrand(brandName);
    }

    private void addBrand(String brandName) {
        if (Helper.isNetworkAvailable()) {
            btnAddBrand.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().addBrand(Helper.getToken(), brandName);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnAddBrand.setEnabled(true);
                    if (response.isSuccessful()) {
                        ResponseDefault resp = response.body();
                        if (!resp.isError()) {
                            inputBrandName.setText("");
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Helper.playSuccess();
                        } else {
                            Helper.playError();
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } else {
                        TastyToast.makeText(getContext(), "Response Not Success", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        Helper.playError();
                    }
                    Helper.playVibrate();
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnAddBrand.setEnabled(true);
                }
            });
        }
    }

    private void validateCategory() {
        String categoryName = inputCategory.getText().toString().toUpperCase().trim();
        if (categoryName.isEmpty()) {
            inputCategory.setError("Enter Category");
            inputCategory.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
            return;
        }
        if (categoryName.length() < 3) {
            inputCategory.setError("Category too Short");
            inputCategory.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
        } else
            addCategory(categoryName);
    }

    private void addCategory(String categoryName) {
        if (Helper.isNetworkAvailable()) {
            btnAddCategory.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().addCategory(Helper.getToken(), categoryName);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnAddCategory.setEnabled(true);
                    if (response.isSuccessful()) {
                        ResponseDefault resp = response.body();
                        if (!resp.isError()) {
                            inputCategory.setText("");
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Helper.playSuccess();
                        } else {
                            Helper.playError();
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } else {
                        TastyToast.makeText(getContext(), "Response Not Success", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        Helper.playError();
                    }
                    Helper.playVibrate();
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnAddCategory.setEnabled(true);
                }
            });
        }
    }

    private void validateSize() {
        String sizeName = inputSize.getText().toString().toUpperCase().trim();
        if (sizeName.isEmpty()) {
            inputSize.setError("Enter Size");
            inputSize.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
            return;
        }
        if (sizeName.length() < 2) {
            inputSize.setError("Size too Short");
            inputSize.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
        } else
            addSize(sizeName);
    }

    private void addSize(String sizeName) {
        if (Helper.isNetworkAvailable()) {
            btnAddSize.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().addSize(Helper.getToken(), sizeName);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnAddSize.setEnabled(true);
                    if (response.isSuccessful()) {
                        ResponseDefault resp = response.body();
                        if (!resp.isError()) {
                            inputSize.setText("");
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Helper.playSuccess();
                        } else {
                            Helper.playError();
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } else {
                        TastyToast.makeText(getContext(), "Response Not Success", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        Helper.playError();
                    }
                    Helper.playVibrate();
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnAddSize.setEnabled(true);
                }
            });
        }
    }

    private void validateLocation() {
        String locationName = inputLocation.getText().toString().toUpperCase().trim();
        if (locationName.isEmpty()) {
            inputLocation.setError("Enter Location");
            inputLocation.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
            return;
        }
        if (locationName.length() < 2) {
            inputLocation.setError("Location too Short");
            inputLocation.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
        } else
            addLocation(locationName);
    }

    private void addLocation(String locationName) {
        if (Helper.isNetworkAvailable()) {
            btnAddLocation.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().addLocation(Helper.getToken(), locationName);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnAddLocation.setEnabled(true);
                    if (response.isSuccessful()) {
                        ResponseDefault resp = response.body();
                        if (!resp.isError()) {
                            inputLocation.setText("");
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Helper.playSuccess();
                        } else {
                            Helper.playError();
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } else {
                        TastyToast.makeText(getContext(), "Response Not Success", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        Helper.playError();
                    }
                    Helper.playVibrate();
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnAddLocation.setEnabled(true);
                }
            });
        }
    }
}