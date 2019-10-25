package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.EmptyTableException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import models.User;
import utils.ConnectionUtil;

public class UserDao {
	private Connection conn;

	public UserDao() {
		this.conn = ConnectionUtil.getConnection();
	}
	
	public void setConnection(Connection conn) {
		try {
			if (this.conn != null && !this.conn.isClosed()) {
				System.out.println("Closing connection");
				this.conn.close();
			}
			this.conn = conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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
			
			// If result set is empty, throw exception
			throw new UserDoesNotExistException("Incorrect username/password!");
		} catch (SQLException e) {
			throw new UserDoesNotExistException("Incorrect username/password!");
		}
	}

	public List<User> getAllUsers() throws EmptyTableException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM users ORDER BY user_name";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			// If result set is not empty, traverse it
			if (rs.next()) {
				List<User> users = new ArrayList<>();
				while (rs.next()) {
					User p = extractUser(rs);
					users.add(p);
				}
				return users;
			}
			
			// If result set is empty, throw exception
			throw new EmptyTableException("No users in the database");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public User getUserByUsername(String username) throws UserDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM User WHERE LOWER(user_name) = LOWER(?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();

			// Search for matching username in result set
			while (rs.next()) {
				User user = extractUser(rs);
				if (user.getUsername().equals(username)) {
					return user;
				}
			}
			
			// If username is not in result set, throw exception
			throw new UserDoesNotExistException("User with username: " + username + " does not exist");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserDoesNotExistException("User with username: " + username + " does not exist");
		}
	}
	
	public User getUserById(int id) throws UserDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM User WHERE user_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();

			// Search for matching user id in result set
			while (rs.next()) {
				User user = extractUser(rs);
				if (user.getId() == id) {
					return user;
				}
			}
			
			// If user id is not in result set, throw exception
			throw new UserDoesNotExistException("User with id: " + id + " does not exist");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserDoesNotExistException("User with id: " + id + " does not exist");
		}
	}
	
	public boolean createUser(String username, String password) throws UserAlreadyExistsException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO users (user_name, pass_word) VALUES(?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, username);
			statement.setString(2, password);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserAlreadyExistsException("\nUsername: " + username + " is already in use!\n");
		}
	}

	public int updateUser(int id, String newUsername, String newPassword) throws UserAlreadyExistsException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "UPDATE users SET user_name = ?, pass_word = ? WHERE user_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, newUsername);
			statement.setString(2, newPassword);
			statement.setInt(3, id);
			int updateCount = statement.executeUpdate();

			return updateCount;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserAlreadyExistsException("\nUsername: " + newUsername + " is already in use!\n");
		}
	}

	public int deleteUser(int id) throws UserDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM users WHERE user_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			int deleteCount = statement.executeUpdate();
			
			return deleteCount;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserDoesNotExistException("User with id: " + id + " does not exist");
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
