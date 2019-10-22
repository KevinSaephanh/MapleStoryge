package services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import models.Account;
import utils.ConnectionUtil;

public class AccountService {
	public boolean createAccount(Account acc) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO maplestoryges (storage_type, title) VALUES(?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, acc.getAccountType().toString());
			statement.setString(2, acc.getTitle());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateAccount(Account acc) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO maplestoryges (title) VALUES(?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, acc.getAccountType().toString());
			statement.setString(2, acc.getTitle());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deposit(Account acc, BigDecimal amount) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "UPDATE maplestoryges SET mesos = mesos + ?";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setBigDecimal(1, amount);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteAccount(int maplestoryge_id) {
		try (Connection connection = ConnectionUtil.getConnection()) {

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
