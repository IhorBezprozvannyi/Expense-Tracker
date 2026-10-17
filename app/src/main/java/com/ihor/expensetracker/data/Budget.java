package com.ihor.expensetracker.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity (
        tableName = "budgets",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "categoryId"
        )
)
public class Budget {
    @PrimaryKey (autoGenerate = true)
    public int id;

    public int categoryId;
    public long limit;
}
