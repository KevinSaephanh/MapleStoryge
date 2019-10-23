package views;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import dao.AccountDao;
import exceptions.AccountAlreadyExistsException;
import main.AudioClips;
import models.Account;
import models.AccountType;
import models.User;
import services.UserService;
import utils.InputValidation;
import utils.ScannerUtil;

public class LoggedInMenu implements View {
	private User currentUser;
	private UserService userService = new UserService();
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
			printUpdateMenu();
			userService = new UserService(currentUser);
			userService.updateUser();
			clip.close();
			return this;
		case 5:
			clip.close();
			userService = new UserService(currentUser);
			userService.deleteUser();
			try {
				return new MainMenu();
			} catch (UnsupportedAudioFileException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (LineUnavailableException e1) {
				e1.printStackTrace();
			}
			return this;
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

	public void createAccount(AccountType at) {
		// Loop until title input is valid
		while (true) {
			String title = Prompt.promptTitle();
			if (InputValidation.isValidTitle(title)) {
				try {
					Account newAcc = new Account(title, at);
					int accountId = new AccountDao().createAccount(newAcc);
					
					new AccountDao().createSharedAccount(accountId, currentUser.getId());
					System.out.println("\nNew maple storage has been created!\n");
					return;
				} catch (AccountAlreadyExistsException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
