package services;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountDoesNotExistException;
import models.Account;
import utils.ConnectionUtil;

public class AccountService {
	public int createAccount(Account acc) throws AccountAlreadyExistsException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			if (!AccountValidatorService.isTitleInUse(acc.getTitle())) {
				// Create the account record in the database under table maplestoryges
				String sql = "INSERT INTO maplestoryges (storage_type, title) VALUES(?, ?) RETURNING maplestoryge_id";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, acc.getAccountType().toString());
				statement.setString(2, acc.getTitle());
				statement.execute();

				ResultSet rs = statement.getResultSet();
				if (rs.next()) {
					int accountId = rs.getInt("maplestoryge_id");
					return accountId;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If title is in use, throw exception
		throw new AccountAlreadyExistsException("Title: " + acc.getTitle() + " is already in use!");
	}

	public int updateAccount(Account acc, String title) throws AccountAlreadyExistsException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			if (!AccountValidatorService.isTitleInUse(acc.getTitle())) {
				String sql = "UPDATE maplestoryges SET title = ? WHERE maplestoryge_id = ?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, title);
				statement.setInt(2, acc.getId());
				int updateCount = statement.executeUpdate();

				return updateCount;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If title is in use, throw exception
		throw new AccountAlreadyExistsException("Title: " + acc.getTitle() + " is already in use!");
	}

	public int deleteAccount(Account acc) throws AccountDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM maplestoryges WHERE maplestoryge_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, acc.getId());
			int deleteCount = statement.executeUpdate();

			return deleteCount;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If account does not exist, throw exception
		throw new AccountDoesNotExistException("Account with id: " + acc.getId() + " does not exist");
	}

	public int createSharedAccount(int accountId, int userId) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			// Create the account record in the database under table maplestoryges
			String sql = "INSERT INTO users_maplestoryges (user_id, maplestoryge_id) VALUES(?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, userId);
			statement.setInt(2, accountId);

			int createCount = statement.executeUpdate();
			return createCount;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public BigDecimal deposit(int id, BigDecimal amount) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "{call deposit(?, ?)}";
			CallableStatement statement = connection.prepareCall(sql);
			
			statement.setInt(1, id);
			statement.setBigDecimal(2, amount);
			statement.execute();

			// Get new balance from result set
			ResultSet rs = statement.getResultSet();
			if (rs.next()) {
				BigDecimal balance = rs.getBigDecimal(1);
				return balance;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public BigDecimal withdraw(int id, BigDecimal amount) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "{call withdraw(?, ?)}";
			CallableStatement statement = connection.prepareCall(sql);
			
			statement.setInt(1, id);
			statement.setBigDecimal(2, amount);
			statement.execute();

			// Get new balance from result set
			ResultSet rs = statement.getResultSet();
			if (rs.next()) {
				BigDecimal balance = rs.getBigDecimal(1);
				return balance;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public BigDecimal transfer(BigDecimal transferAmount, String withdrawAccTitle, String depositAccTitle) {
		Connection connection = ConnectionUtil.getConnection();
		try {
			connection.setAutoCommit(false);

			String withdrawSQL = "UPDATE maplestoryges SET mesos = mesos - ? WHERE title = ? RETURNING mesos";
			String depositSQL = "UPDATE maplestoryges SET mesos = mesos + ? WHERE title = ?";
			PreparedStatement withdrawStatement = connection.prepareStatement(withdrawSQL);
			PreparedStatement depositStatement = connection.prepareStatement(depositSQL);

			// Set and execute deposit statement
			depositStatement.setBigDecimal(1, transferAmount);
			depositStatement.setString(2, depositAccTitle);
			depositStatement.executeUpdate();

			// Set and execute withdraw statement
			withdrawStatement.setBigDecimal(1, transferAmount);
			withdrawStatement.setString(2, withdrawAccTitle);
			withdrawStatement.execute();

			// Get result set from RETURNING clause
			ResultSet rs = withdrawStatement.getResultSet();
			if (rs.next()) {
				BigDecimal balance = rs.getBigDecimal("mesos");
				connection.commit();
				connection.close();
				return balance;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("An error occurred, time to rollback!");

			// Attempt to rollback to last committed state
			try {
				connection.rollback();
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}
}
