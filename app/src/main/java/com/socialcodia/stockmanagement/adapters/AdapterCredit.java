package com.socialcodia.stockmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.models.ModelSale;

import java.util.List;

public class AdapterCredit extends RecyclerView.Adapter<AdapterCredit.ViewHolder> {

    private List<ModelSale> modelSaleList;
    private Context context;

    public AdapterCredit(Context context, List<ModelSale> modelSaleList)
    {
        this.context = context;
        this.modelSaleList = modelSaleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_sale,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelSale sale = modelSaleList.get(position);

        holder.tvProductName.setText(sale.getProductName());
        holder.tvProductSize.setText(sale.getProductSize());
        holder.tvProductCategory.setText(sale.getProductCategory());
        holder.tvSaleTime.setVisibility(View.GONE);
        holder.tvProductPrice.setText(String.valueOf(sale.getProductPrice()));
        holder.tvSaleQuantity.setText(String.valueOf(sale.getSaleQuantity()));
        holder.tvSalePrice.setText(String.valueOf(sale.getSalePrice()));
        holder.tvProductBrand.setText(sale.getProductBrand());
        holder.tvProductManufacture.setText(sale.getProductManufacture());
        holder.tvProductExpire.setText(sale.getProductExpire());
        holder.tvCount.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return modelSaleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvProductName, tvProductSize, tvProductCategory, tvSaleTime, tvProductPrice, tvSaleQuantity, tvSalePrice, tvProductBrand,tvProductManufacture,tvProductExpire,tvCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductSize = itemView.findViewById(R.id.tvProductSize);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            tvSaleTime = itemView.findViewById(R.id.tvSaleTime);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvSaleQuantity = itemView.findViewById(R.id.tvSaleQuantity);
            tvSalePrice = itemView.findViewById(R.id.tvSalePrice);
            tvProductBrand = itemView.findViewById(R.id.tvProductBrand);
            tvProductManufacture = itemView.findViewById(R.id.tvProductManufacture);
            tvProductExpire = itemView.findViewById(R.id.tvProductExpire);
            tvCount = itemView.findViewById(R.id.tvCount);
        }
    }

}
