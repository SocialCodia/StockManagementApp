package com.socialcodia.stockmanagement.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private String token;
    private SharedPrefHandler sp;
    private ModelUser user;
    private List<ModelProduct> modelProductList;
    private EditText inputSearch;
    private AdapterProduct adapterProduct;
    private ShimmerFrameLayout shimmerProduct;
    private CardView cvSearchProduct;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_products, container, false);
        sp = SharedPrefHandler.getInstance(getContext());
        inputSearch = view.findViewById(R.id.inputSearchProduct);
        user = sp.getUser();

        token = user.getToken();
        recyclerView = view.findViewById(R.id.rvProducts);
        shimmerProduct = view.findViewById(R.id.shimmerProduct);
        cvSearchProduct = view.findViewById(R.id.cvSearchProduct);
        cvSearchProduct.setVisibility(View.GONE);
        shimmerProduct.startShimmer();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        getProducts();
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        return view;
    }

    private void filter(String text)
    {
        List<ModelProduct> p = new ArrayList<>();
        for(ModelProduct sale : modelProductList)
        {
            if (sale.getProductName().toLowerCase().trim().contains(text.toLowerCase()))
            {
                p.add(sale);
            }
        }
        adapterProduct.updateList(p);
    }

    private void getProducts()
    {
        Call<ResponseProducts> call = ApiClient.getInstance().getApi().getAvailableProducts(token);
        call.enqueue(new Callback<ResponseProducts>() {
            @Override
            public void onResponse(Call<ResponseProducts> call, Response<ResponseProducts> response) {
                if (response.isSuccessful())
                {
                    ResponseProducts products = response.body();
                    if (!products.isError())
                    {
                        cvSearchProduct.setVisibility(View.VISIBLE);
                        shimmerProduct.stopShimmer();
                        shimmerProduct.showShimmer(false);
                        shimmerProduct.setVisibility(View.GONE);
                        modelProductList = new ArrayList<>();
                        modelProductList = products.getProducts();
                        adapterProduct = new AdapterProduct(getContext(),modelProductList);
                        recyclerView.setAdapter(adapterProduct);
                    }
                    else
                        Toast.makeText(getContext(), products.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "Response Is Not Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseProducts> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}