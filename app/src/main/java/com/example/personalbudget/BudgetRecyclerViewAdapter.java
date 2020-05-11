package com.example.personalbudget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class BudgetRecyclerViewAdapter extends RecyclerView.Adapter<BudgetRecyclerViewAdapter.ViewHolder> {
    private BudgetData budgetData;
    private LayoutInflater layoutInflater;
    private ItemClickListener clickListener;
    private Context context;
    private int selectedPosition;

    BudgetRecyclerViewAdapter(Context context, BudgetData budgetData) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.budgetData = budgetData;
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
        holder.dateTextView.setText(String.format("%td/%tm/%tY", date, date, date));
        holder.valueTextView.setText(currency.getSymbol() + ' ' + value.toString());

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
    }

    public void removeBudgetItem(BudgetItem budgetItem) {
        this.budgetData.removeBudgetItem(budgetItem);
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
            FloatingActionButton removeButton = ((Activity)context).findViewById(R.id.RemoveBudgetItemButton);

            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }

            if (selectedPosition == getAdapterPosition()) {
                selectedPosition = RecyclerView.NO_POSITION;
                notifyDataSetChanged();
                removeButton.setVisibility(FloatingActionButton.INVISIBLE);
                removeButton.setClickable(false);
            }
            else {
                notifyItemChanged(selectedPosition);
                selectedPosition = getLayoutPosition();
                notifyItemChanged(selectedPosition);
                removeButton.setVisibility(FloatingActionButton.VISIBLE);
                removeButton.setClickable(true);

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
