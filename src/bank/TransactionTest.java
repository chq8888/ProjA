package bank;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testklass för Transaction.
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public class TransactionTest {

	/**
	 * Testmetod för getAccount()
	 */
	@Test
	public void testGetAccountId() {
		Transaction t = new Transaction(1002, "UT", 0, 0);
		Assert.assertEquals(t.getAccountId(), 1002);
	}

	/**
	 * Testmetod för setAccountId()
	 */
	@Test
	public void testSetAccountId() {
		Transaction t = new Transaction(1002, "UT", 0, 0);
		t.setAccountId(1005);
		Assert.assertEquals(t.getAccountId(), 1005);
	}

	/**
	 * Testmetod för getAmount()
	 */
	@Test
	public void testGetAmount() {
		Transaction t = new Transaction(1002, "UT", 100.00, 0);
		Assert.assertEquals(t.getAmount(), 100.00, 0);
	}

	/**
	 * Testmetod för setAmount()
	 */
	@Test
	public void testSetAmount() {
		Transaction t = new Transaction(1002, "UT", 0, 0);
		t.setAmount(100.00);
		Assert.assertEquals(t.getAmount(), 100.00, 0);
	}

	/**
	 * Testmetod för getBalance()
	 */
	@Test
	public void testGetBalance() {
		Transaction t = new Transaction(1002, "UT", 0, 0);
		Assert.assertEquals(t.getBalance(), 0, 0);
	}

	/**
	 * Testmetod för setBalance()
	 */
	@Test
	public void testSetBalance() {
		Transaction t = new Transaction(1002, "UT", 0, 0);
		t.setBalance(5000);
		Assert.assertEquals(t.getBalance(), 5000, 0);
	}

}
