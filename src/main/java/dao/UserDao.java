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
	// UserDoesNotExistException
	public boolean getUser(String username, String password) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM users WHERE user_name = '?' AND pass_word = '?'";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();
			
			if (rs.isBeforeFirst()) {
				System.out.println("MATCH");
				return true;
			}
			return false;
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
	
	public boolean createUser(User user) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			if (doesUserExist(user.getUsername())) {
				String sql = "INSERT INTO users (user_name, pass_word) VALUES(?, ?)";
				PreparedStatement statement = connection.prepareStatement(sql);
				
				statement.setString(1, user.getUsername());
				statement.setString(2, user.getPassword());
				statement.executeUpdate();
				return true;
			}
			 return false;
//			String sql = "INSERT INTO users (user_name, pass_word) VALUES(?, ?)";
//			PreparedStatement statement = connection.prepareStatement(sql);
//			
//			statement.setString(1, user.getUsername());
//			statement.setString(2, user.getPassword());
//			statement.executeUpdate();
//			return true;
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
            String sql = "DELETE FROM users WHERE LOWER(username) = ?";
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
	
	// UserAlreadyExistsException
	private boolean doesUserExist(String username) {
		try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM users WHERE user_name = '?'";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			// Check if result set returned empty, hence the username is unique
			if (!resultSet.isBeforeFirst()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
