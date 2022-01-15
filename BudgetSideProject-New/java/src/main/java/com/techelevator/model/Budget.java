package com.techelevator.model;


import java.math.BigDecimal;
import java.util.Date;

public class Budget {
    private int transactionId;
    private Date transactionDate;
    private BigDecimal savings; //How much money I have
    private BigDecimal moneySpent; //How much I spent
    private BigDecimal moneyEarned; // How much I earned
    private BigDecimal weeklyBudget; //How much I have to use this week
    private BigDecimal savingGoals; // How much I want to save this month

    public Budget(int transactionId, Date transactionDate, BigDecimal savings, BigDecimal moneySpent, BigDecimal moneyEarned, BigDecimal weeklyBudget, BigDecimal savingGoals) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.savings = savings;
        this.moneySpent = moneySpent;
        this.moneyEarned = moneyEarned;
        this.weeklyBudget = weeklyBudget;
        this.savingGoals = savingGoals;
    }

    public Budget() {

    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getSavings() {
        return savings;
    }

    public void setSavings(BigDecimal savings) {
        this.savings = savings;
    }

    public BigDecimal getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(BigDecimal moneySpent) {
        this.moneySpent = moneySpent;
    }

    public BigDecimal getMoneyEarned() {
        return moneyEarned;
    }

    public void setMoneyEarned(BigDecimal moneyEarned) {
        this.moneyEarned = moneyEarned;
    }

    public BigDecimal getWeeklyBudget() {
        return weeklyBudget;
    }

    public void setWeeklyBudget(BigDecimal weeklyBudget) {
        this.weeklyBudget = weeklyBudget;
    }

    public BigDecimal getSavingGoals() {
        return savingGoals;
    }

    public void setSavingGoals(BigDecimal savingGoals) {
        this.savingGoals = savingGoals;
    }
}
