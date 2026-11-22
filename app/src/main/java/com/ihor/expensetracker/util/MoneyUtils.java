package com.ihor.expensetracker.util;

import com.ihor.expensetracker.data.Expense;

import java.math.BigDecimal;
import java.util.List;
import java.math.RoundingMode;

public class MoneyUtils {

    public static long sum(List<Expense> expenses) {
        long total = 0;
        for (Expense e : expenses) {
            total += e.amount;
        }
        return total;
    }

    public static long parse(String text) {
        if (text == null) return -1;
        String t = text.trim().replace(',', '.');
        if (t.isEmpty()) return -1;
        try {
            return new BigDecimal(t)
                    .movePointRight(2)
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValueExact();
        } catch (ArithmeticException | NumberFormatException e) {
            return -1;
        }
    }

    public static String format(long cents) {
        return BigDecimal.valueOf(cents, 2).toPlainString();
    }
}
