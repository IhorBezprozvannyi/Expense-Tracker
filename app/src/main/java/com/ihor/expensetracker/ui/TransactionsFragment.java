package com.ihor.expensetracker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ihor.expensetracker.databinding.FragmentTransactionsBinding;
import com.ihor.expensetracker.viewmodel.ExpenseViewModel;
import com.ihor.expensetracker.R;

public class TransactionsFragment extends Fragment {

    private FragmentTransactionsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ExpenseViewModel viewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        ExpenseAdapter adapter = new ExpenseAdapter();

        binding.recyclerExpenses.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerExpenses.setAdapter(adapter);

        viewModel.getAllCategories().observe(getViewLifecycleOwner(), adapter::setCategories);
        viewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            adapter.submitList(expenses);
            adapter.setOnExpenseClickListener(expense ->
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, AddExpenseFragment.newInstance(expense))
                            .addToBackStack(null)
                            .commit());
            binding.textEmpty.setVisibility(expenses.isEmpty() ? View.VISIBLE : View.GONE);
        });
        binding.fabAdd.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new AddExpenseFragment())
                        .addToBackStack(null)
                        .commit());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // avoids leaking the view hierarchy
    }
}
