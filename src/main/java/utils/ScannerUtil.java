package utils;

import java.math.BigDecimal;
import java.util.Scanner;

public class ScannerUtil {
	private static Scanner scanner = new Scanner(System.in);

	public static int getInput(int max) {
		while (true) {
			if (scanner.hasNextInt()) {
				int input = scanner.nextInt();
				
				// Check if input is greater than 0 and less than or equal to max
				if (input > 0 && input <= max) {
					return input;
				}
			}
			
			// Error message, loop continues
			System.out.println("Please enter a valid number!");
			scanner.nextLine();
		}
	}
	
	public static String getStringInput() {
		String input = "";
		while(input.isEmpty()) {
			input = scanner.nextLine();
		}
		return input;
	}
	
	public static BigDecimal getBigDecimalInput() {
		BigDecimal input = null;
		
		if (scanner.hasNextBigDecimal()) {
			input = scanner.nextBigDecimal();
		}
		
		return input;
	}
}
