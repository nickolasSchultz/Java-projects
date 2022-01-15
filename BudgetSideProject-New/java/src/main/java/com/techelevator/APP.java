package com.techelevator;

import com.techelevator.model.Budget;
import com.techelevator.services.BudgetServices;
import com.techelevator.view.ConsoleService;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


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
    private void mainMenu() {
        while (true) {
            display();
            String choice = (String) consoleService.getChoiceFromOptions(MAIN_MENU_OPTIONS);

            if (MAIN_MENU_OPTION_DID_YOU_MAKE_ANY_MONEY.equals(choice)) {
                earnMoney();
            } else if (MAIN_MENU_OPTION_DID_YOU_SPEND.equals(choice)) {
                spendMoney();
            } else if (MAIN_MENU_OPTION_EDIT.equals(choice)) {

            }  else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }
    private void display(){
        //print out savings, weekly budget, and goals
        viewSavings();
        viewSavingsGoals();
        viewWeeklyBudget();
    }

    private void viewSavings(){
        //takes the budget object and returns the specified property
            Budget budget = budgetServices.getSavings();
        System.out.println("Savings: $"+ budget.getSavings());
    }
    private void viewSavingsGoals(){
        //takes the budget object and returns the specified property
        Budget budget = budgetServices.getSavingsGoals();
        System.out.println("Savings goal: $"+ budget.getSavingGoals());
    }
    private void viewWeeklyBudget(){
        //takes the budget object and returns the specified property
        Budget budget = budgetServices.getWeeklyBudget();
        System.out.println("Weekly Budget: $"+ budget.getWeeklyBudget());
    }
    private void spendMoney(){
        //Takes user input puts it in a budget object and sends it to budget services
        int amount = consoleService.getUserInputInteger("How much money did you spend?");
        Budget budget = new Budget();
        budget.setMoneySpent(BigDecimal.valueOf(amount));
        budgetServices.spendMoney(budget);
    }
    private void earnMoney(){
        //Takes user input puts it in a budget object and sends it to budget services
        int amount = consoleService.getUserInputInteger("How much money did you earn?");
        Budget budget = new Budget();
        budget.setMoneyEarned(BigDecimal.valueOf(amount));
        budgetServices.earnMoney(budget);
    }

    private void exitProgram() {
        System.exit(0);
    }
}
