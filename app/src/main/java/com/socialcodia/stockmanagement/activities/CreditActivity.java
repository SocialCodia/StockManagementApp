package com.socialcodia.stockmanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.adapters.AdapterCredit;
import com.socialcodia.stockmanagement.adapters.AdapterCreditPayment;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelCredit;
import com.socialcodia.stockmanagement.models.ModelCreditPayment;
import com.socialcodia.stockmanagement.models.ModelCreditor;
import com.socialcodia.stockmanagement.models.ModelSale;
import com.socialcodia.stockmanagement.pojos.ResponseCredit;
import com.socialcodia.stockmanagement.pojos.ResponseCreditPayment;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Intent intent;
    private int creditId;
    private ModelCredit modelCredit;
    private List<ModelSale> modelSaleList;
    private List<ModelCreditPayment> modelCreditPaymentList;
    private RecyclerView recyclerView;
    private RecyclerView rvCreditPayment;
    private String[] callPermission;
    private TextView tvCreditorName, tvCreditorMobile, tvCreditorAddress, tvCreditDescription, tvCreditDate, tvCreditAmount, tvCreditPaidAmount, tvCreditRemainingAmount,tvCreditStatus;
    private EditText inputPaymentAmount;
    private Button btnAcceptPayment;
    private CardView cvAcceptPayment;
    private String amount,creditorName;
    private AdapterCreditPayment adapterCreditPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        init();
        actionBar.setTitle("Credit");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        if (intent.hasExtra("intentCreditId"))
        {
            creditId = Integer.parseInt(intent.getStringExtra("intentCreditId"));
        }
        else
        {
            TastyToast.makeText(getApplicationContext(),"Sorry for Inconvenience, Restart Your App",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT).show();
            onBackPressed();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        rvCreditPayment.setLayoutManager(layoutManager1);

        btnAcceptPayment.setOnClickListener(v->validateData());

        getCreditById();
        getPayments();
    }

    private void getPayments()
    {
        Call<ResponseCreditPayment> call = ApiClient.getInstance().getApi().getCreditPayments(Helper.getToken(),creditId);
        call.enqueue(new Callback<ResponseCreditPayment>() {
            @Override
            public void onResponse(Call<ResponseCreditPayment> call, Response<ResponseCreditPayment> response) {
                if (response.isSuccessful())
                {
                    ResponseCreditPayment resp = response.body();
                    if (!resp.isError())
                    {
                        modelCreditPaymentList = resp.getPayments();
                        adapterCreditPayment = new AdapterCreditPayment(modelCreditPaymentList,getApplicationContext());
                        rvCreditPayment.setAdapter(adapterCreditPayment);
                    }
                    else
                    {
                        TastyToast.makeText(getApplicationContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                }
                else
                {
                    TastyToast.makeText(getApplicationContext(),"Something Went Wrong",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseCreditPayment> call, Throwable t) {

            }
        });
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
            new SweetAlertDialog(CreditActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText( creditorName + " \n is paying you " + amount + " Rupees")
                    .setConfirmText("Accept")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            acceptPayment();
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

    private void acceptPayment()
    {
        btnAcceptPayment.setEnabled(false);
        Call<ResponseDefault> call = ApiClient.getInstance().getApi().addCreditPayment(Helper.getToken(),Integer.parseInt(amount),creditId);
        call.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                if (response.isSuccessful())
                {
                    btnAcceptPayment.setEnabled(true);
                    ResponseDefault resp = response.body();
                    if (!resp.isError())
                    {
                        ModelCreditPayment payment = new ModelCreditPayment();
                        payment.setPaymentAmount(Integer.parseInt(amount));
                        payment.setPaymentDate("Now");
                        modelCreditPaymentList.add(payment);
                        adapterCreditPayment.updateList(modelCreditPaymentList);
                        adapterCreditPayment.notifyItemInserted(modelCreditPaymentList.size());
                        inputPaymentAmount.setText("");
                        showPaymentSuccessAlert();
                    }
                    else
                    {
                        TastyToast.makeText(getApplicationContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        Helper.playError();
                        Helper.playVibrate();
                    }
                }
                else
                {
                    TastyToast.makeText(getApplicationContext(),"Something Went Wrong",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    btnAcceptPayment.setEnabled(true);
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
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(CreditActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Payment Success");
        sweetAlertDialog.show();
        int remAmount = Integer.parseInt(tvCreditRemainingAmount.getText().toString());
        int pdAmount = Integer.parseInt(tvCreditPaidAmount.getText().toString());
        tvCreditRemainingAmount.setText(String.valueOf(remAmount - Integer.parseInt(amount)));
        tvCreditPaidAmount.setText(String.valueOf(pdAmount + Integer.parseInt(amount)));
        if (remAmount - Integer.parseInt(amount)<1)
        {
            tvCreditStatus.setText("PAID");
            tvCreditStatus.setBackgroundColor(Color.BLUE);
        }
        Helper.playSuccess();
        Helper.playVibrate();
//        getAllPayments();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_call,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.miCall)
        {
            makeCall();
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeCall()
    {
        if (checkCallPermission())
        {
            double mobileNumber = Double.parseDouble(tvCreditorMobile.getText().toString().trim());
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+"+91"+mobileNumber));
            startActivity(intent);
        }
        else
            requestCallPermission();
    }

    private boolean checkCallPermission()
    {
        boolean permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                == (PackageManager.PERMISSION_GRANTED);
        return permission;
    }

    private void requestCallPermission()
    {
        ActivityCompat.requestPermissions(this,callPermission,100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0)
        {
            boolean callPermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (callPermissionAccepted)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            else
                checkCallPermission();
        }
    }

    private void getCreditById()
    {
        Call<ResponseCredit> call = ApiClient.getInstance().getApi().getCreditById(Helper.getToken(),creditId);
        call.enqueue(new Callback<ResponseCredit>() {
            @Override
            public void onResponse(Call<ResponseCredit> call, Response<ResponseCredit> response) {
                if (response.isSuccessful())
                {
                    ResponseCredit resp = response.body();
                    if (!resp.isError())
                    {
                        modelCredit = (ModelCredit) resp.getCredit();
                        creditId = modelCredit.getCreditId();
                        ModelCreditor creditor = modelCredit.getCreditor();
                        setCreditorInfo(creditor);
                        setCreditInfo(modelCredit);
                        modelSaleList = modelCredit.getSales();
                        AdapterCredit adapterCredit = new AdapterCredit(getApplicationContext(),modelSaleList);
                        recyclerView.setAdapter(adapterCredit);
                    }
                    else
                    {
                        TastyToast.makeText(getApplicationContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                }
                else
                    TastyToast.makeText(getApplicationContext(),"Response Not Success",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT);
            }

            @Override
            public void onFailure(Call<ResponseCredit> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setCreditorInfo(ModelCreditor creditor)
    {
        creditorName = creditor.getCreditorName();
        tvCreditorName.setText(creditor.getCreditorName());
        tvCreditorMobile.setText(creditor.getCreditorMobile());
        tvCreditorAddress.setText(creditor.getCreditorAddress());
    }

    private void setCreditInfo(ModelCredit credit)
    {
        tvCreditDescription.setText(credit.getCreditDescription());
        tvCreditDate.setText(credit.getCreditDate());
        tvCreditAmount.setText(String.valueOf(credit.getCreditTotalAmount()));
        tvCreditPaidAmount.setText(String.valueOf(credit.getCreditPaidAmount()));
        tvCreditRemainingAmount.setText(String.valueOf(credit.getCreditRemainingAmount()));
        tvCreditStatus.setText(credit.getCreditStatus());
        if (credit.getCreditStatus().toLowerCase().equals("unpaid"))
            tvCreditStatus.setBackgroundColor(Color.RED);
        else
            tvCreditStatus.setBackgroundColor(Color.BLUE);

        if (credit.getCreditRemainingAmount()>1 && credit.getCreditStatus().equals("UNPAID"))
            cvAcceptPayment.setVisibility(View.VISIBLE);
        else
            cvAcceptPayment.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void init()
    {
        actionBar = getSupportActionBar();
        intent = getIntent();
        modelSaleList = new ArrayList<>();
        modelCreditPaymentList = new ArrayList<>();
        recyclerView = findViewById(R.id.rvCredit);
        rvCreditPayment = findViewById(R.id.rvCreditPayment);
        tvCreditorName = findViewById(R.id.tvCreditorName);
        tvCreditorMobile = findViewById(R.id.tvCreditorMobile);
        tvCreditorAddress = findViewById(R.id.tvCreditorAddress);
        tvCreditDescription = findViewById(R.id.tvCreditDescription);
        tvCreditDate = findViewById(R.id.tvCreditDate);
        tvCreditAmount = findViewById(R.id.tvCreditAmount);
        tvCreditPaidAmount = findViewById(R.id.tvCreditPaidAmount);
        tvCreditRemainingAmount = findViewById(R.id.tvCreditRemainingAmount);
        inputPaymentAmount = findViewById(R.id.inputPaymentAmount);
        btnAcceptPayment = findViewById(R.id.btnAcceptPayment);
        cvAcceptPayment = findViewById(R.id.cvAcceptPayment);
        tvCreditStatus = findViewById(R.id.tvCreditStatus);
        callPermission = new String[]{Manifest.permission.CALL_PHONE};
    }
}