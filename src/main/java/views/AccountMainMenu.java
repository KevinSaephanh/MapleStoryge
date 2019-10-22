package views;

import java.util.List;

import dao.AccountDao;
import exceptions.AccountDoesNotExistException;
import exceptions.EmptyTableException;
import models.Account;
import models.User;
import utils.ScannerUtil;

public class AccountMainMenu implements View {
	private User currentUser;
	private Account currentAccount;
	private AccountDao accountDao = new AccountDao();

	public AccountMainMenu(User currentUser) {
		this.currentUser = currentUser;
	}

	private void printMenu() {
		System.out.println("Choose from one of the options below:");
		System.out.println("1) View all your maple storages\n" + "2) Look through all maple storages\n"
				+ "3) Search for a storage by its title\n" + "0) Back to user menu");
	}

	private void printAccounts(List<Account> accounts) {
		System.out.printf("%11s %15s %23s\n", "Mesos", "Title", "Storage Type");
		System.out.println("-----------------------------------------------------------");
		for (int i = 0; i < accounts.size(); i++) {
			System.out.printf("%d)  $%7.2f %20s %15s\n", i, accounts.get(i).getBalance(), accounts.get(i).getTitle(),
					accounts.get(i).getAccountType().toString());
		}
		System.out.printf("%d) Back to account menu\n", accounts.size());
		System.out.println(
				"If you would like to edit a storage, input the number provided at the beginning of the corresponding row");
	}

	@Override
	public View process() {
		printMenu();
		int selection = ScannerUtil.getInput(3);

		switch (selection) {
		case 1:
			try {
				List<Account> accounts = accountDao.getUserAccountsByID(currentUser.getId());
				printAccounts(accounts);
				
				return processPrintAccountsReply(accounts);
			} catch (EmptyTableException e) {
				e.printStackTrace();
			}
			return this;
		case 2:
			try {
				List<Account> accounts = accountDao.getAllAccounts();
				printAccounts(accounts);
				
				return processPrintAccountsReply(accounts);
			} catch (EmptyTableException e) {
				e.printStackTrace();
			}
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

	private View processPrintAccountsReply(List<Account> accounts) {
		// Set input limit to size of account list
		int selection = ScannerUtil.getInput(accounts.size());
		if (accounts.get(selection) != null) {
			currentAccount = accounts.get(selection);
		}
		
		// If user selected 0, return to account main menu
		if (selection == 0) {
			return this;
		}
		return new AccountMenu(currentUser, currentAccount);
	}

	private void searchByTitle() {
		String title = Prompt.promptTitle();

		try {
			currentAccount = accountDao.getAccountByTitle(title);

			// Check if account returned is null
			if (currentAccount != null) {
				currentAccount.toString();
			}
		} catch (AccountDoesNotExistException e) {
			e.printStackTrace();
		}
	}
}
