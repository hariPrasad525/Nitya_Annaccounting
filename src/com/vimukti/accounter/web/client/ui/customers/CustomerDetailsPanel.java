package com.vimukti.accounter.web.client.ui.customers;

import java.util.Set;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class CustomerDetailsPanel extends FlowPanel {
	ClientCustomer selectedCustomer;
	LabelItem name, email, currency, fax, customersince, webpageadress, notes,
			address;
	AmountLabel balance, openingBalance;
	Label heading, custname;
	private ClientAddress payeeAddress;
	private Set<ClientAddress> addressListOfCustomer;
	private DynamicForm leftform, rightform;
	protected static final AccounterMessages messages = Global.get().messages();

	public CustomerDetailsPanel(ClientCustomer clientCustomer) {
		this.selectedCustomer = clientCustomer;
		createControls();
		showCustomerDetails(clientCustomer);
		setStyleName("customerDetailsPanel");
	}

	private void createControls() {

		name = new LabelItem(messages.name(), "name");

		email = new LabelItem(messages.email(), "email");
		ClientCompany company = Accounter.getCompany();
		ClientCurrency customerCurrency = selectedCustomer != null ? company
				.getCurrency(selectedCustomer.getCurrency()) : company
				.getPrimaryCurrency();

		balance = new AmountLabel(
				messages.balanceWithCurrencyName(customerCurrency
						.getFormalName()), customerCurrency);

		currency = new LabelItem(messages.currency(), "currency");

		fax = new LabelItem(messages.faxNumber(), "fax");

		customersince = new LabelItem(messages.payeeSince(Global.get()
				.Customer()), "customersince");

		webpageadress = new LabelItem(messages.webPageAddress(),
				"webpageadress");

		notes = new LabelItem(messages.notes(), "notes");

		address = new LabelItem(messages.address(), "address");

		openingBalance = new AmountLabel(
				messages.openingBalanceWithCurrencyName(customerCurrency
						.getFormalName()), customerCurrency);

		leftform = new DynamicForm("leftForm");
		rightform = new DynamicForm("rightform");

		leftform.add(name, balance, openingBalance, currency, customersince);

		rightform.add(email, fax, webpageadress, address, notes);
		rightform.addStyleName("customers_detail_rightpanel");

		StyledPanel hp = new StyledPanel("panel");

		StyledPanel headingPanel = new StyledPanel("customers_detail_panel");

		heading = new Label(messages.payeeDetails(Global.get().Customers())
				+ " :");
		custname = new Label();
		custname.setText(messages.noPayeeSelected(Global.get().Customer()));

		headingPanel.add(heading);
		headingPanel.add(custname);
		add(headingPanel);
		hp.add(leftform);
		hp.add(rightform);

		add(hp);
		hp.getElement().getParentElement().addClassName("details-Panel");
	}

	protected void showCustomerDetails(ClientCustomer selectedCustomer) {
		if (selectedCustomer != null) {
			addressListOfCustomer = selectedCustomer.getAddress();
			custname.setText(selectedCustomer.getName());
			name.setValue(selectedCustomer.getName());

			email.setValue(selectedCustomer.getEmail());

			ClientCurrency customerCurrency = Accounter.getCompany()
					.getCurrency(selectedCustomer.getCurrency());

			balance.setTitle(messages.balanceWithCurrencyName(customerCurrency
					.getFormalName()));
			balance.setCurrency(customerCurrency);
			balance.setAmount(selectedCustomer.getBalance());

			currency.setValue(customerCurrency.getFormalName());

			fax.setValue(selectedCustomer.getFaxNo());

			long payeeSince = selectedCustomer.getPayeeSince();
			ClientFinanceDate clientFinanceDate = new ClientFinanceDate();
			if (payeeSince > 0) {
				clientFinanceDate = new ClientFinanceDate(payeeSince);
			}
			customersince.setValue(UIUtils
					.getDateByCompanyType(clientFinanceDate));

			webpageadress.setValue(selectedCustomer.getWebPageAddress());

			openingBalance.setTitle(messages
					.openingBalanceWithCurrencyName(customerCurrency
							.getFormalName()));
			openingBalance.setCurrency(customerCurrency);
			openingBalance.setAmount(selectedCustomer.getOpeningBalance());

			notes.setValue(selectedCustomer.getMemo());
			notes.getMainWidget().getElement().getParentElement()
					.addClassName("customer-detail-notespanel");

			payeeAddress = getAddress(ClientAddress.TYPE_BILL_TO);
			if (payeeAddress != null) {

				address.setValue(getValidAddress(payeeAddress));

			} else
				address.setValue("");

		} else {
			name.setValue("");
			email.setValue("");
			balance.setAmount(0.00);
			currency.setValue("");
			fax.setValue("");
			customersince.setValue("");
			webpageadress.setValue("");
			openingBalance.setAmount(0.00);
			notes.setValue("");
			address.setValue("");
		}
	}

	public void setCustomer(ClientCustomer customer) {
		this.selectedCustomer = customer;
		showCustomerDetails(customer);
	}

	public ClientCustomer getCustomer() {
		return selectedCustomer;
	}

	public ClientAddress getAddress(int type) {
		for (ClientAddress address : addressListOfCustomer) {
			if (address.getType() == type) {
				return address;
			}

		}
		return null;
	}

	protected String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "," + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "," + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "," + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "," + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "," + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion() + ".";
		}
		return toToSet;
	}

}
