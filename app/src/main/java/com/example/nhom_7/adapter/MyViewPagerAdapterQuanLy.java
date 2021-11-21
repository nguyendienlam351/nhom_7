package com.example.nhom_7.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nhom_7.GioHang;
import com.example.nhom_7.Home;
import com.example.nhom_7.QuanLy;
import com.example.nhom_7.ThongTinKhachHang;
import com.example.nhom_7.ThongTinQuanLy;

public class MyViewPagerAdapterQuanLy extends FragmentStateAdapter {

    public MyViewPagerAdapterQuanLy(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Home();
            case 1:
                return new QuanLy();
            case 2:
                return new GioHang();
            case 3:
                return new ThongTinQuanLy();
            default:
                return new Home();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
