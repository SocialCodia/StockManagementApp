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
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.adapters.AdapterProduct;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.models.ModelProduct;
import com.socialcodia.stockmanagement.models.ModelUser;
import com.socialcodia.stockmanagement.pojos.ResponseProducts;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpiredProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SharedPrefHandler sp;
    private ModelUser user;
    private String token;
    private List<ModelProduct> modelProductList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private AdapterProduct adapterProduct;
    private Menu menu;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expired_products, container, false);
        init(view);

        recyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        setHasOptionsMenu(true);
        getExpiredProducts();
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
        searchView.setQueryHint("Search Products");
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


    private void filter(String text) {
        List<ModelProduct> p = new ArrayList<>();
        for (ModelProduct product : modelProductList) {
            if (product.getProductName().toLowerCase().trim().contains(text.toLowerCase())) {
                p.add(product);
            }
        }
        adapterProduct.updateList(p);
    }


    private void getExpiredProducts() {
        Call<ResponseProducts> call = ApiClient.getInstance().getApi().getExpiredProducts(token);
        call.enqueue(new Callback<ResponseProducts>() {
            @Override
            public void onResponse(Call<ResponseProducts> call, Response<ResponseProducts> response) {
                if (response.isSuccessful()) {
                    ResponseProducts responseProducts = response.body();
                    if (!responseProducts.isError()) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        modelProductList = responseProducts.getProducts();
                        adapterProduct = new AdapterProduct(getContext(), modelProductList);
                        recyclerView.setAdapter(adapterProduct);
                        setMenuToVisible();
                    } else
                        Toast.makeText(getContext(), responseProducts.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getContext(), "Response was not success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseProducts> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void init(View view) {
        recyclerView = view.findViewById(R.id.rvExpiredProducts);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        sp = SharedPrefHandler.getInstance(getContext());
        user = sp.getUser();
        token = user.getToken();
        modelProductList = new ArrayList<>();
    }
}