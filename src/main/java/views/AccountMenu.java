package views;

import java.math.BigDecimal;

import dao.AccountDao;
import exceptions.AccountDoesNotExistException;
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
		System.out.println("1) View all your maple storages\n" + 
							"2) Search for your storage by its title\n" +
							"3) \n" +
				 			"0) Back to user menu");
	}

	private void printEditMenu() {
		System.out.println(currentAccount.toString());
		System.out.println("Choose from one of the options below:");
		System.out.println("1) Deposit mesos\n" +
							"2) Withdraw mesos\n" +
							"0) Back to main account menu");
	}
	
	private void printAllAccounts() {
		
	}

	@Override
	public View process() {
		printAccountMainMenu();

		int selection = ScannerUtil.getInput(2);

		switch (selection) {
		case 1:
			printAllAccounts();
			int accID = ScannerUtil.getInput(3);
			
		case 2:
			searchByTitle();
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
