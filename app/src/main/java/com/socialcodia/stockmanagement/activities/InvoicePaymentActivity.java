package com.socialcodia.stockmanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.adapters.AdapterPayment;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelInvoice;
import com.socialcodia.stockmanagement.models.ModelPayment;
import com.socialcodia.stockmanagement.models.ModelUser;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;
import com.socialcodia.stockmanagement.pojos.ResponseInvoiceSingle;
import com.socialcodia.stockmanagement.pojos.ResponsePayments;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoicePaymentActivity extends AppCompatActivity {

    private SharedPrefHandler sp;
    private ModelUser user;
    private String token, invoiceNumber, sellerName, amount;
    private int sellerId;
    private Intent intent;
    private ImageView ivSeller;
    private List<ModelPayment> modelPaymentList;
    private RecyclerView recyclerView;
    private TextView tvSellerName, tvSellerMobile, tvSellerAddress, tvInvoiceNumber, tvInvoiceDate, tvInvoiceAmount, tvInvoicePaidAmount, tvInvoiceRemainingAmount, tvInvoiceStatus;
    private ActionBar actionBar;
    private Button btnAcceptPayment;
    private EditText inputPaymentAmount;
    private LinearLayout layoutAcceptPayment;
    private AdapterPayment adapterPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_payment);
        init();

        actionBar.setTitle("Payments");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        if (intent.hasExtra("intentInvoiceNumber")) {
            invoiceNumber = intent.getStringExtra("intentInvoiceNumber");
        } else {
            Toast.makeText(this, "Failed To Fetch Invoice Number", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        ivSeller.setOnClickListener(v->sendToSellerActivity(sellerId));
        btnAcceptPayment.setOnClickListener(v -> validateData());
        getInvoiceByInvoiceNumber();
        getAllPayments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.miViewInvoice)
        {
            sendToWebView(invoiceNumber);
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendToWebView(String invoiceNumber) {
        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
        intent.putExtra("intentInvoiceNumber",invoiceNumber);
        startActivity(intent);
    }

    private void sendToSellerActivity(int sellerId) {
        Intent intent =  new Intent(getApplicationContext(), SellerActivity.class);
        intent.putExtra("intentSellerId",String.valueOf(sellerId));
        startActivity(intent);
    }

    private void validateData() {
        amount = inputPaymentAmount.getText().toString().trim();
        if (amount.isEmpty()) {
            inputPaymentAmount.setError("Enter Amount");
            inputPaymentAmount.requestFocus();
            Helper.playVibrate();
            return;
        }
        if (Integer.parseInt(amount) < 1) {
            inputPaymentAmount.setError("Enter Another Amount");
            inputPaymentAmount.requestFocus();
            Helper.playVibrate();
        } else {
            showAddPaymentAlert();
        }
    }

    public void showAddPaymentAlert() {
        if (Helper.isNetworkAvailable()) {
            new SweetAlertDialog(InvoicePaymentActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("The Seller " + sellerName + " \n is paying you " + amount + " Rupees \nFor Invoice " + invoiceNumber)
                    .setConfirmText("Accept")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            acceptPayment(amount);
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    private void acceptPayment(String amount) {
        btnAcceptPayment.setEnabled(false);
        Call<ResponseDefault> call = ApiClient.getInstance().getApi().addPayment(token, sellerId, invoiceNumber, Integer.parseInt(amount));
        call.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                btnAcceptPayment.setEnabled(true);
                if (response.isSuccessful()) {
                    ResponseDefault resp = response.body();
                    if (!resp.isError()) {
                        showPaymentSuccessAlert();
                        ModelPayment modelPayment = new ModelPayment();
                        modelPayment.setPaymentAmount(Integer.valueOf(amount));
                        modelPayment.setPaymentDate("Now");
                        modelPaymentList.add(modelPayment);
                        adapterPayment.updateList(modelPaymentList);
                        adapterPayment.notifyItemInserted(modelPaymentList.size());
                        inputPaymentAmount.setText("");
                    } else {
                        if (resp.getMessage().toLowerCase().trim().equals("amount could not be greater than invoice amount")) {
                            inputPaymentAmount.setError(resp.getMessage());
                            inputPaymentAmount.requestFocus();
                            Helper.playError();
                        } else {
                            Toast.makeText(InvoicePaymentActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Helper.playVibrate();
                    }
                } else {
                    Toast.makeText(InvoicePaymentActivity.this, "Response Not Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                t.printStackTrace();
                btnAcceptPayment.setEnabled(true);
            }
        });
    }

    public void showPaymentSuccessAlert() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(InvoicePaymentActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Payment Success");
        sweetAlertDialog.show();
        int remAmount = Integer.parseInt(tvInvoiceRemainingAmount.getText().toString());
        int pdAmount = Integer.parseInt(tvInvoicePaidAmount.getText().toString());
        tvInvoiceRemainingAmount.setText(String.valueOf(remAmount - Integer.parseInt(amount)));
        tvInvoicePaidAmount.setText(String.valueOf(pdAmount + Integer.parseInt(amount)));
        Helper.playSuccess();
        Helper.playVibrate();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getAllPayments() {
        Call<ResponsePayments> call = ApiClient.getInstance().getApi().getInvoicePaymentsByInvoiceNumber(token, invoiceNumber);
        call.enqueue(new Callback<ResponsePayments>() {
            @Override
            public void onResponse(Call<ResponsePayments> call, Response<ResponsePayments> response) {
                if (response.isSuccessful()) {
                    ResponsePayments resp = response.body();
                    if (!resp.isError()) {
                        modelPaymentList = resp.getPayments();
                        adapterPayment = new AdapterPayment(getApplicationContext(), modelPaymentList);
                        recyclerView.setAdapter(adapterPayment);
                    } else
                        Toast.makeText(InvoicePaymentActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(InvoicePaymentActivity.this, "Response is not success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponsePayments> call, Throwable t) {

            }
        });
    }

    private void getInvoiceByInvoiceNumber() {
        Call<ResponseInvoiceSingle> call = ApiClient.getInstance().getApi().getInvoiceByInvoiceNumber(token, invoiceNumber);
        call.enqueue(new Callback<ResponseInvoiceSingle>() {
            @Override
            public void onResponse(Call<ResponseInvoiceSingle> call, Response<ResponseInvoiceSingle> response) {
                if (response.isSuccessful()) {
                    ResponseInvoiceSingle resp = response.body();
                    if (!resp.isError()) {
                        ModelInvoice invoice = resp.getInvoice();
                        setDataToView(invoice);
                    } else
                        Toast.makeText(InvoicePaymentActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Response Not Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseInvoiceSingle> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setDataToView(ModelInvoice invoice) {
        try {
            Picasso.get().load(invoice.getSellerImage()).placeholder(R.drawable.user).into(ivSeller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (invoice.getInvoiceStatus().equals("PAID"))
            tvInvoiceStatus.setBackgroundColor(Color.BLUE);
        else
            tvInvoiceStatus.setBackgroundColor(Color.RED);
        if (invoice.getInvoiceRemainingAmount() > 0 || invoice.getInvoiceStatus().equals("UNPAID"))
            layoutAcceptPayment.setVisibility(View.VISIBLE);
        else
            layoutAcceptPayment.setVisibility(View.GONE);

        sellerId = invoice.getSellerId();
        sellerName = invoice.getSellerName();
        invoiceNumber = invoice.getInvoiceNumber();

        tvSellerName.setText(invoice.getSellerName());
        tvSellerMobile.setText(invoice.getSellerContactNumber());
        tvSellerAddress.setText(invoice.getSellerAddress());
        tvInvoiceNumber.setText(invoice.getInvoiceNumber());
        tvInvoiceDate.setText(invoice.getInvoiceDate());
        tvInvoiceAmount.setText(String.valueOf(invoice.getInvoiceAmount()));
        tvInvoicePaidAmount.setText(String.valueOf(invoice.getInvoicePaidAmount()));
        tvInvoiceRemainingAmount.setText(String.valueOf(invoice.getInvoiceRemainingAmount()));
        tvInvoiceStatus.setText(invoice.getInvoiceStatus());
    }

    private void init() {
        sp = SharedPrefHandler.getInstance(getApplicationContext());
        user = sp.getUser();
        token = user.getToken();
        ivSeller = findViewById(R.id.ivSeller);
        tvSellerName = findViewById(R.id.tvSellerName);
        tvSellerMobile = findViewById(R.id.tvSellerMobile);
        tvSellerAddress = findViewById(R.id.tvSellerAddress);
        tvInvoiceNumber = findViewById(R.id.tvInvoiceNumber);
        tvInvoiceDate = findViewById(R.id.tvInvoiceDate);
        tvInvoiceAmount = findViewById(R.id.tvInvoiceAmount);
        tvInvoicePaidAmount = findViewById(R.id.tvInvoicePaidAmount);
        tvInvoiceRemainingAmount = findViewById(R.id.tvInvoiceRemainingAmount);
        tvInvoiceStatus = findViewById(R.id.tvInvoiceStatus);
        recyclerView = findViewById(R.id.rvPayments);
        btnAcceptPayment = findViewById(R.id.btnAcceptPayment);
        inputPaymentAmount = findViewById(R.id.inputPaymentAmount);
        layoutAcceptPayment = findViewById(R.id.layoutAcceptPayment);
        intent = getIntent();
        modelPaymentList = new ArrayList<>();
        actionBar = getSupportActionBar();
    }
}