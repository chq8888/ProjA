package bank;

import java.sql.Timestamp;

/**
 * Transaction håller information om en transaktion.
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public class Transaction {
	public static final String TYPE_UT = "UT";
	public static final String TYPE_IN = "IN";

	private int accountId;
	private Timestamp timestamp;
	private String transType;
	private double amount;
	private double balance;

	/**
	 * Konstruktor till Transaction
	 *
	 * @param accountId
	 *            kontonr
	 * @param transType
	 *            transaktionstyp dvs insättning/uttag
	 * @param amount
	 *            belopp på transaktion
	 * @param balance
	 *            saldo efter transaktion
	 */
	public Transaction(int accountId, String transType, double amount, double balance) {
		this.accountId = accountId;
		this.transType = transType;
		this.amount = amount;
		this.balance = balance;
	}

	/**
	 * Hämtar konto id
	 * 
	 * @return konto id
	 */
	public int getAccountId() {
		return accountId;
	}

	/**
	 * Sätter konto id
	 * 
	 * @param accountId
	 *            konto id
	 */
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	/**
	 * Hämtar datum och tid för transaktion
	 * 
	 * @return en timestamp objekt
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * Sätter timestamp
	 * 
	 * @param timestamp tid nu
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Hämtar belopp
	 * 
	 * @return belopp
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Sätter belopp
	 * 
	 * @param amount
	 *            belopp
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * Hämtar saldo
	 * 
	 * @return saldo efter transaktion
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Sätter saldo
	 * 
	 * @param balance
	 *            saldo på konto
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * Hämtar transaktionstyp
	 * 
	 * @return transaktionstyp
	 */
	public String getType() {
		return transType;
	}

	/**
	 *  Sätter transaktionstyp            
	 * 
	 * @param transType typ av transaktion
	 */
	public void setType(String transType) {
		this.transType = transType;
	}
}
