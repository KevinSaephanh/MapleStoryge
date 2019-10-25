package views;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import main.AudioClips;
import models.AccountType;
import models.User;
import services.AccountService;
import services.UserService;
import utils.ScannerUtil;

public class LoggedInMenu implements View {
	private User currentUser;
	private AccountService accountService;
	private UserService userService = new UserService();
	private Clip clip;
	
	public LoggedInMenu(User currentUser) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.currentUser = currentUser;
		accountService = new AccountService(currentUser);
		
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

	@Override
	public View process() {
		printMenu();
		int selection = ScannerUtil.getInput(5);

		switch (selection) {
		case 1:
			accountService.createAccount(AccountType.CHECKING);
			clip.close();
			return this;
		case 2:
			accountService.createAccount(AccountType.SAVINGS);
			clip.close();
			return this;
		case 3:
			try {
				clip.close();
				return new AccountMainMenu(currentUser);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			} 
			return this;
		case 4:
			String newUsername = userService.updateUser(currentUser.getId());
			if (!newUsername.isEmpty()) {
				currentUser.setUsername(newUsername);
				return this;
			}
			
			clip.close();
			return this;
		case 5:
			userService.deleteUser(currentUser.getId());
			try {
				return new MainMenu();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			} 
			
			clip.close();
			return this;
		case 0:
			System.out.println("Logging out...\n");
			try {
				clip.close();
				return new MainMenu();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			} 
		default:
			clip.close();
			return this;
		}
	}
}
