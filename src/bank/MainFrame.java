package bank;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * MainFrame innehåller main-metod samt hanterar GUI.
 * 
 * @author Grupp 2 (Hampus B, Åsa W, Anna N, Cam H, Hamid R)
 *
 */
public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = -7920924260406616668L;

	private JTextField _txtPNr;
	private JTextField _txtId;
	private JTextField _txtName;
	private JTextField _txtAmount;
	private JTextArea _taOutput;
	private JButton _btnOk;
	private JComboBox<KeyValue> _cbCommand;
	private Border _bDefaultBorder;
	private BankLogic _bankLogic;

	/**
	 * Konstruktor
	 * 
	 * @param title
	 *            titel
	 */
	public MainFrame(String title) {
		setTitle(title);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);

		// initiera combobox
		_cbCommand = new JComboBox<>();
		_cbCommand.addItem(new KeyValue("cmdAddCustomer", "Lägg till ny kund"));
		_cbCommand.addItem(new KeyValue("cmdShowCustomerList", "Visa kundlista"));
		_cbCommand.addItem(new KeyValue("cmdShowCustomerInfo", "Visa kundinfo"));
		_cbCommand.addItem(new KeyValue("cmdChangeCustomerName", "Ändra kundens namn"));
		_cbCommand.addItem(new KeyValue("cmdRemoveCustomer", "Ta bort kund"));
		_cbCommand.addItem(new KeyValue("cmdDisableAll", "------------------------------------"));
		_cbCommand.addItem(new KeyValue("cmdFindAccount", "Sök konto"));
		_cbCommand.addItem(new KeyValue("cmdAddSavingsAccount", "Lägg till sparkonto"));
		_cbCommand.addItem(new KeyValue("cmdAddCreditAccount", "Lägg till kreditkonto"));
		_cbCommand.addItem(new KeyValue("cmdCloseAccount", "Ta bort konto"));
		_cbCommand.addItem(new KeyValue("cmdDisableAll", "-------------------------------------"));
		_cbCommand.addItem(new KeyValue("cmdDeposit", "Insättning"));
		_cbCommand.addItem(new KeyValue("cmdWithdraw", "Uttag"));
		_cbCommand.addItem(new KeyValue("cmdDisableAll", "---------------------------------------"));
		_cbCommand.addItem(new KeyValue("cmdSaveCustomerList", "Spara kundlista"));
		_cbCommand.addItem(new KeyValue("cmdSaveCustomerInfo", "Spara kundinfo"));
		_cbCommand.addItem(new KeyValue("cmdSaveAccountSummary", "Spara kontohistorik"));

		_cbCommand.addActionListener(this);

		_txtPNr = new JTextField(10);
		_txtId = new JTextField(5);
		_txtName = new JTextField(10);
		_txtAmount = new JTextField(6);

		JPanel _pnlInput = new JPanel(new FlowLayout());
		_pnlInput.add(new JLabel("Personnr: "));
		_pnlInput.add(_txtPNr);
		_pnlInput.add(new JLabel("Kontonr: "));
		_pnlInput.add(_txtId);
		_pnlInput.add(new JLabel("Namn: "));
		_pnlInput.add(_txtName);
		_pnlInput.add(new JLabel("Belopp: "));
		_pnlInput.add(_txtAmount);

		_taOutput = new JTextArea();
		_taOutput.setEditable(false);
		_taOutput.setFont(new Font("Courier New", Font.PLAIN, 14));
		JScrollPane _scOutput = new JScrollPane(_taOutput);
		_scOutput.setPreferredSize(new Dimension(500, 250));

		JPanel _pnlCenter = new JPanel(new BorderLayout());
		_pnlCenter.add(_pnlInput, BorderLayout.PAGE_START);
		_pnlCenter.add(_scOutput, BorderLayout.CENTER);

		_btnOk = new JButton("OK");
		_btnOk.setPreferredSize(new Dimension(50, 50));
		_btnOk.addActionListener(this);

		add(_cbCommand, BorderLayout.PAGE_START);
		add(_pnlCenter, BorderLayout.CENTER);
		add(_btnOk, BorderLayout.PAGE_END);

		pack();

		_bDefaultBorder = _txtPNr.getBorder();
		_cbCommand.setSelectedIndex(0);
	}

	/**
	 * Händelsehanterare för både Combobox och OK-knappen
	 */
	@Override
	public void actionPerformed(ActionEvent ev) {

		if (ev.getSource() == _cbCommand) { // hantera combobox

			String _command = ((KeyValue) _cbCommand.getSelectedItem()).getKey();

			switch (_command) {
			case "cmdAddCustomer":
			case "cmdChangeCustomerName":
				enableComponent(true, false, true, false, true);
				break;
			case "cmdShowCustomerInfo":
			case "cmdRemoveCustomer":
			case "cmdAddSavingsAccount":
			case "cmdAddCreditAccount":
			case "cmdSaveCustomerInfo":
			case "cmdSaveAccountSummary":
				enableComponent(true, false, false, false, true);
				break;
			case "cmdFindAccount":
			case "cmdCloseAccount":
				enableComponent(true, true, false, false, true);
				break;
			case "cmdDeposit":
			case "cmdWithdraw":
				enableComponent(true, true, false, true, true);
				break;
			case "cmdShowCustomerList":
			case "cmdSaveCustomerList":
				enableComponent(false, false, false, false, true);
				break;
			case "cmdDisableAll":
				enableComponent(false, false, false, false, false);
			}

			_btnOk.setActionCommand(_command);

		} else if (ev.getSource() == _btnOk) { // hantera ok-knapp
			Path _path;
			String _name, _data;
			boolean _success;
			long _pNr;
			int _accountId;
			double _amount;
			ArrayList<String> _result;

			_taOutput.setText("");

			if (!validateInput())
				return;

			try {
				_bankLogic = new BankLogic();

				String _command = _btnOk.getActionCommand();

				switch (_command) {
				case "cmdAddCustomer":
					_name = Helper.toUpperCaseLetter(_txtName.getText());
					_pNr = Long.parseLong(_txtPNr.getText());

					_success = _bankLogic.addCustomer(_name, _pNr);

					if (_success)
						displayMessage("Registrering lyckades");
					else
						displayMessage(String.format("Personnr %d finns redan", _pNr));

					break;
				case "cmdChangeCustomerName":
					_name = Helper.toUpperCaseLetter(_txtName.getText());
					_pNr = Long.parseLong(_txtPNr.getText());

					_success = _bankLogic.changeCustomerName(_name, _pNr);

					if (_success)
						displayMessage("Namnändring lyckades");
					else
						displayMessage("Namnändring misslyckades\nKontrollera inmatning");

					break;
				case "cmdRemoveCustomer":
					_pNr = Long.parseLong(_txtPNr.getText());

					_result = _bankLogic.removeCustomer(_pNr);

					if (_result.size() == 0)
						displayMessage("Kunden hittades inte");
					else {
						_data = Helper.listToString(_result);
						_taOutput.setText(_data);
					}

					break;
				case "cmdAddSavingsAccount":
					_pNr = Long.parseLong(_txtPNr.getText());

					_accountId = _bankLogic.addSavingsAccount(_pNr);

					if (_accountId == -1)
						displayMessage("Kan inte lägga till sparkonto. Försök igen.");
					else
						displayMessage(String.format("Nytt sparkonto skapades\nmed kontonr %d", _accountId));

					break;
				case "cmdAddCreditAccount":
					_pNr = Long.parseLong(_txtPNr.getText());

					_accountId = _bankLogic.addCreditAccount(_pNr);

					if (_accountId == -1)
						displayMessage("Kan inte lägga till kreditkonto. Försök igen.");
					else
						displayMessage(String.format("Nytt kreditkonto skapades\nmed kontonr %d", _accountId));

					break;
				case "cmdFindAccount":
					_pNr = Long.parseLong(_txtPNr.getText());
					_accountId = Integer.parseInt(_txtId.getText());
					_result = _bankLogic.getTransactions(_pNr, _accountId);
					
					if (_result.size() == 0)
						displayMessage("Konto finns inte.\nKontrollera personnr och kontonr.");
					else {
						_data = Helper.listToString(_result);
						_taOutput.setText(_data);
					}

					break;
				case "cmdCloseAccount":
					_pNr = Long.parseLong(_txtPNr.getText());
					_accountId = Integer.parseInt(_txtId.getText());

					_data = _bankLogic.closeAccount(_pNr, _accountId);

					if (_data == null)
						displayMessage(String.format("Kontonr %d finns inte", _accountId));
					else
						_taOutput.setText(_data);

					break;
				case "cmdDeposit":
					_pNr = Long.parseLong(_txtPNr.getText());
					_accountId = Integer.parseInt(_txtId.getText());
					_amount = Double.parseDouble(_txtAmount.getText());

					_success = _bankLogic.deposit(_pNr, _accountId, _amount);

					if (_success)
						displayMessage(String.format("Insättning %.2f kr lyckades", _amount));
					else
						displayMessage(String.format("Insättning %.2f kr misslyckades\nKontrollera kontonr och belopp",
								_amount));

					break;
				case "cmdWithdraw":
					_pNr = Long.parseLong(_txtPNr.getText());
					_accountId = Integer.parseInt(_txtId.getText());
					_amount = Double.parseDouble(_txtAmount.getText());

					_success = _bankLogic.withdraw(_pNr, _accountId, _amount);

					if (_success)
						displayMessage(String.format("Uttag %.2f kr lyckades", _amount));
					else
						displayMessage(
								String.format("Uttag %.2f kr misslyckades\nKontrollera kontonr och belopp", _amount));

					break;
				case "cmdShowCustomerList":
				case "cmdSaveCustomerList":
					_result = _bankLogic.getCustomers();
					_data = Helper.listToString(_result);

					if (_command.equals("cmdShowCustomerList")) {
						_taOutput.setText(_data);
					} else {
						_path = getPath();
						if (_path != null)
							Helper.saveToFile(_path, _data);
					}

					break;
				case "cmdSaveCustomerInfo":
				case "cmdShowCustomerInfo":
					_pNr = Long.parseLong(_txtPNr.getText());
					_result = _bankLogic.getCustomer(_pNr);

					if (_result.size() > 0) {
						_data = Helper.listToString(_result);

						if (_command.equals("cmdSaveCustomerInfo")) {
							_path = getPath();
							if (_path != null)
								Helper.saveToFile(_path, _data);
						} else
							_taOutput.setText(_data);
					} else
						displayMessage("Kan inte hitta kunden");

					break;
				case "cmdSaveAccountSummary":
					_pNr = Long.parseLong(_txtPNr.getText());
					_result = _bankLogic.getAccountSummary(_pNr);
					_data = Helper.listToString(_result);

					_path = getPath();
					if (_path != null)
						Helper.saveToFile(_path, _data);

					break;
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	/**
	 * Visa felmeddelande
	 *
	 * @param msg
	 *            meddelande som skall visas
	 */
	private void displayMessage(String msg) {

		JOptionPane.showMessageDialog(this, msg, "Meddelande", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Returnerar sökväg efter användare har valt en fil i FileChoose dialog
	 * 
	 * @return sökväg efter användare har valt en fil i FileChoose dialog
	 */
	private Path getPath() {
		JFileChooser _fc = new JFileChooser();

		_fc.setDialogTitle("Spara som");
		if (_fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

			return Paths.get(_fc.getSelectedFile().getAbsolutePath());
		} else
			return null;
	}

	/**
	 * Returnerar true om validering av alla aktiva textbox-värden är giltiga
	 * 
	 * @return true om validering av alla aktiva textbox-värden är giltiga
	 */
	private boolean validateInput() {
		boolean isOk = true;

		if (_txtPNr.isEnabled() && !Helper.isValidPNr(_txtPNr.getText())) {
			_txtPNr.setBackground(Color.red);
			isOk = false;
		} else
			_txtPNr.setBackground(Color.white);

		if (_txtId.isEnabled() && !Helper.isValidAccountId(_txtId.getText())) {
			_txtId.setBackground(Color.red);
			isOk = false;
		} else
			_txtId.setBackground(Color.white);

		if (_txtName.isEnabled() && !Helper.isValidName(_txtName.getText())) {
			_txtName.setBackground(Color.red);
			isOk = false;
		} else
			_txtName.setBackground(Color.white);

		if (_txtAmount.isEnabled() && !Helper.isValidNr(_txtAmount.getText())) {
			_txtAmount.setBackground(Color.red);
			isOk = false;
		} else
			_txtAmount.setBackground(Color.white);

		if (!isOk)
			Toolkit.getDefaultToolkit().beep();

		return isOk;
	}

	/**
	 * Aktivera eller avaktivera textbox
	 * 
	 * @param selPNr
	 *            true om personnummerinmatning skall aktiveras
	 * @param selId
	 *            true om kontonummerinmatning skall aktiveras
	 * @param selName
	 *            true om namninmatning skall aktiveras
	 * @param selAmount
	 *            true om beloppinmatning skall aktiveras
	 * @param selOkButt
	 *            true om OK-knapp skall aktiveras
	 */
	private void enableComponent(boolean selPNr, boolean selId, boolean selName, boolean selAmount, boolean selOkButt) {
		_txtPNr.setEnabled(selPNr);
		_txtId.setEnabled(selId);
		_txtName.setEnabled(selName);
		_txtAmount.setEnabled(selAmount);
		_btnOk.setEnabled(selOkButt);
		_txtPNr.setBackground(Color.white);
		_txtId.setBackground(Color.white);
		_txtName.setBackground(Color.white);
		_txtAmount.setBackground(Color.white);

		if (selPNr) {
			_txtPNr.setBorder(BorderFactory.createLineBorder(Color.RED));
		} else {
			_txtPNr.setText("");
			_txtPNr.setBorder(_bDefaultBorder);
		}

		if (selId) {
			_txtId.setBorder(BorderFactory.createLineBorder(Color.RED));
		} else {
			_txtId.setText("");
			_txtId.setBorder(_bDefaultBorder);
		}

		if (selName) {
			_txtName.setBorder(BorderFactory.createLineBorder(Color.RED));
		} else {
			_txtName.setText("");
			_txtName.setBorder(_bDefaultBorder);
		}

		if (selAmount) {
			_txtAmount.setBorder(BorderFactory.createLineBorder(Color.RED));
		} else {
			_txtAmount.setText("");
			_txtAmount.setBorder(_bDefaultBorder);
		}
	}

	/**
	 * Main-metod
	 * 
	 * @param args
	 *            args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame _frame = new MainFrame("Newton Bank");
				_frame.setVisible(true);
			}
		});
	}
}

/**
 * Denna klassen används i Combobox Key/Value
 */
class KeyValue {
	private String _key;
	private String _value;

	/**
	 * Konstruktor
	 * 
	 * @param key
	 *            namn på nyckel
	 * @param value
	 *            värde på nyckel
	 */
	public KeyValue(String key, String value) {
		_key = key;
		_value = value;
	}

	/**
	 * @return nyckel
	 */
	public String getKey() {
		return _key;
	}

	/**
	 * @return värde
	 */
	public String getValue() {
		return _value;
	}

	/**
	 * @return string på denna objekt
	 */
	public String toString() {
		return _value;
	}
}
