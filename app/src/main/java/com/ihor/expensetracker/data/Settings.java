package com.ihor.expensetracker.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings")
public class Settings {

    @PrimaryKey
    public int id = 1;

    public long startingBalance;
}
