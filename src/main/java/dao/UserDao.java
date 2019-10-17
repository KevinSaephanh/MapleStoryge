package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.User;
import utils.ConnectionUtil;

public class UserDao {
	public List<User> getAllUser() {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM users ORDER BY username";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			List<User> User = new ArrayList<>();
			while (resultSet.next()) {
				User p = extractUser(resultSet);
				User.add(p);
			}
			return User;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public User getUserByName(String username) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			connection.setAutoCommit(false);
			
			String sql = "SELECT * FROM User WHERE username = LOWER(?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				User user = extractUser(resultSet);
				if (user.getUsername().equals(username)) {
					return user;
				}
			}
			return null;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void createUser(User user) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO users (username, password) VALUES(?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateUser(User user, String newUsername) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "UPDATE users SET username = ?, password = ? "
							+ "WHERE username = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, newUsername);
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getUsername());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteUser(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "DELETE FROM users WHERE LOWER(username) = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, user.getUsername());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private User extractUser(ResultSet resultSet) throws SQLException {
		String username = resultSet.getString("username");
		String password = resultSet.getString("password");

		User user = new User(username, password);
		return user;
	}
}
