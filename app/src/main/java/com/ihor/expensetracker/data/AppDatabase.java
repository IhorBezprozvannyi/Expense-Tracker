package com.ihor.expensetracker.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                    ).addCallback(SEED_CALLBACK).build();
                }
            }
        }
        return INSTANCE;
    }
    private static final RoomDatabase.Callback SEED_CALLBACK = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            String[][] defaults = {
                    {"Dining", "ic_dining"},
                    {"Groceries", "ic_groceries"},
                    {"Transport", "ic_transport"},
                    {"Shopping", "ic_shopping"},
                    {"Home", "ic_home"},
                    {"Health", "ic_health"},
                    {"Bills", "ic_bills"},
                    {"More", "ic_more"}
            };
            for (String[] c : defaults) {
                db.execSQL("INSERT INTO categories (name, icon) VALUES (?, ?)",
                        new Object[]{c[0], c[1]});
            }
        }
    };

}
