package com.example.lotus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class pageradapter extends FragmentPagerAdapter {
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0)return new ChatFragment();
        if (position==1) return new ContactFragment();
        else return new RequestFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  null;
    }

    public pageradapter(@NonNull FragmentManager fm) {
        super(fm);
    }
}
