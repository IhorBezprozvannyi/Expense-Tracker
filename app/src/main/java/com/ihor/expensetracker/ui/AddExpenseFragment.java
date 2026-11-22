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
import com.ihor.expensetracker.data.Expense;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
        Bundle args = getArguments();
        if (args != null && args.containsKey("id")) {
            editing = new Expense();
            editing.id = args.getInt("id");
            editing.amount = args.getLong("amount");
            editing.categoryId = args.getInt("categoryId");
            editing.date = args.getLong("date");
            editing.note = args.getString("note");

            selectedCategoryId = editing.categoryId;
            selectedDate = editing.date;
        }

        viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        CategoryAdapter adapter = new CategoryAdapter(c -> selectedCategoryId = c.id);
        binding.recyclerCategories.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.recyclerCategories.setAdapter(adapter);
        viewModel.getAllCategories().observe(getViewLifecycleOwner(), adapter::setItems);

        updateDateButton();
        binding.buttonDate.setOnClickListener(v -> showDatePicker());
        binding.buttonSave.setOnClickListener(v -> save());

        if (editing != null) {
            binding.textTitle.setText("Edit expense");
            binding.buttonSave.setText("Save changes");
            binding.editAmount.setText(MoneyUtils.format(editing.amount));
            binding.editNote.setText(editing.note);
            adapter.setSelectedId(editing.categoryId);
            updateDateButton();

            binding.buttonDelete.setVisibility(View.VISIBLE);
            binding.buttonDelete.setOnClickListener(v -> confirmDelete());
        }
    }

    private Expense editing; // null = adding a new expense, not null = editing

    public static AddExpenseFragment newInstance(Expense e) {
        Bundle b = new Bundle();
        b.putInt("id", e.id);
        b.putLong("amount", e.amount);
        b.putInt("categoryId", e.categoryId);
        b.putLong("date", e.date);
        b.putString("note", e.note);
        AddExpenseFragment f = new AddExpenseFragment();
        f.setArguments(b);
        return f;
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

        String finalNote = note.isEmpty() ? null : note;
        if (editing == null) {
            viewModel.addExpense(amount, selectedCategoryId, selectedDate, finalNote);
        } else {
            Expense updated = new Expense();
            updated.id = editing.id; // same id = "change this row", not "add a new one"
            updated.amount = amount;
            updated.categoryId = selectedCategoryId;
            updated.date = selectedDate;
            updated.note = finalNote;
            viewModel.updateExpense(updated);
        }
        getParentFragmentManager().popBackStack(); // go back to the list
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void confirmDelete() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete expense?")
                .setMessage("This can't be undone.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteExpense(editing);
                    getParentFragmentManager().popBackStack();
                })
                .show();
    }
}
