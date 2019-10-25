package views;

import java.io.IOException;
import java.math.BigDecimal;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import models.Account;
import models.User;
import services.AccountService;
import utils.ScannerUtil;

public class AccountMenu implements View {
	private User currentUser;
	private Account currentAccount;
	private AccountService accountService;

	public AccountMenu(User currentUser, Account currentAccount) {
		this.currentUser = currentUser;
		this.currentAccount = currentAccount;
		accountService = new AccountService(currentUser);
	}

	private void printMenu() {
		System.out.println(currentAccount.toString());
		System.out.println("Choose from one of the options below:");
		System.out.println(
				"1) Deposit mesos\n" + "2) Withdraw mesos\n" + "3) Transfer mesos from this storage to another\n"
						+ "4) Delete this account\n" + "0) Back to main account menu");
	}

	@Override
	public View process() {
		printMenu();
		int selection = ScannerUtil.getInput(4);

		switch (selection) {
		case 1:
			BigDecimal newBalance = accountService.deposit(currentAccount.getId());
			currentAccount.setBalance(newBalance);
			return this;
		case 2:
			newBalance = accountService.withdraw(currentAccount.getId(), currentAccount.getBalance());
			currentAccount.setBalance(newBalance);
			return this;
		case 3:
			String title = Prompt.promptTitle();
			Account depositAcc = accountService.searchByTitle(title);
			newBalance = accountService.transferFunds(currentAccount.getId(), depositAcc.getId(),
					currentAccount.getBalance());
			currentAccount.setBalance(newBalance);
			return this;
		case 4:
			accountService.deleteAccount(currentAccount.getId());
			try {
				return new AccountMainMenu(currentUser);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}
			return this;
		case 0:
			try {
				return new AccountMainMenu(currentUser);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}
			return this;
		default:
			return this;
		}
	}
}
