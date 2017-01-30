package bank;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * Klassen ska hantera en bank med kunder, sparkonto och kreditkonto. Det finns
 * ett antal publika metoder som hanterar kunder och dess konton.
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public class BankLogic {
	private Repository _db;

	/**
	 * Konstruktor
	 */
	public BankLogic() {
		_db = new Repository();
	}

	/**
	 * Hämta en lista som innehåler alla kunder
	 * 
	 * @return en lista med alla kunder
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public ArrayList<String> getCustomers() throws SQLException {
		ArrayList<String> _list = new ArrayList<>();

		_db.connect();
		_list.add("\nPersonnr       Namn");

		for (Customer _cust : _db.findAllCustomer()) {
			_list.add(String.format("%d   %s", _cust.getPNr(), _cust.getName()));
		}

		_db.disconnect();

		return _list;
	}

	/**
	 * Skapar en ny kund, kunden skapas endast om det inte finns någon kund med
	 * personnr
	 *
	 * @param name
	 *            ett namn
	 * @param pNr
	 *            ett personnr
	 * @return affected row om posten är inlagd i tabellen annars 0
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public boolean addCustomer(String name, long pNr) throws SQLException {
		boolean _success;

		_db.connect();
		if (_db.findCustomer(pNr) != null)
			_success = false;
		else
			_success = _db.addCustomer(new Customer(name, pNr)) == 0 ? false : true;

		_db.disconnect();

		return _success;

	}

	/**
	 * Hämta en kund och dess konto som tillhör kunden
	 * 
	 * @param pNr
	 *            ett personnr
	 * @return en lista som innehåller informationen om kunden inklusive dennes
	 *         konton
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public ArrayList<String> getCustomer(long pNr) throws SQLException {
		ArrayList<String> _list = new ArrayList<>();
		;

		_db.connect();

		Customer _cust = _db.findCustomer(pNr);
		if (_cust != null) {
			_list.add(String.format("\r\nPersonnr: %d\r\nNamn: %s\r\n", _cust.getPNr(), _cust.getName()));

			ArrayList<Account> _accounts = _db.findAllAccount(pNr);
			for (Account _ac : _accounts) {
				_list.add(String.format("Kontonr: %d   %s", _ac.getId(), Helper.toUpperCaseLetter(_ac.getType())));
			}
		}

		_db.disconnect();

		return _list;
	}

	/**
	 * Byter namn på en kund
	 *
	 * @param name
	 *            ett namn
	 * @param pNr
	 *            ett personnr
	 * @return true om namnet ändrades annars returneras false
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public boolean changeCustomerName(String name, long pNr) throws SQLException {
		boolean _success = false;

		_db.connect();

		Customer _cust = _db.findCustomer(pNr);
		if (_cust != null) {
			_cust.setName(name);

			_success = _db.updateCustomer(_cust);
		}

		_db.disconnect();

		return _success;
	}

	/**
	 * Tar bort en kund med personnr ur banken, alla kundens eventuella konton
	 * tas också bort och resultatet returneras
	 *
	 * @param pNr
	 *            ett personnr
	 * @return Listan som returneras ska innehålla information om alla konton
	 *         som togs bort, saldot som kunden får tillbaka samt vad räntan
	 *         blev
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public ArrayList<String> removeCustomer(long pNr) throws SQLException {
		double _balance;
		double _rate;
		double _totalBalance = 0;
		double _totalRate = 0;
		ArrayList<String> _result = new ArrayList<>();

		_db.connect();

		Customer _cust = _db.findCustomer(pNr);
		if (_cust != null) {
			ArrayList<Account> _accounts = _db.findAllAccount(pNr);

			for (Account _ac : _accounts) {
				_balance = _ac.getBalance();
				_rate = _ac.calculateRate();
				_result.add(String.format("%15s, Kontonr: %-7d, Saldo: %12.2f kr, Ränta: %7.2f kr",
						Helper.toUpperCaseLetter(_ac.getType()), _ac.getId(), _balance, _rate));

				_totalBalance += _balance;
				_totalRate += _rate;
			}

			_result.add(String.format("\nTotala saldo: %.2f kr", _totalBalance));
			_result.add(String.format("Totala ränta: %.2f kr", _totalRate));

			if (!_db.removeCustomer(pNr))
				_result.clear();
		}

		_db.disconnect();

		return _result;
	}

	/**
	 * Skapar ett sparkonto till en kund
	 * 
	 * @param pNr
	 *            ett persomnr
	 * @return kontonr som det skapade kontot fick, annars -1 om inget konto
	 *         skapades
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public int addSavingsAccount(long pNr) throws SQLException {
		int accountId = -1;

		_db.connect();

		Customer _cust = _db.findCustomer(pNr);
		if (_cust != null) {
			Account _ac = new SavingsAccount(0, 1);
			accountId = _db.addAccount(_ac, pNr);
		}

		_db.disconnect();

		return accountId;
	}

	/**
	 * Skapar ett kreditkonto till en kund
	 * 
	 * @param pNr
	 *            ett persomnr
	 * @return kontonr som det skapade kontot fick, annars -1 om inget konto
	 *         skapades
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public int addCreditAccount(long pNr) throws SQLException {
		int accountId = -1;

		_db.connect();

		Customer _cust = _db.findCustomer(pNr);
		if (_cust != null) {
			Account _ac = new CreditAccount(0, 0.5, 7, 5000);
			accountId = _db.addAccount(_ac, pNr);
		}

		_db.disconnect();

		return accountId;
	}

	/**
	 * Hämta information om en person konto
	 * 
	 * @param pNr
	 *            personnr
	 * @param accountId
	 *            kontonr
	 * @return en sträng som innehåller kontonr, saldo, kontotyp, räntesats om
	 *         det finns konto annars null
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public String getAccount(long pNr, int accountId) throws SQLException {
		String _result = null;

		_db.connect();

		Account _ac = _db.findAccount(pNr, accountId);
		if (_ac != null) {
			if (_ac instanceof SavingsAccount) {
				SavingsAccount _sa = (SavingsAccount) _ac;
				_result = String.format("\nTyp: %s\nKontonr: %d\nSaldo:  %.2f\nRänta: %.2f",
						Helper.toUpperCaseLetter(_sa.getType()), _sa.getId(), _sa.getBalance(), _sa.getRate());
			} else if (_ac instanceof CreditAccount) {
				CreditAccount _ca = (CreditAccount) _ac;
				_result = String.format(
						"\nTyp: %s\nKontonr: %d\nSaldo:  %.2f\nRänta: %.2f\nKredit ränta: %.2f\nKredit: %d",
						Helper.toUpperCaseLetter(_ca.getType()), _ca.getId(), _ca.getBalance(), _ca.getRate(),
						_ca.getCreditRate(), _ca.getCredit());
			}
		}

		_db.disconnect();

		return _result;
	}

	/**
	 * Gör en insättning på konto med kontonr som tillhör kunden
	 * 
	 * @param pNr
	 *            personnr
	 * @param accountId
	 *            ett kontonr
	 * @param amount
	 *            belopp som sätts in
	 * @return true om det lyckades annars false
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public boolean deposit(long pNr, int accountId, double amount) throws SQLException {
		boolean _success = false;

		_db.connect();

		Account _ac = _db.findAccount(pNr, accountId);
		if (_ac != null) {
			_success = _ac.deposit(amount); // insättning i Account

			if (_success) {
				_success = _db.updateAccount(_ac); // insättning i databas

				if (_success) {
					Transaction _tr = new Transaction(_ac.getId(), Transaction.TYPE_IN, amount, _ac.getBalance());
					_db.addTransaction(_tr);
				}
			}
		}

		_db.disconnect();

		return _success;
	}

	/**
	 * Gör ett uttag på konto med kontonr som tillhör kunden
	 * 
	 * @param pNr
	 *            personnr
	 * @param accountId
	 *            ett kontonr
	 * @param amount
	 *            belopp som dras från konto
	 * @return true om det lyckades annars false
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public boolean withdraw(long pNr, int accountId, double amount) throws SQLException {
		boolean _success = false;

		_db.connect();

		Account _ac = _db.findAccount(pNr, accountId);
		if (_ac != null) {

			if (_ac.getType().equals(SavingsAccount.ACCOUNT_TYPE)) {

				Transaction _tr = _db.findLastTransaction(accountId);

				if (_tr != null) {
					SavingsAccount _sa = (SavingsAccount) _ac;
					// check ett fritt uttag/år
					int _diff = LocalDate.now().getYear() - _tr.getTimestamp().toLocalDateTime().getYear();

					if (_diff == 0)
						_sa.setWithdrawRate(2); // uttag avgift på 2% om man gör
												// fler uttag/år
				}
			}

			_success = _ac.withdraw(amount);

			if (_success)
				_success = _db.updateAccount(_ac);

			if (_success) {
				// skapa en trasaktion i databasen
				Transaction _tr = new Transaction(_ac.getId(), Transaction.TYPE_UT, -amount, _ac.getBalance());
				_db.addTransaction(_tr);
			}
		}

		_db.disconnect();

		return _success;
	}

	/**
	 * Ta bort ett konto med kontonr som tillhör kunden
	 *
	 * @param pNr
	 *            ett personnr
	 * @param accountId
	 *            ett kontonr
	 * @return en sträng med saldo och ränta om konto tas bort annars null
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public String closeAccount(long pNr, int accountId) throws SQLException {
		String _result = null;

		_db.connect();

		Account _ac = _db.findAccount(pNr, accountId);
		if (_ac != null) {

			if (_db.removeAccount(accountId)) {
				_result = String.format("\n   Saldo:     %.2f kr\n   Ränta:     %.2f kr\n", _ac.getBalance(),
						_ac.calculateRate());
				_result += "   -------------------------------------------------\n";
				_result += String.format("   Återbetala belopp: %.2f kr\n", _ac.getBalance() + _ac.calculateRate());
			}
		}

		_db.disconnect();

		return _result;
	}

	/**
	 * Hämtar en lista som innehåller presentation av ett konto samt alla
	 * transaktioner som gjorts
	 *
	 * @param pNr
	 *            ett personnr
	 * @param accountId
	 *            ett kontonr
	 * @return ArrayList med alla transaktion på ett konto
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public ArrayList<String> getTransactions(long pNr, int accountId) throws SQLException {
		ArrayList<String> _result = new ArrayList<>();
		ArrayList<Transaction> _trans;

		_db.connect();

		Account _ac = _db.findAccount(pNr, accountId);
		if (_ac != null) {
			_result.add(String.format("\nKontonr: %d     Saldo: %.2f kr     %s (%.2f%%)\n", 
					accountId, _ac.getBalance(), Helper.toUpperCaseLetter(_ac.getType()), _ac.getRate()));

			_trans = _db.findTransaction(accountId);
			for (Transaction _tr : _trans) {
				_result.add(String.format("%s   %s:   %12.2f kr   Saldo: %12.2f kr", 
						_tr.getTimestamp().toString().substring(0, 19), Helper.toUpperCaseLetter(_tr.getType()), _tr.getAmount(), _tr.getBalance()));
			}
		}
		
		_db.disconnect();
		
		return _result;
	}

	/**
	 * Hämtar en lista med alla transaktioner på en kund
	 *
	 * @param pNr
	 *            personnummer
	 * @return ArrayList med alla transaktioner på ett konto
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public ArrayList<String> getAccountSummary(long pNr) throws SQLException {
		ArrayList<String> _result = new ArrayList<>();
		ArrayList<Account> _accounts;
		ArrayList<Transaction> _trans;

		_db.connect();

		Customer _cust = _db.findCustomer(pNr);
		if (_cust != null) {
			_result.add(String.format("Personnr: %d,   Namn: %s,   Datum: %s %s\r\n", _cust.getPNr(), _cust.getName(),
					LocalDate.now(), LocalTime.now().toString().substring(0, 8)));

			_accounts = _db.findAllAccount(pNr);

			for (Account _ac : _accounts) {
				_result.add(String.format("Kontonr: %d  Saldo: %12.2f kr  %s (%.2f%%)", _ac.getId(), _ac.getBalance(),
						Helper.toUpperCaseLetter(_ac.getType()), _ac.getRate()));

				_trans = _db.findTransaction(_ac.getId());

				if (_accounts.size() > 0)
					_result.add("----------------------------------------------------------------------");

				for (Transaction _tr : _trans) {
					_result.add(String.format("%s  %s:   %12.2f kr   Saldo: %12.2f kr",
							_tr.getTimestamp().toString().substring(0, 19), Helper.toUpperCaseLetter(_tr.getType()),
							_tr.getAmount(), _tr.getBalance()));
				}

				if (_accounts.size() > 0)
					_result.add("----------------------------------------------------------------------\r\n");
			}
		}

		_db.disconnect();

		return _result;
	}
}
