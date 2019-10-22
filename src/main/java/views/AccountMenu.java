package views;

import java.math.BigDecimal;

import models.Account;
import models.User;
import services.AccountService;
import utils.InputValidation;
import utils.ScannerUtil;

public class AccountMenu implements View {
	private User currentUser;
	private Account currentAccount;
	private AccountService accountService = new AccountService();

	public AccountMenu(User currentUser, Account currentAccount) {
		this.currentUser = currentUser;
		this.currentAccount = currentAccount;
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
			deposit();
			return this;
		case 2:
			withdraw();
			return this;
		case 3:
			transferFunds();
			return this;
		case 0:
			return new AccountMainMenu(currentUser);
		default:
			return this;
		}
	}

	private void deposit() {
		while (true) {
			BigDecimal amount = Prompt.promptDeposit();
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccount.getBalance())) {
				accountService.deposit(currentAccount, amount);
				break;
			}
		}
	}

	private void withdraw() {
		while (true) {
			BigDecimal amount = Prompt.promptWithdraw();
			if (InputValidation.isAmountGreaterThanZero(amount)) {
				accountService.withdraw(currentAccount, amount);
				break;
			}
		}
	}

	private void transferFunds() {
		while (true) {
			BigDecimal amount = Prompt.promptTransfer();
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccount.getBalance())) {
				
				break;
			}
		}
	}
}
