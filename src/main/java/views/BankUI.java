package views;

import java.util.Scanner;

public class BankUI {
	private static Scanner scanner = new Scanner(System.in);

	// MapleStory Bank - mesos currency
	// User accounts have a bank account with single/multiple checking/savings
	// Checking/savings are the same so no need to make into separate classes
	// Accounts will have primary keys
	// Joint accounts can be made between users
	// A joint account is just a single account
	public static int mainMenu() {
		System.out.println("Welcome to MapleStoryge!");
		System.out.println("Choose from one of the options below (1-3): ");
		System.out.println("1) Signup\n" + 
							"2) Login\n" + 
							"3) Exit");
		while (!scanner.hasNextInt()) {
			System.out.println("Enter a valid number!");
			scanner.nextLine();
		}
		int reply = scanner.nextInt();
		return reply;
	}

	public static int loggedInMenu() {
		System.out.println("Choose from one of the options below (1-3): ");
		System.out.println("1) Create a checking account\n" + 
							"2) Create a savings account\n" +
							"3) View an account\n"+ 
							"4) Log out");
		while (!scanner.hasNextInt()) {
			System.out.println("Enter a valid number!");
			scanner.nextLine();
		}
		int reply = scanner.nextInt();
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
		while (!scanner.hasNextInt()) {
			System.out.println("Enter a valid number!");
			scanner.nextLine();
		}
		int reply = scanner.nextInt();
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

	public static double promptDeposit() {
		System.out.print("Enter an amount to deposit: ");
		while (!scanner.hasNextDouble()) {
			System.out.println("Enter a valid amount!");
			scanner.nextLine();
		}
		double deposit = scanner.nextDouble();
		return deposit;
	}

	public static double promptWithdraw() {
		System.out.print("Enter an amount to withdraw: ");
		while (!scanner.hasNextDouble()) {
			System.out.println("Enter a valid amount!");
			scanner.nextLine();
		}
		double withdraw = scanner.nextDouble();
		return withdraw;
	}
}
