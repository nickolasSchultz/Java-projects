package com.techelevator.controller;


import com.techelevator.dao.BudgetDao;
import com.techelevator.model.Budget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController

public class BudgetController {
@Autowired
BudgetDao budgetDao;
    //uses the interface to grab my getSavings method and then maps it to the get method for the URL
    @GetMapping("/savings")
    public Budget getSavings(){
    return budgetDao.getSavings();
}
    //uses the interface to grab my getSavings method and then maps it to the get method for the URL
    @GetMapping("/savingsGoals")
    public Budget getSavingsGoals(){
        return budgetDao.getSavingsGoals();
    }
    @GetMapping("/weeklyBudget")
    public Budget getWeeklyBudget(){
        return budgetDao.getWeeklyBudget();
    }
    @PostMapping("/spendMoney")
    public void spendMoney(@RequestBody Budget budget){
        BigDecimal amount = budget.getMoneySpent();

        budgetDao.spendFunction(amount);
    }
    @PostMapping("/earnMoney")
    public void earnMoney(@RequestBody Budget budget){
        BigDecimal amount = budget.getMoneyEarned();

        budgetDao.addFunction(amount);
    }

}
