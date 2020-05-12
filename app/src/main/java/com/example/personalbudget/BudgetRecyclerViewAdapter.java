package com.example.personalbudget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        TextView totalValueTextView = ((Activity) this.context).findViewById(R.id.totalValueTextView);
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingUsed(false);
        totalValueTextView.setText(this.budgetData.getBudgetDataCurrency().getSymbol() + ' ' +
                decimalFormat.format(this.budgetData.getTotalValue()));
    }

    public void removeBudgetItem(BudgetItem budgetItem) {
        this.budgetData.removeBudgetItem(budgetItem);
        this.budgetData.sortByDate();

        TextView totalValueTextView = ((Activity) this.context).findViewById(R.id.totalValueTextView);
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingUsed(false);
        totalValueTextView.setText(this.budgetData.getBudgetDataCurrency().getSymbol() + ' ' +
                decimalFormat.format(this.budgetData.getTotalValue()));
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
            FloatingActionButton editButton = ((Activity)context).findViewById(R.id.EditBudgetItemButton);

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

                if(editButton.hasOnClickListeners() == false) {
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /* show popup window */
                            final View popupView = layoutInflater.inflate(R.layout.add_budget_item_window, null);
                            int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                            int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                            View container = popupWindow.getContentView().getRootView();
                            Context context = popupWindow.getContentView().getContext();
                            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) container.getLayoutParams();
                            layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                            layoutParams.dimAmount = 0.5f;
                            windowManager.updateViewLayout(container, layoutParams);

                            /* dismiss if user touches outside of the window */
                            popupView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent event) {
                                    popupWindow.dismiss();
                                    return true;
                                }
                            });

                            EditText dateEditText = popupView.findViewById(R.id.addBudgetItemWindowDateEditText);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            dateEditText.setText(BudgetRecyclerViewAdapter.this.budgetData.getBudgetItem(
                                    BudgetRecyclerViewAdapter.this.selectedPosition).getDate().format(formatter));

                            EditText valueEditText = popupView.findViewById(R.id.addBudgetItemWindowValueEditText);
                            valueEditText.setText(BudgetRecyclerViewAdapter.this.budgetData.getBudgetItem(
                                    BudgetRecyclerViewAdapter.this.selectedPosition).getBudgetItemValue().toString());

                            Button doneButton = popupView.findViewById(R.id.addBudgetItemWindowDoneButton);
                            doneButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    LocalDate date = LocalDate.parse(dateEditText.getText(), formatter);
                                    BudgetRecyclerViewAdapter.this.budgetData.getBudgetItem(
                                            BudgetRecyclerViewAdapter.this.selectedPosition).setDate(date);

                                    BigDecimal value = new BigDecimal(valueEditText.getText().toString());
                                    BudgetRecyclerViewAdapter.this.budgetData.getBudgetItem(
                                            BudgetRecyclerViewAdapter.this.selectedPosition).setBudgetItemValue(value);

                                    BudgetRecyclerViewAdapter.this.notifyDataSetChanged();

                                    TextView totalValueTextView = ((Activity) BudgetRecyclerViewAdapter.this.context).
                                            findViewById(R.id.totalValueTextView);
                                    DecimalFormat decimalFormat = new DecimalFormat();
                                    decimalFormat.setMinimumFractionDigits(2);
                                    decimalFormat.setMaximumFractionDigits(2);
                                    decimalFormat.setGroupingUsed(false);
                                    totalValueTextView.setText(BudgetRecyclerViewAdapter.this.budgetData.getBudgetDataCurrency().getSymbol() +
                                            ' ' + decimalFormat.format(BudgetRecyclerViewAdapter.this.budgetData.getTotalValue()));

                                    popupWindow.dismiss();
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
