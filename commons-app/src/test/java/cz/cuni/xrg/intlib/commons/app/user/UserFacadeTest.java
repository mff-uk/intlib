package cz.cuni.xrg.intlib.commons.app.user;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for facade persisting {@link User}s.
 *
 * @author Jan Vojt
 */
@ContextConfiguration(locations = {"classpath:commons-app-test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback=true)
public class UserFacadeTest {
	
	@Autowired
	private UserFacade facade;

	/**
	 * Test of getAllUsers method, of class UserFacade.
	 */
	@Test @Transactional
	public void testGetAllUsers() {
		List<User> users = facade.getAllUsers();
		assertNotNull(users);
		assertEquals(1, users.size());
		
		User user = users.get(0);
		assertNotNull(user);
		assertNotNull(user.getRoles());
		assertEquals(2, user.getRoles().size());
	}

	/**
	 * Test of getUser method, of class UserFacade.
	 */
	@Test @Transactional
	public void testGetUser() {
		User user = facade.getUser(1L);
		assertNotNull(user);
		assertNotNull(user.getRoles());
		assertEquals(2, user.getRoles().size());
	}

	/**
	 * Test of save method, of class UserFacade.
	 */
	@Test @Transactional
	public void testSave() {
		User user = new User("Jay Doe", "abcd", "jay@example.com");
		facade.save(user);
		User u = facade.getUser(2L);
		assertEquals(user.getName(), u.getName());
		assertEquals(user.getPassword(), u.getPassword());
		assertEquals(user.getEmail(), u.getEmail());
	}

	/**
	 * Test of delete method, of class UserFacade.
	 */
	@Test @Transactional
	public void testDelete() {
		User user = mock(User.class);
		when(user.getId()).thenReturn(1L);
		
		facade.delete(user);
		List<User> users = facade.getAllUsers();
		assertNotNull(users);
		assertTrue(users.isEmpty());
	}
}