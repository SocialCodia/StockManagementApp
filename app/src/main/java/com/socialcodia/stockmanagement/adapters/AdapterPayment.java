package com.socialcodia.stockmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.models.ModelPayment;

import java.util.List;

public class AdapterPayment extends RecyclerView.Adapter<AdapterPayment.ViewHolder>{

    private Context context;
    private List<ModelPayment> modelPaymentList;

    public AdapterPayment(Context context, List<ModelPayment> modelPaymentList) {
        this.context = context;
        this.modelPaymentList = modelPaymentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_payment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelPayment payment = modelPaymentList.get(position);
        int paymentId= payment.getPaymentId();
        String invoiceNumber = payment.getInvoiceNumber();
        String paymentDate = payment.getPaymentDate();
        int paymentAmount = payment.getPaymentAmount();
        int sellerId = payment.getSellerId();
        holder.tvCount.setText(String.valueOf(position+1));
        holder.tvPaymentAmount.setText(String.valueOf(paymentAmount));
        holder.tvPaymentDate.setText(String.valueOf(paymentDate));
    }

    public void updateList(List<ModelPayment> list){
        modelPaymentList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return modelPaymentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
    private TextView tvCount,tvPaymentAmount,tvPaymentDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvPaymentAmount = itemView.findViewById(R.id.tvPaymentAmount);
            tvPaymentDate = itemView.findViewById(R.id.tvPaymentDate);
        }
    }
}
