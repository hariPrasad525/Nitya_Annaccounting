package com.nitya.accounter.core.reports.generators;

import java.util.Set;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.core.Vendor;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Lists.PayeeStatementsList;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.StatementServerReport;

public class VendorStatementRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_VENDORSTATEMENT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		StatementServerReport statementReport1 = new StatementServerReport(
				true, this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(statementReport1);
		statementReport1.resetVariables();
		try {
			long vendorID = getInputAsLong(0);
			Vendor vedorById = financeTool.getVendorManager().getVedorById(
					vendorID);
			statementReport1.setCurrency(vedorById.getCurrency().getSymbol());
			statementReport1.onResultSuccess(financeTool.getReportManager()
					.getPayeeStatementsList(true, vendorID, 0, startDate,
							endDate, company.getID()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		ReportGridTemplate<PayeeStatementsList> gridTemplate = statementReport1
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
