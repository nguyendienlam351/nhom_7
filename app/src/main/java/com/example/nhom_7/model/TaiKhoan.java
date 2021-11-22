package com.example.nhom_7.model;

import java.util.ArrayList;

public class TaiKhoan {
    String hoTen;
    String email;
    String soDienThoai;
    String diaChi;
    String maKH;
    String loaiTaiKhoan;
    ArrayList<ChiTietDH> gioHang = new ArrayList<ChiTietDH>();

    public TaiKhoan() {
    }

    public TaiKhoan(String hoTen, String email, String soDienThoai, String diaChi, String maKH, String loaiTaiKhoan) {
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.maKH = maKH;
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    public String getLoaiTaiKhoan() {
        return loaiTaiKhoan;
    }

    public void setLoaiTaiKhoan(String loaiTaiKhoan) {
        this.loaiTaiKhoan = loaiTaiKhoan;
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

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public ArrayList<ChiTietDH> getGioHang() {
        return gioHang;
    }

    public void setGioHang(ArrayList<ChiTietDH> gioHang) {
        this.gioHang = gioHang;
    }
}
