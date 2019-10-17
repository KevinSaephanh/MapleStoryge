package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.User;
import utils.ConnectionUtil;

public class UserDao {
	public List<User> getAllUser() {
		try (Connection connection = ConnectionUtil.getConnection()) {
			Statement statement = connection.createStatement();
			String query = "SELECT * FROM User";
			ResultSet resultSet = statement.executeQuery(query);

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
	
	public List<User> getUserByName(String username) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM User WHERE username = LOWER(?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();
			
			List<User> users = new ArrayList<>();
			while (resultSet.next()) {
				User user = extractUser(resultSet);
				users.add(user);
			}
			return users;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private User extractUser(ResultSet resultSet) throws SQLException {
		int id = resultSet.getInt("id");
		String username = resultSet.getString("username");
		String password = resultSet.getString("password");

		User user = new User(username, password);
		return user;
	}
}
