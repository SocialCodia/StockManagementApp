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
import com.socialcodia.stockmanagement.adapters.AdapterCredits;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelCredits;
import com.socialcodia.stockmanagement.pojos.ResponseCredits;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ModelCredits> modelCreditsList;
    private AdapterCredits adapterCredits;
    private Menu menu;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credits, container, false);

        recyclerView = view.findViewById(R.id.rvCredits);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        modelCreditsList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        setHasOptionsMenu(true);
        getCredits();
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
        searchView.setQueryHint("Search Credit");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                    if (modelCreditsList != null || !modelCreditsList.isEmpty()) {
                        filter(s.toString());
                }
                return true;
            }
        });
    }

    private void filter(String text) {
        List<ModelCredits> p = new ArrayList<>();
        for (ModelCredits credits : modelCreditsList) {
            if (credits.getCreditor().getCreditorName().toLowerCase().trim().contains(text.toLowerCase()) || credits.getCreditor().getCreditorMobile().toLowerCase().trim().contains(text.toLowerCase()) || credits.getCreditStatus().toLowerCase().trim().contains(text.toLowerCase())) {
                p.add(credits);
            }
        }
        adapterCredits.updateList(p);
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


    private void getCredits() {
        Call<ResponseCredits> call = ApiClient.getInstance().getApi().getCredits(Helper.getToken());
        call.enqueue(new Callback<ResponseCredits>() {
            @Override
            public void onResponse(Call<ResponseCredits> call, Response<ResponseCredits> response) {
                if (response.isSuccessful()) {
                    ResponseCredits resp = response.body();
                    if (!resp.isError()) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        modelCreditsList = resp.getCredits();
                        adapterCredits = new AdapterCredits(modelCreditsList, getContext());
                        recyclerView.setAdapter(adapterCredits);
                        setMenuToVisible();
                    } else
                    {
                        try {

                            TastyToast.makeText(getContext(), resp.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                } else {
                    TastyToast.makeText(getContext(), "Response Not Success", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCredits> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}