package com.socialcodia.stockmanagement.helper;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.models.ModelSale;

import java.util.List;

public class SaleRecordManager {
    public Activity activity;
    private List<ModelSale> modelSaleList;

    public SaleRecordManager(Activity activity) {
        this.activity = activity;
    }

    RecyclerView recyclerView = (RecyclerView) this.activity.findViewById(R.id.rvProducts);


//    public static void setSellProductResponseToView()
//    {
//        modelProductList = DbHandler.getModelProductList();
//        AdapterProductSale adapterProductSale;
//        SellProductActivity activity = new SellProductActivity();
//        RecyclerView recyclerView;
//        recyclerView = activity.findViewById(R.id.rvProducts);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(SocialCodia.getInstance());
//        recyclerView.setLayoutManager(layoutManager);
//        modelProductList = DbHandler.getModelProductList();
//        adapterProductSale = new AdapterProductSale(activity, modelProductList);
//        recyclerView.setAdapter(adapterProductSale);
//    }
}
