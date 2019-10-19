package views;

import dao.AccountDao;
import dao.UserDao;
import models.Account;
import models.AccountType;
import models.User;
import utils.InputValidation;
import utils.ScannerUtil;

public class LoggedInMenu implements View {
	private User currentUser;
	private UserDao userDao = new UserDao();
	private AccountDao accountDao = new AccountDao();

	public LoggedInMenu(User currentUser) {
		super();
		this.currentUser = currentUser;
	}
	
	public void printMenu() {
		System.out.println("Choose from one of the options below (1-6): ");
		System.out.println("1) Create a checking storage\n" + 
							"2) Create a savings storage\n" +
							"3) View/Edit your storages\n"+ 
							"4) Update your personal info\n" +
							"5) Delete your user account\n" +
							"0) Log out");
	}

	@Override
	public View process() {
		printMenu();
		int selection = ScannerUtil.getInput(5);
		
		switch (selection) {
		case 1:
			createAccount(AccountType.CHECKING);
			return this;
		case 2:
			createAccount(AccountType.SAVINGS);
			return this;
		case 3:
			return new AccountMenu();
		case 4:
			updateUser();
			return this;
		case 5:
			return deleteUser();
		case 0:
			System.out.println("Logging out...");
			return new MainMenu();
		default:
			return this;
		}
	}

	private void createAccount(AccountType at) {
		// Loop until title input is valid
		while (true) {
			String title = Prompt.promptTitle();
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
	
	private void updateUser() {
		String username = Prompt.promptUsername();
		String password = Prompt.promptPassword();

		userDao.updateUser(currentUser, username, password);
	}

	private View deleteUser() {
		String answer = Prompt.promptConfirmDelete();
		
		switch (answer.toLowerCase().charAt(0)) {
		case 'y':
			userDao.deleteUser(currentUser.getId());
			System.out.printf("User %s has been deleted", currentUser.getUsername());
			System.out.println("Returning to main menu");
			return new MainMenu();
		case 'n': default:
			return this;
		}
	}
}
