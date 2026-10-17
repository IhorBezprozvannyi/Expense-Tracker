package com.ihor.expensetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Insert
    void insert(Category category);

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategories();
}
