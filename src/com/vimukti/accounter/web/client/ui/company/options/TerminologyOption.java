/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

/**
 * @author vimukti36
 * 
 */
public class TerminologyOption extends AbstractPreferenceOption {

	LabelItem terminologyforCustomerLabel;

	LabelItem customerDescriptionLabel;

	SelectCombo customerTerminolgyCombo;

	LabelItem vendorsHeaderLabel;

	LabelItem vendorsDescriptionLabel;

	RadioGroupItem vendorRadioGroup;

	public String[] customerTermsList = { messages.customer(),
			messages.Client(), messages.Tenant(), messages.Donar(),
			messages.Guest(), messages.Member(), messages.Patient() };

	public TerminologyOption() {
		super("");
		createControls();
		initData();
	}

	public void createControls() {
		terminologyforCustomerLabel = new LabelItem(
				messages.useTerminologyFor(Global.get().Customer()), "header");

		customerDescriptionLabel = new LabelItem(
				messages.customerDescription(), "organisation_comment");

		customerTerminolgyCombo = new SelectCombo(messages.select());
		for (int i = 0; i < customerTermsList.length; i++) {
			customerTerminolgyCombo.addItem(customerTermsList[i]);
		}
		customerTerminolgyCombo.addStyleName("terminologycustomerradio");

		customerTerminolgyCombo.setSelected(messages.customer());

		add(terminologyforCustomerLabel);
		add(customerDescriptionLabel);
		add(customerTerminolgyCombo);

		vendorsHeaderLabel = new LabelItem(messages.useTerminologyFor(Global
				.get().Vendor()), "header");

		vendorsDescriptionLabel = new LabelItem(messages.vendorDescription(),
				"organisation_comment");

		vendorRadioGroup = new RadioGroupItem();
		vendorRadioGroup.setGroupName("vendorRadioGroup");
		vendorRadioGroup.addStyleName("terminologycustomerradio");
		vendorRadioGroup.setShowTitle(false);

		vendorRadioGroup.setValueMap(messages.Vendor(), messages.Supplier());
		vendorRadioGroup.setValue(messages.Vendor());

		add(vendorsHeaderLabel);
		add(vendorsDescriptionLabel);
		add(vendorRadioGroup);
	}

	@Override
	public String getTitle() {
		return messages.terminology();
	}

	@Override
	public void onSave() {

		if (customerTerminolgyCombo.getValue().equals(messages.Tenant())) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.TENANT);
		} else if (customerTerminolgyCombo.getValue().equals(
				messages.customer())) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.CUSTOMER);
		} else if (customerTerminolgyCombo.getValue().equals(messages.Guest())) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.GUEST);
		} else if (customerTerminolgyCombo.getValue().equals(messages.Member())) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.MEMBER);
		} else if (customerTerminolgyCombo.getValue()
				.equals(messages.Patient())) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.PATITEINT);
		} else if (customerTerminolgyCombo.getValue().equals(messages.Client())) {
			getCompanyPreferences().setReferCustomers(ClientCustomer.CLIENT);
		} else {
			getCompanyPreferences().setReferCustomers(ClientCustomer.DONAR);
		}

		if (vendorRadioGroup.getValue().equals(messages.Vendor())) {
			getCompanyPreferences().setReferVendors(ClientVendor.VENDOR);
		} else if (vendorRadioGroup.getValue().equals(messages.Supplier())) {
			getCompanyPreferences().setReferVendors(ClientVendor.SUPPLIER);
		}

	}

	@Override
	public String getAnchor() {
		return messages.terminology();
	}

	@Override
	public void initData() {

		int referCustomers = getCompanyPreferences().getReferCustomers();
		
		customerTerminolgyCombo.setComboItem(customerTermsList[referCustomers]);

		int referVendors = getCompanyPreferences().getReferVendors();

		switch (referVendors) {
		case ClientVendor.VENDOR:
			vendorRadioGroup.setValue(messages.Vendor());
			break;
		case ClientVendor.SUPPLIER:
			vendorRadioGroup.setValue(messages.Supplier());
			break;
		}

	}
}
