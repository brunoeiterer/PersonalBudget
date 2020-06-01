package com.example.personalbudget;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;

import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BudgetFragment extends Fragment {
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.budget_layout, viewGroup, false);
        BudgetData budgetData = (BudgetData) getArguments().getParcelable("budgetContent");
        RecyclerView budgetRecyclerView = view.findViewById(R.id.budgetRecyclerView);
        budgetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final BudgetRecyclerViewAdapter budgetRecyclerViewAdapter =
                new BudgetRecyclerViewAdapter(this, budgetData);
        budgetRecyclerView.setAdapter(budgetRecyclerViewAdapter);

        TextView totalValueTextView = view.findViewById(R.id.totalValueTextView);
        totalValueTextView.setText("Total: " + budgetData.getBudgetDataCurrency().getSymbol() + ' ' +
                budgetData.getTotalValue().setScale(2).toPlainString());

        /* set add button callback */
        final FloatingActionButton addBudgetItemButton = view.findViewById(R.id.AddBudgetItemButton);
        addBudgetItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* show popup window */
                final View popupView = layoutInflater.inflate(R.layout.add_budget_item_window, null);
                int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                View container = popupWindow.getContentView().getRootView();
                Context context = popupWindow.getContentView().getContext();
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) container.getLayoutParams();
                layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                layoutParams.dimAmount = 0.5f;
                windowManager.updateViewLayout(container, layoutParams);

                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

                Button doneButton = popupView.findViewById(R.id.addBudgetItemWindowDoneButton);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText dateEditText = popupView.findViewById(R.id.addBudgetItemWindowDateEditText);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate date;
                        try {
                            date = LocalDate.parse(dateEditText.getText(), formatter);

                            EditText valueEditText = popupView.findViewById(R.id.addBudgetItemWindowValueEditText);
                            BigDecimal value = new BigDecimal(valueEditText.getText().toString());

                            addBudgetItem(date, value, budgetRecyclerViewAdapter);

                            popupWindow.dismiss();
                        }
                        catch(DateTimeParseException e) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(R.string.add_budget_item_dialog_invalid_date_tile)
                                    .setMessage(getString(R.string.add_budget_item_dialog_invalid_date_message))
                                    .show();
                        }
                    }
                });

                Button cancelButton = popupView.findViewById(R.id.addBudgetItemWindowCancelButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        return view;
    }

    private void addBudgetItem(LocalDate date, BigDecimal value, BudgetRecyclerViewAdapter budgetRecyclerViewAdapter) {
        BudgetItem budgetItem = new BudgetItem(date, value);
        budgetRecyclerViewAdapter.addBudgetItem(budgetItem);
        budgetRecyclerViewAdapter.notifyDataSetChanged();
    }


}
