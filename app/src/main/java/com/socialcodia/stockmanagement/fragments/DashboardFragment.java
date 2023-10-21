package com.socialcodia.stockmanagement.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.models.ModelCount;
import com.socialcodia.stockmanagement.models.ModelDailySalesStatus;
import com.socialcodia.stockmanagement.models.ModelUser;
import com.socialcodia.stockmanagement.pojos.ResponseCounts;
import com.socialcodia.stockmanagement.pojos.ResponseDailySalesStatus;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment implements View.OnClickListener {
    
    private BarChart barDailySales;
    private ArrayList<BarEntry> listDailySales;
    private SharedPrefHandler sp;
    private ModelUser user;
    private String token;
    private List<ModelDailySalesStatus> modelDailySalesStatusList;
    private TextView tvProductsCount,tvMoreProducts,tvTodaysSaleCount,tvMoreTodaysSale,tvBrandsCount,tvMoreBrands,tvNoticeCount,tvMoreNotice,tvExpiringCount,tvMoreExpiring,tvExpiredCount,tvMoreExpired;
    private ActionBar actionBar;
    private Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        init(view);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        getDailySalesStatus();
        getCounts();
        tvMoreProducts.setOnClickListener(this);
        tvMoreTodaysSale.setOnClickListener(this);
        tvMoreBrands.setOnClickListener(this);
        tvMoreNotice.setOnClickListener(this);
        tvMoreExpiring.setOnClickListener(this);
        tvMoreExpired.setOnClickListener(this);

        return view;
    }

    private void getCounts()
    {
        Call<ResponseCounts> call = ApiClient.getInstance().getApi().getCounts(token);
        call.enqueue(new Callback<ResponseCounts>() {
            @Override
            public void onResponse(Call<ResponseCounts> call, Response<ResponseCounts> response) {
                if (response.isSuccessful())
                {
                    ResponseCounts resp = response.body();
                    if (!resp.isError())
                    {
                        ModelCount count = resp.getCounts();
                        setCountsToView(count);
                    }
                    else
                        Toast.makeText(getContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "Response Is Not Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseCounts> call, Throwable t) {

            }
        });
    }

    private void setCountsToView(ModelCount count)
    {
        int productsCount = count.getProductsCount();
        int productsAvailableCount = count.getProductsAvailableCount();
        int salesCount = count.getSalesCount();
        int brandsCount = count.getBrandsCount();
        int productsNoticeCount = count.getProductsNoticeCount();
        int productsExpiringCount = count.getProductsExpiringCount();
        int productsExpiredCount = count.getProductsExpiredCount();

        tvProductsCount.setText(String.valueOf(productsAvailableCount));
        tvTodaysSaleCount.setText(String.valueOf(salesCount));
        tvBrandsCount.setText(String.valueOf(brandsCount));
        tvNoticeCount.setText(String.valueOf(productsNoticeCount));
        tvExpiringCount.setText(String.valueOf(productsExpiringCount));
        tvExpiredCount.setText(String.valueOf(productsExpiredCount));
    }

    private void getDailySalesStatus()
    {
        Call<ResponseDailySalesStatus> call = ApiClient.getInstance().getApi().getDailySalesStatus(token);
        call.enqueue(new Callback<ResponseDailySalesStatus>() {
            @Override
            public void onResponse(Call<ResponseDailySalesStatus> call, Response<ResponseDailySalesStatus> response) {
                if (response.isSuccessful())
                {
                    ResponseDailySalesStatus resp = response.body();
                    if (!resp.isError())
                    {
                        modelDailySalesStatusList = resp.getStatus();
                        for(ModelDailySalesStatus status : modelDailySalesStatusList)
                        {
                            listDailySales.add(new BarEntry(Integer.parseInt(status.getDay()),Integer.parseInt(status.getTotalSales())));
                        }
                        setDailySalesStatus(listDailySales,"Daily Sales Record");
                    }
                    else
                        Toast.makeText(getContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "Response is not success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseDailySalesStatus> call, Throwable t) {

            }
        });
    }

    private void setDailySalesStatus(ArrayList<BarEntry> list,String desc)
    {
        BarDataSet barDataSet = new BarDataSet(this.listDailySales,desc);
        BarData barData = new BarData(barDataSet);
        barDailySales.setFitBars(true);
        barDailySales.setData(barData);
        barDailySales.getDescription().setText(desc);
        barDailySales.animateY(2000);

    }

    private void init(View view)
    {
        barDailySales = view.findViewById(R.id.barDailySales);
        tvProductsCount = view.findViewById(R.id.tvProductsCount);
        tvMoreProducts = view.findViewById(R.id.tvMoreProducts);
        tvTodaysSaleCount = view.findViewById(R.id.tvTodaysSaleCount);
        tvMoreTodaysSale = view.findViewById(R.id.tvMoreTodaysSale);
        tvBrandsCount = view.findViewById(R.id.tvBrandsCount);
        tvMoreBrands = view.findViewById(R.id.tvMoreBrands);
        tvNoticeCount = view.findViewById(R.id.tvNoticeCount);
        tvMoreNotice = view.findViewById(R.id.tvMoreNotice);
        tvExpiringCount = view.findViewById(R.id.tvExpiringCount);
        tvMoreExpiring = view.findViewById(R.id.tvMoreExpiring);
        tvExpiredCount = view.findViewById(R.id.tvExpiredCount);
        tvMoreExpired = view.findViewById(R.id.tvMoreExpired);

        listDailySales = new ArrayList<>();
        sp = SharedPrefHandler.getInstance(getContext());
        user = sp.getUser();
        token = user.getToken();
        modelDailySalesStatusList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Fragment fragment;
        switch (id)
        {
            case R.id.tvMoreProducts:
                fragment = new ProductsFragment();
                actionBar.setTitle("Products");
                setFragment(fragment);
                break;
            case R.id.tvMoreTodaysSale:
                fragment = new SalesTodayFragment();
                actionBar.setTitle("Today Sales");
                setFragment(fragment);
                break;
            case R.id.tvMoreNotice:
                fragment = new ProductsNoticeFragment();
                actionBar.setTitle("Products Notice");
                setFragment(fragment);
                break;
            case R.id.tvMoreExpiring:
                fragment = new ExpiringProductsFragment();
                actionBar.setTitle("Expiring Products");
                setFragment(fragment);
                break;
            case R.id.tvMoreExpired:
                fragment = new ExpiredProductsFragment();
                actionBar.setTitle("Expired Products");
                setFragment(fragment);
                break;
        }
    }

    private void setFragment(Fragment fragment)
    {
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
    }
}