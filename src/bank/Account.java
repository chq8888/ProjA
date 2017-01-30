package bank;

/**
 * Account, abstrakt klass, superklass till SavingsAccount och CreditAccount.
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public abstract class Account {
	private double balance;
	private double rate;
	private int accountId;
	private String type;

	/**
	 * Konstruktor till Account
	 * 
	 * @param accountId
	 *            kontonummer
	 */
	public Account(int accountId) {
		this.accountId = accountId;
		balance = 0;
	}

	/**
	 * Returnerar saldo på konto
	 * 
	 * @return saldo på konto
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Returnerar ränta på konto
	 * 
	 * @return ränta på konto
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * Returnerar kontonummer
	 * 
	 * @return kontonummer
	 */
	public int getId() {
		return accountId;
	}

	/**
	 * Sätta ränta på konto
	 * 
	 * @param rate
	 *            ränta på konto
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * Sätta saldo på konto
	 * 
	 * @param balance
	 *            saldo på beloppet
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * Returnerar typ av konto
	 * 
	 * @return vilken typ av konto
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sätta kontotyp
	 * 
	 * @param type
	 *            kontotyp
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Abstrakt metod Returnerar räntan i respektive kontoklass
	 * 
	 * @return räntan i respektive kontoklass
	 */
	public abstract double calculateRate();

	/**
	 * Insättning på konto
	 * 
	 * @param amount
	 *            belopp som skall lägga till konto
	 * @return true om insättning lyckades annars false
	 */
	public boolean deposit(double amount) {
		if (amount > 0) {
			setBalance(balance + amount);
			return true;
		}

		return false;
	}

	/**
	 * Abstrakt metod Uttag från konto
	 * 
	 * @param amount
	 *            belopp som skall ta ut på konto
	 * @return true om uttag lyckas annars false
	 */
	public abstract boolean withdraw(double amount);
}
