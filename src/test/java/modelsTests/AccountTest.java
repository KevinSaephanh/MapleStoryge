package modelsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import models.Account;
import models.AccountType;

public class AccountTest {
	private static Account account;
	
	@Before
	public void setUp() {
		account = new Account("NewAccount", AccountType.SAVINGS);
		assertNotNull(account);
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		account = null;
		assertNull(account);
	}
	
	@Test
	public void testSettersAndGetters() {
		account.setAccountType(AccountType.CHECKING);
		assertEquals(account.getAccountType(), AccountType.CHECKING);
		
		account.setId(2);
		assertEquals(account.getId(), 2);
		
		account.setBalance(new BigDecimal(200.21));
		assertEquals(account.getBalance(), new BigDecimal(200.21));
		
		account.setTitle("NewTitle");
		assertEquals(account.getTitle(), "NewTitle");
	}
}
