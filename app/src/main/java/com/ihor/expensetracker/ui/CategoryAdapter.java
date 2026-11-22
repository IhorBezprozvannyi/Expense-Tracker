package com.ihor.expensetracker.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ihor.expensetracker.data.Category;
import com.ihor.expensetracker.databinding.ItemCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategorySelected {
        void onSelected(Category category);
    }

    private List<Category> items = new ArrayList<>();
    private int selectedId = -1;
    private final OnCategorySelected listener;

    public CategoryAdapter(OnCategorySelected listener) {
        this.listener = listener;
    }

    public void setItems(List<Category> categories) {
        items = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category c = items.get(position);
        holder.binding.textName.setText(c.name);
        holder.binding.cardCategory.setChecked(c.id == selectedId);
        holder.binding.cardCategory.setOnClickListener(v -> {
            selectedId = c.id;
            notifyDataSetChanged();
            listener.onSelected(c);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemCategoryBinding binding;

        ViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
