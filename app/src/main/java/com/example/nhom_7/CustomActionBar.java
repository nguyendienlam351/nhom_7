package com.example.nhom_7;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CustomActionBar extends LinearLayout {
    private ViewGroup actionBarLayout;
    private TextView tvActionBarName;
    private ActionBarDelegation delegation = null;

    public void setActionBarName(String actionBarName){
        tvActionBarName.setText(actionBarName);
    }


    public void setDelegation(ActionBarDelegation delegation) {
        this.delegation = delegation;
    }

    public CustomActionBar(Context context) {
        super(context);
        init();
    }

    public CustomActionBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomActionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public CustomActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init(){
        inflate(getContext(), R.layout.action_bar_dndk, this);

        actionBarLayout = (ViewGroup) getChildAt(0);

        tvActionBarName = findViewById(R.id.tvActionBarName);
    }

    public interface ActionBarDelegation{
        public void backOnClick();
    }
}
