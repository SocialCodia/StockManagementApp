package com.socialcodia.stockmanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.adapters.AdapterProductSale;
import com.socialcodia.stockmanagement.helper.DbHandler;
import com.socialcodia.stockmanagement.models.ModelProduct;

import java.util.ArrayList;
import java.util.List;

public class SellProductActivity extends AppCompatActivity {

    private ImageView ivCloseDialog;
    private RecyclerView recyclerView;
    private EditText inputSearchProduct;
    private static List<ModelProduct> modelProductList;
    private AdapterProductSale adapterProductSale;
    private int productId;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product);
        init();
        actionBar = getSupportActionBar();
        actionBar.setTitle("Sell Product");
        setRecyclerView();

        inputSearchProduct.addTextChangedListener(new TextWatcher() {
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

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close,menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.miClose)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
        adapterProductSale.updateList(p);
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        modelProductList = DbHandler.getModelProductList();
        adapterProductSale = new AdapterProductSale(SellProductActivity.this, modelProductList);
        recyclerView.setAdapter(adapterProductSale);
    }


    private void init() {
        recyclerView = findViewById(R.id.rvProducts);
        inputSearchProduct = findViewById(R.id.inputSearchProduct);
        modelProductList = new ArrayList<>();
    }
}