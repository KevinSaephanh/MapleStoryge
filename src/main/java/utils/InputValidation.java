package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
	private static final String USERNAME_PATTERN = "[a-zA-Z0-9\\\\._\\\\-]{2,}";
	private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d).{7}$";
	
	public static boolean isValidUsername(String username) {
		Pattern pattern = Pattern.compile(USERNAME_PATTERN);
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}
	
	public static boolean isValidPassword(String password) {
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
	
	public static boolean isAmountGreaterThanZero(double amount) {
		if (amount <= 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isAmountWithinBalance(double amount, double balance) {
		if (amount <= balance) {
			return true;
		}
		return false;
	}
}
