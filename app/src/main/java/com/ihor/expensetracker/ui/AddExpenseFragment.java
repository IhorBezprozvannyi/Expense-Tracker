package com.ihor.expensetracker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.ihor.expensetracker.databinding.FragmentAddExpenseBinding;
import com.ihor.expensetracker.util.DateUtils;
import com.ihor.expensetracker.util.MoneyUtils;
import com.ihor.expensetracker.viewmodel.ExpenseViewModel;

import java.text.DateFormat;

public class AddExpenseFragment extends Fragment {

    private FragmentAddExpenseBinding binding;
    private ExpenseViewModel viewModel;
    private int selectedCategoryId = -1;
    private long selectedDate = System.currentTimeMillis();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddExpenseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        CategoryAdapter adapter = new CategoryAdapter(c -> selectedCategoryId = c.id);
        binding.recyclerCategories.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.recyclerCategories.setAdapter(adapter);
        viewModel.getAllCategories().observe(getViewLifecycleOwner(), adapter::setItems);

        updateDateButton();
        binding.buttonDate.setOnClickListener(v -> showDatePicker());
        binding.buttonSave.setOnClickListener(v -> save());
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        picker.addOnPositiveButtonClickListener(selection -> {
            selectedDate = DateUtils.fromPickerUtc(selection);
            updateDateButton();
        });
        picker.show(getParentFragmentManager(), "datePicker");
    }

    private void updateDateButton() {
        binding.buttonDate.setText(
                DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate));
    }

    private void save() {
        String amountText = binding.editAmount.getText() == null
                ? "" : binding.editAmount.getText().toString();
        long amount = MoneyUtils.parse(amountText);

        if (amount <= 0) {
            binding.inputAmountLayout.setError("Enter an amount greater than 0");
            return;
        }
        binding.inputAmountLayout.setError(null);

        if (selectedCategoryId == -1) {
            Toast.makeText(requireContext(), "Choose a category", Toast.LENGTH_SHORT).show();
            return;
        }

        String note = binding.editNote.getText() == null
                ? "" : binding.editNote.getText().toString().trim();

        viewModel.addExpense(amount, selectedCategoryId, selectedDate,
                note.isEmpty() ? null : note);
        getParentFragmentManager().popBackStack(); // go back to the list
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
