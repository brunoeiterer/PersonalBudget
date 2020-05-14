package com.example.personalbudget;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;

public class BudgetData implements Parcelable, Serializable {
    private ArrayList<BudgetItem> budgetItemList;
    private Currency budgetDataCurrency;

    public BudgetData(ArrayList<BudgetItem> newBudgetItemList, Currency currency) {
        this.budgetItemList = new ArrayList<>();
        this.budgetItemList.addAll(newBudgetItemList);
        this.budgetDataCurrency = currency;
    }

    public BudgetData() {
        this.budgetItemList = new ArrayList<>();
        this.budgetDataCurrency = Currency.getInstance("BRL");
    }

    public BudgetData(Parcel in) {
        in.readList(this.budgetItemList, BudgetItem.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.budgetItemList);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public BudgetData createFromParcel(Parcel in) {
                return new BudgetData(in);
            }

            public BudgetData[] newArray(int size) {
                return new BudgetData[size];
            }
        };



    public BudgetItem getBudgetItem(int position) {
        return this.budgetItemList.get(position);
    }

    public void addBudgetItem(BudgetItem budgetItem) {
        this.budgetItemList.add(budgetItem);
    }

    public void removeBudgetItem(BudgetItem budgetItem) {
        this.budgetItemList.remove(budgetItem);
    }

    public BigDecimal getTotalValue() {
        BigDecimal totalValue = BigDecimal.ZERO;
        for(int i = 0; i < this.budgetItemList.size(); i++) {
            totalValue = totalValue.add(this.budgetItemList.get(i).getBudgetItemValue());
        }

        return totalValue;
    }

    public Currency getBudgetDataCurrency() {
        return this.budgetDataCurrency;
    }

    public void setBudgetDataCurrency(Currency currency) {
        this.budgetDataCurrency = currency;
    }

    public int getSize() {
        return this.budgetItemList.size();
    }

    public void sortByDate() {
        this.budgetItemList.sort(Comparator.comparing(BudgetItem::getDate));
    }
}
