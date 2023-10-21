package com.socialcodia.stockmanagement.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.adapters.AdapterAdmin;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelAdmin;
import com.socialcodia.stockmanagement.pojos.ResponseAdmin;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ModelAdmin> modelAdminList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admins, container, false);
        init(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        getAdmins();
        return view;
    }

    private void init(View view)
    {
        recyclerView = view.findViewById(R.id.rvAdmin);
    }

    private void getAdmins()
    {
        Call<ResponseAdmin> call = ApiClient.getInstance().getApi().getAdmin(Helper.getToken());
        call.enqueue(new Callback<ResponseAdmin>() {
            @Override
            public void onResponse(Call<ResponseAdmin> call, Response<ResponseAdmin> response) {
                if (response.isSuccessful())
                {
                    ResponseAdmin resp = response.body();
                    if (!resp.isError())
                    {
                        modelAdminList = resp.getAdmins();
                        AdapterAdmin adapterAdmin = new AdapterAdmin(modelAdminList,getContext());
                        recyclerView.setAdapter(adapterAdmin);
                    }
                    else
                    {
                        TastyToast.makeText(getContext(),resp.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAdmin> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}