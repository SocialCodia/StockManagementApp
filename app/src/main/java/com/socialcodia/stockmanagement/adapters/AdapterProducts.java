package com.socialcodia.stockmanagement.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.models.ModelProduct;

import java.util.List;

public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.ViewHolder>
{
    private List<ModelProduct> products;
    private Context context;

    public AdapterProducts(Context context, List<ModelProduct> modelProducts)
    {
        this.products = modelProducts;
        this.context = context;
    }

    public AdapterProducts() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelProduct product = products.get(position);
        String productName = product.getProductName();
        String productBrand = product.getProductBrand();
        String productManufacture = product.getProductManufacture();
        String productExpire = product.getProductExpire();
        String productLocation =  product.getProductLocation();
        String productSize = product.getProductSize();
        String productCategory = product.getProductCategory();
        int productQuantity = product.getProductQuantity();
        int productPrice = product.getProductPrice();

        holder.tvProductName.setText(productName);
        holder.tvProductBrand.setText(productBrand);
        holder.tvProductLocation.setText("Location : "+productLocation);
        holder.tvProductManufacture.setText("Manufacture : "+productManufacture);
        holder.tvProductExpire.setText("Expire : "+productExpire);
        holder.tvProductQuantity.setText("Quantity : "+String.valueOf(productQuantity));
        holder.tvProductPrice.setText("Price : "+String.valueOf(productPrice));
        holder.tvProductSize.setText("("+productSize+")");
        holder.tvProductCategory.setText(productCategory);
        holder.tvCount.setText(String.valueOf(position+1));

        holder.cvProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, productName, Toast.LENGTH_SHORT).show();
            }
        });

        holder.cvProduct.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showAlert();
                return true;
            }
        });
    }

    private void showAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are You Sure Want To Edit This Product");
        builder.setMessage("Are You Want To Edit This Product");
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvProductName,tvProductLocation, tvProductCategory, tvProductSize, tvProductQuantity, tvProductBrand, tvProductPrice, tvProductManufacture, tvProductExpire, tvCount;
        private CardView cvProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductLocation = itemView.findViewById(R.id.tvProductLocation);
            tvProductSize = itemView.findViewById(R.id.tvProductSize);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductBrand = itemView.findViewById(R.id.tvProductBrand);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductExpire = itemView.findViewById(R.id.tvProductExpire);
            tvProductManufacture = itemView.findViewById(R.id.tvProductManufacture);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            cvProduct = itemView.findViewById(R.id.cvProduct);
            tvCount = itemView.findViewById(R.id.tvCount);
        }
    }

    public void updateList(List<ModelProduct> modelProducts)
    {
        this.products = modelProducts;
        notifyDataSetChanged();
    }
}
