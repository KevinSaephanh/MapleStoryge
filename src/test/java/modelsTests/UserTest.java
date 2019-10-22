package modelsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import models.User;

public class UserTest {
	private static User user;

	@Before
	public void setUp() {
		User user = new User("newUser", "newPassword");
		assertNotNull(user);
	}

	@AfterClass
	public static void tearDownAfterClass() {
		user = null;
		assertNull(user);
	}

	@Test
	public void testSettersAndGetters() {
		user.setId(32);
		assertEquals(user.getId(), 32);

		user.setUsername("Kevin");
		assertEquals(user.getUsername(), "Kevin");

		user.setPassword("password");
		assertEquals(user.getPassword(), "password");
	}
}
