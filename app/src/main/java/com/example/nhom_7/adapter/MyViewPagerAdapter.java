package com.example.nhom_7.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nhom_7.GiaoDienDangKy;
import com.example.nhom_7.GiaoDienDangNhap;

public class MyViewPagerAdapter extends FragmentStateAdapter {


    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new GiaoDienDangNhap();
            case 1:
                return new GiaoDienDangKy();
            default:
                return new GiaoDienDangNhap();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
