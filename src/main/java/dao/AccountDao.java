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

			if (rs.next()) {
				List<Account> accounts = new ArrayList<>();
				while (rs.next()) {
					Account acc = extractAccount(rs);
					accounts.add(acc);
					System.out.println(acc.toString());
				}
				return accounts;	
			}
			
			// If result set is empty, throw exception
			throw new EmptyTableException("No accounts in the database");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Account getAccountByTitle(String title) throws AccountDoesNotExistException {
		return null;
	}

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
	
	public boolean deleteAccount(int id) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Account extractAccount(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
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
		
		Account acc = new Account(id, mesos, title, at);
		return acc;
	}
}
