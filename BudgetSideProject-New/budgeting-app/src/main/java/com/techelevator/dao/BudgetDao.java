package com.techelevator.dao;

import com.techelevator.model.Budget;

import java.math.BigDecimal;

public interface BudgetDao {
        Budget getSavings();  // gets my savings
        Budget getWeeklyBudget(); // gets my weekly budget
        Budget getSavingsGoals(); // gets my savings goals
        void spendFunction(BigDecimal amount); // pulls from weekly/bulk savings, adds a spend element
        void addFunction(BigDecimal amount); // adds to savings, adds to made earned money
        Budget getTransactionHistory(); // pulls a list of transaction history between entered dates
}
