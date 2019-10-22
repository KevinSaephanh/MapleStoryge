package views;

import java.math.BigDecimal;
import java.util.List;

import dao.AccountDao;
import exceptions.AccountDoesNotExistException;
import exceptions.EmptyTableException;
import models.Account;
import models.User;
import utils.InputValidation;
import utils.ScannerUtil;

public class AccountMenu implements View {
	private User currentUser;
	private Account currentAccount;
	private AccountDao accountDao = new AccountDao();

	public AccountMenu(User currentUser) {
		this.currentUser = currentUser;
	}

	private void printAccountMainMenu() {
		System.out.println("Choose from one of the options below:");
		System.out.println("1) View all your maple storages\n" + "2) Look through all maple storages\n"
				+ "3) Search for a storage by its title\n" + "0) Back to user menu");
	}

	private void printEditMenu() {
		System.out.println(currentAccount.toString());
		System.out.println("Choose from one of the options below:");
		System.out.println("1) Deposit mesos\n" + "2) Withdraw mesos\n" + "0) Back to main account menu");
	}

	private void printAllAccounts() {
		try {
			List<Account> accounts = accountDao.getAllAccounts();

			for (Account acc : accounts) {
				acc.printAccount();
			}
		} catch (EmptyTableException e) {
			e.printStackTrace();
		}
	}

	private void printSpecificUserAccounts() {

	}

	@Override
	public View process() {
		printAccountMainMenu();

		int selection = ScannerUtil.getInput(3);

		switch (selection) {
		case 1:
			printSpecificUserAccounts();
			return this;
		case 2:
			printAllAccounts();
			return this;
		case 3:
			searchByTitle();
			return this;
		case 0:
			return new LoggedInMenu(currentUser);
		default:
			return this;
		}
	}

	private void searchByTitle() {
		String title = Prompt.promptTitle();

		try {
			currentAccount = accountDao.getAccountByTitle(title);
			
			if (currentAccount != null) {
				currentAccount.printAccount();
			}
		} catch (AccountDoesNotExistException e) {
			e.printStackTrace();
		}
	}

	private void deposit() {
		while (true) {
			BigDecimal amount = Prompt.promptDeposit();
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccount.getBalance())) {
				// Change balance here
				break;
			}
		}
	}

	private void withdraw() {
		while (true) {
			BigDecimal amount = Prompt.promptWithdraw();
			if (InputValidation.isAmountGreaterThanZero(amount)) {
				// Change balance here
				break;
			}
		}
	}

}
