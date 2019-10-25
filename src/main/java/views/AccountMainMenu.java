package views;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import dao.AccountDao;
import exceptions.EmptyTableException;
import exceptions.UserDoesNotExistException;
import main.AudioClips;
import models.Account;
import models.User;
import services.AccountService;
import utils.ScannerUtil;

public class AccountMainMenu implements View {
	private User currentUser;
	private Account currentAccount;
	private AccountDao accountDao = new AccountDao();
	private AccountService accountService;
	private Clip clip;

	public AccountMainMenu(User currentUser)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.currentUser = currentUser;
		accountService = new AccountService(currentUser);

		// Audio set up
		File file = new File(AudioClips.RAINDROP_FLOWER.toString());
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
		clip = AudioSystem.getClip();

		// Open clip in audioInputStream and loop
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	private void printMenu() {
		System.out.println("Choose from one of the options below:");
		System.out.println("1) View all your maple storages\n" + "2) Look through all maple storages\n"
				+ "3) Search for a maple storage by its title\n" + "4) Join a maple storage party\n"
				+ "0) Back to user menu");
	}

	private void printAccounts(List<Account> accounts) {
		System.out.printf("%11s %15s %23s\n", "Mesos", "Title", "Storage Type");
		System.out.println("--------------------------------------------------------------");
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
				clip.close();
				return processPrintAccountsReply(accounts);
			} catch (UserDoesNotExistException e) {
				e.printStackTrace();
			}
			clip.close();
			return this;
		case 2:
			try {
				List<Account> accounts = accountDao.getAllAccounts();
				printAccounts(accounts);
				clip.close();
				return this;
			} catch (EmptyTableException e) {
				e.printStackTrace();
			}
			clip.close();
			return this;
		case 3:
			String title = Prompt.promptTitle();
			Account account = accountService.searchByTitle(title);
			System.out.println(account.toString());
			clip.close();
			return this;
		case 4:
			accountService.joinAccount(currentUser.getId());
			clip.close();
			return this;
		case 0:
			try {
				clip.close();
				return new LoggedInMenu(currentUser);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			} 
		default:
			clip.close();
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
			currentAccount = accounts.get(selection);
			System.out.println(currentUser.getId());
			return new AccountMenu(currentUser, currentAccount);
		}

		return null;
	}
}
