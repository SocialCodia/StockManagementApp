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
import com.socialcodia.stockmanagement.adapters.AdapterSeller;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.models.ModelSeller;
import com.socialcodia.stockmanagement.models.ModelUser;
import com.socialcodia.stockmanagement.pojos.ResponseSellers;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellersFragment extends Fragment {

    private RecyclerView recyclerView;
    private SharedPrefHandler sp;
    private ModelUser user;
    private String token;
    private List<ModelSeller> modelSellerList;
    private AdapterSeller adapterSeller;
    private Menu menu;
    private ShimmerFrameLayout shimmerFrameLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sellers, container, false);
        init(view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        setHasOptionsMenu(true);
        getSellers();
        return view;
    }

    private void getSellers()
    {
        Call<ResponseSellers> call = ApiClient.getInstance().getApi().getAllSellers(token);
        call.enqueue(new Callback<ResponseSellers>() {
            @Override
            public void onResponse(Call<ResponseSellers> call, Response<ResponseSellers> response) {
                if (response.isSuccessful())
                {
                    ResponseSellers resp = response.body();
                    if (!resp.isError())
                    {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        modelSellerList= resp.getSellers();
                        adapterSeller = new AdapterSeller(getContext(),modelSellerList);
                        recyclerView.setAdapter(adapterSeller);
                        setMenuToVisible();
                    }
                    else
                        Toast.makeText(getContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "Response Not Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseSellers> call, Throwable t) {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search,menu);
        menu.findItem(R.id.miSearch).setVisible(false);
        this.menu = menu;
        MenuItem menuItem = menu.findItem(R.id.miSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Seller");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                    if (modelSellerList != null || !modelSellerList.isEmpty()) {
                        filter(s.toString());
                }
                return true;
            }
        });
    }

    private void filter(String text)
    {
        List<ModelSeller> p = new ArrayList<>();
        for(ModelSeller seller : modelSellerList)
        {
            if (seller.getSellerName().toLowerCase().trim().contains(text.toLowerCase()) || seller.getSellerContactNumber().toLowerCase().trim().contains(text.toLowerCase()))
            {
                p.add(seller);
            }
        }
        adapterSeller.updateList(p);
    }



    private void init(View view)
    {
        recyclerView = view.findViewById(R.id.rvSeller);
        sp = SharedPrefHandler.getInstance(getContext());
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        user = sp.getUser();
        token = user.getToken();
        modelSellerList = new ArrayList<>();
    }
}