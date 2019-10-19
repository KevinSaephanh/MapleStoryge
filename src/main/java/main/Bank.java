package main;

import java.math.BigDecimal;
import java.util.List;

import dao.AccountDao;
import dao.UserDao;
import exceptions.EmptyTableException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import models.Account;
import models.AccountType;
import models.User;
import utils.InputValidation;
import views.BankUI;

public class Bank {
	private User currentUser;
	private Account currentAccount;
	private UserDao userDao;
	private AccountDao accountDao;
	private static Bank bank_instance = null;

	private Bank() {
		userDao = new UserDao();
		accountDao = new AccountDao();
	}

	public static Bank getInstance() {
		if (bank_instance == null) {
			bank_instance = new Bank();
		}

		return bank_instance;
	}

	// Make custom exceptions for invalid inputs
	public void start() {
		while (true) {

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
				return;
			}
		}
	}

	private void userActions() {
		while (true) {
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
				updateUser();
				break;
			case 5:
				deleteUser(currentUser);
				return;
			case 6:
				return;
			}
		}
	}
	
	private void login() {
		String username = BankUI.promptUsername();
		String password = BankUI.promptPassword();

		// Check database for matching user input
		currentUser = new User(username, password);
		try {
			currentUser = userDao.getUser(username, password);
			System.out.println("\n\nYou are logged in");
			userActions();
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
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
		UserDao ud = new UserDao();

		try {
			boolean created = ud.createUser(newUser);
			if (created) {
				System.out.println("\nNew user has been created\n");
			}
		} catch (UserAlreadyExistsException e) {
			e.printStackTrace();
		}
	}
	
	private void updateUser() {
		String username = BankUI.promptUsername();
		String password = BankUI.promptPassword();

		userDao.updateUser(currentUser, username, password);
	}

	private void deleteUser(User u) {

	}

	private void createAccount(AccountType at) {
		// Loop until title input is valid
		while (true) {
			String title = BankUI.promptTitle();
			if (InputValidation.isValidTitle(title)) {
				Account newAcc = new Account(title, at);
				boolean created = accountDao.createAccount(newAcc);
				if (created) {
					System.out.println("\nNew maple storage has been created!\n");
				}
				return;
			}
		}
	}

	private void viewAccount() {
		try {
			List<Account> accs = accountDao.getAllAccounts();
//			for(Account a : accs) {
//				System.out.println(a.toString());
//			}
		} catch (EmptyTableException e) {
			e.printStackTrace();
		}
	}

	private void deposit() {
		while (true) {
			BigDecimal amount = BankUI.promptDeposit();
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccount.getBalance())) {
				// Change balance here
				break;
			}
		}
	}

	private void withdraw() {
		while (true) {
			BigDecimal amount = BankUI.promptWithdraw();
			if (InputValidation.isAmountGreaterThanZero(amount)) {
				// Change balance here
				break;
			}
		}
	}
}
