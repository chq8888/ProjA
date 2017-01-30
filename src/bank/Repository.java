package bank;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Repository hanterar alla transaktioner till och från databasen.
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public class Repository {

	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/newtonbank";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	private boolean _debugging = false;

	private Connection _connection;

	/**
	 * Koppla mot newtonbank databas
	 * 
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public void connect() throws SQLException {
		if (_connection == null) {
			_connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
		}
	}

	/**
	 * Koppla från newtonbank databas
	 * 
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public void disconnect() throws SQLException {
		if (_connection != null) {
			_connection.close();
			_connection = null;
		}
	}

	/**
	 * Söka på en viss tabell med en SQL query
	 *
	 * @param query
	 *            en SQL query
	 * @return ResultSet på sök resultat
	 * @throws SQLException
	 *             kastar SQLException
	 */
	private ResultSet executeQuery(String query) throws SQLException {

		Statement _st = _connection.createStatement();
		ResultSet _rs = _st.executeQuery(query);

		if (_debugging)
			System.out.println(query);
		return _rs;
	}

	/**
	 * Lägg till, uppdatera eller ta bort data på en viss tabell med en SQL
	 * query
	 *
	 * @param query
	 *            en SQL query
	 * @param returnKey
	 *            true om primärnyckel skall returnera
	 * @return affected rows eller primärnyckel
	 * @throws SQLException
	 *             kastar SQLException
	 */
	private int executeUpdate(String query, boolean returnKey) throws SQLException {
		Statement _st = _connection.createStatement();
		int _result = -1;

		int _sel = returnKey ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
		_result = _st.executeUpdate(query, _sel);

		if (returnKey) {
			ResultSet _rs = _st.getGeneratedKeys();

			if (_rs.next())
				_result = _rs.getInt(1);
		}

		if (_debugging)
			System.out.println(query);
		return _result;
	}

	/**
	 * Söka på en kund
	 *
	 * @param pNr
	 *            personnr
	 * @return Customer om kunden finns annars null
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public Customer findCustomer(long pNr) throws SQLException {

		ArrayList<Customer> _cust = queryCustomer(String.format("SELECT * FROM Customer WHERE personNr=%d", pNr));

		return _cust.size() > 0 ? _cust.get(0) : null;
	}

	/**
	 * Hämta alla kunder på Customer tabell
	 *
	 * @return en ArrayList av kunder
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public ArrayList<Customer> findAllCustomer() throws SQLException {

		return queryCustomer("SELECT * FROM Customer ORDER BY name");
	}

	/**
	 * Sök Customer tabell med en SQL query
	 * 
	 * @param query
	 *            SQL query
	 * @return en ArrayList av kunder
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public ArrayList<Customer> queryCustomer(String query) throws SQLException {
		ArrayList<Customer> _cust = new ArrayList<>();

		ResultSet _rs = executeQuery(query);

		while (_rs.next()) {
			Customer _c = new Customer(_rs.getString("name"), _rs.getLong("personNr"));
			_cust.add(_c);
		}

		return _cust;
	}

	/**
	 * Söka på ett konto på en viss kund
	 *
	 * @param pNr
	 *            personnr
	 * @param accountId
	 *            kontonr
	 * @return Account om konto finns annars null
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public Account findAccount(long pNr, int accountId) throws SQLException {
		ArrayList<Account> _ac;

		_ac = queryAccount(String.format("SELECT * FROM Account WHERE personNr=%d AND accountId=%d", pNr, accountId));

		return _ac.size() > 0 ? _ac.get(0) : null;
	}

	/**
	 * Hämta alla konton som tillhör en viss kund
	 * 
	 * @param pNr personnummer
	 * @return arrayList av konto
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public ArrayList<Account> findAllAccount(long pNr) throws SQLException {

		return queryAccount(String.format("SELECT * FROM Account WHERE personNr=%d ORDER BY accountId", pNr));
	}

	/**
	 * Sök Account tabell med en SQL query
	 *
	 * @param query
	 *            SQL query
	 * @return en ArrayList av konto
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public ArrayList<Account> queryAccount(String query) throws SQLException {
		ArrayList<Account> _account = new ArrayList<>();

		ResultSet _rs = executeQuery(query);

		while (_rs.next()) {

			switch (_rs.getString("accountType")) {
			case SavingsAccount.ACCOUNT_TYPE:
				SavingsAccount _sa = new SavingsAccount(_rs.getInt("accountId"), _rs.getDouble("rate"));

				_sa.setBalance(_rs.getDouble("balance"));
				_sa.setType(_rs.getString("accountType"));
				_account.add(_sa);
				break;
			case CreditAccount.ACCOUNT_TYPE:
				CreditAccount _ca = new CreditAccount(_rs.getInt("accountId"), _rs.getDouble("rate"),
						_rs.getDouble("creditRate"), _rs.getInt("credit"));

				_ca.setBalance(_rs.getDouble("balance"));
				_ca.setType(_rs.getString("accountType"));
				_account.add(_ca);
			}
		}

		return _account;
	}

	/**
	 * Hämta alla transaktioner som tillhör ett visst konto
	 *
	 * @param accountId
	 *            kontonr
	 * @return arrayList av transaktioner
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public ArrayList<Transaction> findTransaction(int accountId) throws SQLException {

		return queryTransaction(
				String.format("SELECT * FROM Transaction WHERE accountId=%d ORDER BY transDate", accountId));
	}

	/**
	 * Hämta senaste transaktion som tillhör ett visst konto
	 *
	 * @param accountId
	 *            kontonr
	 * @return transaktion eller null om det inte finns några transaktioner
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public Transaction findLastTransaction(int accountId) throws SQLException {
		ArrayList<Transaction> _trans;

		_trans = queryTransaction(String.format(
				"SELECT * FROM Transaction WHERE accountId=%d AND transType='UT' ORDER BY transDate DESC LIMIT 1",
				accountId));

		return _trans.size() > 0 ? _trans.get(0) : null;
	}

	/**
	 * Sök transaktion tabell med en SQL query
	 *
	 * @param query
	 *            SQL query
	 * @return arrayList av transaktionen
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public ArrayList<Transaction> queryTransaction(String query) throws SQLException {
		ArrayList<Transaction> _trans = new ArrayList<>();

		ResultSet _rs = executeQuery(query);

		while (_rs.next()) {
			Transaction _tr = new Transaction(_rs.getInt("accountId"), _rs.getString("transType"),
					_rs.getDouble("amount"), _rs.getDouble("balance"));
			_tr.setTimestamp(_rs.getTimestamp("transDate"));
			_trans.add(_tr);
		}

		return _trans;
	}

	/**
	 * Lägg till ny kund
	 *
	 * @param ct
	 *            Customer objekt
	 * @return affected row
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public int addCustomer(Customer ct) throws SQLException {

		return executeUpdate(
				String.format("INSERT INTO Customer (personNr, name) VALUES(%d, '%s')", ct.getPNr(), ct.getName()),
				false);
	}

	/**
	 * Lägg till nytt konto
	 *
	 * @param ac
	 *            SavingsAccount eller CreditAccount objekt
	 * @param pNr
	 *            personnr till konto
	 * @return generera nyckel om inläggning lyckas annars 0
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public int addAccount(Account ac, long pNr) throws SQLException {
		int _primaryKey;

		if (ac instanceof CreditAccount) {
			CreditAccount _ca = (CreditAccount) ac;
			_primaryKey = executeUpdate(String.format(
					"INSERT INTO Account (personNr, rate, accountType, creditRate, credit) VALUES(%d, %s, '%s', %s, %d)",
					pNr, Double.toString(ac.getRate()), ac.getType(), Double.toString(_ca.getCreditRate()),
					_ca.getCredit()), true);
		} else
			_primaryKey = executeUpdate(
					String.format("INSERT INTO Account (personNr, rate, accountType) VALUES(%d, %s, '%s')", pNr,
							Double.toString(ac.getRate()), ac.getType()),
					true);

		return _primaryKey == 0 ? -1 : _primaryKey;
	}

	/**
	 * Lägg till ny transaktion
	 *
	 * @param tr
	 *            Transaction objekt
	 * @return affected row
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public int addTransaction(Transaction tr) throws SQLException {

		return executeUpdate(String.format(
				"INSERT INTO Transaction (accountId, transType, amount, balance) VALUES(%d, '%s', %s, %s)",
				tr.getAccountId(), tr.getType(), Double.toString(tr.getAmount()), Double.toString(tr.getBalance())),
				false);
	}

	/**
	 * Uppdatera kund info
	 *
	 * @param ct
	 *            Customer objekt
	 * @return true om uppdatering lyckas annars false
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public boolean updateCustomer(Customer ct) throws SQLException {

		return executeUpdate(
				String.format("UPDATE Customer SET name='%s' WHERE personNr=%d", ct.getName(), ct.getPNr()), false) > 0
						? true : false;
	}

	/**
	 * Uppdatera ett konto
	 *
	 * @param ac
	 *            SavingsAccount eller CreditAccount objekt
	 * @return true om uppdatering lyckas annars false
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public boolean updateAccount(Account ac) throws SQLException {

		return executeUpdate(String.format("UPDATE Account SET balance=%s WHERE accountId=%d",
				Double.toString(ac.getBalance()), ac.getId()), false) > 0 ? true : false;
	}

	/**
	 * Ta bort en kund
	 *
	 * @param pNr
	 *            personnr
	 * @return true om borttagning lyckas annars false
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public boolean removeCustomer(long pNr) throws SQLException {

		return executeUpdate(String.format("DELETE FROM Customer WHERE personNr=%d", pNr), false) > 0 ? true : false;
	}

	/**
	 * Ta bort ett konto
	 *
	 * @param accountId
	 *            kontonr
	 * @return true om borttagning lyckas annars false
	 * @throws SQLException
	 *             kastar SQLException
	 */
	public boolean removeAccount(int accountId) throws SQLException {

		return executeUpdate(String.format("DELETE FROM Account WHERE accountId=%d", accountId), false) > 0 ? true
				: false;
	}
}
