package com.socialcodia.stockmanagement.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class AddSellerFragment extends Fragment {
    private ImageView ivSeller;
    private EditText inputSellerName, inputSellerEmail, inputSellerMobile, inputSellerMobile1, inputSellerAddress;
    private Button btnAddSeller;
    private String[] storagePermission;
    private Uri filePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_seller, container, false);
        init(view);


        btnAddSeller.setOnClickListener(v -> validateData());
        ivSeller.setOnClickListener(v -> chooseImage());

        return view;
    }

    private void selectImage() {
        if (checkStoragePermission()) {

        } else
            requestStoragePermission();

    }

    private void validateData() {
        String sellerName = inputSellerName.getText().toString().trim();
        String sellerEmail = inputSellerEmail.getText().toString().trim();
        String sellerContact = inputSellerMobile.getText().toString().trim();
        String sellerContact1 = inputSellerMobile1.getText().toString().trim();
        String sellerAddress = inputSellerAddress.getText().toString().trim();
        if (sellerName.isEmpty()) {
            inputSellerName.setError("Enter Name");
            inputSellerName.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
            return;
        }
        if (sellerName.length() < 4) {
            inputSellerName.setError("Name Should Be Up To 4 Character");
            inputSellerName.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
            return;
        }
        if (sellerContact.isEmpty()) {
            inputSellerMobile.setError("Enter Mobile Number");
            inputSellerMobile.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
            return;
        }
        if (sellerContact.length() != 10) {
            inputSellerMobile.setError("Enter Valid Mobile Number");
            inputSellerMobile.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
            return;
        }
        if (sellerAddress.isEmpty()) {
            inputSellerAddress.setError("Enter Address");
            inputSellerAddress.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
            return;
        }
        if (sellerAddress.length() < 7) {
            inputSellerAddress.setError("Address Should Be Up To 7 Character");
            inputSellerAddress.requestFocus();
            Helper.playWarning();
            Helper.playVibrate();
            return;
        }
        if (filePath != null) {
            File file = new File(getRealFileFromURI(filePath));
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("sellerImage", file.getName(), requestBody);
            addSellerWithImage(sellerName, sellerEmail, sellerContact, sellerContact1, sellerAddress, multipartBody);
        } else {
            addSeller(sellerName, sellerEmail, sellerContact, sellerContact1, sellerAddress);
        }

    }

    private void addSeller(String sellerName, String sellerEmail, String sellerContact, String sellerContact1, String sellerAddress) {
        if (Helper.isNetworkAvailable()) {
            btnAddSeller.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().addSeller(Helper.getToken(), sellerName, sellerEmail, sellerContact, sellerContact1, sellerAddress);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    if (response.isSuccessful()) {
                        ResponseDefault resp = response.body();
                        if (!resp.isError()) {
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Helper.playSuccess();
                        } else {
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            Helper.playWarning();
                        }
                        Helper.playVibrate();
                    } else
                        TastyToast.makeText(getContext(), "Response Not Success", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    btnAddSeller.setEnabled(true);
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnAddSeller.setEnabled(true);
                }
            });
        }
    }

    private void addSellerWithImage(String sellerName, String sellerEmail, String sellerContact, String sellerContact1, String sellerAddress, MultipartBody.Part multipartBody) {
        if (Helper.isNetworkAvailable()) {
            btnAddSeller.setEnabled(false);
            Map<String, RequestBody> map = new HashMap<>();
            map.put("sellerName", doRequestBody(sellerName));
            map.put("sellerContactNumber", doRequestBody(sellerContact));
            map.put("sellerContactNumber1", doRequestBody(sellerContact1));
            map.put("sellerAddress", doRequestBody(sellerAddress));
            map.put("sellerEmail", doRequestBody(sellerEmail));
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().addSellerWithImage(Helper.getToken(), map, multipartBody);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    if (response.isSuccessful()) {

                        ResponseDefault resp = response.body();
                        if (!resp.isError()) {
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            Helper.playSuccess();
                        } else {
                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            Helper.playWarning();
                        }
                        Helper.playVibrate();
                    } else
                        TastyToast.makeText(getContext(), "Response Not Success", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    btnAddSeller.setEnabled(true);
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnAddSeller.setEnabled(true);
                }
            });
        }
    }

    private String getRealFileFromURI(Uri filePath) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), filePath, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnIndex);
        cursor.close();
        return result;
    }


    private RequestBody doRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plane"), value);
    }

    private void init(View view) {
        ivSeller = view.findViewById(R.id.ivSeller);
        inputSellerName = view.findViewById(R.id.inputSellerName);
        inputSellerEmail = view.findViewById(R.id.inputSellerEmail);
        inputSellerMobile = view.findViewById(R.id.inputSellerMobile);
        inputSellerMobile1 = view.findViewById(R.id.inputSellerMobile1);
        inputSellerAddress = view.findViewById(R.id.inputSellerAddress);
        btnAddSeller = view.findViewById(R.id.btnAddSeller);
        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    private boolean checkStoragePermission() {
        boolean permission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return permission;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), storagePermission, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean permission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (permission)
                TastyToast.makeText(getContext(), "Permission Granted", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            else
                TastyToast.makeText(getContext(), "Permission Declined", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

        }
    }

    private void chooseImage() {
        if (checkStoragePermission()) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1000);
        } else
            requestStoragePermission();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            filePath = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                ivSeller.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}