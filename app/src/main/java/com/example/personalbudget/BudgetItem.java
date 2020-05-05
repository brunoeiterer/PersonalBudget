package com.example.personalbudget;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class BudgetItem implements Parcelable {
    private Date budgetItemDate;
    private BigDecimal budgetItemValue;
    private Currency budgetItemCurrency;

    public Date getDate() {
        return this.budgetItemDate;
    }


    public void setDate(Date newDate) {
        this.budgetItemDate = newDate;
    }

    public BigDecimal getBudgetItemValue() {
        return this.budgetItemValue;
    }

    public void setBudgetItemValue(BigDecimal newValue) {
        this.budgetItemValue = newValue;
    }

    public Currency getBudgetItemCurrency() {
        return this.budgetItemCurrency;
    }

    public void setBudgetItemCurrency(Currency newCurrency) {
        this.budgetItemCurrency = newCurrency;
    }

    public BudgetItem() {
        this.setDate(new Date());
        this.setBudgetItemValue(new BigDecimal("0"));
        this.setBudgetItemCurrency(Currency.getInstance("BRL"));
    }

    public BudgetItem(Parcel in) {
        long date = in.readLong();
        this.setDate(new Date(date));

        String value = in.readString();
        this.setBudgetItemValue(new BigDecimal(value));

        String currency = in.readString();
        this.setBudgetItemCurrency(Currency.getInstance(currency));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.getDate().getTime());
        dest.writeString(this.getBudgetItemValue().toString());
        dest.writeString(this.getBudgetItemCurrency().toString());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BudgetItem createFromParcel(Parcel in) {
            return new BudgetItem(in);
        }

        public BudgetItem[] newArray(int size) {
            return new BudgetItem[size];
        }
    };
}
