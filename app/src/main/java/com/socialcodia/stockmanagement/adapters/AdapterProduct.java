package com.socialcodia.stockmanagement.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.activities.EditProductActivity;
import com.socialcodia.stockmanagement.models.ModelProduct;

import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder>
{
    private List<ModelProduct> products;
    private Context context;

    public AdapterProduct(Context context, List<ModelProduct> modelProducts)
    {
        this.products = modelProducts;
        this.context = context;
    }

    public AdapterProduct() {
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
        int productId = product.getProductId();
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

        Log.d("socialcodia", "onBindViewHolder: "+productId);

        holder.cvProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, productName, Toast.LENGTH_SHORT).show();
            }
        });


        holder.cvProduct.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showAlert(productName,holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private void showAlert(String productName, int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Product");
        builder.setMessage("Are You Want To Edit "+productName);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendToEditProductActivity(products.get(position).getProductId());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void sendToEditProductActivity(int productId)
    {
        Intent intent = new Intent(context, EditProductActivity.class);
        intent.putExtra("intentProductId",String.valueOf(productId));
        context.startActivity(intent);
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
