package com.example.nhom_7.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonHang {
    String maDH;
    String NgayDat;
    String TrangThai;
    int Tong;
    ArrayList<ChiTietDH> chiTietDonHangs = new ArrayList<>();
    TaiKhoan taiKhoan;

    public ArrayList<ChiTietDH> getChiTietDonHangs() {
        return chiTietDonHangs;
    }

    public void setChiTietDonHangs(ArrayList<ChiTietDH> chiTietDonHangs) {
        this.chiTietDonHangs = chiTietDonHangs;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public DonHang(){};

    public String getMaDH() {
        return maDH;
    }

    public void setMaDH(String maDH) {
        this.maDH = maDH;
    }

    public String getNgayDat() {
        return NgayDat;
    }

    public void setNgayDat(String ngayDat) {
        NgayDat = ngayDat;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    public int getTong() {
        return Tong;
    }

    public void setTong(int tong) {
        Tong = tong;
    }

    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("trangThai",TrangThai);
        return result;
    }

}
