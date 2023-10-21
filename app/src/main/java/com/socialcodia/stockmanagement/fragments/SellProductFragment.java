package com.socialcodia.stockmanagement.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.activities.SellProductActivity;
import com.socialcodia.stockmanagement.adapters.AdapterSaleEditable;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.etc.Scanner;
import com.socialcodia.stockmanagement.helper.DbHandler;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelProduct;
import com.socialcodia.stockmanagement.models.ModelSale;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;
import com.socialcodia.stockmanagement.pojos.ResponseProducts;
import com.socialcodia.stockmanagement.pojos.ResponseSale;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellProductFragment extends Fragment {

    private CardView cvSearchProduct;
    private List<ModelProduct> modelProductList;
    private List<ModelSale> modelSaleList;
    private AdapterSaleEditable adapterSaleEditable;
    private RecyclerView recyclerView;
    private TextView tvTotalPrice, tvSalePrice;
    private ProgressBar progressBar;
    private LinearLayout layoutTotal;

    //Bottom Sheet Dialog
    private EditText inputCreditorName, inputCreditorMobile, inputCreditPaidAmount, inputCreditorAddress, inputCreditDescription;
    private Button btnAddCreditRecord;
    private BottomSheetDialog bottomSheetDialog;
    private String[] cameraPermission;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell_product, container, false);

        init(view);

        progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapterSaleEditable = new AdapterSaleEditable(getContext(), modelSaleList, SellProductFragment.this);
        recyclerView.setAdapter(adapterSaleEditable);

        cvSearchProduct.setOnClickListener(view1 -> sendToSellProductActivity());
        layoutTotal.setVisibility(View.GONE);
        setHasOptionsMenu(true);
        getProducts();
        return view;
    }

    private void sendToSellProductActivity() {
        if (!modelProductList.isEmpty()) {
            Intent intent = new Intent(getContext(), SellProductActivity.class);
            startActivityForResult(intent, 200);
        } else
            TastyToast.makeText(getActivity(), "Please wait...", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
    }




    public void sellProductByBarCode(String barCode) {
        if (Helper.isNetworkAvailable()) {
            TastyToast.makeText(getContext(), "Selling Product. Please wait...", TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
            Call<ResponseSale> call = ApiClient.getInstance().getApi().sellProductByBarCode(Helper.getToken(), barCode);
            call.enqueue(new Callback<ResponseSale>() {
                @Override
                public void onResponse(Call<ResponseSale> call, Response<ResponseSale> response) {
                    if (response.isSuccessful()) {
                        ResponseSale resp = response.body();
                        if (!resp.isError()) {
                            layoutTotal.setVisibility(View.VISIBLE);
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            ModelSale sale = resp.getProduct();
                            sale.setSalePrice(sale.getProductPrice());
                            sale.setProductTotalPrice(sale.getProductPrice());
                            modelSaleList.add(sale);
                            adapterSaleEditable.notifyItemInserted(modelSaleList.size());
                            Helper.playSuccess();
                            Helper.playVibrateLong();
                        } else {
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            Helper.playError();
                            Helper.playVibrate();
                        }
                    } else
                        TastyToast.makeText(getContext(), getString(R.string.SWW), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }

                @Override
                public void onFailure(Call<ResponseSale> call, Throwable t) {

                }
            });
        }
    }

    private void sellProductById(int productId) {
        if (Helper.isNetworkAvailable()) {
            TastyToast.makeText(getContext(), "Selling Product. Please wait...", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
            Call<ResponseSale> call = ApiClient.getInstance().getApi().sellProductById(Helper.getToken(), productId);
            call.enqueue(new Callback<ResponseSale>() {
                @Override
                public void onResponse(Call<ResponseSale> call, Response<ResponseSale> response) {
                    if (response.isSuccessful()) {
                        ResponseSale resp = response.body();
                        if (!resp.isError()) {
                            layoutTotal.setVisibility(View.VISIBLE);
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            ModelSale sale = resp.getProduct();
                            sale.setSalePrice(sale.getProductPrice());
                            sale.setProductTotalPrice(sale.getProductPrice());
                            modelSaleList.add(sale);
                            adapterSaleEditable.notifyItemInserted(modelSaleList.size());
                            Helper.playSuccess();
                            Helper.playVibrateLong();
                        } else {
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            Helper.playError();
                            Helper.playVibrate();
                        }
                    } else
                        TastyToast.makeText(getContext(), getString(R.string.SWW), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }

                @Override
                public void onFailure(Call<ResponseSale> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void getProducts() {
        Call<ResponseProducts> call = ApiClient.getInstance().getApi().getAvailableProducts(Helper.getToken());
        call.enqueue(new Callback<ResponseProducts>() {
            @Override
            public void onResponse(Call<ResponseProducts> call, Response<ResponseProducts> response) {
                if (response.isSuccessful()) {
                    ResponseProducts products = response.body();
                    if (!products.isError()) {
                        progressBar.setVisibility(View.GONE);
                        modelProductList = new ArrayList<>();
                        modelProductList = products.getProducts();
                        DbHandler.setModelProductList(modelProductList);
                    } else
                        Toast.makeText(getContext(), products.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getContext(), "Response Is Not Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseProducts> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void updateTotalValue(int totalPrice, int salePrice) {
        tvTotalPrice.setText(String.valueOf(totalPrice));
        tvSalePrice.setText(String.valueOf(salePrice));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_sale, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.miScanner:
                if (checkCameraPermission())
                {
                    scanCode();
                }
                else
                   requestCameraPermission();
                break;
            case R.id.miAddCreditRecord:
                showBottomSheetDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkCameraPermission()
    {
        return ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(getActivity(),cameraPermission,1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0)
        {
            boolean permission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (permission)
            {
                scanCode();
            }
            else
                TastyToast.makeText(getContext(),"Permission Declined",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
        }
    }

    private void showBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_add_credit_record, getActivity().findViewById(R.id.bottomSheetContainer));

        inputCreditorName = view.findViewById(R.id.inputCreditorName);
        inputCreditorMobile = view.findViewById(R.id.inputCreditorMobile);
        inputCreditPaidAmount = view.findViewById(R.id.inputCreditPaidAmount);
        inputCreditorAddress = view.findViewById(R.id.inputCreditorAddress);
        inputCreditDescription = view.findViewById(R.id.inputCreditDescription);
        btnAddCreditRecord = view.findViewById(R.id.btnAddCreditRecord);

        btnAddCreditRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void validateData()
    {
        String name = inputCreditorName.getText().toString().trim();
        String mobile = inputCreditorMobile.getText().toString().trim();
        String paidAmount = inputCreditPaidAmount.getText().toString().trim();
        String address = inputCreditorAddress.getText().toString().trim();
        String desc = inputCreditDescription.getText().toString().trim();

        if (name.isEmpty())
        {
            inputCreditorName.setError("Enter Name");
            inputCreditorName.requestFocus();
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        if (mobile.isEmpty())
        {
            inputCreditorMobile.setError("Enter Mobile");
            inputCreditorMobile.requestFocus();
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (mobile.length()!=10)
        {
            inputCreditorMobile.setError("Enter Valid Mobile Number");
            inputCreditorMobile.requestFocus();
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (address.isEmpty())
        {
            inputCreditorAddress.setError("Enter Address");
            inputCreditorAddress.requestFocus();
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        else
        {
            addCreditRecord(name,mobile,paidAmount,address,desc);
        }

    }

    private void addCreditRecord(String name, String mobile, String paidAmount, String address, String desc)
    {
        List<String> list = new ArrayList<>();
        for (ModelSale sale : modelSaleList)
        {
             list.add('"'+String.valueOf(sale.getSaleId())+'"');
        }
        String salesId = String.valueOf(list);
        if (salesId == null ||salesId.isEmpty() || salesId.length()<1)
        {
            TastyToast.makeText(getContext(),"Please Sale Some Products To Add Sale Record",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (Helper.isNetworkAvailable())
        {
            btnAddCreditRecord.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().addCreditRecord(Helper.getToken(),name,mobile,address,paidAmount,desc,salesId);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnAddCreditRecord.setEnabled(true);
                    if (response.isSuccessful())
                    {
                        ResponseDefault resp = response.body();
                        if (!resp.isError())
                        {
                            TastyToast.makeText(getContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                            Helper.playSuccess();
                            bottomSheetDialog.dismiss();
                        }
                        else
                        {
                            TastyToast.makeText(getContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                            Helper.playError();
                        }
                        Helper.playVibrate();
                    }
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnAddCreditRecord.setEnabled(true);
                }
            });
        }
    }

    private void scanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.setCaptureActivity(Scanner.class);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Sell Products By Scanning Bar Code");
//        intentIntegrator.initiateScan();
        intentIntegrator.forSupportFragment(SellProductFragment.this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && data != null) {
            String productId = data.getStringExtra("intentProductId");
            Toast.makeText(getContext(), productId, Toast.LENGTH_SHORT).show();
        }

        if (result != null) {
            if (result.getContents() != null)
                sellProductByBarCode(result.getContents());
            else
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            prepareToSellProduct();
        }
    }

    private void prepareToSellProduct() {
        int productId = 0;
        productId = DbHandler.getProductId();
        if (productId != 0) {
            sellProductById(productId);
            DbHandler.removeProductId();
        }
    }

    private void init(View view) {
        cvSearchProduct = view.findViewById(R.id.cvSearchProduct);
        layoutTotal = view.findViewById(R.id.layoutTotal);
        recyclerView = view.findViewById(R.id.rvSellEditable);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        tvSalePrice = view.findViewById(R.id.tvSalePrice);
        progressBar = view.findViewById(R.id.progressBar);
        modelProductList = new ArrayList<>();
        modelSaleList = new ArrayList<>();
        cameraPermission = new String[]{Manifest.permission.CAMERA};
    }


}