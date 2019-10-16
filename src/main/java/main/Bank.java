package main;

import models.Account;
import models.AccountType;
import models.User;
import utils.InputValidation;
import views.BankUI;

public class Bank {
	private User currentUser;
	private Account currentAccount;
	private static Bank bank_instance = null;

	private Bank() {}
	
	public static Bank getInstance() {
		if (bank_instance == null) {
			bank_instance = new Bank();
		}
		
		return bank_instance;
	}
	
	// Make custom exceptions for invalid inputs
	public void start() {
		boolean running = true;

		while (running) {
			int reply = BankUI.mainMenu();

			switch (reply) {
			case 1:
				createUser();
				break;
			case 2:
				login();
				break;
			case 3:
				System.out.println("Exiting app");
				running = false;
				break;
			default:
				System.out.println("Please select a valid number!");
			}
		}
	}

	private void userActions() {
		boolean running = true;

		while (running) {
			int reply = BankUI.loggedInMenu();

			switch (reply) {
			case 1:
				createAccount(AccountType.CHECKING);
				break;
			case 2:
				createAccount(AccountType.SAVINGS);
				break;
			case 3:
				viewAccount();
				break;
			case 4:
				System.out.println("Logging out...");
				running = false;
				break;
			default:
				System.out.println("Please select a valid number!");
			}
		}
	}

	private void createUser() {
		String username = "";
		String password = "";
		
		// Loop until username input is valid
		while (true) {
			username = BankUI.promptUsername();
			if (InputValidation.isValidUsername(username)) {
				break;
			}
		}
		
		// Loop until password input is valid
		while (true) {
			password = BankUI.promptPassword();
			if (InputValidation.isValidPassword(password)) {
				break;
			}
		}

		// Create new user in database
		User newUser = new User(username, password);
		System.out.println(newUser.toString());
	}

	private void login() {
		String username = BankUI.promptUsername();
		String password = BankUI.promptPassword();

		currentUser = new User(username, password);
		System.out.println(currentUser.toString());
		userActions();
	}

	private void createAccount(AccountType at) {
		Account newAcc = new Account(at);
		
		// Loop until title input is valid
		while (true) {
			String title = BankUI.promptTitle();
			if (InputValidation.isValidTitle(title)) {
				break;
			}
		}
	}

	private void viewAccount() {
		// Give list of accounts that contain user
	}

	private void deposit() {
		double amount = BankUI.promptDeposit();
		if (InputValidation.isAmountGreaterThanZero(amount) &&
				InputValidation.isAmountWithinBalance(amount, currentAccount.getBalance())) {
				
		}
	}
	
	private void withdraw() {
		double amount = BankUI.promptWithdraw();
		if(InputValidation.isAmountGreaterThanZero(amount)) {
			
		}
	}
}
