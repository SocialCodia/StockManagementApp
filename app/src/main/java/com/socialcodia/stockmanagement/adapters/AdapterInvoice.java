package com.socialcodia.stockmanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.activities.InvoicePaymentActivity;
import com.socialcodia.stockmanagement.activities.SellerActivity;
import com.socialcodia.stockmanagement.models.ModelInvoice;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterInvoice extends RecyclerView.Adapter<AdapterInvoice.ViewHolder> {

    private List<ModelInvoice> modelInvoiceList;
    private Context context;

    public AdapterInvoice(Context context, List<ModelInvoice> modelInvoiceList) {
        this.modelInvoiceList = modelInvoiceList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_invoice, parent, false);
        return new ViewHolder(view);
    }

    public void updateList(List<ModelInvoice> invoices)
    {
        this.modelInvoiceList = invoices;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelInvoice invoice = modelInvoiceList.get(position);
        int invoiceId = invoice.getInvoiceId();
        String invoiceNumber = invoice.getInvoiceNumber();
        String invoiceDate = invoice.getInvoiceDate();
        int invoiceAmount = invoice.getInvoiceAmount();
        int invoicePaidAmount = invoice.getInvoicePaidAmount();
        int invoiceRemainingAmount = invoice.getInvoiceRemainingAmount();
        String invoiceStatus = invoice.getInvoiceStatus();
        String sellerName = invoice.getSellerName();
        String sellerImage = invoice.getSellerImage();
        String sellerContactNumber = invoice.getSellerContactNumber();
        String sellerContactNumber1 = invoice.getSellerContactNumber1();
        String sellerAddress = invoice.getSellerAddress();

        holder.tvSellerName.setText(sellerName);
        holder.tvSellerMobile.setText(sellerContactNumber);
        holder.tvInvoiceNumber.setText("Invoice Number : " + invoiceNumber);
        holder.tvInvoiceDate.setText("Invoice Date : " + invoiceDate);
        holder.tvInvoiceAmount.setText(String.valueOf("Amount : " + invoiceAmount));
        holder.tvInvoicePaidAmount.setText(String.valueOf("Paid : " + invoicePaidAmount));
        holder.tvInvoiceRemainingAmount.setText(String.valueOf("Remaining : " + invoiceRemainingAmount));
        holder.tvInvoiceStatus.setText(invoiceStatus);
        holder.ivSeller.setOnClickListener(v -> sendToSellerActivity(invoice.getSellerId()));
        if (invoiceStatus.equals("UNPAID")) {
            holder.tvInvoiceStatus.setBackgroundColor(Color.RED);
        }
        try {
            Picasso.get().load(sellerImage).placeholder(R.drawable.user).into(holder.ivSeller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.cvInvoice.setOnClickListener(view -> sendToInvoicePayment(invoiceNumber));

    }

    private void sendToSellerActivity(int sellerId) {
        Intent intent =  new Intent(context, SellerActivity.class);
        intent.putExtra("intentSellerId",String.valueOf(sellerId));
        context.startActivity(intent);
    }

    private void sendToInvoicePayment(String invoiceNumber) {
        Intent intent = new Intent(context, InvoicePaymentActivity.class);
        intent.putExtra("intentInvoiceNumber", invoiceNumber);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return modelInvoiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cvInvoice;
        private ImageView ivSeller;
        private TextView tvSellerName, tvSellerMobile, tvInvoiceNumber, tvInvoiceDate, tvInvoiceAmount, tvInvoicePaidAmount, tvInvoiceRemainingAmount, tvInvoiceStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvInvoice = itemView.findViewById(R.id.cvInvoice);
            ivSeller = itemView.findViewById(R.id.ivSeller);
            tvSellerName = itemView.findViewById(R.id.tvSellerName);
            tvSellerMobile = itemView.findViewById(R.id.tvSellerMobile);
            tvInvoiceNumber = itemView.findViewById(R.id.tvInvoiceNumber);
            tvInvoiceDate = itemView.findViewById(R.id.tvInvoiceDate);
            tvInvoiceAmount = itemView.findViewById(R.id.tvInvoiceAmount);
            tvInvoicePaidAmount = itemView.findViewById(R.id.tvInvoicePaidAmount);
            tvInvoiceRemainingAmount = itemView.findViewById(R.id.tvInvoiceRemainingAmount);
            tvInvoiceStatus = itemView.findViewById(R.id.tvInvoiceStatus);
        }
    }
}
