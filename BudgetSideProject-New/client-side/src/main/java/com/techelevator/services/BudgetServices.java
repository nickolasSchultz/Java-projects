package com.techelevator.services;

import com.techelevator.model.Budget;
import org.springframework.web.client.RestTemplate;


public class BudgetServices {

    private String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public BudgetServices(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public Budget getSavings (){
      //  HttpHeaders headers = new HttpHeaders();
      //  HttpEntity entity = new HttpEntity(headers);
       Budget budget = restTemplate.getForObject(BASE_URL + "/savings" ,Budget.class);
       return budget;

    }
}
