package views;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import dao.UserDao;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import main.AudioClips;
import models.User;
import services.UserService;
import utils.InputValidation;
import utils.ScannerUtil;

public class MainMenu implements View {
	private UserDao userDao = new UserDao();
	private UserService userService = new UserService();
	private Clip clip;
	
	public MainMenu() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File file = new File(AudioClips.LOGIN.toString());
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
		clip = AudioSystem.getClip();
		
		// Open clip in audioInputStream and loop
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

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
			clip.close();
			return null;
		case 1:
			signup();
			clip.close();
			return this;
		case 2:
			User user = login();
			try {
				clip.close();
				return new LoggedInMenu(user);
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

	private void signup() {
		String username = "";
		String password = "";

		// Loop until username input is valid
		while (true) {
			username = Prompt.promptUsername();
			if (InputValidation.isValidUsername(username)) {
				break;
			} else {

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
				System.out.println(currentUser.getId());
				if (currentUser != null)
					return currentUser;
			} catch (UserDoesNotExistException e) {
				e.printStackTrace();
			}
		}
	}
}
