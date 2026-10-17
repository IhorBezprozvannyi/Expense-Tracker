package com.ihor.expensetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BudgetDAO{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Budget budget);

    @Query("SELECT * FROM budgets")
    LiveData<List<Budget>> getAllBudgets();
}
