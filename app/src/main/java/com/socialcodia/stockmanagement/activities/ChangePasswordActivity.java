package com.socialcodia.stockmanagement.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class ChangePasswordActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private EditText inputPassword, inputNewPassword,inputConfirmPassword;
    private Button btnUpdatePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        inputNewPassword = findViewById(R.id.inputNewPassword);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Change Password");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnUpdatePassword.setOnClickListener(v->validateData());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    private void validateData()
    {
        String password = inputPassword.getText().toString().trim();
        String newPassword = inputNewPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();
        if (password.isEmpty())
        {
            inputPassword.setError("Enter Password");
            inputPassword.requestFocus();
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        if (password.length()<7 || password.length()>30)
        {
            inputPassword.setError("Password Should Be Greater Than 7 Character");
            inputPassword.requestFocus();
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        if (newPassword.isEmpty())
        {
            inputNewPassword.setError("Enter New Password");
            inputNewPassword.requestFocus();
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        if (newPassword.length()<7 || newPassword.length()>30)
        {
            inputNewPassword.setError("Password Should Be Greater Than 7 Character");
            inputNewPassword.requestFocus();
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        if (confirmPassword.isEmpty())
        {
            inputConfirmPassword.setError("Enter Confirm Password");
            inputConfirmPassword.requestFocus();
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        if (confirmPassword.length()<7 || confirmPassword.length()>30)
        {
            inputConfirmPassword.setError("Password Should Be Greater Than 7 Character");
            inputConfirmPassword.requestFocus();
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        if (!newPassword.equals(confirmPassword))
        {
            inputNewPassword.setError("Password Not Matched");
            inputNewPassword.requestFocus();
            inputConfirmPassword.setError("Password Not Matched");
            inputConfirmPassword.requestFocus();
            inputConfirmPassword.setText("");
            inputNewPassword.setError("");
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        if (password.equals(newPassword))
        {
            inputNewPassword.setError("You can't use your old password");
            inputNewPassword.requestFocus();
            inputNewPassword.setText("");
            Helper.playVibrate();
            Helper.playError();
        }
        else
        {
            doUpdatePassword(password,newPassword);
        }
    }

    private void doUpdatePassword(String password, String newPassword)
    {
        if (Helper.isNetworkAvailable())
        {
            btnUpdatePassword.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().updatePassword(Helper.getToken(),password,newPassword);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnUpdatePassword.setEnabled(true);
                    if (response.isSuccessful())
                    {
                        ResponseDefault resp = response.body();
                        if (!resp.isError())
                        {
                            TastyToast.makeText(getApplicationContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                            Helper.playSuccess();
                        }
                        else
                        {
                            if (resp.getMessage().equals("Wrong Password"))
                            {
                                inputPassword.setError("Wrong Password");
                                inputPassword.requestFocus();
                            }
                            else
                                TastyToast.makeText(getApplicationContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                            Helper.playError();
                        }
                        Helper.playVibrate();
                    }
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnUpdatePassword.setEnabled(true);
                }
            });
        }
    }

}