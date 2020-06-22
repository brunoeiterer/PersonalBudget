package com.example.personalbudget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Currency;

public class BudgetRecyclerViewAdapter extends RecyclerView.Adapter<BudgetRecyclerViewAdapter.ViewHolder> {
    private BudgetData budgetData;
    private LayoutInflater layoutInflater;
    private ItemClickListener clickListener;
    private Fragment fragment;
    private int selectedPosition;

    BudgetRecyclerViewAdapter(Fragment fragment, BudgetData budgetData) {
        this.fragment = fragment;
        this.layoutInflater = LayoutInflater.from(this.fragment.getActivity());
        this.budgetData = budgetData;
        this.budgetData.sortByDate();
        this.selectedPosition = RecyclerView.NO_POSITION;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.budget_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocalDate date = this.budgetData.getBudgetItem(position).getDate();
        BigDecimal value = this.budgetData.getBudgetItem(position).getBudgetItemValue();
        Currency currency = this.budgetData.getBudgetDataCurrency();
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingUsed(false);
        holder.dateTextView.setText(String.format("%td/%tm/%tY", date, date, date));
        holder.valueTextView.setText(currency.getSymbol() + ' ' + decimalFormat.format(value));

        if(selectedPosition == position) {
            holder.itemView.setSelected(true);
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
        else {
            holder.itemView.setSelected(false);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return budgetData.getSize();
    }

    public void addBudgetItem(BudgetItem budgetItem) {
        this.budgetData.addBudgetItem(budgetItem);
        this.budgetData.sortByDate();

        TextView totalValueTextView = this.fragment.getView().findViewById(R.id.totalValueTextView);
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingUsed(false);
        totalValueTextView.setText(this.budgetData.getBudgetDataCurrency().getSymbol() + ' ' +
                decimalFormat.format(this.budgetData.getTotalValue()));

        TabLayout tabLayout = this.fragment.getActivity().findViewById(R.id.tabLayout);
        TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        try{
            BudgetDataFileHandler.WriteBudgetDataToFile(tab.getText().toString(), this.budgetData);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void removeBudgetItem(BudgetItem budgetItem) {
        this.budgetData.removeBudgetItem(budgetItem);
        this.budgetData.sortByDate();

        this.UnselectItem();

        TextView totalValueTextView = this.fragment.getView().findViewById(R.id.totalValueTextView);
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingUsed(false);
        totalValueTextView.setText(this.budgetData.getBudgetDataCurrency().getSymbol() + ' ' +
                decimalFormat.format(this.budgetData.getTotalValue()));

        TabLayout tabLayout = this.fragment.getActivity().findViewById(R.id.tabLayout);
        TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        try{
            BudgetDataFileHandler.WriteBudgetDataToFile(tab.getText().toString(), this.budgetData);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UnselectItem() {
        this.selectedPosition = RecyclerView.NO_POSITION;

        final FloatingActionButton removeButton = this.fragment.getView().findViewById(R.id.RemoveBudgetItemButton);
        final FloatingActionButton editButton = this.fragment.getView().findViewById(R.id.EditBudgetItemButton);
        notifyDataSetChanged();
        removeButton.setVisibility(FloatingActionButton.INVISIBLE);
        removeButton.setClickable(false);
        editButton.setVisibility(FloatingActionButton.INVISIBLE);
        editButton.setClickable(false);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dateTextView;
        private TextView valueTextView;

        ViewHolder(View itemView) {
            super(itemView);
            this.dateTextView = itemView.findViewById(R.id.dateTextView);
            this.valueTextView = itemView.findViewById(R.id.valueTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final FloatingActionButton removeButton = BudgetRecyclerViewAdapter.this.fragment.getView().findViewById(R.id.RemoveBudgetItemButton);
            final FloatingActionButton editButton = BudgetRecyclerViewAdapter.this.fragment.getView().findViewById(R.id.EditBudgetItemButton);

            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }

            if (selectedPosition == getAdapterPosition()) {
                selectedPosition = RecyclerView.NO_POSITION;
                notifyDataSetChanged();
                removeButton.setVisibility(FloatingActionButton.INVISIBLE);
                removeButton.setClickable(false);
                editButton.setVisibility(FloatingActionButton.INVISIBLE);
                editButton.setClickable(false);
            }
            else {
                notifyItemChanged(selectedPosition);
                selectedPosition = getLayoutPosition();
                notifyItemChanged(selectedPosition);
                removeButton.setVisibility(FloatingActionButton.VISIBLE);
                removeButton.setClickable(true);

                final FloatingActionButton addBudgetItemButton = BudgetRecyclerViewAdapter.this.fragment.getView().findViewById(R.id.AddBudgetItemButton);

                final Rect actualPosition = new Rect();
                addBudgetItemButton.getGlobalVisibleRect(actualPosition);
                final Rect screen = new Rect(0, 0, Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels);
                Boolean visible = actualPosition.intersect(screen);

                if(removeButton.hasOnClickListeners() == false) {
                    removeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BudgetItem budgetItem = BudgetRecyclerViewAdapter.this.budgetData.getBudgetItem(selectedPosition);
                            BudgetRecyclerViewAdapter.this.removeBudgetItem(budgetItem);
                            notifyDataSetChanged();
                        }
                    });
                }

                editButton.setVisibility(FloatingActionButton.VISIBLE);
                editButton.setClickable(true);

//                editButton.show();
//                editButton.invalidate();

                if(editButton.hasOnClickListeners() == false) {
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /* show popup window */
                            AddBudgetItemDialogFragment addBudgetItemDialogFragment =
                                    new AddBudgetItemDialogFragment(BudgetRecyclerViewAdapter.this);

                            addBudgetItemDialogFragment.show(
                                    BudgetRecyclerViewAdapter.this.fragment.getActivity().getSupportFragmentManager() , null);
                            BudgetRecyclerViewAdapter.this.fragment.getActivity().getSupportFragmentManager().executePendingTransactions();

                            Window window = addBudgetItemDialogFragment.getDialog().getWindow();
                            WindowManager.LayoutParams layoutParams = window.getAttributes();
                            layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                            layoutParams.dimAmount = 0.5f;
                            window.setAttributes(layoutParams);
                        }
                    });
                }
            }
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
