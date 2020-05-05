package com.example.personalbudget;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BudgetFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.budget_layout, viewGroup, false);
        RecyclerView budgetRecyclerView = view.findViewById(R.id.budgetRecyclerView);
        budgetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BudgetRecyclerViewAdapter budgetRecyclerViewAdapter =
                new BudgetRecyclerViewAdapter(getActivity(), (BudgetData) getArguments().getParcelable("budgetContent"));
        budgetRecyclerView.setAdapter(budgetRecyclerViewAdapter);
        return view;
    }
}
