package serviceTests;

import static org.mockito.Mockito.when;

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
	private UserService userService;

	@Mock
	private static Connection connection;

	@Mock
	private static PreparedStatement preparedStatement;

	@Mock
	private static ResultSet resultSet;

	@BeforeClass
	public static void setUpBeforeClass() throws SQLException {
		// Create connection object
		Connection connection = ConnectionUtil.getConnection();

		// Set schema to testing so that all operations happen on the testing schema
		connection.setSchema("testing");

		// Copy table that will be tested from public to testing with no data
		connection.createStatement()
				.executeUpdate("CREATE TABLE if not exists testing.users AS TABLE public.users" + " WITH NO DATA");

		// Insert a single record for testing purposes
		connection.createStatement().executeUpdate(
				"INSERT INTO testing.users " + "(user_id, user_name, pass_word) VALUES (4, 'NewUser', 'NewUser3')");

		// Create spy objects watching Statement and Connection
		Statement statement = Mockito.spy(connection.createStatement());
		preparedStatement = (PreparedStatement) statement;
		connection = Mockito.spy(connection);
	}

	@Before
	public void setUp() throws SQLException {
		Mockito.reset(connection);
		Mockito.reset(preparedStatement);
		Mockito.when(connection.createStatement()).thenReturn(preparedStatement);
		
		User newUser = new User(6, "Fellow", "Hello1234");
		
		when(resultSet.first()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        when(resultSet.getString(2)).thenReturn(newUser.getUsername());
        when(resultSet.getString(3)).thenReturn(newUser.getPassword());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// Drop created table after tests complete
		ConnectionUtil.getConnection().createStatement().executeUpdate("DROP TABLE testing.users");
		connection.close();
	}

	@Test
	public void testCreateUser() throws UserAlreadyExistsException {
		try {
			preparedStatement = connection
					.prepareStatement("INSERT INTO testing.users(user_name, pass_word) VALUES (?, ?)");
			preparedStatement.setString(1, "NewGuy");
			preparedStatement.setString(2, "NewGuyInTown4");
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserAlreadyExistsException("User already exists");
		}
	}

	@Test
	public void testUpdateUser() throws UserAlreadyExistsException {
		try {
			preparedStatement = connection
					.prepareStatement("UPDATE testing.users SET user_name = ?, pass_word = ? WHERE user_id = ?");
			preparedStatement.setString(1, "ExperiencedUser");
			preparedStatement.setString(2, "IHaveExperience2");
			preparedStatement.setInt(3, 4);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserAlreadyExistsException("User already exists");
		}
	}
	
	@Test
	public void testDeleteUser() throws UserDoesNotExistException {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM users WHERE user_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, 4);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserDoesNotExistException("User does not exist");
		}
	}
	
	// Test UserDoesNotExistException and UserAlreadyExistsException
}
