package com.socialcodia.stockmanagement.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.etc.SearchableSpinner;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddAdminFragment extends Fragment {

    private ImageView ivAdmin;
    private EditText inputAdminName, inputPassword,inputAdminEmail;
    private SearchableSpinner selectPosition;
    private Button btnAddAdmin;
    private String[] storagePermission;
    private Uri filePath;
    private ArrayList<String> list;
    private int positionId;
    private String currentAdminPassword,name,email,password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_admin, container, false);

        init(view);

        list.add("Admin");
        list.add("Manager");
        list.add("Helper");
        selectPosition.setTitle("Select Position");
        try {
            selectPosition.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,list));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        selectPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionId = i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ivAdmin.setOnClickListener(v->chooseImage());
        btnAddAdmin.setOnClickListener(v->validateData());
        return view;
    }

    private RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plane"), value);
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

    private void validateData()
    {
         name = inputAdminName.getText().toString().trim();
         email = inputAdminEmail.getText().toString().trim();
         password = inputPassword.getText().toString().trim();
        if (name.isEmpty())
        {
            inputAdminName.setError("Enter Name");
            inputAdminName.requestFocus();
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (name.length()<3 || name.length()> 30)
        {
            inputAdminName.setError("Invalid Name Length");
            inputAdminName.requestFocus();
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        if (email.isEmpty())
        {
            inputAdminEmail.setError("Enter Email");
            inputAdminEmail.requestFocus();
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            inputAdminEmail.setError("Enter Valid Email");
            inputAdminEmail.requestFocus();
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (password.isEmpty())
        {
            inputPassword.setError("Enter Password");
            inputPassword.requestFocus();
            Helper.playVibrate();
            Helper.playError();
            return;
        }
        if (password.length()<7)
        {
            inputPassword.setError("Password Should Be Greater Than 7 Character");
            inputPassword.requestFocus();
            Helper.playError();
            Helper.playVibrate();
            return;
        }
        if (positionId == -1 || positionId < 0 || String.valueOf(positionId).isEmpty())
        {
            TastyToast.makeText(getContext(),"Select Position",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
            Helper.playVibrate();
            Helper.playSuccess();
        }
        else
        {
            askForPassword();
        }
    }

    private void askForPassword()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Your Password");
        EditText editText = new EditText(getContext());
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setHint("Enter Password");
        builder.setView(editText);
        builder.setCancelable(false);
        builder.setPositiveButton("Add Admin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TastyToast.makeText(getContext(),"Cancelled",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT);
            }
        });
        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        String password = editText.getText().toString().trim();
                        currentAdminPassword = password;
                        if (password.isEmpty())
                        {
                            editText.setError("Enter Password");
                            editText.requestFocus();
                            Helper.playError();
                            Helper.playVibrate();
                            return;
                        }
                        else if (password.length()<7)
                        {
                            editText.setError("Wrong Password");
                            editText.requestFocus();
                            Helper.playError();
                            Helper.playVibrate();
                        }
                        else if (filePath==null)
                        {
                            addAdmin(name,email,password,positionId);
                            mAlertDialog.dismiss();
                        }
                        else
                        {
                            File file = new File(getRealFileFromURI(filePath));
                            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("adminImage", file.getName(), requestBody);
                            addAdminWithImage(name,email,password,positionId,multipartBody);
                            mAlertDialog.dismiss();
                        }
                    }
                });
            }
        });
        mAlertDialog.show();
    }

    private void addAdmin(String name, String email, String password, int positionId)
    {
        if (Helper.isNetworkAvailable())
        {
            btnAddAdmin.setEnabled(false);
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().addAdmin(Helper.getToken(),name,email,password,String.valueOf(positionId),currentAdminPassword);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnAddAdmin.setEnabled(true);
                    if (response.isSuccessful())
                    {
                        ResponseDefault resp = response.body();
                        if (!resp.isError())
                        {
                            TastyToast.makeText(getContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                            Helper.playVibrate();
                            Helper.playSuccess();
                            inputPassword.setText("");
                            inputAdminEmail.setText("");
                            inputAdminName.setText("");
                        }
                        else
                        {
                            TastyToast.makeText(getContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                            Helper.playVibrate();
                            Helper.playError();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                    btnAddAdmin.setEnabled(true);
                }
            });
        }
    }

    private void addAdminWithImage(String name, String email, String password, int positionId, MultipartBody.Part multipartBody)
    {
        if (Helper.isNetworkAvailable())
        {
            btnAddAdmin.setEnabled(false);
            Map<String,RequestBody> map = new HashMap<>();
            map.put("adminName",toRequestBody(name));
            map.put("adminEmail",toRequestBody(email));
            map.put("adminPassword",toRequestBody(password));
            map.put("adminPosition",toRequestBody(String.valueOf(positionId)));
            map.put("currentAdminPassword",toRequestBody(currentAdminPassword));
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().addAdminWithImage(Helper.getToken(),map,multipartBody);
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    btnAddAdmin.setEnabled(true);
                    if (response.isSuccessful())
                    {
                        ResponseDefault resp = response.body();
                        if (!resp.isError())
                        {
                            TastyToast.makeText(getContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                            Helper.playVibrate();
                            Helper.playSuccess();
                            inputPassword.setText("");
                            inputAdminEmail.setText("");
                            inputAdminName.setText("");
                            filePath = null;
                            ivAdmin.setImageResource(R.drawable.user);
                        }
                        else
                        {
                            TastyToast.makeText(getContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                            Helper.playVibrate();
                            Helper.playError();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    btnAddAdmin.setEnabled(true);
                    t.printStackTrace();
                }
            });
        }
    }

    private void chooseImage()
    {
        if (checkStoragePermission())
        {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,200);
        }
        else
            requestStoragePermission();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data!=null)
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                ivAdmin.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean checkStoragePermission()
    {
        boolean permission = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return permission;
    }

    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(getActivity(),storagePermission,100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0)
        {
            boolean permission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (permission)
            {
                TastyToast.makeText(getContext(), "Permission Granted", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                chooseImage();
            }
            else
                TastyToast.makeText(getContext(), "Permission Declined", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }
    }

    private void init(View view)
    {
        ivAdmin = view.findViewById(R.id.ivAdmin);
        inputAdminName = view.findViewById(R.id.inputAdminName);
        inputPassword = view.findViewById(R.id.inputPassword);
        inputAdminEmail = view.findViewById(R.id.inputAdminEmail);
        selectPosition = view.findViewById(R.id.selectPosition);
        btnAddAdmin = view.findViewById(R.id.btnAddAdmin);
        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        list = new ArrayList<>();
    }
}