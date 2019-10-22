package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.EmptyTableException;
import exceptions.UserDoesNotExistException;
import models.User;
import utils.ConnectionUtil;

public class UserDao {
	public User getUser(String username, String password) throws UserDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM users WHERE user_name = ? AND pass_word = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();

			// Check if result set returned query
			if (rs.next()) {
				return extractUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If result set is empty, throw exception
		throw new UserDoesNotExistException("Incorrect username/password!");
	}

	public List<User> getAllUsers() throws EmptyTableException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM users ORDER BY user_name";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				List<User> User = new ArrayList<>();
				while (rs.next()) {
					User p = extractUser(rs);
					User.add(p);
				}
				return User;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If result set is empty, throw exception
		throw new EmptyTableException("No users in the database");
	}

	// UserDoesNotExistException
	public User getUserByUsername(String username) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			connection.setAutoCommit(false);

			String sql = "SELECT * FROM User WHERE LOWER(user_name) = LOWER(?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				User user = extractUser(rs);
				if (user.getUsername().equals(username)) {
					return user;
				}
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private User extractUser(ResultSet rs) throws SQLException {
		int user_id = rs.getInt("user_id");
		String username = rs.getString("user_name");
		String password = rs.getString("pass_word");

		User user = new User(user_id, username, password);
		return user;
	}
}
