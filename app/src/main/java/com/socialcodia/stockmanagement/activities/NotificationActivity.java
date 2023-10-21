package com.socialcodia.stockmanagement.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Button btnUpdateFirebaseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        btnUpdateFirebaseToken = findViewById(R.id.btnUpdateFirebaseToken);

        actionBar = getSupportActionBar();

        actionBar.setTitle("Notification Setting");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        btnUpdateFirebaseToken.setOnClickListener(v->updateFirebaseToken());

    }

    private void updateFirebaseToken()
    {
        if (Helper.isNetworkAvailable())
        {
            btnUpdateFirebaseToken.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().updateFirebaseToken(Helper.getToken(),Helper.getFirebaseToken());
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnUpdateFirebaseToken.setEnabled(true);
                    if (response.isSuccessful())
                    {
                        ResponseDefault resp = response.body();
                        if (!resp.isError())
                        {
                           showSuccessAlert(resp.getMessage());
                        }
                        else
                        {
                            TastyToast.makeText(getApplicationContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnUpdateFirebaseToken.setEnabled(true);
                }
            });
        }
    }

    public void showSuccessAlert(String message) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(NotificationActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText(message);
        sweetAlertDialog.show();
        Helper.playSuccess();
        Helper.playVibrate();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}