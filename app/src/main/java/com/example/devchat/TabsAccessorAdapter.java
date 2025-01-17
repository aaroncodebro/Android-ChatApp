package com.example.devchat;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class TabsAccessorAdapter extends FragmentPagerAdapter {

    private static ArrayList<Fragment> mFragmentList;
    private static ArrayList<String> mFragmentTitleList;

   TabsAccessorAdapter(FragmentManager fmng)
   {
       super(fmng);
       mFragmentList=new ArrayList<>();
       mFragmentTitleList=new ArrayList<>();
   }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public static void addFragment(Fragment fragment, String title)
    {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
