package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;

import java.math.BigDecimal;
import java.security.Principal;

public interface AccountDao {

     Balance getAccountBalance(String user);

     Account getByUserId(int userId);

     void transferMoney(TransferUser transferUser);
  //   void addReceiverMoney(TransferUser transferUser);

     Transfer[] findByUserId(int userId);

     Transfer returnDetailsBasedOnId(int transferId);

}
