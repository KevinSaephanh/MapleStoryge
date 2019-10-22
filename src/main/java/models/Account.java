package models;

import java.math.BigDecimal;

public class Account {
	private int id;
	private BigDecimal balance;
	private String title;
	private AccountType accountType;

	public Account(String title, AccountType accountType) {
		balance = new BigDecimal(0.00);
		this.title = title;
		this.accountType = accountType;
	}

	public Account(int id, BigDecimal balance, String title, AccountType accountType) {
		super();
		this.id = id;
		this.balance = balance;
		this.title = title;
		this.accountType = accountType;
	}

	public Account(BigDecimal balance) {
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		balance.setScale(2);
		this.balance = balance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountType != other.accountType)
			return false;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\nMesos: " + balance + "\nTitle: " + title + "\nAccount Type: " + accountType.toString() + "\n";
	}
	
	public void printAccount() {
		System.out.printf(" %7.2f %20s %15s\n", balance, title, accountType.toString());
	}
}
