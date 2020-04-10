package com.example.personalbudget;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class TabsAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabs;

    private ArrayList<String> tabTextList;

    public TabsAdapter(FragmentManager fragmentManager, int newNumberOfTabs, ArrayList<String> newTabTextList) {
        super(fragmentManager);

        tabTextList = new ArrayList<String>();
        this.tabTextList.addAll(newTabTextList);

        this.numberOfTabs = newNumberOfTabs;
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            default:
                BudgetFragment bills = new BudgetFragment();
                Bundle bundle = new Bundle();
                bundle.putString("TabData", this.tabTextList.get(position));
                bills.setArguments(bundle);
                return bills;
        }
    }
}
