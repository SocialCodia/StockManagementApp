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
import com.socialcodia.stockmanagement.adapters.AdapterInvoice;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.models.ModelInvoice;
import com.socialcodia.stockmanagement.models.ModelUser;
import com.socialcodia.stockmanagement.pojos.ResponseInvoices;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InvoicesFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ModelInvoice> modelInvoices;
    private SharedPrefHandler sp;
    private ModelUser user;
    private String token;
    private AdapterInvoice adapterInvoice;
    private Menu menu;
    private ShimmerFrameLayout shimmerFrameLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_invoices, container, false);
        init(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        setHasOptionsMenu(true);
        getAllInvoice();
        return view;
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
        MenuItem menuItem = menu.findItem(R.id.miSearch);
        this.menu = menu;
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Invoice");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                    if (modelInvoices!=null || !modelInvoices.isEmpty())
                        filter(s.toString());
                return true;
            }
        });
    }


    private void filter(String text)
    {
        List<ModelInvoice> p = new ArrayList<>();
        for(ModelInvoice invoice : modelInvoices)
        {
            if (invoice.getSellerName().toLowerCase().trim().contains(text.toLowerCase()) || invoice.getInvoiceStatus().toLowerCase().trim().contains(text.toLowerCase())  || invoice.getInvoiceNumber().toLowerCase().trim().contains(text.toLowerCase()))
            {
                p.add(invoice);
            }
        }
        adapterInvoice.updateList(p);
    }


    private void getAllInvoice()
    {
        Call<ResponseInvoices> call = ApiClient.getInstance().getApi().getAllInvoice(token);
        call.enqueue(new Callback<ResponseInvoices>() {
            @Override
            public void onResponse(Call<ResponseInvoices> call, Response<ResponseInvoices> response) {
                if (response.isSuccessful())
                {
                    ResponseInvoices resp = response.body();
                    if (!resp.isError())
                    {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        modelInvoices = resp.getInvoices();
                        setMenuToVisible();
                        adapterInvoice = new AdapterInvoice(getContext(),modelInvoices);
                        recyclerView.setAdapter(adapterInvoice);
                    }
                    else
                        Toast.makeText(getContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "Response Not Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseInvoices> call, Throwable t) {

            }
        });
    }

    private void init(View view)
    {
        recyclerView = view.findViewById(R.id.rvInvoices);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        modelInvoices = new ArrayList<>();
        sp = SharedPrefHandler.getInstance(getContext());
        user = sp.getUser();
        token = user.getToken();
    }
}