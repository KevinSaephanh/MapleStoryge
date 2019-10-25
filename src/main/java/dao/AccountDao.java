package dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountDoesNotExistException;
import exceptions.EmptyTableException;
import exceptions.UserDoesNotExistException;
import models.Account;
import models.AccountType;
import services.AccountValidatorService;
import utils.ConnectionUtil;

public class AccountDao {
	private Connection conn;

	public AccountDao() {
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

	public List<Account> getSpecificUsersAccounts(int userId) throws UserDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM maplestoryges JOIN users_maplestoryges USING(maplestoryge_id) WHERE user_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, userId);

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

		throw new UserDoesNotExistException("User with id: " + userId + " does not exist");
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

	public Account getAccountById(int id) throws AccountDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM maplestoryges WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				Account acc = extractAccount(rs);
				return acc;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// If result set is empty, throw exception
		throw new AccountDoesNotExistException("Account with id: " + id + " does not exist");
	}

	public int createAccount(AccountType at, String title) throws AccountAlreadyExistsException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			if (!AccountValidatorService.isTitleInUse(title)) {
				// Create the account record in the database under table maplestoryges
				String sql = "INSERT INTO maplestoryges (storage_type, title) VALUES(?, ?) RETURNING maplestoryge_id";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, at.toString());
				statement.setString(2, title);
				statement.execute();

				ResultSet rs = statement.getResultSet();
				if (rs.next()) {
					int accountId = rs.getInt("maplestoryge_id");
					return accountId;
				}
			}

			// If title is in use, throw exception
			throw new AccountAlreadyExistsException("Title: " + title + " is already in use!");
		} catch (SQLException e) {
			throw new AccountAlreadyExistsException("Title: " + title + " is already in use!");
		}
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

	public int deleteAccount(int id) throws AccountDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM maplestoryges WHERE maplestoryge_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			int deleteCount = statement.executeUpdate();

			return deleteCount;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountDoesNotExistException("Storage with id: " + id + " does not exist");
		}
	}

	public int createSharedAccount(int userId, int accountId) {
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

	public int deleteSharedAccount(int id) throws AccountDoesNotExistException {
		Connection connection = ConnectionUtil.getConnection();
		try {
			connection.setAutoCommit(false);
			
			String sql = "DELETE FROM users_maplestoryges WHERE maplestoryge_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();

			connection.commit();
			connection.close();
			return 1;
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
			
			throw new AccountDoesNotExistException("Shared storage with id: " + id + " does not exist");
		}
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

	public BigDecimal transfer(BigDecimal transferAmount, int withdrawAccId, int depositAccId) {
		Connection connection = ConnectionUtil.getConnection();
		try {
			connection.setAutoCommit(false);

			String withdrawSQL = "UPDATE maplestoryges SET mesos = mesos - ? WHERE maplestoryge_id = ? RETURNING mesos";
			String depositSQL = "UPDATE maplestoryges SET mesos = mesos + ? WHERE maplestoryge_id = ?";
			PreparedStatement withdrawStatement = connection.prepareStatement(withdrawSQL);
			PreparedStatement depositStatement = connection.prepareStatement(depositSQL);

			// Set and execute deposit statement
			depositStatement.setBigDecimal(1, transferAmount);
			depositStatement.setInt(2, depositAccId);
			depositStatement.executeUpdate();

			// Set and execute withdraw statement
			withdrawStatement.setBigDecimal(1, transferAmount);
			withdrawStatement.setInt(2, withdrawAccId);
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
