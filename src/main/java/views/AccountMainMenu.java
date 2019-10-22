package views;

import java.util.List;

import dao.AccountDao;
import exceptions.AccountDoesNotExistException;
import exceptions.EmptyTableException;
import exceptions.UserDoesNotExistException;
import models.Account;
import models.User;
import services.AccountService;
import utils.ScannerUtil;

public class AccountMainMenu implements View {
	private User currentUser;
	private AccountDao accountDao = new AccountDao();

	public AccountMainMenu(User currentUser) {
		this.currentUser = currentUser;
	}

	private void printMenu() {
		System.out.println("Choose from one of the options below:");
		System.out.println("1) View all your maple storages\n" + "2) Look through all maple storages\n"
				+ "3) Search for a maple storage by its title\n" + "4) Join a maple storage party\n"
				+ "0) Back to user menu");
	}

	private void printAccounts(List<Account> accounts) {
		System.out.printf("%11s %15s %23s\n", "Mesos", "Title", "Storage Type");
		System.out.println("-----------------------------------------------------------");
		for (int i = 0; i < accounts.size(); i++) {
			System.out.printf("%d)  $%7.2f %20s %15s\n", i, accounts.get(i).getBalance(), accounts.get(i).getTitle(),
					accounts.get(i).getAccountType().toString());
		}
	}

	@Override
	public View process() {
		printMenu();
		int selection = ScannerUtil.getInput(4);

		switch (selection) {
		case 1:
			try {
				List<Account> accounts = accountDao.getSpecificUsersAccounts(currentUser.getId());
				printAccounts(accounts);
				return processPrintAccountsReply(accounts);
			} catch (UserDoesNotExistException e) {
				e.printStackTrace();
			}
			return this;
		case 2:
			try {
				List<Account> accounts = accountDao.getAllAccounts();
				printAccounts(accounts);
				return this;
			} catch (EmptyTableException e) {
				e.printStackTrace();
			}
			return this;
		case 3:
			Account account = searchByTitle();
			System.out.println(account.toString());
			return this;
		case 4:
			joinAccount();
			return this;
		case 0:
			return new LoggedInMenu(currentUser);
		default:
			return this;
		}

	}

	private View processPrintAccountsReply(List<Account> accounts) {
		System.out.printf("%d) Back to account menu\n\n", accounts.size());
		System.out.println("Wanna edit a storage? Input the number provided at the beginning of the row");
		int selection = ScannerUtil.getInput(accounts.size());

		// If selection equals accounts size, return to account main menu
		if (selection == accounts.size()) {
			System.out.println("Returning to main account menu\n");
			return this;
		}

		// Check if index specified in accounts list is not null
		if (accounts.get(selection) != null) {
			Account currentAccount = accounts.get(selection);
			return new AccountMenu(currentUser, currentAccount);
		}

		return null;
	}

	private Account searchByTitle() {
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

	private void joinAccount() {
		Account account = searchByTitle();

		AccountService accountService = new AccountService();
		accountService.createSharedAccount(account.getId(), currentUser.getId());
		System.out.printf("You're now part of the %s party!\n\n", account.getTitle());
	}
}
