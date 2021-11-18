package com.example.nhom_7;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Home extends Fragment {
    CustomActionBar actionBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_home,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        actionBar.setActionBarName("Home");
    }

    private void setControl(View view) {
        actionBar = view.findViewById(R.id.actionbar);
    }
}
