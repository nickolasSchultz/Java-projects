package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferUser;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {
@Autowired
     AccountDao accountDao;
@Autowired
     UserDao userDao;

//    public TenmoController(AccountDao accountDao, UserDao userDao) {
//        this.accountDao = accountDao;
//        this.userDao = userDao;
//    }

    @GetMapping("/balance")
    public Balance getAccountBalance(Principal principal){
        String user = principal.getName();
        return accountDao.getAccountBalance(user);
    }
    @GetMapping("/users")
    public List<User> getListOfUser(Principal principal){
        List<User> allUsers = userDao.findAll(principal.getName());
        return allUsers;
    }

    @PostMapping("/transfer/{id}")
    public void transferMoney(@RequestBody TransferUser transferUser, Principal principal){
        int senderAccountId = accountDao.getByUserId(transferUser.getSenderId()).getAccountId();
        int receiverAccountId = accountDao.getByUserId(transferUser.getReceiverId()).getAccountId();
        TransferUser transferUser1 = new TransferUser(senderAccountId,receiverAccountId,transferUser.getAmountTo());


        accountDao.transferMoney(transferUser1);
    }

    @GetMapping("/transfers/{userId}")
    public Transfer[] findByUserId(@PathVariable int userId){

        return accountDao.findByUserId(userId);
    }

    @GetMapping("/transfer/{transferId}")
    public Transfer returnDetailsBasedOnId(@PathVariable int transferId) {
        return accountDao.returnDetailsBasedOnId(transferId);
    }
}
