package bank;

/**
 * SavingsAccount, subklass till Account.
 * 
 * Håller information om ett sparkonto och har metoder för bl a transaktioner
 * samt ränteberäkningar.
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public class SavingsAccount extends Account {
	public static final String ACCOUNT_TYPE = "SPARKONTO";

	private double withdrawRate;

	/**
	 * Konstruktor för SavingsAccount, anropar konstruktorn i superklassen
	 * Account
	 *
	 * @param accountId
	 *            kontonr
	 * @param rate
	 *            räntan på kontot
	 */
	public SavingsAccount(int accountId, double rate) {
		super(accountId);
		super.setType(ACCOUNT_TYPE);
		super.setRate(rate);
		withdrawRate = 0;
	}

	/**
	 * Sätta uttagsränta om kunden gör fler än ett uttag per år
	 * 
	 * @param rate
	 *            uttag räntan
	 */
	public void setWithdrawRate(double rate) {
		withdrawRate = rate;
	}

	/**
	 * Uttag av ett belopp från ett konto
	 * 
	 * @param amount
	 *            belopp som skall ta ut på konto
	 * @return true om uttag lyckas annars false
	 */
	@Override
	public boolean withdraw(double amount) {
		if (amount < 0)
			return false;

		double balance = getBalance();
		double withdraw = amount + (withdrawRate / 100 * amount);

		if (balance - withdraw >= 0) {
			setBalance(balance - withdraw);
			return true;
		} else
			return false;
	}

	/**
	 * Returnerar ränta på konto
	 * 
	 * @return ränta på konto
	 */
	@Override
	public double calculateRate() {
		return getBalance() * getRate() / 100;
	}

}
