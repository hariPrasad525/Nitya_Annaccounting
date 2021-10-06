package com.vimukti.accounter.web.client.ui.company.options;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;

public class ExpensesByCustomerOption extends AbstractPreferenceOption {

	CheckboxItem expenseandProductandServiceTrackingbyCustomer;

	CheckboxItem UseBillableExpenseCheckBox;

	public ExpensesByCustomerOption() {
		super("");
		super.setStyleName("ExpensesByCustomerOption");
		createControls();
		initData();
	}

	@Override
	public String getTitle() {

		return messages.ExpensesbyCustomer(Global.get().Customer());
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setProductandSerivesTrackingByCustomerEnabled(
				expenseandProductandServiceTrackingbyCustomer.getValue());
		getCompanyPreferences().setBillableExpsesEnbldForProductandServices(
				UseBillableExpenseCheckBox.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.ExpensesbyCustomer(Global.get().Customer());
	}

	@Override
	public void createControls() {
		expenseandProductandServiceTrackingbyCustomer = new CheckboxItem(
				messages.Expenseandproductservicetrackingbycustomer(Global
						.get().customer()), "header");

		UseBillableExpenseCheckBox = new CheckboxItem(
				messages.useBillabelExpenses(), "header");
		add(expenseandProductandServiceTrackingbyCustomer);
		add(UseBillableExpenseCheckBox);
	}

	@Override
	public void initData() {
		expenseandProductandServiceTrackingbyCustomer
				.setValue(getCompanyPreferences()
						.isProductandSerivesTrackingByCustomerEnabled());
		UseBillableExpenseCheckBox.setValue(getCompanyPreferences()
				.isBillableExpsesEnbldForProductandServices());
	}

}
