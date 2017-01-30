package bank;

/**
 * CreditAccount, subklass till Account.
 * 
 * Håller information om ett kreditkonto och har metoder för bl a transaktioner
 * samt ränteberäkningar.
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public class CreditAccount extends Account {
	public static final String ACCOUNT_TYPE = "KREDITKONTO";

	private double creditRate;
	private int credit;

	/**
	 * Konstruktor för CreditAccount, anropar konstruktorn i superklassen
	 * Account.
	 * 
	 * @param accountId
	 *            kontonummer
	 * @param rate
	 *            räntesats
	 * @param creditRate
	 *            krediträntesats
	 * @param credit
	 *            kreditgräns
	 */
	public CreditAccount(int accountId, double rate, double creditRate, int credit) {
		super(accountId);
		super.setType(ACCOUNT_TYPE);
		super.setRate(rate);
		this.creditRate = creditRate;
		this.credit = credit;
	}

	/**
	 * Returnerar kreditgräns
	 * 
	 * @return kreditgräns
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * Returnerar krediträntesats
	 * 
	 * @return krediträntesats
	 */
	public double getCreditRate() {
		return creditRate;
	}

	/**
	 * Uttag av ett belopp från konto
	 * 
	 * @param amount
	 *            belopp som skall tas ut på konto
	 * @return true om uttag lyckas annars false
	 */
	@Override
	public boolean withdraw(double amount) {
		double balance = getBalance();

		if (balance - amount >= -credit) {
			setBalance(balance - amount);
			return true;
		} else
			return false;
	}

	/**
	 * Beräknar och returnerar räntan. Om saldot är under 0 beräknas räntan mha
	 * krediträntan.
	 * 
	 * @return ränta på konto
	 */
	@Override
	public double calculateRate() {
		double r = getRate();

		if (super.getBalance() < 0) {
			r = getCreditRate();
		}

		return getBalance() * r / 100;
	}
}
