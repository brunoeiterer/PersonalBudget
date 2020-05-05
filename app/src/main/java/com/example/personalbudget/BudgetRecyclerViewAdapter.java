package com.example.personalbudget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class BudgetRecyclerViewAdapter extends RecyclerView.Adapter<BudgetRecyclerViewAdapter.ViewHolder> {
    private BudgetData budgetData;
    private LayoutInflater layoutInflater;
    private ItemClickListener clickListener;
    private Context context;

    BudgetRecyclerViewAdapter(Context context, BudgetData budgetData) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.budgetData = budgetData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.budget_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Date date = this.budgetData.getBudgetItem(position).getDate();
        BigDecimal value = this.budgetData.getBudgetItem(position).getBudgetItemValue();
        Currency currency = this.budgetData.getBudgetItem(position).getBudgetItemCurrency();
        holder.dateTextView.setText(date.toString());
        holder.valueTextView.setText(currency.getSymbol() + ' ' + value.toString());
    }

    @Override
    public int getItemCount() {
        return budgetData.getSize();
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
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
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
