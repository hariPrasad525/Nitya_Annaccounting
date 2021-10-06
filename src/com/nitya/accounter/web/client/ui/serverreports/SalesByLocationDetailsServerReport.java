package com.nitya.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.SalesByLocationDetails;
import com.nitya.accounter.web.client.ui.core.ReportUtility;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class SalesByLocationDetailsServerReport extends
		AbstractFinaneReport<SalesByLocationDetails> {
	private String sectionName = "";
	private double accountBalance = 0.0D;

	private String currentsectionName = "";
	private boolean isLocation;
	private boolean isCustomer;
	private List<String> parents = new ArrayList<String>();
	private List<String> list = new ArrayList<String>();
	private List<String> last = new ArrayList<String>();
	private boolean haveSubs = false;
	private int depth;

	public SalesByLocationDetailsServerReport(
			IFinanceReport<SalesByLocationDetails> reportView,
			boolean isLocation, boolean isCustomer) {
		this.reportView = reportView;
		this.isLocation = isLocation;
		this.isCustomer = isCustomer;

	}

	public SalesByLocationDetailsServerReport(long startDate, long endDate,
			int generationType, boolean isLocation, boolean isCustomer) {
		super(startDate, endDate, generationType);
		this.isLocation = isLocation;
		this.isCustomer = isCustomer;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", messages.date(), messages.type(),
				messages.number(), messages.Account(),
				messages.productOrService(), messages.amount(),
				messages.balance() };
	}

	@Override
	public String getTitle() {
		String actionsting = null;
		if (isCustomer) {
			if (isLocation) {
				actionsting = messages.getSalesByLocationDetails(Global.get()
						.Location());
			} else {
				actionsting = messages.salesByClassDetails();
			}
		} else {
			if (isLocation) {
				actionsting = messages.purchasesbyLocationDetail(Global.get()
						.Location());
			} else {
				actionsting = messages.purchasesbyClassDetail();
			}
		}
		return actionsting;
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", messages.date(), messages.type(),
				messages.number(), messages.Account(),
				messages.productOrService(), messages.amount(),
				messages.balance() };
	}

	@Override
	public int[] getColumnTypes() {

		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 225;
		case 1:
		case 5:
			return 115;
		case 2:
			return 100;
		case 4:
			return 150;
		default:
			return 75;
		}
	}

	@Override
	public void processRecord(SalesByLocationDetails record) {
		if (isLocation) {
			if (sectionDepth == 0) {
				this.sectionName = getRecordSectionName(record);
				addSection(new String[] { sectionName }, new String[] { "" },
						new int[] { 6 });
			} else if (sectionDepth == 1) {
				// No need to do anything, just allow adding this record
				if (!sectionName.equals(getRecordSectionName(record))) {
					endSection();
				} else {
					return;
				}
			} else if (sectionDepth == 2) {
				addSection(new String[] { "", "" }, new String[] { "", "", "",
						"", messages.total() }, new int[] { 6 });
			}

			// Go on recursive calling if we reached this place
			processRecord(record);
		} else {
			parents = record.getParents();

			// close all extra
			for (int x = list.size(); x > parents.size(); x--) {
				last.remove(x - 1);
				list.remove(x - 1);
				endSection();
				depth--;
				if (depth > list.size()) {
					endSection();
					depth--;
					haveSubs = true;
				} else {
					haveSubs = true;
				}
			}

			int rest = list.size();

			for (int x = list.size() - 1; x >= 0; x--) {
				if (!list.get(x).equals(parents.get(x))) {
					last.remove(x);
					list.remove(x);
					endSection();
					depth--;
					rest--;
					if (depth > list.size()) {
						endSection();
						depth--;
					} else {
						haveSubs = true;
					}
				}
			}

			String name;

			for (int x = rest; x < parents.size(); x++) {
				name = parents.get(x);
				last.add(name);
				list.add(name);
				addSection(new String[] { name }, new String[] { "", "",
						messages.reportTotal(name) }, new int[] { 6 }, record
						.getDepthsByName().get(name));
				// startSection(name,depth);
				depth++;
				haveSubs = false;
			}
			if (haveSubs) {

				name = getLast(); // get last one and + Other
				addSection(new String[] { name }, new String[] { "", "",
						messages.reportTotal(name) }, new int[] { 6 },
						record.getDepth());
				// startSection(name, depth);
				depth++;
				haveSubs = false;
			}

			return;
		}
	}

	private String getLast() {
		return last.get(last.size() - 1) + "-" + messages.other();
	}

	private String getRecordSectionName(SalesByLocationDetails record) {
		return record.getLocationName() == null ? getMessages().notSpecified()
				: record.getLocationName();

	}

	@Override
	public Object getColumnData(SalesByLocationDetails record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return getDateByCompanyType(new ClientFinanceDate(record.getDate()));
		case 2:
			return ReportUtility.getTransactionName(record.getType());
		case 3:
			return record.getNumber();
		case 4:
			return record.getAccount();
		case 5:
			return record.getProuductOrService();
		case 6:
			return record.getAmount();
		case 7:
			if (!currentsectionName.equals(getRecordSectionName(record))) {
				currentsectionName = getRecordSectionName(record);
				accountBalance = 0.0D;
			}
			double amount = record.getAmount();
			accountBalance += amount;
			return (accountBalance);
		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(SalesByLocationDetails obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(SalesByLocationDetails obj) {
		return obj.getEndDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
		this.currentsectionName = "";
		list.clear();
		last.clear();
		parents.clear();
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

}
