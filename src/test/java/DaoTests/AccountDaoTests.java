package DaoTests;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.postgresql.core.BaseStatement;

import dao.AccountDao;
import exceptions.EmptyTableException;
import models.Account;
import utils.ConnectionUtil;

@RunWith(MockitoJUnitRunner.class)
public class AccountDaoTests {
	private static AccountDao accountDao = new AccountDao();
	private static Connection connection;
	private static BaseStatement baseStatement;
	
	public AccountDaoTests() throws SQLException {
		
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Create connection object
		Connection connection = ConnectionUtil.getConnection();
		
		// Set schema to testing so that all operations happen on the testing schema
		connection.setSchema("testing");		
		
		// Copy table that will be tested from public to testing with no data
		connection.createStatement()
			.executeUpdate("CREATE TABLE if not exists testing.maplestoryges AS TABLE public.maplestoryges"
					+ " WITH NO DATA");
			
		// Insert a single record for testing purposes
		connection.createStatement()
			.executeUpdate("INSERT INTO testing.maplestoryges "
					+ "(storage_type, title) VALUES ('checking', 'Example')");
		
		// Create spy objects watching Statement and Connection
		Statement statement = Mockito.spy(connection.createStatement());
		baseStatement = (BaseStatement) statement;
		connection = Mockito.spy(connection);
	}
	
	@Before
	public void setUp() throws SQLException {
		Mockito.reset(connection);
		Mockito.reset(baseStatement);
		accountDao.setConnection(connection);
		Mockito.when(connection.createStatement()).thenReturn(baseStatement);
		
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// Drop created table after tests complete
		ConnectionUtil
			.getConnection()
			.createStatement()
			.executeUpdate("drop TABLE testing.maplestoryges");
		
		ConnectionUtil.testMode = false;
	}
	
	@Test
	public void getAllUsersTest() throws SQLException, EmptyTableException {
		List<Account> accounts = accountDao.getAllAccounts();
		Mockito.verify(baseStatement).executeQuery(Mockito.anyString());
	}
}
