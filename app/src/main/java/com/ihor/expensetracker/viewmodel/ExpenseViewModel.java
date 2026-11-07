package com.ihor.expensetracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ihor.expensetracker.data.Category;
import com.ihor.expensetracker.data.Expense;
import com.ihor.expensetracker.data.ExpenseRepository;
import com.ihor.expensetracker.util.DateUtils;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private final ExpenseRepository repository;
    private final LiveData<List<Expense>> allExpenses;
    private final LiveData<List<Category>> allCategories;
    private final LiveData<Long> monthTotal;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
        allCategories = repository.getAllCategories();

        long now = System.currentTimeMillis();
        monthTotal = repository.getTotalForRange(
                DateUtils.startOfMonth(now), DateUtils.endOfMonth(now));
    }

    public LiveData<List<Expense>> getAllExpenses() { return allExpenses; }
    public LiveData<List<Category>> getAllCategories() { return allCategories; }

    /** Total spent this month in cents. Emits null when there are no expenses. */
    public LiveData<Long> getMonthTotal() { return monthTotal; }

    public void addExpense(long amount, int categoryId, long date, String note) {
        Expense e = new Expense();
        e.amount = amount;
        e.categoryId = categoryId;
        e.date = date;
        e.note = note;
        repository.insertExpense(e);
    }

    public void updateExpense(Expense expense) { repository.updateExpense(expense); }
    public void deleteExpense(Expense expense) { repository.deleteExpense(expense); }
}
