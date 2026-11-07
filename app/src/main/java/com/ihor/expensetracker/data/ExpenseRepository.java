package com.ihor.expensetracker.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ExpenseRepository {

    private final ExpenseDAO expenseDao;
    private final CategoryDAO categoryDao;
    private final BudgetDAO budgetDao;
    private final SettingsDAO settingsDao;
    private final ExecutorService executor;

    public ExpenseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        expenseDao = db.expenseDao();
        categoryDao = db.categoryDao();
        budgetDao = db.budgetDao();
        settingsDao = db.settingsDao();
        executor = Executors.newFixedThreadPool(4);
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return expenseDao.getAllExpenses();
    }

    public void insertExpense(Expense expense) {
        executor.execute(() -> expenseDao.insert(expense));
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public void insertCategory(Category category) {
        executor.execute(() -> categoryDao.insert(category));
    }

    public void updateExpense(Expense expense) {
        executor.execute(() -> expenseDao.update(expense));
    }

    public void deleteExpense(Expense expense) {
        executor.execute(() -> expenseDao.delete(expense));
    }

    public LiveData<Long> getTotalForRange(long start, long end) {
        return expenseDao.getTotalForRange(start, end);
    }
}
