package com.socialcodia.stockmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelUser;
import com.socialcodia.stockmanagement.pojos.ResponseLogin;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);

        SharedPrefHandler sp = new SharedPrefHandler(getApplicationContext());
        if(sp.isUserLoggedIn())
            sendToMain();

        btnLogin.setOnClickListener(v->validateData());

    }

    private void validateData()
    {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        if (email.isEmpty())
        {
            inputEmail.setError("Enter Email");
            inputEmail.requestFocus();
            Helper.playVibrate();
            return;
        }
        if (password.isEmpty())
        {
            inputPassword.setError("Enter Password");
            inputPassword.requestFocus();
            Helper.playVibrate();
            return;
        }
        if (password.length()<7)
        {
            inputPassword.setError("Enter Valid Password");
            inputPassword.requestFocus();
            Helper.playVibrate();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            inputEmail.setError("Email is not Valid");
            inputEmail.requestFocus();
            Helper.playVibrate();
        }
        else
        {
            doLogin(email,password);
        }
    }

    private void doLogin(String email, String password)
    {
        btnLogin.setEnabled(false);
        Call<ResponseLogin> call = ApiClient.getInstance().getApi().login(email,password);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response.isSuccessful())
                {
                    btnLogin.setEnabled(true);
                    ResponseLogin res = response.body();
                    if (!res.isError())
                    {
                        SharedPrefHandler sp = SharedPrefHandler.getInstance(LoginActivity.this);
                        ModelUser user = res.getUser();
                        sp.saveUser(user);
                        sendToMain();
                    }
                    else
                    {
                        if (res.getMessage().toLowerCase().trim().equals("wrong password"))
                        {
                            inputPassword.setError("Wrong Password");
                            inputPassword.requestFocus();
                            Helper.playVibrate();
                        }
                        else if (res.getMessage().trim().toLowerCase().equals("user not found"))
                        {
                            inputEmail.setError("Email Not Registered");
                            inputEmail.requestFocus();
                            Helper.playVibrate();
                        }
                        else
                            Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {

            }
        });
    }

    private void sendToMain()
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();
    }

}