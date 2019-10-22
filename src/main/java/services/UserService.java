package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import models.User;
import utils.ConnectionUtil;

public class UserService {
	public boolean createUser(User user) throws UserAlreadyExistsException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			if (!isUsernameInUse(user.getUsername())) {
				String sql = "INSERT INTO users (user_name, pass_word) VALUES(?, ?)";
				PreparedStatement statement = connection.prepareStatement(sql);

				statement.setString(1, user.getUsername());
				statement.setString(2, user.getPassword());
				statement.executeUpdate();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If username is in use, throw exception
		throw new UserAlreadyExistsException("Username: " + user.getUsername() + " is already in use!");
	}

	public int updateUser(User user, String newUsername, String newPassword) throws UserAlreadyExistsException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			if (!isUsernameInUse(newUsername)) {
				String sql = "UPDATE users SET user_name = ?, pass_word = ? " + "WHERE user_id = ?";
				PreparedStatement statement = connection.prepareStatement(sql);

				statement.setString(1, newUsername);
				statement.setString(2, newPassword);
				statement.setInt(3, user.getId());
				int updateCount = statement.executeUpdate();

				return updateCount;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If username is in use, throw exception
		throw new UserAlreadyExistsException("Username: " + user.getUsername() + " is already in use!");
	}

	public int deleteUser(int user_id) throws UserDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM users WHERE user_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			int deleteCount = statement.executeUpdate();
			
			return deleteCount;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		throw new UserDoesNotExistException("User with id: " + user_id + " does not exist");
	}

	private boolean isUsernameInUse(String username) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM users WHERE user_name = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();

			// If result set is not empty, username is in use
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}
