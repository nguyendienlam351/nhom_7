package com.example.nhom_7.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nhom_7.GioHang;
import com.example.nhom_7.Home;
import com.example.nhom_7.ThongTinKhachHang;

public class MyViewPagerAdapterKhachHang extends FragmentStateAdapter {
    public MyViewPagerAdapterKhachHang(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Home();
            case 1:
                return new GioHang();
            case 2:
                return new ThongTinKhachHang();
            default:
                return new Home();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
