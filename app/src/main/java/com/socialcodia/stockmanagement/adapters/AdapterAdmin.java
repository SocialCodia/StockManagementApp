package com.socialcodia.stockmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.models.ModelAdmin;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.ViewHolder> {

    private List<ModelAdmin> modelAdminList;
    private Context context;


    public AdapterAdmin(List<ModelAdmin> modelAdminList, Context context) {
        this.modelAdminList = modelAdminList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.row_admin,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelAdmin admin = modelAdminList.get(position);
        holder.tvAdminName.setText(admin.getAdminName());
        holder.tvAdminEmail.setText(admin.getAdminEmail());
        holder.tvAdminPosition.setText(admin.getAdminPosition());
        try {
            Picasso.get().load(admin.getAdminImage()).placeholder(R.drawable.user).into(holder.ivAdmin);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return modelAdminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivAdmin;
        private TextView tvAdminName,tvAdminEmail,tvAdminPosition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAdmin = itemView.findViewById(R.id.ivAdmin);
            tvAdminName = itemView.findViewById(R.id.tvAdminName);
            tvAdminEmail = itemView.findViewById(R.id.tvAdminEmail);
            tvAdminPosition = itemView.findViewById(R.id.tvAdminPosition);
        }
    }
}
