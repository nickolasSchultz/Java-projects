package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {
    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private JdbcTemplate jdbcTemplate;
    private JdbcUserDao jdbcUserDao;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RestTemplate restTemplate = new RestTemplate();

    //    @Override
//    public BigDecimal getAccountBalance(Principal principal) {
//      //sql statement to get account balance from the principal/accountId
//        String sql = "SELECT balance  FROM accounts a JOIN users u ON a.user_id = u.user_id WHERE a.user_id = ?;";
//
//        // from the principal, you get the user name.
//
//        // with the user name you can go to the JdbcUserDao and call a method to exchange
//        // that for an id
//
//        // you can plug that id into the code below
//
//        //jdbcUserDao.findIdByUsername(principal.getName());
//        BigDecimal balance = jdbcTemplate.queryForObject(sql,BigDecimal.class,
//                jdbcUserDao.findIdByUsername(principal.getName()));
//        return balance;
//    }
    public Balance getAccountBalance(String user) {
        Balance balance = new Balance();
        String sql = "SELECT balance FROM accounts JOIN users ON accounts.user_id = users.user_id WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user);

        if (results.next()) {
            double newBalance = results.getDouble("balance");
            BigDecimal newBal = new BigDecimal(newBalance);

            balance.setBalance(newBal);
        }
        return balance;
    }

    @Override
    public Account getByUserId(int userId) {
        Account account = new Account();
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        if (results.next()) {
            BigDecimal newBalance = results.getBigDecimal("balance");
            int accountId = results.getInt("account_id");
            int userId1 = results.getInt("user_id");
            account.setAccountId(accountId);
            account.setUserId(userId1);
            account.setAccountBalance(newBalance);
        }
        return account;
    }

    public Transfer transferTable(TransferUser transferUser) {
        // Inserting into the transfer table
        Transfer transfer = new Transfer();

        try {
            String sql4 = "INSERT INTO transfers (transfer_type_id, transfer_status_id,account_from, account_to, amount) " +
                    "VALUES(2,2,?,?,?) RETURNING transfer_id; ";
            int transferId = jdbcTemplate.queryForObject(sql4, Integer.class, transferUser.getSenderId(), transferUser.getReceiverId(), transferUser.getAmountTo());

            String sql5 = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                    "FROM transfers " +
                    "WHERE transfer_id = ? ;";
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql5,transferId);
            if(result.next()){
                int transferTypeId = result.getInt("transfer_type_id");
                int transferStatusId = result.getInt("transfer_status_id");
                int accountFrom = result.getInt("account_from");
                int accountTo = result.getInt("account_to");
                BigDecimal amount = result.getBigDecimal("amount");

                transfer.setTransferId(transferId);
                transfer.setTransferStatusId(transferStatusId);
                transfer.setAccountFrom(accountFrom);
                transfer.setAccountTo(accountTo);
                transfer.setAmount(amount);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return transfer;

    }

    public void updateDeniedTable(Transfer transfer) {

        String sql5 = "UPDATE transfers SET transfer_status_id = 3 WHERE transfer_id =?;";
        jdbcTemplate.update(sql5, transfer.getTransferId());

    }

    @Override
    public void transferMoney(TransferUser transferUser) {
        Balance balance = new Balance();
        System.out.println(transferUser);
        Balance senderBalance = new Balance();
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferUser.getSenderId());


        if (result.next()) {
            double newBalance = result.getDouble("balance");
            BigDecimal newBigBalance = new BigDecimal(newBalance);
            senderBalance.setBalance(newBigBalance);
        }
        Balance receiverBalance = new Balance();
        String sql1 = "SELECT balance FROM accounts WHERE account_id = ?";
        SqlRowSet resultReceived = jdbcTemplate.queryForRowSet(sql1, transferUser.getReceiverId());

        if (resultReceived.next()) {
            double newBalance1 = resultReceived.getDouble("balance");
            BigDecimal newBigBalance1 = new BigDecimal(newBalance1);
            receiverBalance.setBalance(newBigBalance1);
        }
        Transfer transfer = transferTable(transferUser);
        // taking amountTo out of sender account
        if (senderBalance.getBalance().compareTo(transferUser.getAmountTo()) >= 0) {

            BigDecimal updatedSenderMoney = senderBalance.getBalance().subtract(transferUser.getAmountTo());
            balance.setBalance(updatedSenderMoney);
            String sql2 = "UPDATE accounts SET balance = ? WHERE account_id = ?";
            jdbcTemplate.update(sql2, balance.getBalance(), transferUser.getSenderId());

            // taking amountTo and adding it to receiver account
            BigDecimal updatedReceiverMoney = receiverBalance.getBalance().add(transferUser.getAmountTo());
            balance.setBalance(updatedReceiverMoney);
            String sql3 = "UPDATE accounts SET balance = ? WHERE account_id = ?";
            jdbcTemplate.update(sql3, balance.getBalance(), transferUser.getReceiverId());
        } else
            updateDeniedTable(transfer);
        System.out.println("Transfer denied");


//        if (senderBalance.getBalance().compareTo(transferUser.getAmountTo()) >= 0){
//
//        }

    }


    // get all transfers by account id
    @Override
    public Transfer[] findByUserId(int userId){
        List<Transfer> transfersList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers WHERE account_from = (SELECT account_id FROM accounts WHERE user_id = ?) " +
        "OR account_to = (SELECT account_id FROM accounts WHERE user_id = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);

        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transfersList.add(transfer);
        }
        Transfer[] transferArray = new Transfer[transfersList.size()];
        transfersList.toArray(transferArray);
        return transferArray;
    }

    // retrieve the details of any transfer based upon the transfer ID
    @Override
    public Transfer returnDetailsBasedOnId(int transferId){
        Transfer transfer = new Transfer();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
                "WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        while (results.next()) {
            transfer = mapRowToTransfer(results);
        } return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }

}
//    public void addReceiverMoney(TransferUser transferUser) {
//        Balance receiverBalance = new Balance();
//        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
//        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferUser.getReceiverId());
//
//        if (result.next()){
//            double newBalance = result.getDouble("balance");
//            BigDecimal newBigBalance = new BigDecimal(newBalance);
//            receiverBalance.setBalance(newBigBalance);
//        }
//    }