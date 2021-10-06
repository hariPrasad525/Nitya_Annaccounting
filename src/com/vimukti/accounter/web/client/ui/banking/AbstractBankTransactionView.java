package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.ClassListCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DateItem;

/*Pavani vimukti5 Abstract Class for all Banking Transaction Views
 *         modified by Ravi Kiran.G
 * 
 */
public abstract class AbstractBankTransactionView<T extends ClientTransaction>
		extends AbstractTransactionBaseView<T> {

	// private ClientTransaction bankingTransactionObject;

	// protected CustomerTransactionUSGrid transactionCustomerGrid;
	// AbstractTransactionGrid<ClientTransactionItem> transactionVendorGrid,
	// transactionCustomerGrid;
	// @Override
	// public AbstractTransactionGrid<ClientTransactionItem> getGrid() {
	// return null;
	// }

	// protected int transactionType;
	protected DateItem deliveryDate;

	private AbstractBankTransactionView<?> bankingTransactionViewInstance;

	protected abstract void taxCodeSelected(ClientTAXCode taxCode);

	// protected TextItem refText;
	protected AmountField amtText;
	public ClientAccounterClass accounterClass;
	// protected PaymentMethod paymentMethod;
	protected ClientAccount account;

	protected List<ClientAccount> accountsList;

	protected ClientAddress billingAddress;

	protected Set<ClientAddress> addressList;
	protected AddressCombo billToCombo;

	protected long payFromAccount;
	protected CheckboxItem vatinclusiveCheck;

	protected List<ClientAccount> accounts;
	protected AmountLabel netAmount;
	protected TaxItemsForm vatTotalNonEditableText;
	protected ClientVendor selectedVendor;
	protected ClientTAXCode taxCode;

	public AbstractBankTransactionView(int transactionType) {
		super(transactionType);
		bankingTransactionViewInstance = this;

	}

	protected void initTransactionViewData() {

		initAccounts();
	}

	protected abstract void initMemoAndReference();

	protected abstract void initTransactionTotalNonEditableItem();

	private void initAccounts() {
		accounts = getCompany().getActiveAccounts();
	}

	// private void getPayFromAccounts() {
	// listOfAccounts = new ArrayList<ClientAccount>();
	// for (ClientAccount account : FinanceApplication.getCompany()
	// .getAccounts()) {
	// if (account.getType() == ClientAccount.TYPE_CASH
	// || account.getType() == ClientAccount.TYPE_BANK
	// || account.getType() == ClientAccount.TYPE_CREDIT_CARD
	// || account.getType() == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
	// || account.getType() == ClientAccount.TYPE_LONG_TERM_LIABILITY) {
	//
	// listOfAccounts.add(account);
	// }
	//
	// }
	// payFrmSelect.initCombo(listOfAccounts);
	//
	// }

	public AmountField createBalanceText(ClientCurrency currency) {

		AmountField balText = new AmountField(messages.balance(), this,
				currency, "balText");
		// balText.setWidth("*");

		balText.setEnabled(!isInViewMode());
		// balText.setShowDisabled(false);
		return balText;

	}

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button, messages.Accounts(),
				messages.productOrServiceItem());
		// FinanceApplication.constants().comment());

	}

	public AmountField createAmountText(ClientCurrency currency) {

		AmountField amtText = new AmountField(messages.amount(), this,
				currency, "amtText");
		// amtText.setWidth("*");

		amtText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");

		amtText.setEnabled(!isInViewMode());
		// amtText.setShowDisabled(false);

		return amtText;

	}

	protected AmountField createVATTotalNonEditableItem(ClientCurrency currency) {

		AmountField amountItem = new AmountField(messages.tax(), this,
				currency, "amountItem");
		amountItem.setEnabled(false);

		return amountItem;

	}

	protected AmountLabel createVATTotalNonEditableLabel() {

		AmountLabel amountItem = new AmountLabel(messages.tax());
		amountItem.setEnabled(false);

		return amountItem;

	}

	protected void payFromAccountSelected(long accountID) {
		this.payFromAccount = accountID;

	}

	public PayFromAccountsCombo createPayFromselectItem() {
		PayFromAccountsCombo payFrmSelect = new PayFromAccountsCombo(
				messages.paymentFrom());
		payFrmSelect.setRequired(true);
		// payFrmSelect.setWidth("*");

		// payFrmSelect.setWidth("*");
		// payFrmSelect.setWrapTitle(false);
		payFrmSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						payFromAccountSelected(selectItem.getID());
						// selectedAccount = (Account) selectItem;
						// adjustBalance();

					}

				});
		payFrmSelect.setEnabled(!isInViewMode());
		// payFrmSelect.setShowDisabled(false);
		return payFrmSelect;
	}

	public AddressCombo createBillToComboItem() {

		AddressCombo addressCombo = new AddressCombo(messages.billTo(), false);

		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						billToaddressSelected(selectItem);

					}

				});

		addressCombo.setEnabled(!isInViewMode());
		// addressCombo.setShowDisabled(false);
		return addressCombo;

	}

	protected void initBillToCombo() {

		if (billToCombo == null || addressList == null)
			return;

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;

		for (ClientAddress address : addressList) {
			if (address.getType() == ClientAddress.TYPE_BILL_TO) {

				tempSet.add(address);
				billingAddress = address;
				clientAddress = address;
				break;
			}
		}
		List<ClientAddress> list = new ArrayList<ClientAddress>(tempSet);
		billToCombo.initCombo(list);
		if (list == null || list.size() == 0)
			billToCombo.setEnabled(false);
		else
			billToCombo.setEnabled(!isInViewMode());
		// billToCombo.setShowDisabled(false);

		if (isInViewMode()) {
			if (billingAddress != null) {
				billToCombo.setComboItem(billingAddress);
				return;
			}
		}

		if (clientAddress != null) {
			billToCombo.setComboItem(clientAddress);
			billToaddressSelected(clientAddress);

		}

	}

	protected void billToaddressSelected(ClientAddress selectedAddress) {
		if (selectedAddress != null)
			return;
		this.billingAddress = selectedAddress;

	}

	public ClientAddress getAddressById(long addressId) {
		for (ClientAddress address : addressList) {
			if (address.getID() == addressId) {
				return address;
			}
		}
		return null;
	}

	protected TAXCodeCombo createTaxCodeSelectItem() {

		TAXCodeCombo taxCodeCombo = new TAXCodeCombo(messages.tax(), false);
		taxCodeCombo.setRequired(true);

		taxCodeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					public void selectedComboBoxItem(ClientTAXCode selectItem) {

						taxCodeSelected(selectItem);

					}

				});

		taxCodeCombo.setEnabled(!isInViewMode());

		// formItems.add(taxCodeCombo);

		return taxCodeCombo;

	}

	protected AmountField createTransactionTotalNonEditableItem(
			ClientCurrency currency) {

		AmountField amountItem = new AmountField(messages.total(), this,
				currency, "amountItem");
		amountItem.setEnabled(false);

		return amountItem;

	}

	protected AmountLabel createTransactionTotalNonEditableLabel(
			ClientCurrency clientCurrency) {

		AmountLabel amountLabel = new AmountLabel(
				messages.currencyTotal(clientCurrency.getFormalName()),
				clientCurrency);

		return amountLabel;

	}

	// protected void onAddNew(String menuItem) {
	// ClientTransactionItem transactionItem = new ClientTransactionItem();
	// if (menuItem.equals(FinanceApplication.constants().accounts()))
	// {
	// transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
	// if (FinanceApplication.getCompany().getAccountingType() ==
	// ClientCompany.ACCOUNTING_TYPE_UK) {
	// transactionItem.setVatCode(vendor != null ? (vendor
	// .getVATCode() != null ? vendor.getVATCode() : "") : "");
	// }
	// } else if (menuItem.equals(FinanceApplication.constants()
	// .items())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
	// } else if (menuItem.equals(FinanceApplication.constants()
	// .comment())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
	// } else if (menuItem.equals(FinanceApplication.constants()
	// .VATItem()))
	// transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
	// vendorTransactionGrid.addData(transactionItem);
	//
	// }
	//

	protected void onAddNew(String menuItem) {
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		if (menuItem.equals(messages.Accounts())) {
			transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
			transactionItem.setTaxCode(getPreferences().getDefaultTaxCode());

		} else if (menuItem.equals(messages.productOrServiceItem())) {
			transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
			if (getPreferences().isTrackTax()) {
				transactionItem
						.setTaxCode(selectedVendor != null ? (selectedVendor
								.getTAXCode() != 0 ? selectedVendor
								.getTAXCode() : getPreferences()
								.getDefaultTaxCode()) : 0);
			}

		}
		addNewData(transactionItem);
	}

	protected abstract void addNewData(ClientTransactionItem transactionItem);

	@Override
	protected void addAccount() {
		ClientTransactionItem transactionItem = new ClientTransactionItem();

		transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
		transactionItem.setTaxCode(getPreferences().getDefaultTaxCode());
		long defaultTaxCode = getCompany().getPreferences().getDefaultTaxCode();
		if (getPreferences().isTaxPerDetailLine()) {
			transactionItem.setTaxCode(selectedVendor != null ? (selectedVendor
					.getTAXCode() > 0 ? selectedVendor.getTAXCode()
					: defaultTaxCode) : defaultTaxCode);
		} else {
			if (taxCode != null) {
				transactionItem.setTaxCode(taxCode.getID());
			}
		}

		addAccountTransactionItem(transactionItem);
	}

	@Override
	protected void addItem() {
		ClientTransactionItem transactionItem = new ClientTransactionItem();

		transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			transactionItem.setTaxCode(selectedVendor != null ? (selectedVendor
					.getTAXCode() != 0 ? selectedVendor.getTAXCode()
					: getPreferences().getDefaultTaxCode()) : 0);
		} else {
			if (taxCode != null) {
				transactionItem.setTaxCode(taxCode.getID());
			}
		}
		if (isTrackClass() && !getPreferences().isClassPerDetailLine()
				&& accounterClass != null) {
			transactionItem.setAccounterClass(accounterClass.getID());
		}
		addItemTransactionItem(transactionItem);
	}

	protected abstract void addAccountTransactionItem(ClientTransactionItem item);

	protected abstract void addItemTransactionItem(ClientTransactionItem item);

	@Override
	public boolean isTrackTax() {
		if (transaction != null && transaction.haveTax()) {
			return true;
		} else {
			return getPreferences().isTrackTax() && isTrackPaidTax();
		}
	}

	/**
	 * Create for class Tracking
	 * 
	 * @return
	 */
	public ClassListCombo createAccounterClassListCombo() {
		classListCombo = new ClassListCombo(messages.accounterClass(), true);
		classListCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccounterClass>() {

					@Override
					public void selectedComboBoxItem(
							ClientAccounterClass selectItem) {
						accounterClass = selectItem;
						classSelected(selectItem);
					}
				});

		classListCombo
				.addNewAccounterClassHandler(new ValueCallBack<ClientAccounterClass>() {

					@Override
					public void execute(final ClientAccounterClass accouterClass) {
						accounterClass = accouterClass;
						Accounter.createCRUDService().create(accounterClass,
								new AsyncCallback<Long>() {

									@Override
									public void onSuccess(Long result) {
										accounterClass.setID(result);
										getCompany()
												.processUpdateOrCreateObject(
														accounterClass);
										classSelected(accounterClass);
									}

									@Override
									public void onFailure(Throwable caught) {
										caught.printStackTrace();
									}
								});
					}
				});

		classListCombo.setEnabled(!isInViewMode());

		return classListCombo;
	}

	protected abstract void classSelected(
			ClientAccounterClass clientAccounterClass);

}
