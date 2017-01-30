package bank;

import org.junit.Test;
import org.junit.Assert;

/**
 * Testklass för Customer
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public class CustomerTest {

	/**
	 * Testmetod för setName()
	 */
	@Test
	public void testSetName() {
		Customer c = new Customer("Hamid", 9229);
		c.setName("Hampus");
		Assert.assertEquals(c.getName(), "Hampus");
	}

	/**
	 * Testmetod för setPNr()
	 */
	@Test
	public void testSetPNr() {
		Customer c = new Customer(" ", 1991);
		c.setPNr(1994);
		Assert.assertEquals(c.getPNr(), 1994);
	}
}
