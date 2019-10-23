package services;

import java.math.BigDecimal;

import dao.AccountDao;
import exceptions.AccountDoesNotExistException;
import models.Account;
import models.User;
import utils.InputValidation;
import views.Prompt;

public class AccountService {
	private AccountDao accountDao = new AccountDao();
	private User currentUser;
	private Account currentAccount;
	
	public AccountService() {
		
	}
	
	public AccountService(Account currentAccount, User currentUser) {
		this.currentAccount = currentAccount;
		this.currentUser = currentUser;
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
	
	public void deposit() {
		while (true) {
			BigDecimal amount = Prompt.promptDeposit();
			if (InputValidation.isAmountGreaterThanZero(amount)) {
				BigDecimal newBalance = accountDao.deposit(currentAccount.getId(), amount);
				currentAccount.setBalance(newBalance);
				System.out.printf("You deposited %.2f mesos!\n", amount);
				return;
			}
		}
	}

	public void withdraw() {
		while (true) {
			BigDecimal amount = Prompt.promptWithdraw();
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccount.getBalance())) {
				BigDecimal newBalance = accountDao.withdraw(currentAccount.getId(), amount);
				currentAccount.setBalance(newBalance);
				System.out.printf("You withdrew %.2f mesos!\n", amount);
				return;
			}
		}
	}

	public void transferFunds() {
		String depositAccTitle = Prompt.promptTitle();
		
		while (true) {
			BigDecimal amount = Prompt.promptTransfer();
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccount.getBalance())) {
				BigDecimal newBalance = accountDao.transfer(amount, currentAccount.getTitle(), depositAccTitle);
				currentAccount.setBalance(newBalance);
				return;
			}
		}
	}
}
