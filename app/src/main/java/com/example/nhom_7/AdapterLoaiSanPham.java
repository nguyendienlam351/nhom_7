package com.example.nhom_7;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterLoaiSanPham extends RecyclerView.Adapter<AdapterLoaiSanPham.MyViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<LoaiSanPham> data;
    private AdapterLoaiSanPham.MyItemClickListener delegation;

    public void setDelegation(AdapterLoaiSanPham.MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public AdapterLoaiSanPham(Activity context, int layoutID, ArrayList<LoaiSanPham> data){
        this.context= context;
        this.layoutID=layoutID;
        this.data=data;
    }
    @NonNull
    @Override
    public AdapterLoaiSanPham.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view =(CardView) layoutInflater.inflate(viewType, parent, false);
        return new AdapterLoaiSanPham.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLoaiSanPham.MyViewHolder holder, int position) {
        LoaiSanPham loaiSP = data.get(position);
        if(loaiSP==null){
            return;
        }
        holder.tvLoaiSP.setText(loaiSP.getTenLoai());

        holder.imbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getDeleteLoaiSP(loaiSP);
            }
        });
        holder.imbUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getUpDateLoaiSP(loaiSP);
            }
        });
    }
    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLoaiSP;
        ImageButton imbUpdate;
        ImageButton imbDelete;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvLoaiSP = itemView.findViewById(R.id.tvLoaiSP);
            imbUpdate=itemView.findViewById(R.id.imbUpdate);
            imbDelete=itemView.findViewById(R.id.imbXoa);
        }
    }
    public interface MyItemClickListener{
        void getDeleteLoaiSP(LoaiSanPham loaiSanPham);
        void getUpDateLoaiSP(LoaiSanPham loaiSanPham);
    }
}
