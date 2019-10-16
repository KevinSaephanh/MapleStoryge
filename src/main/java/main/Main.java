package main;

/*
 * @author Kevin Saephanh
 * */

/*
 * Bank app
 * Models
 * 	User
 * 		username
 * 		password
 * 	Account
 * 		type
 * 		title
 * 		balance
 * BankUI
 * 		main menu
 * 		logged in menu
 * 		prompts
 * 			username
 * 			password
 * 			title
 * 			deposit
 * 			withdraw
 * 			joint?
 * Input validation
 * 		username
 * 		password
 * 		title
 * 		non-negative deposits/withdraws
 * 		deposit less than or equal to balance
 * */

public class Main {
	public static void main(String[] args) {
		Bank bank = Bank.getInstance();
		bank.start();
	}
}
