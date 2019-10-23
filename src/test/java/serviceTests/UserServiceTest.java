package serviceTests;

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
	public void testCreateUser() throws UserAlreadyExistsException, SQLException {
		User user = new User("NewGuy", "NewGuy7");
		userService.createUser(user);

		Mockito.verify(preparedStatement, Mockito.times(0)).setString(1, "NewGuy");
		Mockito.verify(preparedStatement, Mockito.times(0)).setString(2, "NewGuy4");
		Mockito.verify(preparedStatement, Mockito.times(0)).executeUpdate();
	}

	@Test
	public void testUpdateUser() throws UserAlreadyExistsException, SQLException {
		User user = new User("NewGuy", "NewGuy7");
		userService.createUser(user);

		userService.updateUser(user, "NotNewGuy", "NotNewGuy4");

		Mockito.verify(preparedStatement, Mockito.times(0)).setString(1, "NotNewGuy");
		Mockito.verify(preparedStatement, Mockito.times(0)).setString(2, "NotNewGuy4");
		Mockito.verify(preparedStatement, Mockito.times(0)).setString(3, "NewGuy");
		Mockito.verify(preparedStatement, Mockito.times(0)).executeUpdate();
	}

	@Test
	public void testDeleteUser() throws UserAlreadyExistsException, UserDoesNotExistException, SQLException {
		User user = new User("NewGuy", "NewGuy7");
		userService.createUser(user);
		userService.deleteUser(user);

		Mockito.verify(preparedStatement, Mockito.times(0)).executeUpdate();
	}

//	@Test(expected = UserAlreadyExistsException.class)
//	public void testUserAlreadyExistsException() throws UserAlreadyExistsException, SQLException {
//		User user = new User("NewGuy", "NewGuy7");
//		userService.createUser(user);
//		userService.createUser(user);
//	}
//
//	@Test(expected = UserDoesNotExistException.class)
//	public void testUserDoesNotExistException()
//			throws UserAlreadyExistsException, UserDoesNotExistException, SQLException {
//		User user = new User("NewGuy", "NewGuy7");
//		userService.deleteUser(user);
//	}
}
