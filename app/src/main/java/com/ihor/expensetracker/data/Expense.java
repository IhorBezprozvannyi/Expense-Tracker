package com.ihor.expensetracker.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(
        tableName = "expenses",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "categoryId"
        )
)
public class Expense {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public long amount;
    public int categoryId;
    public long date;
    public String note;
}
