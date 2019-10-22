package utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
	private static final String USERNAME_PATTERN = "[a-zA-Z0-9\\\\._\\\\-]{2,25}";
	private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d).{7,50}$";
	private static final String TITLE_PATTERN = "^{3,30}$";
	private static final String DECIMAL_PATTERN = "^\\d+\\.\\d{0,2}$";
	
	public static boolean isValidUsername(String username) {
		boolean match = getMatch(USERNAME_PATTERN, username);
		
		if (!match) {
			System.out.println("Username must follow this format:\n" +
								"- Must be at least 2 characters long\n" +
								"- Length cannot exceed 25 characters\n" +
								"- Cannot contain special characters");
		}
		
		return match;
	}
	
	public static boolean isValidPassword(String password) {
		boolean match = getMatch(PASSWORD_PATTERN, password);
		
		if (!match) {
			System.out.println("Password must follow this format:\n" +
								"- Must be at least 7 characters long\n" +
								"- Length cannot exceed 50 characters\n" +
								"- Must have at least one uppercase letter");
		}
		
		return match;
	}
	
	public static boolean isValidTitle(String title) {		
		if (title.length() < 3 || title.length() > 30) {
			System.out.println("Your storage name must follow this format:\n" +
								"- Must be at least 3 characters long\n" +
								"- Length cannot exceed 30 characters");
			return false;
		}
		
		return true;
	}
	
	public static boolean isPreciseDecimalScale(BigDecimal amount) {
		boolean match = getMatch(DECIMAL_PATTERN, String.valueOf(amount));
		
		if (!match) {
			System.out.println("The decimal scale maximum is 2 decimal places!");
		}
		
		return match;
	}
	
	public static boolean isAmountGreaterThanZero(BigDecimal amount) {
		if (amount.compareTo(new BigDecimal(0.00)) <= 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isAmountWithinBalance(BigDecimal amount, BigDecimal balance) {
		if (amount.compareTo(balance) <= 0) {
			return true;
		}
		return false;
	}
	
	/*
	 * Takes in a specific pattern and input
	 * Then a comparison is made between the input and patter
	 * Returns true if the input matches the pattern, else returns false
	 * */
	private static boolean getMatch(String inputPattern, String input) {
		Pattern pattern = Pattern.compile(inputPattern);
		Matcher matcher = pattern.matcher(input);
		boolean match = matcher.matches();
		
		return match;
	}
}
