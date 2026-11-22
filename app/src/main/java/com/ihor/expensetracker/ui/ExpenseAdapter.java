package com.ihor.expensetracker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ihor.expensetracker.data.Category;
import com.ihor.expensetracker.data.Expense;
import com.ihor.expensetracker.databinding.ItemExpenseBinding;
import com.ihor.expensetracker.util.MoneyUtils;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExpenseAdapter extends ListAdapter<Expense, ExpenseAdapter.ViewHolder> {

    private Map<Integer, String> categoryNames = new HashMap<>();
    private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

    private static final DiffUtil.ItemCallback<Expense> DIFF = new DiffUtil.ItemCallback<Expense>() {
        @Override
        public boolean areItemsTheSame(@NonNull Expense a, @NonNull Expense b) {
            return a.id == b.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Expense a, @NonNull Expense b) {
            return a.amount == b.amount && a.categoryId == b.categoryId
                    && a.date == b.date && Objects.equals(a.note, b.note);
        }
    };

    public ExpenseAdapter() {
        super(DIFF);
    }

    /** Expenses only store a categoryId, so the adapter needs id -> name. */
    public void setCategories(List<Category> categories) {
        categoryNames = new HashMap<>();
        for (Category c : categories) {
            categoryNames.put(c.id, c.name);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExpenseBinding binding = ItemExpenseBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense e = getItem(position);

        String category = categoryNames.get(e.categoryId);
        holder.binding.textCategory.setText(category != null ? category : "Unknown");

        boolean hasNote = e.note != null && !e.note.isEmpty();
        holder.binding.textNote.setText(hasNote ? e.note : "");
        holder.binding.textNote.setVisibility(hasNote ? View.VISIBLE : View.GONE);

        holder.binding.textAmount.setText("-€" + MoneyUtils.format(e.amount));
        holder.binding.textDate.setText(dateFormat.format(e.date));
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onClick(e);
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemExpenseBinding binding;

        ViewHolder(ItemExpenseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnExpenseClick {
        void onClick(Expense expense);
    }

    private OnExpenseClick clickListener;

    public void setOnExpenseClickListener(OnExpenseClick listener) {
        this.clickListener = listener;
    }
}