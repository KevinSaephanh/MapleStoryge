package views;

import dao.UserDao;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import models.User;
import services.UserService;
import utils.InputValidation;
import utils.ScannerUtil;

public class MainMenu implements View {
	private UserDao userDao = new UserDao();
	private UserService userService = new UserService();

	public static void printMenu() {
		System.out.println("Welcome to MapleStoryge UwU!");
		System.out.println("----------------------------\n");
		System.out.println("Choose from one of the options below:");
		System.out.println("1) Signup\n" + "2) Login\n" + "0) Exit");
	}

	@Override
	public View process() {
		printMenu();
		int selection = ScannerUtil.getInput(2);

		switch (selection) {
		case 0:
			System.out.println("Exiting MapleStoryge");
			return null;
		case 1:
			signup();
			return this;
		case 2:
			User user = login();
			return new LoggedInMenu(user);
		default:
			return this;
		}
	}

	private void signup() {
		String username = "";
		String password = "";

		// Loop until username input is valid
		while (true) {
			username = Prompt.promptUsername();
			if (InputValidation.isValidUsername(username)) {
				break;
			}
		}

		// Loop until password input is valid
		while (true) {
			password = Prompt.promptPassword();
			if (InputValidation.isValidPassword(password)) {
				break;
			}
		}

		// Create new user in database
		User newUser = new User(username, password);
		try {
			boolean created = userService.createUser(newUser);
			if (created) {
				System.out.println("\nNew user has been created\n");
			}
		} catch (UserAlreadyExistsException e) {
			e.printStackTrace();
		}
	}

	private User login() {
		while (true) {
			String username = Prompt.promptUsername();
			String password = Prompt.promptPassword();

			// Check database for matching user input
			User currentUser = new User(username, password);
			try {
				currentUser = userDao.getUser(username, password);
				if (currentUser != null)
					return currentUser;
			} catch (UserDoesNotExistException e) {
				e.printStackTrace();
			}
		}
	}
}
