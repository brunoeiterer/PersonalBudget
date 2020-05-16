package com.example.personalbudget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        ArrayList<String> tabNames = new ArrayList<>();
        for(int i = 0; i < tabLayout.getTabCount(); i++) {
            tabNames.add(tabLayout.getTabAt(i).getText().toString());
        }

        BudgetDataFileHandler.InitializeBudgetDataFileHandler(tabNames, this);

        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), this);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Fragment currentFragment = tabsAdapter.getItem(tab.getPosition());
                RecyclerView budgetRecyclerView = currentFragment.getView().findViewById(R.id.budgetRecyclerView);
                BudgetRecyclerViewAdapter budgetRecyclerViewAdapter = (BudgetRecyclerViewAdapter) budgetRecyclerView.getAdapter();
                budgetRecyclerViewAdapter.UnselectItem();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

            }
        );
    }
}
