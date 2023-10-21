package com.socialcodia.stockmanagement.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.adapters.AdapterProduct;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelProduct;
import com.socialcodia.stockmanagement.pojos.ResponseProducts;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsRecordFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ModelProduct> modelProductList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private AdapterProduct adapterProduct;
    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_record, container, false);
        init(view);
        recyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        setHasOptionsMenu(true);
        getProductsRecord();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        menu.findItem(R.id.miSearch).setVisible(false);
        MenuItem menuItem = menu.findItem(R.id.miSearch);
        this.menu = menu;
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Product");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                    if (modelProductList != null || !modelProductList.isEmpty()) {
                        filter(s.toString());
                }
                return true;
            }
        });
    }

    private void setMenuToVisible()
    {
        try {
            menu.findItem(R.id.miSearch).setVisible(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void filter(String text) {
        List<ModelProduct> p = new ArrayList<>();
        for (ModelProduct product : modelProductList) {
            if (product.getProductName().toLowerCase().trim().contains(text.toLowerCase())) {
                p.add(product);
            }
        }
        adapterProduct.updateList(p);
    }


    private void getProductsRecord() {
        Call<ResponseProducts> call = ApiClient.getInstance().getApi().getProductsRecord(Helper.getToken());
        call.enqueue(new Callback<ResponseProducts>() {
            @Override
            public void onResponse(Call<ResponseProducts> call, Response<ResponseProducts> response) {
                if (response.isSuccessful()) {
                    ResponseProducts resp = response.body();
                    if (!resp.isError()) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        modelProductList = resp.getProducts();
                        adapterProduct = new AdapterProduct(getContext(), modelProductList);
                        recyclerView.setAdapter(adapterProduct);
                        setMenuToVisible();
                    } else {
                        TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } else
                    TastyToast.makeText(getContext(), "Response Not Success", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onFailure(Call<ResponseProducts> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.rvProductsRecord);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        modelProductList = new ArrayList<>();
    }
}