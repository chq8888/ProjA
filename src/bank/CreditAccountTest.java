package bank;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testklass för CreditAccount.
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public class CreditAccountTest {

	/**
	 * Testmetod för calculateRate()
	 */
	@Test
	public void testCalculateRate() {
		CreditAccount ca = new CreditAccount(1, 1, 10, 5000);
		ca.setBalance(1000);
		Assert.assertEquals(ca.getCreditRate(), 10, 0.01);
	}

	/**
	 * Testmetod för withdraw()
	 */
	@Test
	public void testWithdraw() {
		CreditAccount ca = new CreditAccount(1, 1, 10, 5000);
		ca.setBalance(2000);
		ca.withdraw(1000);
		Assert.assertEquals(ca.getBalance(), 1000, 0.01);
	}

}
