package com.example.personalbudget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

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

                AddBudgetItemDialogFragment addBudgetItemDialogFragment = new AddBudgetItemDialogFragment(budgetRecyclerViewAdapter, true);

                addBudgetItemDialogFragment.show(BudgetFragment.this.getActivity().getSupportFragmentManager() , null);
                BudgetFragment.this.getActivity().getSupportFragmentManager().executePendingTransactions();

                Window window = addBudgetItemDialogFragment.getDialog().getWindow();
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                layoutParams.dimAmount = 0.5f;
                window.setAttributes(layoutParams);
            }
        });

        return view;
    }
}
