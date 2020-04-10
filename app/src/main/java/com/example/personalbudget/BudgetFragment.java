package com.example.personalbudget;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class BudgetFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.budget_layout, viewGroup, false);
        TextView budgetTextView = (TextView) view.findViewById(R.id.budgetTextView);
        budgetTextView.setText(getArguments().getString("TabData"));
        return view;
    }
}
