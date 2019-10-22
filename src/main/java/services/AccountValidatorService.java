package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.ConnectionUtil;

public class AccountValidatorService {
	public static boolean isTitleInUse(String title) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM maplestoryges WHERE title = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, title);
			ResultSet rs = statement.executeQuery();

			// If result set is not empty, title is in use
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}
