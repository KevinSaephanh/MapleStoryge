package utils;

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
			
			// Error message
			System.out.println("Please enter a valid number!");
			scanner.nextLine();
		}
	}
}
