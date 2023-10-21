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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.sdsmdg.tastytoast.TastyToast;
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

public class SalesTodayFragment extends Fragment {

    private RecyclerView recyclerView;
    private SharedPrefHandler sp;
    private ModelUser user;
    private String token;
    private List<ModelSale> modelSales;
    private TextView tvTotalPrice, tvSalePrice;
    AdapterSale adapterSale;
    private ShimmerFrameLayout shimmerSalesToday;
    private LinearLayout layoutSalesToday,layoutErrorView;
    int totalPrice,salePrice = 0;
    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_sales_today, container, false);

        shimmerSalesToday = view.findViewById(R.id.shimmerSalesToday);
        tvTotalPrice = view.findViewById(R.id.tvTotalTotalPrice);
        tvSalePrice = view.findViewById(R.id.tvTotalSalePrice);
        layoutSalesToday = view.findViewById(R.id.layoutSalesToday);
        layoutErrorView = view.findViewById(R.id.layoutErrorView);
        recyclerView = view.findViewById(R.id.rvSell);
        sp = SharedPrefHandler.getInstance(getContext());
        user = sp.getUser();
        token = user.getToken();
        modelSales = new ArrayList<>();
        shimmerSalesToday.setVisibility(View.VISIBLE);
        shimmerSalesToday.startShimmer();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        getTodaySales();
        layoutSalesToday.setVisibility(View.GONE);

        setHasOptionsMenu(true);

        return view;
    }

    private void filter(String text)
    {
        List<ModelSale> s = new ArrayList<>();
        for(ModelSale sale : modelSales)
        {
            if (sale.getProductName().toLowerCase().trim().contains(text.toLowerCase()))
            {
                s.add(sale);
            }
        }
        adapterSale.updateList(s);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search,menu);
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

    private void getTodaySales()
    {
        Call<ResponseSales> call = ApiClient.getInstance().getApi().getTodaySales(token);
        call.enqueue(new Callback<ResponseSales>() {
            @Override
            public void onResponse(Call<ResponseSales> call, Response<ResponseSales> response) {
                if (response.isSuccessful())
                {
                    ResponseSales responseSales = response.body();
                    if (!responseSales.isError())
                    {
                        shimmerSalesToday.stopShimmer();
                        shimmerSalesToday.setVisibility(View.GONE);
                        layoutSalesToday.setVisibility(View.VISIBLE);
                        layoutErrorView.setVisibility(View.GONE);
                        modelSales = responseSales.getSales();
                        adapterSale = new AdapterSale(getContext(),modelSales);
                        recyclerView.setAdapter(adapterSale);
                        setTotalValue();
                        setMenuToVisible();
                    }
                    else
                    {
                        shimmerSalesToday.stopShimmer();
                        shimmerSalesToday.setVisibility(View.GONE);
                        layoutSalesToday.setVisibility(View.GONE);
                        layoutErrorView.setVisibility(View.VISIBLE);
                    }
                }
                else
                    TastyToast.makeText(getContext(),"Something Went Wrong",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            }

            @Override
            public void onFailure(Call<ResponseSales> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void setTotalValue()
    {
        for (ModelSale sale : modelSales)
        {
            totalPrice =  totalPrice + sale.getSaleQuantity()* sale.getProductPrice();
            salePrice =  salePrice + sale.getSalePrice();
        }
        tvSalePrice.setText(String.valueOf(salePrice));
        tvTotalPrice.setText(String.valueOf(totalPrice));
    }
}