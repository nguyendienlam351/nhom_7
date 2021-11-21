package com.example.nhom_7.model;

import java.util.ArrayList;
import java.util.Map;

public class SanPham {
    String maSanPham;
    String anh;
    String ten;
    int gia;
    String loai;
    String moTa;
    ArrayList<String> size;
    ArrayList<Float> danhGia = new ArrayList<Float>();

    public SanPham() {
    }

    public SanPham(String maSanPham, String anh, String ten, int gia, String loai, String moTa, ArrayList<String> size) {
        this.maSanPham = maSanPham;
        this.anh = anh;
        this.ten = ten;
        this.gia = gia;
        this.loai = loai;
        this.moTa = moTa;
        this.size = size;
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

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public ArrayList<String> getSize() {
        return size;
    }

    public void setSize(ArrayList<String> size) {
        this.size = size;
    }

    public ArrayList<Float> getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(ArrayList<Float> danhGia) {
        this.danhGia = danhGia;
    }
}