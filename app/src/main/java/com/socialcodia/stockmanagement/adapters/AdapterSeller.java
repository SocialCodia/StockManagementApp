package com.socialcodia.stockmanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.activities.SellerActivity;
import com.socialcodia.stockmanagement.models.ModelSeller;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSeller extends RecyclerView.Adapter<AdapterSeller.ViewHolder>
{

    private List<ModelSeller> modelSellerList;
    private Context context;

    public AdapterSeller(Context context, List<ModelSeller> modelSellerList) {
        this.modelSellerList = modelSellerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_seller,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelSeller seller = modelSellerList.get(position);
        String sellerName = seller.getSellerName();
        String sellerMobile = seller.getSellerContactNumber();
        String sellerImage = seller.getSellerImage();
        int sellerId = seller.getSellerId();

        holder.tvSellerName.setText(sellerName);
        holder.tvSellerMobile.setText(sellerMobile);
        try {
            Picasso.get().load(sellerImage).placeholder(R.drawable.user).into(holder.ivSeller);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        holder.cvSeller.setOnClickListener(v-> sendToSellerActivity(sellerId));

    }


    private void sendToSellerActivity(int sellerId)
    {
        Intent intent = new Intent(context,SellerActivity.class);
        intent.putExtra("intentSellerId",String.valueOf(sellerId));
        context.startActivity(intent);
    }


    public void updateList(List<ModelSeller> modelSellers)
    {
        this.modelSellerList = modelSellers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return modelSellerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cvSeller;
        private ImageView ivSeller;
        private TextView tvSellerName,tvSellerMobile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvSeller = itemView.findViewById(R.id.cvSeller);
            ivSeller = itemView.findViewById(R.id.ivSeller);
            tvSellerName = itemView.findViewById(R.id.tvSellerName);
            tvSellerMobile = itemView.findViewById(R.id.tvSellerMobile);
        }
    }
}
