/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Administrator
 * 
 */
public class SetupReferPage extends AbstractSetupPage {

	private static SetupReferPageUiBinder uiBinder = GWT
			.create(SetupReferPageUiBinder.class);
	@UiField
	FlowPanel mainViewPanel;
	@UiField
	Label headerLabel;
	@UiField
	Label vendorsLabel;
	@UiField
	Label customerLabel;
	@UiField
	Label customerCommentLabel;
	@UiField
	Label supplierCommentLabel;
	@UiField
	ListBox customerListBox;
	@UiField
	ListBox vendorListBox;

	interface SetupReferPageUiBinder extends UiBinder<Widget, SetupReferPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public SetupReferPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.terminology());
		// adding Items to customer list box
		customerListBox.addItem(messages.customer());
		customerListBox.addItem(messages.Client());
		customerListBox.addItem(messages.Tenant());
		customerListBox.addItem(messages.Donar());
		customerListBox.addItem(messages.Guest());
		customerListBox.addItem(messages.Member());
		customerListBox.addItem(messages.Patient());

		vendorListBox.addItem(messages.Vendor());
		vendorListBox.addItem(messages.Supplier());

		customerLabel.setText(messages.customer());
		vendorsLabel.setText(messages.Vendor());

		customerCommentLabel.setText(messages
				.howDoYouReferYourCustoemrs());
		supplierCommentLabel.setText(messages
				.howDoYouReferYourVendors());

	}

	public void onLoad() {

		int referCustomers = preferences.getReferCustomers();
		int referSuplliers = preferences.getReferVendors();

		customerListBox.setSelectedIndex(referCustomers);
		vendorListBox.setSelectedIndex(referSuplliers);
	}

	@Override
	public void onSave() {
		int customer = customerListBox.getSelectedIndex();
		int vendor = vendorListBox.getSelectedIndex();
		preferences.setReferCustomers(customer);
		preferences.setReferVendors(vendor);
	}

	@Override
	protected boolean validate() {
		if (customerListBox.getSelectedIndex() == -1
				&& vendorListBox.getSelectedIndex() == -1) {
			Accounter.showError(messages.howDoYouRefer() + " "
					+ messages.payees(Global.get().customers())
					+ " " + Global.get().vendor() + " "
					+ messages.Accounts() + "?");
			return false;
		} else if (customerListBox.getSelectedIndex() == -1
				&& vendorListBox.getSelectedIndex() == -1) {
			Accounter.showError(messages.howDoYouRefer() + " "
					+ messages.payees(Global.get().customers())
					+ " " + Global.get().vendor() + "?");
			return false;
		} else if (customerListBox.getSelectedIndex() == -1) {
			Accounter.showError(messages
					.howDoYouReferYourCustoemrs());
			return false;
		} else if (vendorListBox.getSelectedIndex() == -1) {
			Accounter
					.showError(messages.howDoYouReferYourVendors());
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String getViewName() {
		return messages.terminology();
	}

}
