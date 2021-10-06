package com.nitya.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.combo.AccountCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Umasree V
 * 
 */
public class AssignAccountsTo1099Dialog extends BaseDialog {

	private FlexTable flexTable;
	private String[] strings;
	private int[] boxNums = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14 };
	private int rowCount = 0;
	private HashMap<Integer, BoxAccount> boxAccounts;
	private Anchor anchor;

	public AssignAccountsTo1099Dialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("AssignAccountsTo1099Dialog");
		AccounterMessages c = messages;
		strings = new String[] { c.box1Label(), c.box2Label(), c.box3Label(),
				c.box4Label(), c.box5Label(), c.box6Label(), c.box7Label(),
				c.box8Label(), c.box9Label(), c.box10Label(), c.box13Label(),
				c.box14Label() };
		boxAccounts = new HashMap<Integer, AssignAccountsTo1099Dialog.BoxAccount>();
		loadAccounts();
		createControls();
	}

	private void loadAccounts() {
		for (ClientAccount element : getCompany().getActiveAccounts()) {
			int boxNumber = element.getBoxNumber();
			if (boxNumber != 0) {
				BoxAccount boxAccount = boxAccounts.get(boxNumber);
				if (boxAccount == null) {
					boxAccount = new BoxAccount();
					boxAccount.isMultiple = false;
				} else {
					boxAccount.isMultiple = true;
				}
				boxAccount.accounts.add(element);
				boxAccounts.put(boxNumber, boxAccount);
			}
		}

	}

	private void createControls() {
		flexTable = new FlexTable();
		flexTable.insertRow(rowCount);
		flexTable.setWidget(rowCount, 0, new Label(messages.use()));
		flexTable.setWidget(rowCount, 1,
				new Label(messages.get1099Information()));
		flexTable.setWidget(rowCount, 2, new Label(messages.Account()));
		rowCount++;
		for (String string : strings) {
			addRow(string, boxNums[rowCount - 1]);
		}

		setBodyLayout(flexTable);
	}

	private void addRow(String string, final int boxNo) {
		final ArrayList<ClientAccount> activeAccounts = getCompany()
				.getActiveAccounts();// getAccounts();
		final CheckBox checkBox = new CheckBox();
		Label label = new Label(string);
		final AccountCombo accountCombo = new AccountCombo("", true) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return activeAccounts;
			}
		};
		accountCombo.setEnabled(false);

		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						BoxAccount account = boxAccounts.get(boxNo);
						if (account == null) {
							account = new BoxAccount();
						}
						account.isMultiple = false;
						account.accounts = new ArrayList<ClientAccount>();
						account.accounts.add(accountCombo.getSelectedValue());
						boxAccounts.put(boxNo, account);
					}
				});

		anchor = new Anchor(messages.selectMultiple());

		checkBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (checkBox.isChecked()) {
					accountCombo.setEnabled(true);
				} else {
					accountCombo.setEnabled(false);
					accountCombo.setValue(null);
				}
			}
		});
		ClientAccount clientAccount = getAccountByBoxNum(boxNo);
		if (clientAccount != null) {
			checkBox.setChecked(true);
			accountCombo.setComboItem(clientAccount);
			accountCombo.setEnabled(true);
		}
		DynamicForm accountsform = new DynamicForm("accountsform");
		accountCombo.setTitle(messages.Accounts());
		accountsform.add(accountCombo);

		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (checkBox.isChecked()) {
					String accountsToAssign = messages.selectAccountsToAssign();
					final SelectItemsTo1099Dialog<ClientAccount> selectItemsTo1099Dialog = new SelectItemsTo1099Dialog<ClientAccount>(
							accountsToAssign, accountsToAssign);
					ArrayList<ClientAccount> selectedAccounts = new ArrayList<ClientAccount>();
					BoxAccount boxAccount = boxAccounts.get(boxNo);
					if (boxAccount != null) {
						selectedAccounts = boxAccount.accounts;
					}
					selectItemsTo1099Dialog.setSelectedItems(selectedAccounts);
					selectItemsTo1099Dialog.setAvailableItems(getCompany()
							.getActiveAccounts());

					selectItemsTo1099Dialog
							.setCallBack(new ActionCallback<ArrayList<ClientAccount>>() {

								@Override
								public void actionResult(
										ArrayList<ClientAccount> result) {
									// for (ClientAccount account :
									// selectItemsTo1099Dialog.tempSelectedItemsList)
									// {
									// account.setBoxNumber(boxNo);
									// saveOrUpdate(account);
									// }
									// for (ClientAccount account :
									// selectItemsTo1099Dialog.tempAvailItemsList)
									// {
									// if (account.getBoxNumber() == boxNo) {
									// account.setBoxNumber(0);
									// saveOrUpdate(account);
									// }
									// }
									BoxAccount account = boxAccounts.get(boxNo);
									if (account == null) {
										account = new BoxAccount();
									}
									account.isMultiple = true;
									account.accounts = result;
									boxAccounts.put(boxNo, account);

								}
							});
					ViewManager.getInstance().showDialog(
							selectItemsTo1099Dialog);

				}
			}

		});

		flexTable.insertRow(rowCount);
		flexTable.setWidget(rowCount, 0, checkBox);
		flexTable.setWidget(rowCount, 1, label);
		flexTable.setWidget(rowCount, 2, accountsform);
		flexTable.setWidget(rowCount, 3, anchor);

		rowCount++;
	}

	// private ArrayList<ClientAccount> getAccountsByBoxNum(int boxNo) {
	// ArrayList<ClientAccount> accounts = new ArrayList<ClientAccount>();
	// Session session = HibernateUtil.getCurrentSession();
	// org.hibernate.Transaction transaction = session.beginTransaction();
	//
	// Query query = session.getNamedQuery("get.accounts.by.boxNumber")
	// .setParameter("boxNo", boxNo);
	// ArrayList<Account> list = (ArrayList<Account>) query.list();
	// for (Account account : list) {
	// ClientConvertUtil clientConvertUtil = new ClientConvertUtil();
	// ClientAccount clientObject;
	// try {
	// clientObject = clientConvertUtil.toClientObject(account,
	// ClientAccount.class);
	//
	// accounts.add(clientObject);
	// } catch (AccounterException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// return accounts;
	//
	// }

	private ClientAccount getAccountByBoxNum(int i) {
		ArrayList<ClientAccount> activeAccounts = getCompany()
				.getActiveAccounts();
		for (ClientAccount clientAccount : activeAccounts) {
			if (clientAccount.getBoxNumber() == i) {
				return clientAccount;
			}
		}
		return null;
	}

	private ClientAccount getAccountByID(long id) {
		ArrayList<ClientAccount> activeAccounts = getCompany()
				.getActiveAccounts();
		for (ClientAccount clientAccount : activeAccounts) {
			if (clientAccount.getID() == id) {
				return clientAccount;
			}
		}
		return null;
	}

	private ArrayList<ClientAccount> getAccounts() {
		ArrayList<ClientAccount> gridAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (account.getType() != ClientAccount.TYPE_CASH
					&& account.getType() != ClientAccount.TYPE_BANK
					&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE)
				gridAccounts.add(account);
		}
		return gridAccounts;
	}

	@Override
	protected boolean onOK() {
		for (int boxNum : boxNums) {
			ClientAccount account = getAccountByBoxNum(boxNum);
			if (account != null) {
				account.setBoxNumber(0);
				saveOrUpdate(account);
			}
		}
		for (Entry<Integer, BoxAccount> element : boxAccounts.entrySet()) {
			Integer boxNo = element.getKey();
			ArrayList<ClientAccount> accounts = element.getValue().accounts;
			for (ClientAccount clientAccount : accounts) {
				ClientAccount accountByID = getAccountByID(clientAccount
						.getID());
				if (accountByID.getBoxNumber() == 0
						|| accountByID.getBoxNumber() == boxNo) {
					accountByID.setBoxNumber(boxNo);
					saveOrUpdate(accountByID);
				} else {
					Accounter.showError(messages
							.pleaseSelectOneAccountForOneBoxOnly());
					return false;
				}
			}
		}
		// for (int i = 1; i <= boxNums.length; i++) {
		// CheckBox checkBox = (CheckBox) flexTable.getWidget(i, 0);
		// DynamicForm accountsForm = (DynamicForm) flexTable.getWidget(i, 2);
		// AccountCombo accountsCombo = (AccountCombo) accountsForm
		// .getField("AccountsCombo");
		// ClientAccount selectedValue = accountsCombo.getSelectedValue();
		//
		// if (checkBox.isChecked() && selectedValue != null) {
		// if (selectedValue.getBoxNumber() == 0) {
		// selectedValue.setBoxNumber(boxNums[i - 1]);
		// saveOrUpdate(selectedValue);
		// } else {
		// Accounter
		// .showError("Please Select One Account For One Box Only..");
		// return false;
		// }
		// }
		//
		// }
		getCallback().actionResult(boxNums);

		return true;
	}

	class BoxAccount {
		boolean isMultiple;
		ArrayList<ClientAccount> accounts = new ArrayList<ClientAccount>();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
