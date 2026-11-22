package com.ihor.expensetracker;

import static org.junit.Assert.assertEquals;

import com.ihor.expensetracker.data.Expense;
import com.ihor.expensetracker.util.MoneyUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoneyUtilsTest {

    private Expense expense(long amount) {
        Expense e = new Expense();
        e.amount = amount;
        return e;
    }

    @Test
    public void sum_addsAllAmounts() {
        List<Expense> list = Arrays.asList(expense(1000), expense(500), expense(50));
        assertEquals(1550, MoneyUtils.sum(list));
    }

    @Test
    public void sum_emptyList_isZero() {
        assertEquals(0, MoneyUtils.sum(new ArrayList<>()));
    }

    @Test
    public void format_alwaysShowsTwoDecimals() {
        assertEquals("10.50", MoneyUtils.format(1050));
        assertEquals("0.05", MoneyUtils.format(5));
        assertEquals("12.00", MoneyUtils.format(1200));
    }

    @Test
    public void parse_handlesDotCommaAndInvalid() {
        assertEquals(1050, MoneyUtils.parse("10.50"));
        assertEquals(1050, MoneyUtils.parse("10,5"));
        assertEquals(500, MoneyUtils.parse("5"));
        assertEquals(-1, MoneyUtils.parse("abc"));
    }
}
