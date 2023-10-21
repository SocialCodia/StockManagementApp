package com.socialcodia.stockmanagement.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.models.ModelCreditPayment;

import java.util.List;

public class AdapterCreditPayment extends RecyclerView.Adapter<AdapterCreditPayment.ViewHolder> {

    private List<ModelCreditPayment> modelCreditPaymentList;
    private Context context;

    public AdapterCreditPayment(List<ModelCreditPayment> modelCreditPaymentList, Context context) {
        this.modelCreditPaymentList = modelCreditPaymentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_payment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelCreditPayment payment = modelCreditPaymentList.get(position);
        int paymentAmount = payment.getPaymentAmount();
        String paymentDate = payment.getPaymentDate();
        holder.tvCount.setText(String.valueOf(position+1));
        holder.tvPaymentAmount.setText(String.valueOf(paymentAmount));
        holder.tvPaymentDate.setText(paymentDate);
        Log.d("TAG", "onBindViewHolder: "+paymentAmount);
    }

    public void updateList(List<ModelCreditPayment> modelCreditPayments)
    {
        modelCreditPaymentList = modelCreditPayments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return modelCreditPaymentList.size();
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
