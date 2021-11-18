package com.example.nhom_7;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPagerAdapterGDC extends FragmentStateAdapter {

    public MyViewPagerAdapterGDC(@NonNull FragmentActivity fragmentActivity) {
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
                return new ThongTinUser();
            default:
                return new Home();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
