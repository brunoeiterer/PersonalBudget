package com.example.personalbudget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;

public class TabsAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabs;
    private Context activityContext;
    private ArrayList<BudgetFragment> fragmentsList;

    public TabsAdapter(FragmentManager fragmentManager, int newNumberOfTabs, Context activityContext) {
        super(fragmentManager);

        this.activityContext = activityContext;

        this.numberOfTabs = newNumberOfTabs;

        this.fragmentsList = new ArrayList<>();
        for(int i = 0; i < this.numberOfTabs; i++) {
            BudgetFragment budgetFragment = new BudgetFragment();
            BudgetData budgetData = getFragmentContent(i);
            Bundle bundle = new Bundle();
            bundle.putParcelable("budgetContent", budgetData);
            budgetFragment.setArguments(bundle);
            this.fragmentsList.add(budgetFragment);
        }
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragmentsList.get(position);
    }

    private BudgetData getFragmentContent(int position) {
//        BudgetData fragmentContent = new BudgetData();
//        fragmentContent.setBudgetDataCurrency(Currency.getInstance("BRL"));
//
//        BudgetItem firstItem = new BudgetItem();
//        firstItem.setBudgetItemValue(BigDecimal.valueOf(100));
//        firstItem.setDate(LocalDate.now());
//
//        BudgetItem secondItem = new BudgetItem();
//        secondItem.setBudgetItemValue(BigDecimal.valueOf(72.50));
//        secondItem.setDate(LocalDate.now());
//
//        fragmentContent.addBudgetItem(firstItem);
//        fragmentContent.addBudgetItem(secondItem);

        TabLayout tabLayout = ((Activity) this.activityContext).findViewById(R.id.tabLayout);
        try {
            return BudgetDataFileHandler.ReadBudgetDataFromFile(tabLayout.getTabAt(position).getText().toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new BudgetData();
    }
}

