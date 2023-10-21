package com.socialcodia.stockmanagement.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelUser;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private SharedPrefHandler sp;
    private ModelUser user;
    private ImageView userProfileImage;
    private EditText  inputName,inputEmail;
    private Button btnUpdateProfile;
    private Uri filePath;
    private String[] storagePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        init();

        actionBar.setTitle("Update Profile");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setUserToView();

        btnUpdateProfile.setOnClickListener(v-> validateData());
        userProfileImage.setOnClickListener(v->chooseImage());

    }

    private void validateData()
    {
        String name = inputName.getText().toString().trim();
        if (name.isEmpty())
        {
            inputName.setError("Enter Name");
            inputName.requestFocus();
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (name.length()<3)
        {
            inputName.setError("Name too Short");
            inputName.requestFocus();
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        if (name.length()>30)
        {
            inputName.setError("Name too Long");
            inputName.requestFocus();
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (filePath==null)
        {
            updateProfile(name);
        }
        else
        {
            File file = new File(getRealPathFromUri(filePath));
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("adminImage",file.getName(),requestBody);
            updateAdminWithImage(name,part);
        }
    }

    private String getRealPathFromUri(Uri filePath)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),filePath,proj,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnIndex);
        cursor.close();
        return result;
    }

    private boolean checkStoragePermission()
    {
        return ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(EditProfileActivity.this,storagePermission,100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0)
        {
            boolean permission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (permission)
            {
                TastyToast.makeText(getApplicationContext(),"Permission Granted",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                chooseImage();
            }
            else
                TastyToast.makeText(getApplicationContext(),"Permission Declined",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
        }
    }

    private void chooseImage(){
        if (checkStoragePermission())
        {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,300);
        }
        else
            requestStoragePermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == RESULT_OK && data!=null)
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                userProfileImage.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void setUserToView()
    {
        try {
            Picasso.get().load(user.getImage()).placeholder(R.drawable.user).into(userProfileImage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        inputName.setText(user.getName());
        inputEmail.setText(user.getEmail());
        inputEmail.setEnabled(false);
    }

    private void init()
    {
        actionBar = getSupportActionBar();
        sp = SharedPrefHandler.getInstance(getApplicationContext());
        user = sp.getUser();
        userProfileImage = findViewById(R.id.userProfileImage);
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    private void updateAdminWithImage(String adminName, MultipartBody.Part part)
    {
        if(Helper.isNetworkAvailable())
        {
            btnUpdateProfile.setEnabled(false);
            Map<String, RequestBody> map = new HashMap<>();
            map.put("adminName",toRequestBody(adminName));
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().updateAdminSelfWithImage(Helper.getToken(),map,part);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnUpdateProfile.setEnabled(true);
                    if (response.isSuccessful())
                    {
                        ResponseDefault resp = response.body();
                        if (!resp.isError())
                        {
                            TastyToast.makeText(getApplicationContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                            Helper.playVibrate();
                            Helper.playSuccess();
                        }
                        else
                        {
                            TastyToast.makeText(getApplicationContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                            Helper.playVibrate();
                            Helper.playError();
                        }
                    }
                    else
                    {
                        TastyToast.makeText(getApplicationContext(),String.valueOf(R.string.SWW),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        Helper.playVibrate();
                        Helper.playError();
                    }
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnUpdateProfile.setEnabled(true);
                }
            });

        }
    }

    private RequestBody toRequestBody(String value)
    {
        return RequestBody.create(MediaType.parse("text/plane"),value);
    }

    private void updateProfile(String adminName)
    {
        if (Helper.isNetworkAvailable())
        {
            btnUpdateProfile.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().updateAdminSelf(Helper.getToken(),adminName);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnUpdateProfile.setEnabled(true);
                    if (response.isSuccessful())
                    {
                        ResponseDefault resp = response.body();
                        if (!resp.isError())
                        {
                            TastyToast.makeText(getApplicationContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                            Helper.playVibrate();
                            Helper.playSuccess();
                        }
                        else
                        {
                            TastyToast.makeText(getApplicationContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                            Helper.playVibrate();
                            Helper.playError();
                        }
                    }
                    else
                    {
                        TastyToast.makeText(getApplicationContext(),String.valueOf(R.string.SWW),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        Helper.playVibrate();
                        Helper.playError();
                    }
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnUpdateProfile.setEnabled(true);
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}