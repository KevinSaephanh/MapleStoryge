package serviceTests;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import dao.AccountDao;
import exceptions.AccountDoesNotExistException;
import models.Account;
import models.AccountType;
import services.AccountService;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {
	@Mock
	private AccountDao accountDao = Mockito.mock(AccountDao.class);

	@Mock
	private AccountService accountService;

	@Before
	public void setUp() {
		accountService = new AccountService(accountDao);
	}
	
	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		Account account = new Account("NewAccount", AccountType.CHECKING);
		int accountId = 20;
		
		Mockito.when(accountDao.getAccountById(accountId)).thenReturn(account);
		accountService.deposit(accountId);
		Mockito.verify(accountDao).getAccountById(accountId);
		
		assertEquals("0 + 100 = 1100", new BigDecimal(100), account.getBalance());
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		Account account = new Account("NewAccount", AccountType.SAVINGS);
		account.setBalance(new BigDecimal(2345));
		int accountId = 20;
		
		Mockito.when(accountDao.getAccountById(accountId)).thenReturn(account);
		accountService.withdraw(accountId);
		Mockito.verify(accountDao).getAccountById(accountId);
		
		assertEquals("2345 - 345 = 2000", new BigDecimal(2000), account.getBalance());
	}

	@Test
	public void testTransferFunds() throws AccountDoesNotExistException {
		Account account1 = new Account("NewAccount", AccountType.SAVINGS);
		int accountId1 = 10;

		account1.setBalance(new BigDecimal(250));
		assertEquals(account1.getBalance(), new BigDecimal(250));

		Account account2 = new Account("DiffAccount", AccountType.SAVINGS);
		int accountId2 = 20;
		
		account2.setBalance(new BigDecimal(1234.32));
		assertEquals(account2.getBalance(), new BigDecimal(1234.32));
		
		Mockito.when(accountDao.transfer(new BigDecimal(200), accountId2, accountId1)).thenReturn(new BigDecimal(450));
		Mockito.verify(accountDao).getAccountById(accountId1);

		Mockito.when(accountDao.getAccountById(accountId2)).thenReturn(account2);
		Mockito.verify(accountDao).getAccountById(accountId2);
	}
}
