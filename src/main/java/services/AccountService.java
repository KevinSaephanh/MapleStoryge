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
	private Account currentAccount;
	
	public AccountService() {
		
	}
	
	public AccountService(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	public AccountService(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public AccountService(Account currentAccount, User currentUser) {
		this.currentAccount = currentAccount;
		this.currentUser = currentUser;
	}
	
	public void createAccount(AccountType at) {
		// Loop until title input is valid
		while (true) {
			String title = Prompt.promptTitle();
			if (InputValidation.isValidTitle(title)) {
				try {
					Account newAcc = new Account(title, at);
					int accountId = accountDao.createAccount(newAcc);
					
					accountDao.createSharedAccount(accountId, currentUser.getId());
					System.out.println("\nNew maple storage has been created!\n");
					return;
				} catch (AccountAlreadyExistsException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Account searchByTitle() {
		try {
			String title = Prompt.promptTitle();
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

	public void joinAccount() {
		// Retrieve account by its title
		Account account = searchByTitle();

		// Create the shared account in users_maplestoryges table
		accountDao.createSharedAccount(account.getId(), currentUser.getId());
		System.out.printf("You're now part of the %s party!\n\n", account.getTitle());
	}
	
	public Account deposit(int id) {	
		while (true) {
			BigDecimal amount = Prompt.promptDeposit();
			
			if (InputValidation.isAmountGreaterThanZero(amount)) {
				BigDecimal newBalance = accountDao.deposit(id, amount);
				currentAccount.setBalance(newBalance);
				System.out.printf("You deposited %.2f mesos!\n", amount);
				
				return currentAccount;
			}
		}
	}

	public Account withdraw(int id) {
		while (true) {
			BigDecimal amount = Prompt.promptWithdraw();
			
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccount.getBalance())) {
				BigDecimal newBalance = accountDao.withdraw(id, amount);
				currentAccount.setBalance(newBalance);
				System.out.printf("You withdrew %.2f mesos!\n", amount);
				
				return currentAccount;
			}
		}
	}

	public void transferFunds(int withdrawAccId, int depositAccId) {
		while (true) {
			BigDecimal amount = Prompt.promptTransfer();
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccount.getBalance())) {
				BigDecimal newBalance = accountDao.transfer(amount, withdrawAccId, depositAccId);
				currentAccount.setBalance(newBalance);
				return;
			}
		}
	}
}
