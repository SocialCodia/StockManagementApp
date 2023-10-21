package com.socialcodia.stockmanagement.adapters;

import android.annotation.SuppressLint;
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

import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.models.ModelSale;
import com.socialcodia.stockmanagement.models.ModelUser;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSale extends RecyclerView.Adapter<AdapterSale.ViewHolder> {

    private Context context;
    private List<ModelSale> modelSaleList;
    private String token;
    private SharedPrefHandler sp;
    private ModelUser user;


    public AdapterSale(Context context, List<ModelSale> modelSales)
    {
        this.context = context;
        this.modelSaleList = modelSales;
        sp = SharedPrefHandler.getInstance(context);
        user = sp.getUser();
        token = user.getToken();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_sale,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelSale sale = modelSaleList.get(position);
        int productId = sale.getProductId();
        int saleId = sale.getSaleId();
        String productCategory = sale.getProductCategory();
        String productName = sale.getProductName();
        String productSize = sale.getProductSize();
        String productBrand = sale.getProductBrand();
        int productPrice = sale.getProductPrice();
        String productLocation = sale.getProductLocation();
        int productQuantity = sale.getProductQuantity();
        String productManufacture = sale.getProductManufacture();
        String productExpire = sale.getProductExpire();
        int sellQuantity = sale.getSaleQuantity();
        int saleDiscount = sale.getSaleDiscount();
        int salePrice = sale.getSalePrice();
        int saleQuantity = sale.getSaleQuantity();
        String createdAt = sale.getCreatedAt();

        holder.tvProductName.setText(productName);
        holder.tvProductSize.setText("("+productSize+")");
        holder.tvProductCategory.setText(productCategory);
        holder.tvSaleTime.setText(createdAt);
        holder.tvProductPrice.setText(String.valueOf(productPrice));
        holder.tvSaleQuantity.setText(String.valueOf(sellQuantity));
        holder.tvSalePrice.setText(String.valueOf(salePrice));
        holder.tvProductBrand.setText(productBrand);
        holder.tvProductManufacture.setText(productManufacture);
        holder.tvProductExpire.setText(productExpire);
        holder.tvCount.setText(String.valueOf(position+1));
        holder.tvTotalPrice.setText(String.valueOf(productPrice*saleQuantity));
        holder.cvSell.setOnLongClickListener(view -> {
            showDeleteAlert(holder,sale,position);
            return true;
        });
    }

    private void showDeleteAlert(ViewHolder holder, ModelSale sale, int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure want to delete?");
        builder.setMessage("You are going to delete "+sale.getProductName()+". The Sale Quantity of this product was "+sale.getSaleQuantity()+" and the total price was " +sale.getSalePrice());
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteSoldProduct(sale.getSaleId(),position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Deletion Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void deleteSoldProduct(int sellId, int position)
    {
        Call<ResponseDefault> call = ApiClient.getInstance().getApi().deleteSoldProduct(sellId,token);
        call.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                if (response.isSuccessful())
                {
                    ResponseDefault responseDefault = response.body();
                    if (!responseDefault.isError()) {
                        TastyToast.makeText(context, responseDefault.getMessage(), Toast.LENGTH_SHORT,TastyToast.SUCCESS);
                        Helper.playSuccess();
                        Helper.playVibrate();
                        modelSaleList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,modelSaleList.size());
                    }
                    else
                        TastyToast.makeText(context, responseDefault.getMessage(), Toast.LENGTH_SHORT,TastyToast.ERROR);
                }
                else
                    Toast.makeText(context, "Request Isn't Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void updateList(List<ModelSale> list){
        modelSaleList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return modelSaleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvProductName, tvProductSize, tvProductCategory, tvSaleTime, tvProductPrice, tvSaleQuantity, tvSalePrice, tvProductBrand, tvProductManufacture, tvProductExpire,tvCount,tvTotalPrice;
        private CardView cvSell;
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
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvCount = itemView.findViewById(R.id.tvCount);
            cvSell = itemView.findViewById(R.id.cvSell);
        }
    }
}
