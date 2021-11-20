package com.example.nhom_7.model;

import java.util.HashMap;
import java.util.Map;

public class NhanVien {
    String hoTen;
    int soDienThoai;
    String email;
    String diaChi;
    String loaiTaiKhoan;
    String maKH;

    public NhanVien() {
      //Mặc định của firebase, khi nhận data
    }
    public NhanVien(String hoTen, int soDienThoai, String email, String diaChi, String loaiTaiKhoan, String maKH) {
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.loaiTaiKhoan=loaiTaiKhoan;
        this.maKH = maKH;
    }

    public int getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(int soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getLoaiTaiKhoan() {
        return loaiTaiKhoan;
    }

    public void setLoaiTaiKhoan(String loaiTaiKhoan) {
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }


    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("hoTen",hoTen);
        result.put("soDienThoai",soDienThoai);
        result.put("email",email);
        result.put("diaChi",diaChi);
        result.put("maKH",maKH);
        return result;
    }
}
