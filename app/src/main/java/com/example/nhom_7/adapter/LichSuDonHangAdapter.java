package com.example.nhom_7.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom_7.R;
import com.example.nhom_7.model.DonHang;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class LichSuDonHangAdapter extends RecyclerView.Adapter<LichSuDonHangAdapter.MyViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<DonHang> donHangArrayList;
    private LichSuDonHangAdapter.MyItemClickListener delegation;

    public void setDelegation(LichSuDonHangAdapter.MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public LichSuDonHangAdapter(Activity context, int layoutID, ArrayList<DonHang> donHangArrayList){
        this.context= context;
        this.layoutID=layoutID;
        this.donHangArrayList=donHangArrayList;
    }
    @NonNull
    @Override
    public LichSuDonHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view =(CardView) layoutInflater.inflate(viewType, parent, false);
        return new LichSuDonHangAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichSuDonHangAdapter.MyViewHolder holder, int position) {
        DonHang lichSuDH = donHangArrayList.get(position);
        if(lichSuDH==null){
            return;
        }
        holder.tvMa.setText(lichSuDH.getMaDH());
        NumberFormat formatter = new DecimalFormat("#,###,###");
        holder.tvTong.setText(formatter.format(lichSuDH.getTong()) + " đ");
        holder.tvNgayDat.setText(lichSuDH.getNgayDat());
        holder.tvTrangThai.setText(lichSuDH.getTrangThai());

        holder.btnDaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getXacNhanDonHang(lichSuDH);
            }
        });
        holder.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getHuyDonHang(lichSuDH);
            }
        });
        if(!lichSuDH.getTrangThai().equals("Xác nhận")){
            holder.btnDaNhan.setVisibility(View.GONE);
        }
        else {
            holder.btnDaNhan.setVisibility(View.VISIBLE);
        }
        if(lichSuDH.getTrangThai().equals("Huỷ")||lichSuDH.getTrangThai().equals("Đã nhận")){
            holder.btnHuy.setVisibility(View.GONE);
        }
        else {
            holder.btnHuy.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }
    @Override
    public int getItemCount() {
        return donHangArrayList.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMa,tvNgayDat,tvTrangThai,tvTong;
        Button btnHuy,btnDaNhan;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvMa = itemView.findViewById(R.id.tvMa);
            tvTong = itemView.findViewById(R.id.tvTong);
            tvNgayDat = itemView.findViewById(R.id.tvNgayDat);
            tvTrangThai=itemView.findViewById(R.id.tvTrangthai);
            btnDaNhan=itemView.findViewById(R.id.btnDaNhan);
            btnHuy=itemView.findViewById(R.id.btnHuy);
        }
    }
    public interface MyItemClickListener{
        void getXacNhanDonHang(DonHang lichSuDH);
        void getHuyDonHang(DonHang lichSuDH);
    }
}
