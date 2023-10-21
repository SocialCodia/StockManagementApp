package com.socialcodia.stockmanagement.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.pojos.ResponseInvoiceSingle;
import com.socialcodia.stockmanagement.storages.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private WebView webView;
    private Intent intent;
    private String invoiceNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.webView);
        intent = getIntent();
        if (intent.hasExtra("intentInvoiceNumber"))
        {
            invoiceNumber = intent.getStringExtra("intentInvoiceNumber");
        }
        else
            onBackPressed();


        actionBar = getSupportActionBar();
        actionBar.setTitle("Invoice");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        getInvoice();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void viewWeb(String invoiceUrl) {
        Log.d(Constants.TAG, "viewWeb: "+invoiceUrl);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+invoiceUrl);
    }

    private void getInvoice() {
        Call<ResponseInvoiceSingle> call = ApiClient.getInstance().getApi().getInvoiceUrlByInvoiceNumber(Helper.getToken(), invoiceNumber);
        call.enqueue(new Callback<ResponseInvoiceSingle>() {
            @Override
            public void onResponse(Call<ResponseInvoiceSingle> call, Response<ResponseInvoiceSingle> response) {
                if (response.isSuccessful()) {
                    ResponseInvoiceSingle resp = response.body();
                    if (!resp.isError()) {
                        String invoiceUrl = resp.getInvoice().getInvoiceUrl();
                        viewWeb(invoiceUrl);
                    }
                    else
                        TastyToast.makeText(getApplicationContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                }
                else
                    TastyToast.makeText(getApplicationContext(),String.valueOf(R.string.SWW),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            }

            @Override
            public void onFailure(Call<ResponseInvoiceSingle> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}