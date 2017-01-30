package bank;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testklass för Account
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public class AccountTest {

	/**
	 * Testmetod för setRate()
	 */
	@Test
	public void testSetRate() {
		SavingsAccount sa = new SavingsAccount(1, 1);
		sa.setRate(2);
		Assert.assertEquals(sa.getRate(), 2, 0.01);

	}

	/**
	 * Testmetod för setBalance()
	 */
	@Test
	public void testSetBalance() {
		SavingsAccount sa = new SavingsAccount(1, 1);
		sa.setBalance(1000);
		Assert.assertEquals(sa.getBalance(), 1000, 0.01);
	}

	/**
	 * Testmetod för deposit()
	 */
	@Test
	public void testDeposit() {
		SavingsAccount sa = new SavingsAccount(1, 1);
		sa.deposit(1000);
		Assert.assertEquals(sa.getBalance(), 1000, 0.01);
	}

}
