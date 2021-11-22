package com.example.nhom_7.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nhom_7.DanhSachSanPham;
import com.example.nhom_7.GioHang;
import com.example.nhom_7.Home;
import com.example.nhom_7.LichSuDonHangAdmin;
import com.example.nhom_7.QuanLy;
import com.example.nhom_7.QuanLyLoaiSanPham;
import com.example.nhom_7.ThongTinKhachHang;
import com.example.nhom_7.ThongTinNhanVien;
import com.example.nhom_7.ThongTinQuanLy;

public class MyViewPagerAdapterNhanVien extends FragmentStateAdapter {

    public MyViewPagerAdapterNhanVien(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new DanhSachSanPham();
            case 1:
                return new QuanLyLoaiSanPham();
            case 2:
                return new LichSuDonHangAdmin();
            case 3:
                return new ThongTinNhanVien();
            default:
                return new DanhSachSanPham();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
