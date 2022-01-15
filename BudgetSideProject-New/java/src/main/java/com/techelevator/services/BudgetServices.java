package com.techelevator.services;

import com.techelevator.model.Budget;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


public class BudgetServices {

    private String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public BudgetServices(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public Budget getSavings (){
    // Because it uses a restful api, getSavings takes the http response and puts the json body into a budget object.
       Budget budget = restTemplate.getForObject(BASE_URL + "/savings" ,Budget.class);
       return budget;

    }
    public Budget getSavingsGoals(){
        // Because it uses a restful api, getSavings takes the http response and puts the json body into a budget object.
        Budget budget = restTemplate.getForObject(BASE_URL + "/savingsGoals", Budget.class);
        return budget;
    }
    public Budget getWeeklyBudget(){
        // Because it uses a restful api, getSavings takes the http response and puts the json body into a budget object.
        Budget budget = restTemplate.getForObject(BASE_URL + "/weeklyBudget", Budget.class);
        return budget;
    }
    public void spendMoney(Budget budget){
        // Make a header and add entity to it to post a new transaction
        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(budget, headers);
        restTemplate.postForObject(BASE_URL + "/spendMoney", entity, Budget.class );


    }
    public void earnMoney(Budget budget){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(budget, headers);
        restTemplate.postForObject(BASE_URL + "/earnMoney", entity, Budget.class );

    }
}
