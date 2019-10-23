package services;

import dao.UserDao;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import models.User;
import utils.InputValidation;
import utils.ScannerUtil;
import views.Prompt;

public class UserService {
	private User currentUser;
	private UserDao userDao = new UserDao();

	public UserService() {

	}

	public UserService(User currentUser) {
		this.currentUser = currentUser;
	}

	public void signup() {
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
			boolean created = userDao.createUser(newUser);
			if (created) {
				System.out.println("\nNew user has been created\n");
			}
		} catch (UserAlreadyExistsException e) {
			e.printStackTrace();
		}
	}

	public User login() {
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

	public void updateUser() {
		int selection = ScannerUtil.getInput(3);

		switch (selection) {
		case 1: {
			while (true) {
				String username = Prompt.promptUsername();

				// Check if username is valid
				if (InputValidation.isValidUsername(username)) {
					int update = 0;
					try {
						update = userDao.updateUser(currentUser, username, currentUser.getPassword());

						// If update returns less than 1, then the update q
						if (update > 0) {
							currentUser.setUsername(username);
						}
						break;
					} catch (UserAlreadyExistsException e) {
						e.printStackTrace();
					}
				}
			}
			break;
		}
		case 2: {
			while (true) {
				String password = Prompt.promptPassword();

				// Check if password is valid
				if (InputValidation.isValidPassword(password)) {
					try {
						userDao.updateUser(currentUser, currentUser.getUsername(), password);
					} catch (UserAlreadyExistsException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			break;
		}
		case 3: {
			String username = "";
			String password = "";

			// Loop until username input is valid
			while (true) {
				username = Prompt.promptUsername();
				if (InputValidation.isValidUsername(username)) {
					int update = 0;
					try {
						update = userDao.updateUser(currentUser, username, password);
					} catch (UserAlreadyExistsException e) {
						e.printStackTrace();
					}
					if (update > 0) {
						currentUser.setUsername(username);
					}
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

			try {
				userDao.updateUser(currentUser, username, password);
			} catch (UserAlreadyExistsException e) {
				e.printStackTrace();
			}
		}
		default:
			break;
		}
	}

	public void deleteUser() {
		String answer = Prompt.promptConfirmDelete();

		switch (answer.toLowerCase().charAt(0)) {
		case 'y':
			try {
				userDao.deleteUser(currentUser);
			} catch (UserDoesNotExistException e) {
				e.printStackTrace();
			}
			System.out.printf("User %s has been deleted\n", currentUser.getUsername());
			System.out.println("Returning to main menu\n");
			break;
		case 'n':
		default:
			break;
		}
	}
}
