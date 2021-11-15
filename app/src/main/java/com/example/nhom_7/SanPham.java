package com.example.nhom_7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SanPham {
    String maSanPham;
    String anh;
    String ten;
    int gia;
    String loai;
    String moTa;
    ArrayList<Size> size;

    public SanPham() {
    }

    public SanPham(String maSanPham, String anh, String ten, int gia, String loai, String moTa, ArrayList<Size> size) {
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

    public ArrayList<Size> getSize() {
        return size;
    }

    public void setSize(ArrayList<Size> size) {
        this.size = size;
    }
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("maSanPham",maSanPham);
        result.put("anh",anh);
        result.put("ten",ten);
        result.put("gia",gia);
        result.put("loai",loai);
        result.put("moTa",moTa);
        result.put("size",size);
        return result;
    }
}
