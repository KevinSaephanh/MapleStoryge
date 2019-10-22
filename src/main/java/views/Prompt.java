package views;

import java.math.BigDecimal;

import utils.ScannerUtil;

public class Prompt {
	public static String promptUsername() {
		System.out.print("Enter a username: ");
		String username = ScannerUtil.getStringInput();
		return username;
	}

	public static String promptPassword() {
		System.out.print("Enter a password: ");
		String password = ScannerUtil.getStringInput();
		return password;
	}

	public static String promptTitle() {
		System.out.print("Enter a storage name: ");
		String title = ScannerUtil.getStringInput();
		return title;
	}

	public static BigDecimal promptDeposit() {
		System.out.print("Enter the amount you wish to deposit: $");
		BigDecimal deposit = ScannerUtil.getBigDecimalInput();
		return deposit;
	}

	public static BigDecimal promptWithdraw() {
		System.out.print("Enter the amount you wish to withdraw: $");
		BigDecimal withdraw = ScannerUtil.getBigDecimalInput();
		return withdraw;
	}

	public static BigDecimal promptTransfer() {
		System.out.print("Enter the amount you wish to transfer: $");
		BigDecimal transfer = ScannerUtil.getBigDecimalInput();
		return transfer;
	}

	public static String promptConfirmDelete() {
		System.out.println("Are you sure you want to go through with this delete (y/n)?");
		String reply = ScannerUtil.getStringInput();
		return reply;
	}

	public static String promptJointAccount() {
		System.out.println("Would you like to join this storage OwO (y/n)?");
		String reply = ScannerUtil.getStringInput();
		return reply;
	}
}
