package bank;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testklass för SavingsAccount.
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public class SavingsAccountTest {

	/**
	 * Testmetod för calculateRate()
	 */
	@Test
	public void testCalculateRate() {
		SavingsAccount sa = new SavingsAccount(1, 1);
		sa.setBalance(1000);
		Assert.assertEquals(sa.calculateRate(), 10, 0.01);

	}

	/**
	 * Testmetod för withdraw()
	 */
	@Test
	public void testWithdraw() {
		SavingsAccount sa = new SavingsAccount(1, 1);
		sa.setBalance(1000);
		sa.withdraw(100);
		Assert.assertEquals(sa.getBalance(), 900, 0.01);
	}

	/**
	 * Testmetod för setWithdrawRate()
	 */
	@Test
	public void testSetWithdrawRate() {
		SavingsAccount sa = new SavingsAccount(1, 1);
		sa.setBalance(1000);
		sa.setWithdrawRate(10);
		sa.withdraw(100);
		Assert.assertEquals(sa.getBalance(), 890, 0.01);
	}

}
