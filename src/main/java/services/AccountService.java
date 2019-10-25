package services;

import java.math.BigDecimal;

import dao.AccountDao;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountDoesNotExistException;
import models.Account;
import models.AccountType;
import models.User;
import utils.InputValidation;
import views.Prompt;

public class AccountService {
	private AccountDao accountDao = new AccountDao();
	private User currentUser;
	
	public AccountService(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public AccountService(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	public void createAccount(AccountType at) {
		// Loop until title input is valid
		while (true) {
			String title = Prompt.promptTitle();
			if (InputValidation.isValidTitle(title)) {
				try {
					Account newAcc = new Account(title, at);
					int accountId = accountDao.createAccount(newAcc.getAccountType(), newAcc.getTitle());
					
					accountDao.createSharedAccount(currentUser.getId(), accountId);
					System.out.println("\nNew maple storage has been created!\n");
					return;
				} catch (AccountAlreadyExistsException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Account searchByTitle(String title) {
		try {
			Account account = accountDao.getAccountByTitle(title);

			// Check if account returned is null
			if (account != null) {
				return account;
			}
		} catch (AccountDoesNotExistException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void joinAccount(int currentUserId) {
		// Retrieve account by its title
		String title = Prompt.promptTitle();
		Account account = searchByTitle(title);

		// Create the shared account in users_maplestoryges table
		accountDao.createSharedAccount(currentUserId, account.getId());
		System.out.printf("You're now part of the %s party!\n\n", account.getTitle());
	}
	
	public void deleteAccount(int accountId) {
		try {
			accountDao.deleteSharedAccount(accountId);
			accountDao.deleteAccount(accountId);
			System.out.println("Account successfully deleted!");
		} catch (AccountDoesNotExistException e) {
			e.printStackTrace();
		}
	}
	
	public BigDecimal deposit(int id) {	
		while (true) {
			BigDecimal amount = Prompt.promptDeposit();
			
			if (InputValidation.isAmountGreaterThanZero(amount)) {
				BigDecimal newBalance = accountDao.deposit(id, amount);
				System.out.printf("You deposited %.2f mesos!\n", amount);
				return newBalance;
			}
		}
	}

	public BigDecimal withdraw(int id, BigDecimal currentAccBalance) {
		while (true) {
			BigDecimal amount = Prompt.promptWithdraw();
			
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccBalance)) {
				BigDecimal newBalance = accountDao.withdraw(id, amount);
				System.out.printf("You withdrew %.2f mesos!\n", amount);
				return newBalance;
			}
		}
	}

	public BigDecimal transferFunds(int withdrawAccId, int depositAccId, BigDecimal currentAccountBalance) {
		while (true) {
			BigDecimal amount = Prompt.promptTransfer();
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccountBalance)) {
				BigDecimal newBalance = accountDao.transfer(amount, withdrawAccId, depositAccId);
				return newBalance;
			}
		}
	}
}
