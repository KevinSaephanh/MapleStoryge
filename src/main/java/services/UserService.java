package services;

import dao.UserDao;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import models.User;
import utils.InputValidation;
import views.Prompt;

public class UserService {
	private UserDao userDao = new UserDao();

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
		try {
			boolean created = userDao.createUser(username, password);
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

	public String updateUser(int id) {
		String username = "";
		String password = "";
		
		// Loop until valid username is given
		while (true) {
			username = Prompt.promptUsername();
			if(InputValidation.isValidUsername(username)) {
				break;
			}
		}
		
		// Loop until valid password is given
		while (true) {
			password = Prompt.promptPassword();
			if(InputValidation.isValidPassword(password)) {
				break;
			}
		}
		
		try {
			userDao.updateUser(id, username, password);
		} catch (UserAlreadyExistsException e) {
			e.printStackTrace();
		}

		return username;
	}

	public void deleteUser(int id) {
		String answer = Prompt.promptConfirmDelete();

		switch (answer.toLowerCase().charAt(0)) {
		case 'y':
			try {
				userDao.deleteUser(id);
			} catch (UserDoesNotExistException e) {
				e.printStackTrace();
			}
			System.out.println("Deletion successful!");
			System.out.println("Returning to main menu\n");
			break;
		case 'n':
		default:
			break;
		}
	}
}
