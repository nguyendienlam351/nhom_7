package com.example.nhom_7.model;

import java.util.ArrayList;

public class ChiTietDonHang {
    String maSanPham;
    String anh;
    String ten;
    int gia;
    Size size;
    boolean danhGia;

    public ChiTietDonHang() {
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public boolean isDanhGia() {
        return danhGia;
    }

    public void setDanhGia(boolean danhGia) {
        this.danhGia = danhGia;
    }
}
