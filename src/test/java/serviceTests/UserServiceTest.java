package serviceTests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import models.User;
import services.UserService;
import utils.ConnectionUtil;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	@Mock
	private UserService userService = Mockito.mock(UserService.class);

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

	@Test
	public void testCreateValidUser() throws UserAlreadyExistsException, SQLException {
		User user = new User("NewGuy", "NewGuy7");
		userService.createUser(user);
	}

	@Test
	public void testUpdateUser() throws UserAlreadyExistsException, SQLException {
		
	}
	
	@Test
	public void testDeleteUser() throws UserDoesNotExistException, SQLException {
		
	}
	
	@Test (expected = UserAlreadyExistsException.class)
	public void testUserAlreadyExistsException() throws UserAlreadyExistsException, SQLException {
		User user1 = new User("NewGuy", "NewGuy7");
		userService.createUser(user1);
		
		User user2 = new User("NewGuy", "NewGuy7");
		boolean created = userService.createUser(user2);
		
		if (!created) {
			throw new UserAlreadyExistsException("User with username NewGuy already exists");
		}
	}
	
	@Test (expected = UserDoesNotExistException.class)
	public void testUserDoesNotExistException() throws UserDoesNotExistException, SQLException {
		int deleteCount = userService.deleteUser(255);
		
		if (deleteCount < 1) {
			throw new UserDoesNotExistException("User with id 255 does not exist");
		}
	}
}
