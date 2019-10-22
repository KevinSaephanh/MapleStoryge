package services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exceptions.AccountDoesNotExistException;
import exceptions.AccountAlreadyExistsException;
import models.Account;
import utils.ConnectionUtil;

public class AccountService {
	public boolean createAccount(Account acc) throws AccountAlreadyExistsException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			if (!isTitleInUse(acc.getTitle())) {
				String sql = "INSERT INTO maplestoryges (storage_type, title) VALUES(?, ?)";
				PreparedStatement statement = connection.prepareStatement(sql);

				statement.setString(1, acc.getAccountType().toString());
				statement.setString(2, acc.getTitle());
				statement.executeUpdate();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If title is in use, throw exception
		throw new AccountAlreadyExistsException("Title: " + acc.getTitle() + " is already in use!");
	}

	public int updateAccount(Account acc, String title) throws AccountAlreadyExistsException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			if (!isTitleInUse(acc.getTitle())) {
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

	public int deleteAccount(int maplestoryge_id) throws AccountDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM maplestoryges WHERE maplestoryge_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, maplestoryge_id);
			int deleteCount = statement.executeUpdate();

			return deleteCount;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If account does not exist, throw exception
		throw new AccountDoesNotExistException("Account with id: " + maplestoryge_id + " does not exist");
	}

	public BigDecimal deposit(Account acc, BigDecimal amount) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "UPDATE maplestoryges SET mesos = mesos + ? WHERE maplestoryge_id = ? RETURNING mesos";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setBigDecimal(1, amount);
			statement.setInt(2, acc.getId());
			statement.execute();

			// Get result set from RETURNING clause
			ResultSet rs = statement.getResultSet();
			if (rs.next()) {
				BigDecimal balance = rs.getBigDecimal("mesos");
				return balance;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public BigDecimal withdraw(Account acc, BigDecimal amount) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "UPDATE maplestoryges SET mesos = mesos - ? WHERE maplestoryge_id = ? RETURNING mesos";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setBigDecimal(1, amount);
			statement.setInt(2, acc.getId());
			statement.execute();

			// Get result set from RETURNING clause
			ResultSet rs = statement.getResultSet();
			if (rs.next()) {
				BigDecimal balance = rs.getBigDecimal("mesos");
				return balance;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private boolean isTitleInUse(String title) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM maplestoryges WHERE title = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, title);
			ResultSet rs = statement.executeQuery();

			// If result set is not empty, title is in use
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}
