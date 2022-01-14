package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.view.ConsoleService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;

public class App {



    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = {LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};
    private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
    private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
    private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
    private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};

    private static final String API_BASE_URL = "http://localhost:8080/";
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL));
        app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService) {
        this.console = console;
        this.authenticationService = authenticationService;
        this.accountService = accountService;
    }

    public void run() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewCurrentBalance();
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewTransferHistory();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                viewPendingRequests();
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                sendBucks();
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                requestBucks();
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }

    private void viewCurrentBalance() {
        Balance balance = accountService.getBalance(currentUser.getToken());

        System.out.println("Your current account balance is: " + balance.getBalance());


    }

    private void viewTransferHistory() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity(headers);

        int userId = currentUser.getUser().getId();

      //  int accountId = restTemplate.getForObject(API_BASE_URL + "/users/" + userId, Integer.class);
        ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfers/" + userId, HttpMethod.GET, entity, Transfer[].class);
        Transfer[] transfers = response.getBody();
       System.out.println("Transfer History");
        System.out.println();
        TransferUser transferUser = new TransferUser();
        // loop through the transfers
        User user = new User();
          for(Transfer transfer: transfers) {
            System.out.println("Transfer Id: "+ transfer.getTransferId() + " Sender: " + transfer.getAccountFrom()  +" Receiver: " + transfer.getAccountTo());
        }
        returnDetailsBasedOnId( console.getUserInputInteger("Select the Transfer Id to see transfer details or select 0 to return to menu "), currentUser.getToken());

        }





    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        Balance balance = accountService.getBalance(currentUser.getToken());
        User[] users = accountService.listOfUsers(currentUser.getToken());

        console.displayUsers(users);

        int sendToId = console.getUserInputInteger("Please select a user_id to send TE bucks to");
        int amount = console.getUserInputInteger("Please insert amount to transfer (whole numbers) ");
        TransferUser transferUser = new TransferUser();
        transferUser.setSenderId(currentUser.getUser().getId());
        transferUser.setReceiverId(sendToId);
        transferUser.setAmountTo(BigDecimal.valueOf(amount));
        accountService.transferMoney(transferUser, currentUser.getToken());

        viewCurrentBalance();
        if(balance.getBalance().equals(accountService.getBalance(currentUser.getToken()).getBalance())){
            System.out.println("Transaction denied");
        }

        //gotta add check for status


    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }
    // return info based on transfer id
    private void returnDetailsBasedOnId(int transferId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(headers);


//        System.out.println(transferId);
        Transfer response = restTemplate.exchange(API_BASE_URL + "/transfer/" + transferId,
                HttpMethod.GET, entity, Transfer.class).getBody();
        Transfer transfers = response;
        System.out.println("Transfer details");
        System.out.println();

        // loop through the transfers
        User user = new User();
        System.out.println("Transfer Id: " + transfers.getTransferId());
        System.out.println("Sender Id: " + transfers.getAccountFrom());
        System.out.println("Receiver Id: " + transfers.getAccountTo());
        System.out.println("Transfer type: " + typeIntToString(transfers.getTransferTypeId()));
        System.out.println("Status Id: " + statusIntToString(transfers.getTransferStatusId()));
        System.out.println("Amount: $" + transfers.getAmount() );
    }

    private String statusIntToString (int statusCode){
        if(statusCode == 1){
            return "Pending";
        }
        if(statusCode == 2){
            return "Approved";
        }
        if(statusCode == 3){
            return "Rejected";
        }
        else{
            return "Something went terribly wrong";
        }
    }
    private String typeIntToString (int statusCode){
        if(statusCode == 1){
            return "Request";
        }
        if (statusCode == 2){
            return "Send";
        }
        else{
            return "Something went terribly wrong";
        }
    }

    private void exitProgram() {
        System.exit(0);
    }

    private void registerAndLogin() {
        while (!isAuthenticated()) {
            String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
            if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
                register();
            } else {
                // the only other option on the login menu is to exit
                exitProgram();
            }
        }
    }

    private boolean isAuthenticated() {
        return currentUser != null;
    }

    private void register() {
        System.out.println("Please register a new user account");
        boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                authenticationService.register(credentials);
                isRegistered = true;
                System.out.println("Registration successful. You can now login.");
            } catch (AuthenticationServiceException e) {
                System.out.println("REGISTRATION ERROR: " + e.getMessage());
                System.out.println("Please attempt to register again.");
            }
        }
    }

    private void login() {
        System.out.println("Please log in");
        currentUser = null;
        while (currentUser == null) //will keep looping until user is logged in
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                currentUser = authenticationService.login(credentials);
            } catch (AuthenticationServiceException e) {
                System.out.println("LOGIN ERROR: " + e.getMessage());
                System.out.println("Please attempt to login again.");
            }
        }
    }

    private UserCredentials collectUserCredentials() {
        String username = console.getUserInput("Username");
        String password = console.getUserInput("Password");
        return new UserCredentials(username, password);
    }
}
