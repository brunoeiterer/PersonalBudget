package com.example.personalbudget;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;

import android.os.Bundle;

import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BudgetFragment extends Fragment {
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.budget_layout, viewGroup, false);
        BudgetData budgetData = (BudgetData) getArguments().getParcelable("budgetContent");
        RecyclerView budgetRecyclerView = view.findViewById(R.id.budgetRecyclerView);
        budgetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BudgetRecyclerViewAdapter budgetRecyclerViewAdapter =
                new BudgetRecyclerViewAdapter(getActivity(), budgetData);
        budgetRecyclerView.setAdapter(budgetRecyclerViewAdapter);

        TextView totalValueTextView = view.findViewById(R.id.totalValueTextView);
        totalValueTextView.setText("Total: " + budgetData.getBudgetDataCurrency().getSymbol() + ' ' +
                budgetData.getTotalValue().setScale(2).toPlainString());

        /* set add button callback */
        FloatingActionButton addBudgetItemButton = view.findViewById(R.id.AddBudgetItemButton);
        addBudgetItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = layoutInflater.inflate(R.layout.add_budget_item_window, null);
                int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });


        return view;
    }


}
