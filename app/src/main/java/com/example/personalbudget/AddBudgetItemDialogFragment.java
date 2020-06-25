package com.example.personalbudget;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddBudgetItemDialogFragment extends DialogFragment {

    private BudgetRecyclerViewAdapter budgetRecyclerViewAdapter;
    private LocalDate date;
    private BigDecimal value;
    private boolean isAddingBudgetItem;

    public AddBudgetItemDialogFragment(BudgetRecyclerViewAdapter budgetRecyclerViewAdapter, boolean isAddingBudgetItem) {
        super();
        this.budgetRecyclerViewAdapter = budgetRecyclerViewAdapter;
        this.date = null;
        this.value = null;
        this.isAddingBudgetItem = isAddingBudgetItem;
    }

    public AddBudgetItemDialogFragment(BudgetRecyclerViewAdapter budgetRecyclerViewAdapter, LocalDate date, BigDecimal value, boolean isAddingBudgetItem) {
        super();
        this.budgetRecyclerViewAdapter = budgetRecyclerViewAdapter;
        this.date = date;
        this.value = value;
        this.isAddingBudgetItem = isAddingBudgetItem;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.add_budget_item_window, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                AddBudgetItemDialogFragment.this.dismiss();
                return true;
            }
        });

        EditText dateEditText = view.findViewById(R.id.addBudgetItemWindowDateEditText);
        if(this.date != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dateEditText.setText(this.date.format(dateTimeFormatter));
        }
        int year = this.date != null ? this.date.getYear() : LocalDate.now().getYear();
        int month = this.date != null ? this.date.getMonthValue() : LocalDate.now().getMonthValue();
        int day = this.date != null ? this.date.getDayOfMonth() : LocalDate.now().getDayOfMonth();
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View onClickListener) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddBudgetItemDialogFragment.this.getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        /* add 1 to the month because they start at index 0 */
                        dateEditText.setText(LocalDate.of(year, month + 1, dayOfMonth).format(formatter));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        EditText valueEditText = view.findViewById(R.id.addBudgetItemWindowValueEditText);
        if(this.value != null) {
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setGroupingUsed(false);
            valueEditText.setText(decimalFormat.format(this.value));
        }

        InputFilter twoDecimalPlacesFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(dest.length() > 3 && dest.charAt(dest.length() - 3) == '.') {
                    return "";
                }
                return null;
            }
        };

        InputFilter mandatoryNumberBeforePointFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(dest.length() == 0 && source.length() > 0 && source.charAt(0) == '.') {
                    return "";
                }
                return null;
            }
        };

        valueEditText.setFilters(new InputFilter[] {twoDecimalPlacesFilter, mandatoryNumberBeforePointFilter});

        Button doneButton = view.findViewById(R.id.addBudgetItemWindowDoneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View onClickListener) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(dateEditText.getText(), formatter);

                BigDecimal value = new BigDecimal(valueEditText.getText().toString());

                if(AddBudgetItemDialogFragment.this.isAddingBudgetItem == true) {
                    AddBudgetItemDialogFragment.this.addBudgetItem(date, value, budgetRecyclerViewAdapter);
                }
                else {
                    AddBudgetItemDialogFragment.this.editBudgetItem(date, value, budgetRecyclerViewAdapter);
                }
                AddBudgetItemDialogFragment.this.dismiss();
            }
        });

        Button cancelButton = view.findViewById(R.id.addBudgetItemWindowCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View onClickListener) {
                AddBudgetItemDialogFragment.this.dismiss();
            }
        });
    }

    private void addBudgetItem(LocalDate date, BigDecimal value, BudgetRecyclerViewAdapter budgetRecyclerViewAdapter) {
        BudgetItem budgetItem = new BudgetItem(date, value);
        budgetRecyclerViewAdapter.addBudgetItem(budgetItem);
        budgetRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void editBudgetItem(LocalDate date, BigDecimal value, BudgetRecyclerViewAdapter budgetRecyclerViewAdapter) {
        budgetRecyclerViewAdapter.editBudgetItem(date, value);
    }
}
