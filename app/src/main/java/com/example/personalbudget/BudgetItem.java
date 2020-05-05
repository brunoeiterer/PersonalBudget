package com.example.personalbudget;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class BudgetItem implements Parcelable {
    private Date budgetItemDate;
    private BigDecimal budgetItemValue;

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

    public BudgetItem() {
        this.setDate(new Date());
        this.setBudgetItemValue(new BigDecimal("0"));
    }

    public BudgetItem(Parcel in) {
        long date = in.readLong();
        this.setDate(new Date(date));

        String value = in.readString();
        this.setBudgetItemValue(new BigDecimal(value));

        String currency = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.getDate().getTime());
        dest.writeString(this.getBudgetItemValue().toString());
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
