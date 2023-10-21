package com.socialcodia.stockmanagement.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.activities.SellProductActivity;
import com.socialcodia.stockmanagement.adapters.AdapterSellerSaleEditable;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.etc.Scanner;
import com.socialcodia.stockmanagement.etc.SearchableSpinner;
import com.socialcodia.stockmanagement.helper.DbHandler;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelProduct;
import com.socialcodia.stockmanagement.models.ModelSale;
import com.socialcodia.stockmanagement.models.ModelSeller;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;
import com.socialcodia.stockmanagement.pojos.ResponseInvoiceSingle;
import com.socialcodia.stockmanagement.pojos.ResponseProducts;
import com.socialcodia.stockmanagement.pojos.ResponseSale;
import com.socialcodia.stockmanagement.pojos.ResponseSellers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellToSellerFragment extends Fragment {

    private SearchableSpinner spinnerSeller;
    private List<ModelSeller> modelSellerList;
    private List<ModelProduct> modelProductList;
    private List<ModelSale> modelSaleList;
    private int sellerId;
    private String sellerName, sellerMobile, sellerAddress, sellerImage, invoiceNumber;
    private Button btnSetSeller, btnRemSeller;
    private TextView tvSellerName, tvSellerMobile, tvSellerAddress,tvTotalPrice,tvSalePrice;
    private ImageView ivSeller;
    private CardView cvSearchProduct;
    private AdapterSellerSaleEditable adapterSellerSaleEditable;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sell_to_seller, container, false);

        spinnerSeller = view.findViewById(R.id.spinnerSeller);
        btnSetSeller = view.findViewById(R.id.btnSetSeller);
        btnRemSeller = view.findViewById(R.id.btnRemSeller);
        tvSellerName = view.findViewById(R.id.tvSellerName);
        tvSellerMobile = view.findViewById(R.id.tvSellerMobile);
        tvSellerAddress = view.findViewById(R.id.tvSellerAddress);
        cvSearchProduct = view.findViewById(R.id.cvSearchProduct);
        recyclerView = view.findViewById(R.id.rvSellToSeller);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        tvSalePrice = view.findViewById(R.id.tvSalePrice);
        modelProductList = new ArrayList<>();
        ivSeller = view.findViewById(R.id.ivSeller);
        modelSellerList = new ArrayList<>();
        modelSaleList = new ArrayList<>();

        setHasOptionsMenu(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapterSellerSaleEditable = new AdapterSellerSaleEditable(getContext(),modelSaleList,SellToSellerFragment.this);
        recyclerView.setAdapter(adapterSellerSaleEditable);
        btnSetSeller.setOnClickListener(v -> validateData());
        btnRemSeller.setOnClickListener(v -> showInvoiceCancelDialog());
        cvSearchProduct.setOnClickListener(v->sendToSellProductActivity());
        getSeller();
        getProducts();
        return view;
    }

    private void sendToSellProductActivity()
    {
        if (invoiceNumber==null || invoiceNumber.isEmpty())
        {
            alertSelectSeller();
        }
        else
        {
            if (!modelProductList.isEmpty())
            {
                Intent intent = new Intent(getContext(), SellProductActivity.class);
                startActivityForResult(intent,200);
            }
            else
                TastyToast.makeText(getActivity(),"Please wait...",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT);
        }
    }

    private void validateData() {
        if (sellerName == null || sellerName.isEmpty()) {
            alertSelectSeller();
        } else {
            sellerId = getSellerIdByName(sellerName);
            addInvoice(sellerId);
        }
    }


    public void alertSelectSeller() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("Please Select A Seller");
        sweetAlertDialog.show();
        Helper.playError();
        Helper.playVibrate();
    }

    public void sellProductByBarCode(String barCode)
    {
        if (Helper.isNetworkAvailable())
        {
            TastyToast.makeText(getContext(),"Selling Product. Please wait...",TastyToast.LENGTH_LONG,TastyToast.DEFAULT);
            Call<ResponseSale> call = ApiClient.getInstance().getApi().sellSellerProductByBarCode(Helper.getToken(),barCode,invoiceNumber);
            call.enqueue(new Callback<ResponseSale>() {
                @Override
                public void onResponse(Call<ResponseSale> call, Response<ResponseSale> response) {
                    if (response.isSuccessful())
                    {
                        ResponseSale resp = response.body();
                        if (!resp.isError())
                        {
                            TastyToast.makeText(getContext(),resp.getMessage(),TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
                            ModelSale sale = resp.getProduct();
                            sale.setSalePrice(sale.getProductPrice());
                            sale.setProductTotalPrice(sale.getProductPrice());
                            modelSaleList.add(sale);
                            adapterSellerSaleEditable.notifyItemInserted(modelSaleList.size());
                            Helper.playSuccess();
                            Helper.playVibrateLong();
                        }
                        else
                        {
                            TastyToast.makeText(getContext(),resp.getMessage(),TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            Helper.playError();
                            Helper.playVibrate();
                        }
                    }
                    else
                        TastyToast.makeText(getContext(),getString(R.string.SWW),TastyToast.LENGTH_LONG,TastyToast.ERROR);
                }

                @Override
                public void onFailure(Call<ResponseSale> call, Throwable t) {

                }
            });
        }
    }

    private void sellProductById(int productId)
    {
        if (Helper.isNetworkAvailable())
        {
            TastyToast.makeText(getContext(),"Selling Product. Please wait...",TastyToast.LENGTH_LONG,TastyToast.DEFAULT);
            Call<ResponseSale> call = ApiClient.getInstance().getApi().sellSellerProductById(Helper.getToken(),productId,invoiceNumber);
            call.enqueue(new Callback<ResponseSale>() {
                @Override
                public void onResponse(Call<ResponseSale> call, Response<ResponseSale> response) {
                    if (response.isSuccessful())
                    {
                        ResponseSale resp = response.body();
                        if (!resp.isError())
                        {
                            TastyToast.makeText(getContext(),resp.getMessage(),TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
                            ModelSale sale = resp.getProduct();
                            sale.setSalePrice(sale.getProductPrice());
                            sale.setProductTotalPrice(sale.getProductPrice());
                            modelSaleList.add(sale);
                            adapterSellerSaleEditable.notifyItemInserted(modelSaleList.size());
                            Helper.playSuccess();
                            Helper.playVibrateLong();
                        }
                        else
                        {
                            TastyToast.makeText(getContext(),resp.getMessage(),TastyToast.LENGTH_LONG,TastyToast.ERROR);
                            Helper.playError();
                            Helper.playVibrate();
                        }
                    }
                    else
                        TastyToast.makeText(getContext(),getString(R.string.SWW),TastyToast.LENGTH_LONG,TastyToast.ERROR);
                }

                @Override
                public void onFailure(Call<ResponseSale> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void updateTotalValue(int totalPrice, int salePrice)
    {
        tvTotalPrice.setText(String.valueOf(totalPrice));
        tvSalePrice.setText(String.valueOf(salePrice));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_sale,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (invoiceNumber==null || invoiceNumber.isEmpty())
            alertSelectSeller();
        else
            scanCode();
        return super.onOptionsItemSelected(item);
    }

    private void scanCode()
    {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.setCaptureActivity(Scanner.class);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Sell Products By Scanning Bar Code");
//        intentIntegrator.initiateScan();
        intentIntegrator.forSupportFragment(SellToSellerFragment.this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result =  IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (requestCode==200 && data!=null)
        {
            String productId = data.getStringExtra("intentProductId");
            Toast.makeText(getContext(), productId, Toast.LENGTH_SHORT).show();
        }

        if (result!=null)
        {
            if (result.getContents()!=null)
                sellProductByBarCode(result.getContents());
            else
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
            prepareToSellProduct();
        }
    }

    private void prepareToSellProduct()
    {
        int productId = 0;
        productId = DbHandler.getProductId();
        if (productId!=0)
        {
            sellProductById(productId);
            DbHandler.removeProductId();
        }
    }


    private int getSellerIdByName(String sellerName) {
        int sellerId = 0;
        for (ModelSeller seller : modelSellerList) {
            if (seller.getSellerName().toLowerCase().trim().equals(sellerName.toLowerCase().trim())) {
                sellerId = seller.getSellerId();
                sellerAddress = seller.getSellerAddress();
                sellerMobile = seller.getSellerContactNumber();
                sellerImage = seller.getSellerImage();
                tvSellerName.setText(sellerName);
                tvSellerMobile.setText(sellerMobile);
                tvSellerAddress.setText(sellerAddress);
                try {
                    Picasso.get().load(sellerImage).placeholder(R.drawable.user).into(ivSeller);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return sellerId;
    }

    private void getSeller() {
        btnSetSeller.setEnabled(false);
        Call<ResponseSellers> call = ApiClient.getInstance().getApi().getAllSellers(Helper.getToken());
        call.enqueue(new Callback<ResponseSellers>() {
            @Override
            public void onResponse(Call<ResponseSellers> call, Response<ResponseSellers> response) {
                btnSetSeller.setEnabled(true);
                if (response.isSuccessful()) {
                    ResponseSellers resp = response.body();
                    if (!resp.isError()) {
                        modelSellerList = resp.getSellers();
                        setSellerSpinner(modelSellerList);
                    } else {
                        Toast.makeText(getContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSellers> call, Throwable t) {
                t.printStackTrace();
                btnSetSeller.setEnabled(true);
            }
        });
    }

    private void getProducts() {
        Call<ResponseProducts> call = ApiClient.getInstance().getApi().getAvailableProducts(Helper.getToken());
        call.enqueue(new Callback<ResponseProducts>() {
            @Override
            public void onResponse(Call<ResponseProducts> call, Response<ResponseProducts> response) {
                if (response.isSuccessful()) {
                    ResponseProducts products = response.body();
                    if (!products.isError()) {
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

    private void addInvoice(int sellerId) {
        if (Helper.isNetworkAvailable()) {
            btnSetSeller.setEnabled(false);
            Call<ResponseInvoiceSingle> call = ApiClient.getInstance().getApi().addInvoice(Helper.getToken(), sellerId);
            call.enqueue(new Callback<ResponseInvoiceSingle>() {
                @Override
                public void onResponse(Call<ResponseInvoiceSingle> call, Response<ResponseInvoiceSingle> response) {
                    if (response.isSuccessful()) {
                        btnSetSeller.setEnabled(true);
                        ResponseInvoiceSingle resp = response.body();
                        if (!resp.isError()) {
                            btnSetSeller.setVisibility(View.GONE);
                            btnRemSeller.setVisibility(View.VISIBLE);
                            spinnerSeller.setEnabled(false);
                            invoiceNumber = resp.getInvoice().getInvoiceNumber();
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Helper.playVibrate();
                            Helper.playSuccess();
                        } else {
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            Helper.playError();
                            Helper.playVibrate();
                        }
                    } else {
                        btnSetSeller.setEnabled(true);
                        TastyToast.makeText(getContext(), String.valueOf(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<ResponseInvoiceSingle> call, Throwable t) {
                    t.printStackTrace();
                    btnSetSeller.setEnabled(true);
                }
            });
        }
    }

    public void showInvoiceCancelDialog() {
        if (Helper.isNetworkAvailable()) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Are you sure want to cancel this invoice")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            deleteInvoice();
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    private void deleteInvoice() {
        if (Helper.isNetworkAvailable()) {
            btnRemSeller.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().deleteInvoice(Helper.getToken(), invoiceNumber);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnRemSeller.setEnabled(true);
                    if (response.isSuccessful()) {
                        ResponseDefault resp = response.body();
                        if (!resp.isError()) {
                            invoiceNumber = "";
                            btnRemSeller.setVisibility(View.GONE);
                            btnSetSeller.setVisibility(View.VISIBLE);
                            spinnerSeller.setEnabled(true);
                            tvSellerAddress.setText("");
                            tvSellerMobile.setText("");
                            tvSellerName.setText("");
                            modelSaleList.clear();
                            tvSalePrice.setText("0");
                            tvTotalPrice.setText("0");
                            adapterSellerSaleEditable.notifyDataSetChanged();
                            try {
                                Picasso.get().load(R.drawable.user).into(ivSeller);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Helper.playVibrate();
                            Helper.playSuccess();
                        } else {
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            Helper.playError();
                            Helper.playVibrate();
                        }
                    } else {
                        TastyToast.makeText(getContext(), String.valueOf(R.string.SWW), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnRemSeller.setEnabled(true);
                }
            });
        }
    }

    private void setSellerSpinner(List<ModelSeller> modelSellerList) {
        List<String> list = new ArrayList<>();
        for (ModelSeller seller : modelSellerList) {
            list.add(seller.getSellerName());
        }

        try {

            spinnerSeller.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, list));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        spinnerSeller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sellerName = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}