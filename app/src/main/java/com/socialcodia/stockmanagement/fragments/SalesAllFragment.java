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
import com.socialcodia.stockmanagement.adapters.AdapterSale;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.models.ModelSale;
import com.socialcodia.stockmanagement.models.ModelUser;
import com.socialcodia.stockmanagement.pojos.ResponseSales;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesAllFragment extends Fragment {

    private RecyclerView recyclerView;
    private SharedPrefHandler sp;
    private ModelUser user;
    private String token;
    private List<ModelSale> modelSales;
    private ShimmerFrameLayout shimmerFrameLayout;
    private AdapterSale adapterSale;
    private Menu menu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sales_all, container, false);
        init(view);
        recyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        setHasOptionsMenu(true);
        getAllSales();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        menu.findItem(R.id.miSearch).setVisible(false);
        this.menu = menu;
        MenuItem menuItem = menu.findItem(R.id.miSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Product");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                    if (modelSales != null || !modelSales.isEmpty()) {
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
        List<ModelSale> p = new ArrayList<>();
        for (ModelSale sale : modelSales) {
            if (sale.getProductName().toLowerCase().trim().contains(text.toLowerCase())) {
                p.add(sale);
            }
        }
        adapterSale.updateList(p);
    }

    private void getAllSales() {
        Call<ResponseSales> call = ApiClient.getInstance().getApi().getAllSales(token);
        call.enqueue(new Callback<ResponseSales>() {
            @Override
            public void onResponse(Call<ResponseSales> call, Response<ResponseSales> response) {
                if (response.isSuccessful()) {
                    ResponseSales resp = response.body();
                    if (!resp.isError()) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        modelSales = resp.getSales();
                        adapterSale = new AdapterSale(getContext(), modelSales);
                        recyclerView.setAdapter(adapterSale);
                        setMenuToVisible();
                    } else
                        Toast.makeText(getContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getContext(), "Response Is Not Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseSales> call, Throwable t) {

            }
        });
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.rvSalesAll);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        sp = SharedPrefHandler.getInstance(getContext());
        user = sp.getUser();
        token = user.getToken();
        modelSales = new ArrayList<>();
    }
}