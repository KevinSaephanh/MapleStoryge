package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	public static Connection getConnection() {
		//String url = "jdbc:postgresql://localhost:5432/poke";
		String url = "something";
		try {
			return DriverManager.getConnection(url, System.getenv("MS_ROLE"), System.getenv("MS_PASS"));
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Unable to connect to database");
			return null;
		}
	}
}

