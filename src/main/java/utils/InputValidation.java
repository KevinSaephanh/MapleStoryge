package utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
	private static final String USERNAME_PATTERN = "[a-zA-Z0-9\\\\._\\\\-]{2,25}";
	private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d).{7,50}$";
	private static final String TITLE_PATTERN = "[a-zA-Z0-9\\\\._\\\\-]{3,30}";
	
	public static boolean isValidUsername(String username) {
		Pattern pattern = Pattern.compile(USERNAME_PATTERN);
		Matcher matcher = pattern.matcher(username);
		boolean match = matcher.matches();
		
		if (!match) {
			System.out.println("Username must follow this format:\n" +
								"- Must be at least 2 characters long\n" +
								"- Length cannot exceed 25 characters\n" +
								"- Cannot contain special characters");
		}
		
		return match;
	}
	
	public static boolean isValidPassword(String password) {
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(password);
		boolean match = matcher.matches();
		
		if (!match) {
			System.out.println("Password must follow this format:\n" +
								"- Must be at least 7 characters long\n" +
								"- Length cannot exceed 50 characters\n" +
								"- Must have at least one uppercase letter");
		}
		
		return match;
	}
	
	public static boolean isValidTitle(String title) {
		Pattern pattern = Pattern.compile(TITLE_PATTERN);
		Matcher matcher = pattern.matcher(title);
		boolean match = matcher.matches();
		
		if (!match) {
			System.out.println("Title must follow this format:\n" +
								"- Must be at least 3 characters long\n" +
								"- Length cannot exceed 30 characters\n" +
								"- Cannot contain special characters");
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
}
