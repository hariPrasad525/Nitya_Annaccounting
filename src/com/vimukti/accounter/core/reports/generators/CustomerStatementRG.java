package com.vimukti.accounter.core.reports.generators;

import java.util.Set;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.StatementServerReport;

public class CustomerStatementRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_CUSTOMERSTATEMENT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		StatementServerReport statementReport = new StatementServerReport(
				false, this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(statementReport, financeTool);
		statementReport.resetVariables();
		try {
			long customerID = getInputAsLong(0);
			Customer Customer = financeTool.getCustomerManager()
					.getCustomerByID(customerID);
			statementReport.setCurrency(Customer.getCurrency().getSymbol());
			statementReport.onResultSuccess(financeTool.getReportManager()
					.getPayeeStatementsList(false, customerID, 0, startDate,
							endDate, company.getID()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		ReportGridTemplate<PayeeStatementsList> gridTemplate = statementReport
				.getGridTemplate();
		gridTemplate.addAdditionalDetails(getStatementReportDetails());
		return gridTemplate;
	}

	private String[] getStatementReportDetails() {
		String address1 = "", street = "", city = "", state = "", country = "", postcode = "";
		Payee payee = (Payee) financeTool.getManager().getServerObjectForid(
				AccounterCoreType.PAYEE, getInputAsLong(0));
		String payeeName = payee.getName();
		Set<Address> address = payee.getAddress();
		for (Address clientAddress : address) {
			if (clientAddress.getAddress1() != null
					&& !clientAddress.getAddress1().equals("")) {
				address1 = clientAddress.getAddress1();
			}
			if (clientAddress.getStreet() != null
					&& !clientAddress.getStreet().equals("")) {
				address1.concat(", ");
				street = clientAddress.getStreet();
			}
			if (clientAddress.getCity() != null
					&& !clientAddress.getCity().equals("")) {
				street.concat(", ");
				city = clientAddress.getCity();
			}
			if (clientAddress.getStateOrProvinence() != null
					&& !clientAddress.getStateOrProvinence().equals("")) {
				city.concat(", ");
				state = clientAddress.getStateOrProvinence();
			}
			if (clientAddress.getZipOrPostalCode() != null
					&& !clientAddress.getZipOrPostalCode().equals("")) {
				state.concat(", ");
				postcode = clientAddress.getZipOrPostalCode();
			}
			if (clientAddress.getCountryOrRegion() != null
					&& !clientAddress.getCountryOrRegion().equals("")) {
				postcode.concat(", ");
				country = clientAddress.getCountryOrRegion().concat(".");
			}
			break;
		}
		AccounterMessages messages = Global.get().messages();
		String fromDate = messages.fromDate() + ": " + startDate.toString();
		String toDate = messages.toDate() + ": " + endDate.toString();
		return new String[] { payeeName, address1, street,
				city + " " + postcode, state, country, fromDate, toDate };

	}
}
