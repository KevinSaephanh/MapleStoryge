package views;

import java.io.IOException;

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
		accountService = new AccountService(currentAccount, currentUser);
	}

	private void printMenu() {
		System.out.println(currentAccount.toString());
		System.out.println("Choose from one of the options below:");
		System.out.println("1) Deposit mesos\n" + "2) Withdraw mesos\n"
				+ "3) Transfer mesos from this storage to another\n" + "0) Back to main account menu");
	}

	@Override
	public View process() {
		printMenu();
		int selection = ScannerUtil.getInput(3);

		switch (selection) {
		case 1:
			accountService.deposit();
			return this;
		case 2:
			accountService.withdraw();
			return this;
		case 3:
			accountService.transferFunds();
			return this;
		case 0:
			try {
				return new AccountMainMenu(currentUser);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			return this;
		default:
			return this;
		}
	}
}
