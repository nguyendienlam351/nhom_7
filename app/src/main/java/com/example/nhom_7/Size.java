package com.example.nhom_7;

public class Size {
    String tenSize;
    int soLuong;
    public static final String SIZE_S = "size S";
    public static final String SIZE_M = "size M";
    public static final String SIZE_L = "size L";
    public static final String SIZE_XL = "size XL";

    public Size() {
    }

    public Size(String tenSize, int soLuong) {
        this.tenSize = tenSize;
        this.soLuong = soLuong;
    }

    public String getTenSize() {
        return tenSize;
    }

    public void setTenSize(String tenSize) {
        this.tenSize = tenSize;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return tenSize + ": "+ soLuong;
    }
}