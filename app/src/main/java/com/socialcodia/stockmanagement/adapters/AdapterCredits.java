package com.socialcodia.stockmanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.activities.CreditActivity;
import com.socialcodia.stockmanagement.models.ModelCreditor;
import com.socialcodia.stockmanagement.models.ModelCredits;

import java.util.List;

public class AdapterCredits extends RecyclerView.Adapter<AdapterCredits.ViewHolder> {

    private List<ModelCredits> modelCreditList;
    private Context context;

    public AdapterCredits(List<ModelCredits> modelCreditList, Context context) {
        this.modelCreditList = modelCreditList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_credit,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelCredits credit = modelCreditList.get(position);
        ModelCreditor creditor = credit.getCreditor();

        holder.tvCreditorName.setText(creditor.getCreditorName());
        holder.tvCreditorMobile.setText(creditor.getCreditorMobile());
        holder.tvCreditDate.setText(credit.getCreditDate());
        holder.tvCreditAmount.setText(String.valueOf(credit.getCreditTotalAmount()));
        holder.tvCreditPaidAmount.setText(String.valueOf(credit.getCreditPaidAmount()));
        holder.tvCreditRemainingAmount.setText(String.valueOf(credit.getCreditRemainingAmount()));
        holder.tvCreditStatus.setText(credit.getCreditStatus());
        if (credit.getCreditStatus().toLowerCase().equals("unpaid"))
            holder.tvCreditStatus.setBackgroundColor(Color.RED);
        else
            holder.tvCreditStatus.setBackgroundColor(Color.BLUE);

        holder.cvCredit.setOnClickListener(v->sendToCreditActivity(credit.getCreditId()));

    }

    private void sendToCreditActivity(int creditId)
    {
        Intent intent = new Intent(context, CreditActivity.class);
        intent.putExtra("intentCreditId",String.valueOf(creditId));
        context.startActivity(intent);
    }

    public void updateList(List<ModelCredits> modelCredits)
    {
        this.modelCreditList = modelCredits;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return modelCreditList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        private TextView tvCreditorName, tvCreditorMobile, tvCreditDate, tvCreditAmount, tvCreditPaidAmount, tvCreditRemainingAmount, tvCreditStatus ;
        private CardView cvCredit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCreditorName = itemView.findViewById(R.id.tvCreditorName);
            tvCreditorMobile = itemView.findViewById(R.id.tvCreditorMobile);
            tvCreditDate = itemView.findViewById(R.id.tvCreditDate);
            tvCreditAmount = itemView.findViewById(R.id.tvCreditAmount);
            tvCreditPaidAmount = itemView.findViewById(R.id.tvCreditPaidAmount);
            tvCreditRemainingAmount = itemView.findViewById(R.id.tvCreditRemainingAmount);
            tvCreditStatus = itemView.findViewById(R.id.tvCreditStatus);
            cvCredit = itemView.findViewById(R.id.cvCredit);
        }
    }
}
