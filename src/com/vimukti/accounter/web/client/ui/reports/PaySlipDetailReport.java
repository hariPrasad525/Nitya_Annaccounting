package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.reports.PayHeadSummary;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.PayRollReportActions;
import com.vimukti.accounter.web.client.ui.serverreports.PaySlipDetailServerReport;

public class PaySlipDetailReport extends AbstractReportView<PaySlipDetail> {

	public PaySlipDetailReport() {
		this.getElement().setId("PaySlipDetailReport");
		this.serverReport = new PaySlipDetailServerReport(this) {
			@Override
			protected String getAmountAsString(PaySlipDetail detail,
					double amount) {
				if (detail == null) {
					return DataUtils.getAmountAsStringInPrimaryCurrency(amount);
				}
				if (detail.getType() == 1) {
					if (detail.getAttendanceOrProductionType() == ClientAttendanceOrProductionType.TYPE_PRODUCTION) {
						return (detail.getAmount() == null ? 0 : detail
								.getAmount()) + " " + detail.getUnitName();
					}

					return (detail.getAmount() == null ? 0 : detail.getAmount())
							+ " "
							+ ClientPayHead.getCalculationPeriod(detail
									.getPeriodType());
				} else {
					return DataUtils.getAmountAsStringInPrimaryCurrency(detail
							.getAmount());
				}
			}
		};
	}

	@Override
	public void initData() {
		if (data != null) {
			PaySlipDetail headSummary = (PaySlipDetail) data;
			EmployeeReportToolbar employeeToolBar = (EmployeeReportToolbar) this.toolbar;
			employeeToolBar.setEmployee(headSummary.getEmployeeId());
		}
		super.initData();
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		EmployeeReportToolbar employeeToolBar = (EmployeeReportToolbar) this.toolbar;
		long employeeId = employeeToolBar.getSelectedEmployee() == null ? data == null ? 0
				: ((PaySlipDetail) data).getEmployeeId()
				: employeeToolBar.getSelectedEmployee().getID();
		Accounter.createPayrollService().getPaySlipDetail(employeeId, start,
				end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_EMPLOYEE;
	}

	@Override
	public void OnRecordClick(PaySlipDetail record) {
		EmployeeReportToolbar employeeToolBar = (EmployeeReportToolbar) this.toolbar;
		long employeeId = employeeToolBar.getSelectedEmployee() == null ? data == null ? 0
				: ((PaySlipDetail) data).getEmployeeId()
				: employeeToolBar.getSelectedEmployee().getID();
		PayHeadSummary headSummary = new PayHeadSummary();
		headSummary.setEmployeeId(employeeId);
		headSummary.setDateRange(employeeToolBar.getSelectedDateRange());
		headSummary.setStartDate(employeeToolBar.getStartDate());
		headSummary.setEndDate(employeeToolBar.getEndDate());
		headSummary.setPayHead(record.getPayheadId());
		headSummary.setPayHeadName(record.getName());
		PayRollReportActions action = PayRollReportActions
				.getPayHeadSummaryReportAction();
		action.run(headSummary, false);
	}

	@Override
	public void export(int generationType) {
		EmployeeReportToolbar employeeToolBar = (EmployeeReportToolbar) this.toolbar;
		long employeeId = employeeToolBar.getSelectedEmployee() == null ? data == null ? 0
				: ((PaySlipDetail) data).getEmployeeId()
				: employeeToolBar.getSelectedEmployee().getID();
		UIUtils.downloadMultipleAttachment(String.valueOf(employeeId), 116,
				employeeToolBar.getStartDate(), employeeToolBar.getEndDate());
	}

	@Override
	protected void makeDetailLayout(FlowPanel detailPanel) {
		EmployeeReportToolbar employeeToolBar = (EmployeeReportToolbar) this.toolbar;
		ClientEmployee selectedEmployee = employeeToolBar.getSelectedEmployee();
		if (selectedEmployee != null && selectedEmployee.getID() != 0) {
			FlowPanel leftPanel = new FlowPanel();
			FlowPanel rightPanel = new FlowPanel();
			leftPanel.add(new Label(messages.name() + " : "
					+ selectedEmployee.getName()));

			if (selectedEmployee.getGender() >= 0) {
				leftPanel.add(new Label(messages.gender() + " : "
						+ getGenderString(selectedEmployee.getGender())));
			}

			if (selectedEmployee.getPhoneNo() != null
					&& !selectedEmployee.getPhoneNo().isEmpty()) {
				leftPanel.add(new Label(messages.contactNumber() + " : "
						+ selectedEmployee.getPhoneNo()));
			}

			if (selectedEmployee.getEmail() != null) {
				leftPanel.add(new Label(messages.email() + " : "
						+ selectedEmployee.getEmail()));
			}
			List<ClientAddress> address = new ArrayList<ClientAddress>(
					selectedEmployee.getAddress());
			if (address != null && address.size() > 0) {
				leftPanel.add(new Label(messages.address() + " : "
						+ getAddressAsString(address.get(0))));
			}

			if (selectedEmployee.getPanNumber() != null
					&& !selectedEmployee.getPanNumber().trim().isEmpty()) {
				rightPanel.add(new Label(messages.panOrEinNumber() + " : "
						+ selectedEmployee.getPanNumber()));
			}

			if (selectedEmployee.getBankAccountNo() != null
					&& !selectedEmployee.getBankAccountNo().trim().isEmpty()) {
				rightPanel.add(new Label(messages.bankAccountNumber() + " : "
						+ selectedEmployee.getBankAccountNo()));
			}

			if (selectedEmployee.getBankName() != null
					&& !selectedEmployee.getBankName().trim().isEmpty()) {
				rightPanel.add(new Label(messages.bankName() + " : "
						+ selectedEmployee.getBankName()));
			}

			if (selectedEmployee.getPassportNumber() != null
					&& !selectedEmployee.getPassportNumber().trim().isEmpty()) {
				rightPanel.add(new Label(messages.passportNumber() + " : "
						+ selectedEmployee.getPassportNumber()));
			}

			if (selectedEmployee.getCountryOfIssue() != null
					&& !selectedEmployee.getCountryOfIssue().trim().isEmpty()) {
				rightPanel.add(new Label(messages.countryOfIssue() + " : "
						+ selectedEmployee.getCountryOfIssue()));
			}

			if (selectedEmployee.getVisaNumber() != null
					&& !selectedEmployee.getVisaNumber().trim().isEmpty()) {
				rightPanel.add(new Label(messages.visaNumber() + " : "
						+ selectedEmployee.getVisaNumber()));
			}

			leftPanel.addStyleName("left_employee_details_panel");
			rightPanel.addStyleName("right_employee_details_panel");
			detailPanel.add(leftPanel);
			detailPanel.add(rightPanel);
			detailPanel.addStyleName("employee_details_panel");
		}
	}

	private String getGenderString(int gender) {
		switch (gender) {
		case 0:
			return messages.unspecified();
		case 1:
			return messages.male();
		case 2:
			return messages.female();
		default:
			break;
		}
		return "";
	}

	private String getAddressAsString(ClientAddress clientAddress) {
		final StringBuffer information = new StringBuffer();
		String address1 = clientAddress.getAddress1();
		if (address1 != null && !address1.equals(""))
			information.append(address1);
		String street = clientAddress.getStreet();
		if (street != null && !street.equals(""))
			information.append(", ").append(street);
		String city = clientAddress.getCity();
		if (city != null && !city.equals(""))
			information.append(", ").append(city);
		String state = clientAddress.getStateOrProvinence();
		if (state != null && !state.equals(""))
			information.append(", ").append(state);
		String zip = clientAddress.getZipOrPostalCode();
		if (zip != null && !zip.equals(""))
			information.append(", ").append(zip);
		String country = clientAddress.getCountryOrRegion();
		if (country != null && !country.equals(""))
			information.append(", ").append(country);

		return information.toString();
	}

}
