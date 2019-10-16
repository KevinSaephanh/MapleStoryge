package main;

/*
 * @author Kevin Saephanh
 * */

public class Main {
	public static void main(String[] args) {
		Bank bank = Bank.getInstance();
		bank.start();
	}
}
