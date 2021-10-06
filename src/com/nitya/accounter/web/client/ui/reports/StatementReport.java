package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Set;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.core.Lists.PayeeStatementsList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.StatementServerReport;

public class StatementReport extends AbstractReportView<PayeeStatementsList> {
	public int precategory = 1001;
	private long payeeId = 0;
	private boolean isVendor;

	public StatementReport(boolean isVendor) {
		this.isVendor = isVendor;
		this.serverReport = new StatementServerReport(isVendor, this);
		this.getElement().setId("customervendor_statement_report");
	}

	public StatementReport(boolean isVendor, long payeeId) {
		this.setPayeeId(payeeId);
		this.isVendor = isVendor;
		this.serverReport = new StatementServerReport(isVendor, this);
		this.getElement().setId("customervendor_statement_report");
	}

	@Override
	public void init() {

		super.init();
		this.toolbar.setPayeeId(this.getPayeeId());
		// this.makeReportRequest(payeeId, toolbar.getStartDate(),
		// toolbar.getEndDate());

	}

	@Override
	protected void makeDetailLayout(FlowPanel detailPanel) {
		FlowPanel leftPanel = new FlowPanel();
		FlowPanel rightPanel = new FlowPanel();
		long payeeId = toolbar.getPayeeId();
		if (payeeId != 0) {
			ClientPayee payee = Accounter.getCompany().getPayee(payeeId);
			leftPanel.add(new Label(payee.getName()));
			String startDate = toolbar.getStartDate().toString();
			String endDate = toolbar.getEndDate().toString();
			Label startdateLabel = new Label(messages.fromDate() + ": "
					+ startDate);
			Label endDateLabel = new Label(messages.toDate() + ": " + endDate);
			rightPanel.add(startdateLabel);
			rightPanel.add(endDateLabel);
			Set<ClientAddress> address = payee.getAddress();
			if (!address.isEmpty()) {
				for (ClientAddress clientAddress : address) {
					leftPanel.add(new Label(getAddressAsString(clientAddress)));
				}
			}
		}
		leftPanel.addStyleName("make_leftDetailPanel");
		rightPanel.addStyleName("make_rightDetailPanel");
		detailPanel.add(leftPanel);
		detailPanel.add(rightPanel);
		detailPanel.addStyleName("makeDetailPanel");
	}

	public String getAddressAsString(ClientAddress clientAddress) {
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

	@Override
	protected ClientCurrency getCurrency() {
		ClientCompany company = Accounter.getCompany();
		ClientPayee payee = company.getPayee(getPayeeId());
		if (payee != null) {
			return company.getCurrency(payee.getCurrency());
		} else {
			return super.getCurrency();
		}
	}

	@Override
	public int getToolbarType() {
		if (isVendor)
			return TOOLBAR_TYPE_VENDOR;
		else
			return TOOLBAR_TYPE_CUSTOMER;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		this.makeReportRequest(getPayeeId(), start, end);
	}

	@Override
	public void makeReportRequest(long payee, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		// resetReport(endDate, endDate);
		grid.clear();
		// grid.addLoadingImagePanel();
		if (payee != 0) {
			setPayeeId(payee);
		}
		Accounter.createReportService().getStatements(isVendor, getPayeeId(),
				toolbar.getViewId(), toolbar.getStartDate(),
				toolbar.getEndDate(), this);

	}

	@Override
	public void OnRecordClick(PayeeStatementsList record) {
		record.setStartDate(toolbar.getEndDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactiontype(),
					record.getTransactionId());

	}

	@Override
	public void export(int generationType) {
		if (getPayeeId() == 0) {
			if (isVendor) {
				Accounter.showError(messages
						.pleaseSelect(Global.get().Vendor()));
			} else {
				Accounter.showError(messages.pleaseSelect(Global.get()
						.Customer()));
			}
		} else {
			UIUtils.generateReport(generationType, startDate.getDate(),
					endDate.getDate(), isVendor ? 167 : 150,
					new NumberReportInput(getPayeeId()));
		}
	}

	@Override
	public void printPreview() {

	}

	@Override
	public PayeeStatementsList getObject(PayeeStatementsList parent,
			PayeeStatementsList child) {
		return super.getObject(parent, child);
	}

	@Override
	public int sort(PayeeStatementsList obj1, PayeeStatementsList obj2, int col) {
		int ret = UIUtils.compareInt(obj1.getCategory(), obj2.getCategory());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			int num1 = UIUtils.isInteger(obj1.getTransactionNumber()) ? Integer
					.parseInt(obj1.getTransactionNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getTransactionNumber()) ? Integer
					.parseInt(obj2.getTransactionNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return UIUtils.compareTo(obj1.getTransactionNumber(),
						obj2.getTransactionNumber());

		case 1:
			return UIUtils.compareInt(obj1.getTransactiontype(),
					obj2.getTransactiontype());

		case 3:
			return UIUtils.compareDouble(obj1.getAgeing(), obj2.getAgeing());

		case 0:
			return UIUtils.compareTo(obj1.getTransactionDate(),
					obj2.getTransactionDate());

		case 4:
			return UIUtils.compareDouble(
					obj1.getTotal() * obj1.getCurrencyFactor(), obj2.getTotal()
							* obj1.getCurrencyFactor());

		}
		return 0;
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

	@Override
	public void restoreView(HashMap<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		long payeeID = ((Long) map.get("payeeID"));

		int view_Id = (Integer) map.get("viewId");
		if (this.payeeId == 0
				&& Accounter.getCompany().getPayee(payeeID) != null) {
			setPayeeId(payeeID);
			toolbar.setPayeeId(payeeID);
			Boolean isVendor = (Boolean) map.get("isVendor");
			this.isVendor = isVendor == null ? false : isVendor;
		}
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		CreateStatementToolBar bar = (CreateStatementToolBar) toolbar;
		bar.setStatus(view_Id);
		toolbar.setViewId(view_Id);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		isDatesArranged = true;
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		int viewId = toolbar.getViewId();
		long payeeID = getPayeeId();
		map.put("selectedDateRange", selectedDateRange);
		map.put("payeeID", payeeID);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("isVendor", isVendor);
		map.put("viewId", viewId);
		return map;
	}

	public long getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(long payeeId) {
		this.payeeId = payeeId;
	}

}
