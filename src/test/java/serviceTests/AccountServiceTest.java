package serviceTests;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import exceptions.AccountAlreadyExistsException;
import exceptions.AccountDoesNotExistException;
import models.Account;
import models.AccountType;
import models.User;
import services.AccountService;
import utils.ConnectionUtil;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {
	@Mock
	private AccountService accountService = Mockito.mock(AccountService.class);

	@Mock
	private static Connection connection;

	@Mock
	private PreparedStatement preparedStatement;

	@Mock
	private ResultSet resultSet;

	@BeforeClass
	public static void setUpBeforeClass() throws SQLException {
		connection = ConnectionUtil.getConnection();

		// Create spy objects watching Connection
		connection = Mockito.spy(connection);
	}
	
	@Before
	public void setUp() throws SQLException {
		Mockito.reset(connection);
		Mockito.reset(preparedStatement);
		Mockito.when(connection.createStatement()).thenReturn(preparedStatement);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// Drop created table after tests complete
		ConnectionUtil.getConnection().createStatement().executeUpdate("DROP TABLE users");
		connection.close();
	}
	
//	@Test
//	public void testCreateAccount() throws AccountAlreadyExistsException, SQLException {
//		Account account = new Account("NewAccount", AccountType.CHECKING);
//		accountService.createAccount(account);
//		
//		Mockito.verify(preparedStatement, Mockito.times(0)).setString(1, "NewAccount");
//		Mockito.verify(preparedStatement, Mockito.times(0)).setString(2, AccountType.CHECKING.toString());
//		Mockito.verify(preparedStatement, Mockito.times(0)).executeUpdate();
//	}
//	
//	@Test
//	public void testCreateSharedAccount() throws SQLException {
//		Account account = new Account(1, new BigDecimal(20) ,"NewAccount", AccountType.CHECKING);
//		User user = new User(3, "NewUser", "NewUser234");
//		accountService.createSharedAccount(account.getId(), user.getId());
//		
//		Mockito.verify(preparedStatement, Mockito.times(0)).setInt(1, account.getId());
//		Mockito.verify(preparedStatement, Mockito.times(0)).setInt(2, user.getId());
//		Mockito.verify(preparedStatement, Mockito.times(0)).executeUpdate();
//	}
//	
//	@Test
//	public void testUpdateAccount() throws AccountAlreadyExistsException, SQLException {
//		Account account = new Account("NewAccount", AccountType.CHECKING);
//		accountService.createAccount(account);
//		accountService.updateAccount(account, "OldAccount");
//		
//		Mockito.verify(preparedStatement, Mockito.times(0)).setString(1, "OldAccount");
//		Mockito.verify(preparedStatement, Mockito.times(0)).setString(2, "NewAccount");
//		Mockito.verify(preparedStatement, Mockito.times(0)).executeUpdate();
//	}
//	
//	@Test
//	public void testDeleteAccount() throws AccountAlreadyExistsException, AccountDoesNotExistException, SQLException {
//		Account account = new Account("NewAccount", AccountType.CHECKING);
//		accountService.createAccount(account);
//		accountService.deleteAccount(account);
//		
//		Mockito.verify(preparedStatement, Mockito.times(0)).executeUpdate();
//	}
//	
//	@Test
//	public void testDeposit() throws SQLException {
//		Account account = new Account(1, new BigDecimal(22.33), "Account", AccountType.SAVINGS);
//		accountService.deposit(account.getId(), new BigDecimal(554.22));
//		
//		Mockito.verify(preparedStatement, Mockito.times(0)).setInt(1, account.getId());
//		Mockito.verify(preparedStatement, Mockito.times(0)).setBigDecimal(2, new BigDecimal(554.22));
//		Mockito.verify(preparedStatement, Mockito.times(0)).executeUpdate();
//	}
//	
//	@Test
//	public void testWithdraw() throws SQLException {
//		Account account = new Account(1, new BigDecimal(2245.54), "Account", AccountType.SAVINGS);
//		accountService.withdraw(account.getId(), new BigDecimal(54.22));
//		
//		Mockito.verify(preparedStatement, Mockito.times(0)).setInt(1, account.getId());
//		Mockito.verify(preparedStatement, Mockito.times(0)).setBigDecimal(2, new BigDecimal(54.22));
//		Mockito.verify(preparedStatement, Mockito.times(0)).executeUpdate();
//	}
	
//	@Test(expected = AccountAlreadyExistsException.class)
//	public void testAccountAlreadyExistsException() throws AccountAlreadyExistsException, SQLException {
//		Account account = new Account("NewAccount", AccountType.CHECKING);
//		accountService.createAccount(account);
//		accountService.createAccount(account);
//	}
//	
//	@Test(expected = AccountDoesNotExistException.class)
//	public void testAccountDoesNotExistException() throws AccountDoesNotExistException, SQLException {
//		Account account = new Account("NewAccountAgain", AccountType.SAVINGS);
//		accountService.deleteAccount(account);
//	}
}
