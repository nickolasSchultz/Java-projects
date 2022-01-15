package com.techelevator;

import com.techelevator.model.Budget;
import com.techelevator.services.BudgetServices;
import com.techelevator.view.ConsoleService;
import org.springframework.web.client.RestTemplate;


public class APP {


    private static final String MAIN_MENU_OPTION_EDIT = "EDIT";
    private static final String MAIN_MENU_OPTION_DID_YOU_MAKE_ANY_MONEY = "Did you make any money?";
    private static final String MAIN_MENU_OPTION_DID_YOU_SPEND = "Did you spend any money today?";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_EDIT, MAIN_MENU_OPTION_DID_YOU_MAKE_ANY_MONEY,MAIN_MENU_OPTION_DID_YOU_SPEND};
    private static final String API_BASE_URL = "http://localhost:8080";

    private BudgetServices budgetServices;
    private ConsoleService consoleService;
    private RestTemplate restTemplate = new RestTemplate();

    public APP(BudgetServices budgetServices, ConsoleService consoleService) {
        this.budgetServices = budgetServices;
        this.consoleService = consoleService;
    }

    public static void main(String[] args) {
        APP app = new APP( new BudgetServices(API_BASE_URL), new ConsoleService(System.in, System.out) );
        app.run();
    }
    public void run(){
        System.out.println("BUDGETING TIME!!!!");
        System.out.println();
        System.out.println();
        mainMenu();
    }
    private void mainMenu(){
        //print out savings weekly budget, and goals
        viewSavings();

    }

    private void viewSavings(){
            Budget budget = budgetServices.getSavings();
        System.out.println("Savings: "+budget);
    }
}
