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

import dao.UserDao;
import exceptions.EmptyTableException;
import models.User;
import utils.ConnectionUtil;

@RunWith(MockitoJUnitRunner.class)
public class UserDaoTests {
	private static UserDao userDao = new UserDao();
	private static Connection connection;
	private static BaseStatement baseStatement;
	
	public UserDaoTests() throws SQLException {
		super();
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Create connection object
		Connection connection = ConnectionUtil.getConnection();
		
		// Set schema to testing so that all operations happen on the testing schema
		connection.setSchema("testing");		
		
		// Copy table that will be tested from public to testing with no data
		connection.createStatement()
			.executeUpdate("CREATE TABLE if not exists testing.users AS TABLE public.users"
					+ " WITH NO DATA");
			
		// Insert a single record for testing purposes
		connection.createStatement()
			.executeUpdate("INSERT INTO testing.users "
					+ "(user_name, pass_word) VALUES ('TestUser', 'Testing123')");
		
		// Create spy objects watching Statement and Connection
		Statement statement = Mockito.spy(connection.createStatement());
		baseStatement = (BaseStatement) statement;
		connection = Mockito.spy(connection);
	}
	
	@Before
	public void setUp() throws SQLException {
		Mockito.reset(connection);
		Mockito.reset(baseStatement);
		userDao.setConnection(connection);
		Mockito.when(connection.createStatement()).thenReturn(baseStatement);
		
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// Drop created table after tests complete
		ConnectionUtil
			.getConnection()
			.createStatement()
			.executeUpdate("drop TABLE testing.users");
		
		ConnectionUtil.testMode = false;
	}
	
	@Test
	public void getAllUsersTest() throws SQLException, EmptyTableException {
		List<User> users = userDao.getAllUsers();
		Mockito.verify(baseStatement).executeQuery(Mockito.anyString());
	}
}
