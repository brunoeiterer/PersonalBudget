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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddBudgetItemDialogFragment extends DialogFragment {

    private BudgetRecyclerViewAdapter budgetRecyclerViewAdapter;

    public AddBudgetItemDialogFragment(BudgetRecyclerViewAdapter budgetRecyclerViewAdapter) {
        super();
        this.budgetRecyclerViewAdapter = budgetRecyclerViewAdapter;
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
                }, 2000, 10, 5);
                datePickerDialog.show();
            }
        });

        EditText valueEditText = view.findViewById(R.id.addBudgetItemWindowValueEditText);

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
                if(dest.length() == 0 && source.charAt(0) == '.') {
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

                AddBudgetItemDialogFragment.this.addBudgetItem(date, value, budgetRecyclerViewAdapter);

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

}
