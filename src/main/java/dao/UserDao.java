package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.UserDoesNotExistException;
import models.User;
import utils.ConnectionUtil;

public class UserDao {
	// UserDoesNotExistException
	public boolean getUser(String username, String password) throws UserDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM users WHERE user_name = ? AND pass_word = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();

			// Check if result set returned query
			if (rs.next()) {
				String un = rs.getString("user_name");
				String pw = rs.getString("pass_word");
				
				// Compare result set username and password with parameters
				if (un.equals(username) && pw.equals(password)) {
					return true;
				}
			}
			
			// If result set is empty, throw exception
			throw new UserDoesNotExistException("Incorrect username/password!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	// EmptyTableException
	public List<User> getAllUsers() {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM users ORDER BY user_name";
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
	
	// UserDoesNotExistException
	public User getUserByUsername(String username) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			connection.setAutoCommit(false);
			
			String sql = "SELECT * FROM User WHERE LOWER(user_name) = LOWER(?)";
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
	
	// UserAlreadyExistsException
	public boolean createUser(User user) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			if (isUsernameInUse(user.getUsername())) {
				String sql = "INSERT INTO users (user_name, pass_word) VALUES(?, ?)";
				PreparedStatement statement = connection.prepareStatement(sql);
				
				statement.setString(1, user.getUsername());
				statement.setString(2, user.getPassword());
				statement.executeUpdate();
				return true;
			}
			 return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateUser(User user, String newUsername) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "UPDATE users SET user_name = ?, pass_word = ? "
							+ "WHERE username = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, newUsername);
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getUsername());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteUser(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "DELETE FROM users WHERE LOWER(user_name) = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, user.getUsername());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private User extractUser(ResultSet resultSet) throws SQLException {
		String username = resultSet.getString("user_name");
		String password = resultSet.getString("pass_word");

		User user = new User(username, password);
		return user;
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
