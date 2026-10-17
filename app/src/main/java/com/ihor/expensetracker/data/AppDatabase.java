package com.ihor.expensetracker.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Category.class, Expense.class, Budget.class, Settings.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDAO categoryDao();
    public abstract ExpenseDAO expenseDao();
    public abstract BudgetDAO budgetDao();
    public abstract SettingsDAO settingsDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "expense_tracker_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
