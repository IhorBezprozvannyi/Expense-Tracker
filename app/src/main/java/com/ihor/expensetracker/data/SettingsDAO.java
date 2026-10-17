package com.ihor.expensetracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Upsert;

@Dao
public interface SettingsDAO {

    @Upsert
    void save(Settings settings);

    @Query("SELECT * FROM settings WHERE id = 1")
    LiveData<Settings> getSettings();
}
