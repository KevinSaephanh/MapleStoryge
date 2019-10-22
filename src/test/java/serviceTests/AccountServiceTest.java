package serviceTests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.BeforeClass;
import org.mockito.Mock;

import services.AccountService;
import utils.ConnectionUtil;

public class AccountServiceTest {
	@Mock
	AccountService accountService;
	
	@Mock
	PreparedStatement preparedStatement;
	
	@Mock
	ResultSet resultSet;
	
	@BeforeClass
	public void setUpBeforeClass() {
		Connection conn = ConnectionUtil.getConnection();
	}
}
