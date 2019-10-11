package models;

//Users have the option to creaccountTypee either a checking or savings account
//Both do the same thing, but for name sake differentiaccountTypee them
//Give each account a title to make it easier to navigaccountTypee in the future

public class Account {
	private double balance;
	private AccountType accountType;
	
	public Account(AccountType accountType) {
		balance = 0.00;
		this.accountType = accountType;
	}
	
	public Account(double balance) {
		this.balance = balance;
	}
	
	public AccountType getaccountType() {
		return accountType;
	}

	public void setaccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
