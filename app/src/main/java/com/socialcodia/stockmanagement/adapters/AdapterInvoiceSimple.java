package com.socialcodia.stockmanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.activities.InvoicePaymentActivity;
import com.socialcodia.stockmanagement.activities.WebViewActivity;
import com.socialcodia.stockmanagement.models.ModelInvoice;

import java.util.List;

public class AdapterInvoiceSimple extends RecyclerView.Adapter<AdapterInvoiceSimple.ViewHolder> {

    private Context context;
    private List<ModelInvoice> modelInvoices;

    public AdapterInvoiceSimple(Context context, List<ModelInvoice> modelInvoices) {
        this.context = context;
        this.modelInvoices = modelInvoices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_invoice_simple,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelInvoice invoice = modelInvoices.get(position);
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

        holder.tvInvoiceStatus.setText(invoiceStatus);
        if (invoiceStatus.equals("PAID"))
        {
            holder.tvInvoiceStatus.setBackgroundColor(Color.BLUE);
        }
        else
            holder.tvInvoiceStatus.setBackgroundColor(Color.RED);

        holder.tvInvoiceNumber.setText("Invoice Number : "+invoiceNumber);
        holder.tvInvoiceDate.setText("Invoice Date : "+invoiceDate);
        holder.tvInvoiceAmount.setText(String.valueOf("Amount : "+invoiceAmount));
        holder.tvInvoicePaidAmount.setText(String.valueOf("Paid : "+invoicePaidAmount));
        holder.tvInvoiceRemainingAmount.setText(String.valueOf("Remaining : "+invoiceRemainingAmount));

        holder.btnViewInvoice.setOnClickListener(v->sendToWebView(invoiceNumber));
        holder.btnInvoicePayment.setOnClickListener(v->sendToInvoicePaymentActivity(invoiceNumber));
    }

    private void sendToInvoicePaymentActivity(String invoiceNumber)
    {
        Intent intent = new Intent(context, InvoicePaymentActivity.class);
        intent.putExtra("intentInvoiceNumber",invoiceNumber);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void sendToWebView(String invoiceNumber) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("intentInvoiceNumber",invoiceNumber);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return modelInvoices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvInvoiceNumber,tvInvoiceDate,tvInvoiceAmount,tvInvoicePaidAmount,tvInvoiceRemainingAmount,tvInvoiceStatus;
        private Button btnViewInvoice,btnInvoicePayment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvInvoiceNumber = itemView.findViewById(R.id.tvInvoiceNumber);
            tvInvoiceDate = itemView.findViewById(R.id.tvInvoiceDate);
            tvInvoiceAmount = itemView.findViewById(R.id.tvInvoiceAmount);
            tvInvoicePaidAmount = itemView.findViewById(R.id.tvInvoicePaidAmount);
            tvInvoiceRemainingAmount = itemView.findViewById(R.id.tvInvoiceRemainingAmount);
            tvInvoiceStatus = itemView.findViewById(R.id.tvInvoiceStatus);
            btnViewInvoice = itemView.findViewById(R.id.btnViewInvoice);
            btnInvoicePayment = itemView.findViewById(R.id.btnInvoicePayment);
        }
    }
}
