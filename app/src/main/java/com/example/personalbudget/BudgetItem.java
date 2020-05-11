package com.example.personalbudget;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BudgetItem implements Parcelable {
    private LocalDate budgetItemDate;
    private BigDecimal budgetItemValue;

    public LocalDate getDate() {
        return this.budgetItemDate;
    }


    public void setDate(LocalDate newDate) {
        this.budgetItemDate = newDate;
    }

    public BigDecimal getBudgetItemValue() {
        return this.budgetItemValue;
    }

    public void setBudgetItemValue(BigDecimal newValue) {
        this.budgetItemValue = newValue;
    }

    public BudgetItem() {
        this.setDate(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth()));
        this.setBudgetItemValue(new BigDecimal("0"));
    }

    public BudgetItem(Parcel in) {
        String dateString = in.readString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.setDate(LocalDate.parse(dateString, formatter));

        String value = in.readString();
        this.setBudgetItemValue(new BigDecimal(value));
    }

    public BudgetItem(LocalDate date, BigDecimal value) {
        this.setDate(date);
        this.setBudgetItemValue(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dest.writeString(this.getDate().format(formatter));
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
