package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientEmployee;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.combo.EmployeeCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;

public class EmployeeReportToolbar extends DateRangeReportToolbar {

	protected EmployeeCombo employeeCombo;
	private ClientEmployee selectedEmployee;

	public EmployeeReportToolbar(AbstractReportView reportView) {
		super(reportView);
	}

	@Override
	protected void createControls() {
		employeeCombo = new EmployeeCombo(messages.employee());
		employeeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientEmployee>() {

					@Override
					public void selectedComboBoxItem(ClientEmployee selectItem) {
						setSelectedEmployee(selectItem);
					}
				});

		super.createControls();
	}

	protected void setSelectedEmployee(ClientEmployee selectItem) {
		this.selectedEmployee = selectItem;
		changeDates(getStartDate(), getEndDate());
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		reportview.makeReportRequest(fromItem.getDate(), toItem.getDate());
	}

	@Override
	protected void accountData() {
		if (selectedEmployee != null) {
			accData(selectedEmployee);
		}
	}

	protected com.nitya.accounter.web.client.ui.forms.FormItem<?> getItem() {
		return employeeCombo;
	}

	protected void accData(ClientEmployee employee) {
		if (employee != null) {
			selectedEmployee = employee;
			ClientFinanceDate startDate = fromItem.getDate();
			ClientFinanceDate endDate = toItem.getDate();
			reportview.makeReportRequest(selectedEmployee.getID(), startDate,
					endDate);
			reportview.removeEmptyStyle();
		}
	}

	public ClientEmployee getSelectedEmployee() {
		return employeeCombo.getSelectedValue();
	}

	public void setEmployee(long payHead) {
		employeeCombo.setSelectedEmployee(payHead);
	}
}
