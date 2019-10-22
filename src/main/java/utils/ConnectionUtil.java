package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	private static final String url = "jdbc:postgresql://localhost:5432/postgres";
	private static final String user = System.getenv("MS_ROLE");
	private static final String password = System.getenv("MS_PASS");
	public static boolean testMode = false;
	
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Unable to connect to MapleStoryge database");
			return null;
		}
	}
}

