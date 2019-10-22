package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.AccountDoesNotExistException;
import exceptions.EmptyTableException;
import models.Account;
import models.AccountType;
import utils.ConnectionUtil;

public class AccountDao {
	public List<Account> getAllAccounts() throws EmptyTableException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM maplestoryges ORDER BY title";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			List<Account> accounts = new ArrayList<>();
			while (rs.next()) {
				Account acc = extractAccount(rs);
				accounts.add(acc);
			}
			return accounts;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If result set is empty, throw exception
		throw new EmptyTableException("No accounts in the database");
	}

	public List<Account> getUserAccountsByID(int user_id) throws EmptyTableException {
		return null; // Use joint table
	}

	public Account getAccountByTitle(String title) throws AccountDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM maplestoryges WHERE title = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, title);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				Account acc = extractAccount(rs);
				return acc;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If result set is empty, throw exception
		throw new AccountDoesNotExistException("Account with title: " + title + " does not exist");
	}

	private Account extractAccount(ResultSet rs) throws SQLException {
		int maplestoryge_id = rs.getInt("maplestoryge_id");
		BigDecimal mesos = rs.getBigDecimal("mesos");
		String title = rs.getString("title");
		String accType = rs.getString("storage_type");
		AccountType at = null;

		// Convert account type string to enum
		if (accType.equals("checking")) {
			at = AccountType.CHECKING;
		} else if (accType.equals("savings")) {
			at = AccountType.SAVINGS;
		}

		Account acc = new Account(maplestoryge_id, mesos, title, at);
		return acc;
	}
}
