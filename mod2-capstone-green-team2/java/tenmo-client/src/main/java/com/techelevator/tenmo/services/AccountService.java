package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class AccountService {

    private String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    private String authToken = null;

    public AccountService(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Balance getBalance(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        Balance balance = restTemplate.exchange(BASE_URL + "/balance", HttpMethod.GET, entity, Balance.class).getBody();
        return balance;

    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    User[] users = null;

    public User[] listOfUsers(String token) {
        // User[] users = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<User[]> response = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, entity, User[].class);
        users = response.getBody();
        return users;
    }

    public void transferMoney(TransferUser sendee, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(sendee, headers);
        restTemplate.postForObject(BASE_URL + "/transfer/" + sendee.getSenderId(), entity, Void.class);
    }



//    public Transfer[] findByAccountId(String token) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(currentUser.getToken());
//        HttpEntity entity = new HttpEntity(headers);
//
//        int userId = currentUser.getUser().getId();
//        int accountId = restTemplate.getForObject(BASE_URL + "/users/" + userId, Integer.class);
//        ResponseEntity<Transfer[]> response = restTemplate.getForEntity(BASE_URL + "/transfers/" + accountId, Transfer[].class);
//        Transfer[] transfers = response.getBody();
//        return transfers;
//
//    }

    private Transfer returnDetailsBasedOnId() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity(headers);

        int transferId = currentUser.getUser().getId();
//        System.out.println(transferId);
        Transfer transfer = restTemplate.exchange(BASE_URL + "/transfers/" + transferId,
                HttpMethod.GET, entity, Transfer.class).getBody();
        return transfer;
    }



}

