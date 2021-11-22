package com.example.nhom_7.model;

import java.util.HashMap;
import java.util.Map;

public class LoaiSanPham {
    String maLoai;
    String tenLoai;

    public LoaiSanPham() {
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tenLoai", tenLoai);
        return result;
    }

    @Override
    public String toString() {
        return tenLoai;
    }
}
