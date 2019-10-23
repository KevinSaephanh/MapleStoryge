package views;

import services.AccountService;
import services.UserService;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import exceptions.AccountAlreadyExistsException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import main.AudioClips;
import models.Account;
import models.AccountType;
import models.User;
import utils.InputValidation;
import utils.ScannerUtil;

public class LoggedInMenu implements View {
	private User currentUser;
	private UserService userService = new UserService();
	private AccountService accountService = new AccountService();
	private Clip clip;
	
	public LoggedInMenu(User currentUser) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.currentUser = currentUser;
		
		File file = new File(AudioClips.HENESYS.toString());
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
		clip = AudioSystem.getClip();

		// Open clip in audioInputStream and loop
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void printMenu() {
		System.out.printf("\nHi, %s!\n", currentUser.getUsername());
		System.out.println("Choose from one of the options below:");
		System.out.println(
				"1) Create a checking storage\n" + "2) Create a savings storage\n" + "3) Browse/edit storages\n"
						+ "4) Update your personal info\n" + "5) Delete your user account\n" + "0) Log out");
	}

	public void printUpdateMenu() {
		System.out.println("What info would you like to change?");
		System.out.println("Choose from one of the options below:");
		System.out.println("1) My username\n" + "2) My password\n" + "3) Both my username and password\n"
				+ "0) I've changed my mind, take me back");
	}

	@Override
	public View process() {
		printMenu();
		int selection = ScannerUtil.getInput(5);

		switch (selection) {
		case 1:
			createAccount(AccountType.CHECKING);
			clip.close();
			return this;
		case 2:
			createAccount(AccountType.SAVINGS);
			clip.close();
			return this;
		case 3:
			try {
				clip.close();
				return new AccountMainMenu(currentUser);
			} catch (UnsupportedAudioFileException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (LineUnavailableException e1) {
				e1.printStackTrace();
			}
			return this;
		case 4:
			updateUser();
			clip.close();
			return this;
		case 5:
			clip.close();
			return deleteUser();
		case 0:
			System.out.println("Logging out...\n");
			try {
				clip.close();
				return new MainMenu();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		default:
			clip.close();
			return this;
		}
	}

	private void createAccount(AccountType at) {
		// Loop until title input is valid
		while (true) {
			String title = Prompt.promptTitle();
			if (InputValidation.isValidTitle(title)) {
				try {
					Account newAcc = new Account(title, at);
					int accountId = accountService.createAccount(newAcc);
					
					accountService.createSharedAccount(accountId, currentUser.getId());
					System.out.println("\nNew maple storage has been created!\n");
					return;
				} catch (AccountAlreadyExistsException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void updateUser() {
		printUpdateMenu();

		int selection = ScannerUtil.getInput(3);

		switch (selection) {
		case 1: {
			while (true) {
				String username = Prompt.promptUsername();

				// Check if username is valid
				if (InputValidation.isValidUsername(username)) {
					int update = 0;
					try {
						update = userService.updateUser(currentUser, username, currentUser.getPassword());
						
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
						userService.updateUser(currentUser, currentUser.getUsername(), password);
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
						update = userService.updateUser(currentUser, username, password);
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
				userService.updateUser(currentUser, username, password);
			} catch (UserAlreadyExistsException e) {
				e.printStackTrace();
			}
		}
		default:
			break;
		}
	}

	private View deleteUser() {
		String answer = Prompt.promptConfirmDelete();

		switch (answer.toLowerCase().charAt(0)) {
		case 'y':
			try {
				userService.deleteUser(currentUser.getId());
			} catch (UserDoesNotExistException e) {
				e.printStackTrace();
			}
			System.out.printf("User %s has been deleted\n", currentUser.getUsername());
			System.out.println("Returning to main menu\n");
			try {
				return new MainMenu();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		case 'n':
		default:
			return this;
		}
	}
}
