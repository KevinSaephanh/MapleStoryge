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
			if (InputValidation.isAmountGreaterThanZero(amount)) {
				BigDecimal newBalance = accountService.deposit(currentAccount.getId(), amount);
				currentAccount.setBalance(newBalance);
				System.out.printf("You deposited %.2f mesos!\n", amount);
				return;
			}
		}
	}

	private void withdraw() {
		while (true) {
			BigDecimal amount = Prompt.promptWithdraw();
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccount.getBalance())) {
				BigDecimal newBalance = accountService.withdraw(currentAccount.getId(), amount);
				currentAccount.setBalance(newBalance);
				System.out.printf("You withdrew %.2f mesos!\n", amount);
				return;
			}
		}
	}

	private void transferFunds() {
		String depositAccTitle = Prompt.promptTitle();
		
		while (true) {
			BigDecimal amount = Prompt.promptTransfer();
			if (InputValidation.isAmountGreaterThanZero(amount)
					&& InputValidation.isAmountWithinBalance(amount, currentAccount.getBalance())) {
				BigDecimal newBalance = accountService.transfer(amount, currentAccount.getTitle(), depositAccTitle);
				currentAccount.setBalance(newBalance);
				return;
			}
		}
	}
}
