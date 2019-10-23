package serviceTests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.BeforeClass;
import org.mockito.Mock;
import org.mockito.Mockito;

import services.AccountService;
import utils.ConnectionUtil;

public class AccountServiceTest {
	@Mock
	AccountService accountService = Mockito.mock(AccountService.class);

	@Mock
	Connection connection;

	@Mock
	PreparedStatement preparedStatement;

	@Mock
	ResultSet resultSet;

	@BeforeClass
	public void setUpBeforeClass() throws SQLException {
		connection = ConnectionUtil.getConnection();

		// Create spy objects watching Statement and Connection
		Statement statement = Mockito.spy(connection.createStatement());
		connection = Mockito.spy(connection);
	}
}
