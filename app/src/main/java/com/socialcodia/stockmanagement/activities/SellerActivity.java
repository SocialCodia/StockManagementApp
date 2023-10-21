package com.socialcodia.stockmanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.adapters.AdapterInvoiceSimple;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.models.ModelInvoice;
import com.socialcodia.stockmanagement.models.ModelSeller;
import com.socialcodia.stockmanagement.models.ModelUser;
import com.socialcodia.stockmanagement.pojos.ResponseInvoices;
import com.socialcodia.stockmanagement.pojos.ResponseSeller;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerActivity extends AppCompatActivity {
    private SharedPrefHandler sp;
    private ModelUser user;
    private String token,mobileNumber,mobileNumber1;
    private Intent intent;
    private int sellerId;
    private ImageView ivSeller;
    private TextView tvSellerName,tvSellerAddress,tvTotalInvoiceAmount,tvTotalInvoicePaidAmount,tvTotalInvoiceRemainingAmount;
    private Button btnSellerContact, btnSellerContact1;
    private ActionBar actionBar;
    private List<ModelInvoice> modelInvoices;
    private RecyclerView recyclerView;
    private String[] callPermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        init();
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Seller");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        if (intent.hasExtra("intentSellerId"))
        {
            sellerId = Integer.parseInt(intent.getStringExtra("intentSellerId"));
        }

        getSeller();
        getInvoice();
        btnSellerContact.setVisibility(View.GONE);
        btnSellerContact1.setVisibility(View.GONE);
        btnSellerContact.setOnClickListener(v->makeCall());
        btnSellerContact1.setOnClickListener(v->makeCall1());
    }


    private void makeCall()
    {
        if (checkCallPermission())
        {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+mobileNumber));
            startActivity(intent);
        }
        else
            requestCallPermission();
    }

    private void makeCall1()
    {
        if (checkCallPermission())
        {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+mobileNumber1));
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getInvoice()
    {
        Call<ResponseInvoices> call = ApiClient.getInstance().getApi().getInvoiceBySellerId(token,sellerId);
        call.enqueue(new Callback<ResponseInvoices>() {
            @Override
            public void onResponse(Call<ResponseInvoices> call, Response<ResponseInvoices> response) {
                if (response.isSuccessful())
                {
                    ResponseInvoices resp = response.body();
                    if (!resp.isError())
                    {
                        modelInvoices = resp.getInvoices();
                        int totalAmount = 0;
                        int totalPaidAmount = 0;
                        int totalRemainingAmount = 0;
                        for (ModelInvoice inv : modelInvoices)
                        {
                            totalAmount = totalAmount + inv.getInvoiceAmount();
                            totalPaidAmount = totalPaidAmount + inv.getInvoicePaidAmount();
                            totalRemainingAmount = totalRemainingAmount + inv.getInvoiceRemainingAmount();
                        }
                        tvTotalInvoiceAmount.setText(String.valueOf(totalAmount));
                        tvTotalInvoicePaidAmount.setText(String.valueOf(totalPaidAmount));
                        tvTotalInvoiceRemainingAmount.setText(String.valueOf(totalRemainingAmount));
                        AdapterInvoiceSimple adapterInvoiceSimple = new AdapterInvoiceSimple(getApplicationContext(),modelInvoices);
                        recyclerView.setAdapter(adapterInvoiceSimple);
                    }
                    else
                        Toast.makeText(SellerActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(SellerActivity.this, "Response is not success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseInvoices> call, Throwable t) {

            }
        });
    }


    private void getSeller()
    {
        Call<ResponseSeller> call = ApiClient.getInstance().getApi().getSellerById(token,sellerId);
        call.enqueue(new Callback<ResponseSeller>() {
            @Override
            public void onResponse(Call<ResponseSeller> call, Response<ResponseSeller> response) {
                if (response.isSuccessful())
                {
                    ResponseSeller resp = response.body();
                    if (!resp.isError())
                    {
                        ModelSeller seller = resp.getSeller();
                        setSellerInfo(seller);
                    }
                    else
                        Toast.makeText(SellerActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(SellerActivity.this, "Response is not success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseSeller> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setSellerInfo(ModelSeller seller)
    {
        mobileNumber = seller.getSellerContactNumber();
        mobileNumber1 = seller.getSellerContactNumber1();
        tvSellerName.setText(seller.getSellerName());
        tvSellerAddress.setText(seller.getSellerAddress());
        btnSellerContact.setVisibility(View.VISIBLE);
        btnSellerContact.setText(String.valueOf(seller.getSellerContactNumber()));
        if (seller.getSellerContactNumber1()==null && seller.getSellerContactNumber1().isEmpty() || seller.getSellerContactNumber1().length()<1)
            btnSellerContact1.setVisibility(View.GONE);
        else
        {
            btnSellerContact1.setVisibility(View.VISIBLE);
            btnSellerContact1.setText(String.valueOf(seller.getSellerContactNumber1()));
        }

        try {
            Picasso.get().load(seller.getSellerImage()).placeholder(R.drawable.user).into(ivSeller);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void init()
    {
        ivSeller = findViewById(R.id.ivSeller);
        tvSellerName = findViewById(R.id.tvSellerName);
        tvSellerAddress = findViewById(R.id.tvSellerAddress);
        btnSellerContact = findViewById(R.id.btnSellerContact);
        btnSellerContact1 = findViewById(R.id.btnSellerContact1);
        tvTotalInvoiceAmount = findViewById(R.id.tvTotalInvoiceAmount);
        tvTotalInvoicePaidAmount = findViewById(R.id.tvTotalInvoicePaidAmount);
        tvTotalInvoiceRemainingAmount = findViewById(R.id.tvTotalInvoiceRemainingAmount);
        sp = SharedPrefHandler.getInstance(getApplicationContext());
        recyclerView = findViewById(R.id.rvInvoices);
        modelInvoices = new ArrayList<>();
        user = sp.getUser();
        token = user.getToken();
        intent = getIntent();
        callPermission = new String[]{Manifest.permission.CALL_PHONE};
    }
}