package com.nitya.accounter.core.reports.generators;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Employee;
import com.nitya.accounter.core.PayHead;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.PaySlipDetail;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.serverreports.PaySlipDetailServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PaySlipDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PAYSLIP_DETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PaySlipDetailServerReport byCatgoryServerReport = new PaySlipDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}

			@Override
			protected String getAmountAsString(PaySlipDetail detail,
					double amount) {
				if (detail == null) {
					return Global.get().toCurrencyFormat(amount,
							getCompany().getPrimaryCurrency().getSymbol());
				}
				if (detail.getType() == 1) {
					if (detail.getAttendanceOrProductionType() == ClientAttendanceOrProductionType.TYPE_PRODUCTION) {
						return (detail.getAmount() == null ? 0 : detail
								.getAmount()) + " " + detail.getUnitName();
					}

					return (detail.getAmount() == null ? 0 : detail.getAmount())
							+ " "
							+ getCalculationPeriod(detail.getPeriodType());
				} else {
					return Global.get().toCurrencyFormat(detail.getAmount(),
							getCompany().getPrimaryCurrency().getSymbol());
				}
			}
		};
		updateReport(byCatgoryServerReport, financeTool);
		byCatgoryServerReport.resetVariables();

		long employeeId = getInputAsLong(0);

		byCatgoryServerReport.onResultSuccess(financeTool.getPayrollManager()
				.getPaySlipDetail(employeeId, startDate, endDate,
						company.getId()));

		ReportGridTemplate<PaySlipDetail> gridTemplate = byCatgoryServerReport
				.getGridTemplate();
		gridTemplate
				.addAdditionalDetails(makeDetailLayout((Employee) financeTool
						.getManager().getServerObjectForid(
								AccounterCoreType.EMPLOYEE, employeeId)));
		return gridTemplate;
	}

	protected String getCalculationPeriod(int type) {
		AccounterMessages messages = Global.get().messages();
		switch (type) {
		case PayHead.CALCULATION_PERIOD_DAYS:
			return messages.days();

		case PayHead.CALCULATION_PERIOD_WEEKS:
			return messages.weeks();

		case PayHead.CALCULATION_PERIOD_MONTHS:
			return messages.months();

		default:
			return "";
		}
	}

	private String[] makeDetailLayout(Employee selectedEmployee) {
		AccounterMessages messages = Global.get().messages();
		if (selectedEmployee != null && selectedEmployee.getID() != 0) {
			String employeeName = messages.name() + " : "
					+ selectedEmployee.getName();

			String gender = "";
			if (selectedEmployee.getGender() > 0) {
				gender = messages.gender() + " : "
						+ getGenderString(selectedEmployee.getGender());
			}

			String contactNum = "";
			if (selectedEmployee.getPhoneNo() != null
					&& !selectedEmployee.getPhoneNo().trim().isEmpty()) {
				contactNum = messages.contactNumber() + " : "
						+ selectedEmployee.getPhoneNo();
			}

			String email = "";
			if (selectedEmployee.getEmail() != null) {
				email = messages.email() + " : " + selectedEmployee.getEmail();
			}
			List<Address> address = new ArrayList<Address>(
					selectedEmployee.getAddress());
			String addressStr = "";
			if (address != null && address.size() > 0) {
				addressStr = messages.address() + " : "
						+ getAddressAsString(address.get(0));
			}
			String panNumber = "";
			if (selectedEmployee.getPANno() != null
					&& !selectedEmployee.getPANno().trim().isEmpty()) {
				panNumber = messages.panOrEinNumber() + " : "
						+ selectedEmployee.getPANno();
			}

			String bankAccountNumber = "";
			if (selectedEmployee.getBankAccountNo() != null
					&& !selectedEmployee.getBankAccountNo().trim().isEmpty()) {
				bankAccountNumber = messages.bankAccountNumber() + " : "
						+ selectedEmployee.getBankAccountNo();
			}

			String bankName = "";
			if (selectedEmployee.getBankName() != null
					&& !selectedEmployee.getBankName().trim().isEmpty()) {
				bankName = messages.bankName() + " : "
						+ selectedEmployee.getBankName();
			}

			String passportNumber = "";
			if (selectedEmployee.getPassportNumber() != null
					&& !selectedEmployee.getPassportNumber().trim().isEmpty()) {
				passportNumber = messages.passportNumber() + " : "
						+ selectedEmployee.getPassportNumber();
			}

			String countryOfIssue = "";
			if (selectedEmployee.getCountryOfIssue() != null
					&& !selectedEmployee.getCountryOfIssue().trim().isEmpty()) {
				countryOfIssue = messages.countryOfIssue() + " : "
						+ selectedEmployee.getCountryOfIssue();
			}
			String visaNumber = "";
			if (selectedEmployee.getVisaNumber() != null
					&& !selectedEmployee.getVisaNumber().trim().isEmpty()) {
				visaNumber = messages.visaNumber() + " : "
						+ selectedEmployee.getVisaNumber();
			}

			return new String[] { employeeName, gender, contactNum, email,
					addressStr, panNumber, bankAccountNumber, bankName,
					passportNumber, countryOfIssue, visaNumber };
		}

		return new String[] {};
	}

	private String getGenderString(int gender) {
		AccounterMessages messages = Global.get().messages();
		switch (gender) {
		case 1:
			return messages.unspecified();
		case 2:
			return messages.male();
		case 3:
			return messages.female();
		default:
			break;
		}
		return "";
	}

	private String getAddressAsString(Address clientAddress) {
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
