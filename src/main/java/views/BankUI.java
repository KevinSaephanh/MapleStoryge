package views;

import java.math.BigDecimal;
import java.util.Scanner;

import utils.ScannerUtil;

public class BankUI {
	private static Scanner scanner = new Scanner(System.in);

	// MapleStory Bank - mesos currency
	// User accounts have a bank account with single/multiple checking/savings
	// Accounts will have primary keys
	// Joint accounts can be made between users
	public static int mainMenu() {
		System.out.println("Welcome to MapleStoryge UwU!");
		System.out.println("----------------------------\n");
		System.out.println("Choose from one of the options below (1-3): ");
		System.out.println("1) Signup\n" + 
							"2) Login\n" + 
							"3) Exit");
		int reply = ScannerUtil.getInput(3);
		return reply;
	}

	public static int loggedInMenu() {
		System.out.println("Choose from one of the options below (1-6): ");
		System.out.println("1) Create a checking storage\n" + 
							"2) Create a savings storage\n" +
							"3) View a storage\n"+ 
							"4) Update your personal info\n" +
							"5) Delete your user account\n" +
							"6) Log out");
		int reply = ScannerUtil.getInput(6);
		return reply;
	}

	public static int accountMenu() {
		System.out.println("Choose from one of the options below (1-6): ");
		System.out.println("1) Check balance\n" + 
							"2) Deposit\n" + 
							"3) Withdraw\n" + 
							"4) Transfer money to another account\n" + 
							"5) Close this account\n" +
							"6) Back to user menu");
		int reply = ScannerUtil.getInput(6);
		return reply;
	}
	
	public static int updateUserMenu() {
		System.out.println("Choose from one of the options below (1-3): ");
		System.out.println("1) Update your username\n" + 
							"2) Update your password\n" +
							"3) Back to user menu");
		int reply = ScannerUtil.getInput(3);
		return reply;
	}

	public static String promptUsername() {
		System.out.print("Enter a username: ");
		String username = scanner.next();
		return username;
	}

	public static String promptPassword() {
		System.out.print("Enter a password: ");
		String password = scanner.next();
		return password;
	}
	
	public static String promptTitle() {
		System.out.print("Enter a name for this storage: ");
		String title = scanner.next();
		return title;
	}

	public static BigDecimal promptDeposit() {
		System.out.print("Enter an amount to deposit: ");
		BigDecimal deposit = scanner.nextBigDecimal();
		return deposit;
	}

	public static BigDecimal promptWithdraw() {
		System.out.print("Enter an amount to withdraw: ");
		BigDecimal withdraw = scanner.nextBigDecimal();
		return withdraw;
	}
}
