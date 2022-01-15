package com.techelevator.dao;

import com.techelevator.model.Budget;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class JdbcBudgetDao implements BudgetDao{
    private JdbcTemplate jdbcTemplate;
    public JdbcBudgetDao(JdbcTemplate jdbcTemplate){this.jdbcTemplate = jdbcTemplate;}
    RestTemplate restTemplate = new RestTemplate();

    @Override
    public Budget getSavings() {
        // creates a budget object, queries for my select statement, puts the data in the budget object, and returns it
        Budget budget = new Budget();
        String sql = "SELECT savings FROM budget WHERE transaction_id = ( SELECT MAX (transaction_id) FROM budget);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        if(results.next()){
            BigDecimal newBudget = results.getBigDecimal("savings");
            budget.setSavings(newBudget);
        }
        System.out.println(budget);
        return budget;
    }

    @Override
    public Budget getWeeklyBudget() {
// creates a budget object, queries for my select statement to get savings_goals, puts the data in the budget object, and returns it
        Budget budget = new Budget();
        String sql = "SELECT weekly_budget FROM budget WHERE transaction_id = ( SELECT MAX (transaction_id) FROM budget);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        if(results.next()){
            BigDecimal newBudget = results.getBigDecimal("weekly_budget");
            budget.setWeeklyBudget(newBudget);
        }

        return budget;
    }
    @Override
    public Budget getSavingsGoals() {
        // creates a budget object, queries for my select statement to get savings_goals, puts the data in the budget object, and returns it
        Budget budget = new Budget();
        String sql = "SELECT savings_goals FROM budget WHERE transaction_id = ( SELECT MAX (transaction_id) FROM budget);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        if(results.next()){
            BigDecimal newBudget = results.getBigDecimal("savings_goals");
            budget.setSavingGoals(newBudget);
        }

        return budget;
    }

    @Override
    public void spendFunction(BigDecimal amount) {
        //First I need to grab my table elements that need to be passed down

        BigDecimal savings = null ;
        BigDecimal weeklyBudget = null;
        BigDecimal savingsGoals = null;

        String sql = "SELECT * FROM budget WHERE transaction_id = ( SELECT MAX (transaction_id) FROM budget);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        if(results.next()){
             savings = results.getBigDecimal("savings");
             weeklyBudget = results.getBigDecimal("weekly_budget");
             savingsGoals = results.getBigDecimal("savings_goals");

        }
        //Next I need to fill a budget object with needed values
        Budget budget = new Budget();
        try {
            String sqlTwo = "INSERT INTO budget (money_spent, savings, transaction_date, weekly_budget,savings_goals)"
                    + "VALUES(?,?, current_timestamp,?,?) RETURNING transaction_id";
            int transferId = jdbcTemplate.queryForObject(sqlTwo, Integer.class, amount, savings.subtract(amount), weeklyBudget.subtract(amount), savingsGoals);
        }catch (Exception ex){
            System.out.println(ex.toString());
        }



    }

    @Override
    public void addFunction(BigDecimal amount) {

        BigDecimal savings = null ;
        BigDecimal weeklyBudget = null;
        BigDecimal savingsGoals = null;

        String sql = "SELECT * FROM budget WHERE transaction_id = ( SELECT MAX (transaction_id) FROM budget);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        if(results.next()){
            savings = results.getBigDecimal("savings");
            weeklyBudget = results.getBigDecimal("weekly_budget");
            savingsGoals = results.getBigDecimal("savings_goals");

        }
        //Next I need to fill a budget object with needed values
        Budget budget = new Budget();
        try {
            String sqlTwo = "INSERT INTO budget (money_earned, savings, transaction_date, weekly_budget,savings_goals)"
                    + "VALUES(?,?, current_timestamp,?,?) RETURNING transaction_id";
            int transferId = jdbcTemplate.queryForObject(sqlTwo, Integer.class, amount, savings.add(amount), weeklyBudget.add(amount), savingsGoals);
        }catch (Exception ex){
            System.out.println(ex.toString());
        }



    }

    @Override
    public Budget getTransactionHistory() {
        return null;
    }
}
