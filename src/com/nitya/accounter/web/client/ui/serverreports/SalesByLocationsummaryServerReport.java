package com.nitya.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.SalesByLocationSummary;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class SalesByLocationsummaryServerReport extends
		AbstractFinaneReport<SalesByLocationSummary> {
	private String sectionName = "";
	private String currentsectionName = "";
	private double accountBalance = 0.0D;
	private boolean isLocation;
	private boolean isCustomer;
	private List<String> parents = new ArrayList<String>();
	private List<String> list = new ArrayList<String>();
	private List<String> last = new ArrayList<String>();
	private boolean haveSubs = false;
	private int depth;
	private boolean isEmptySectionEnabled = true;
	private boolean isHavingSubItems = true;

	public SalesByLocationsummaryServerReport(
			IFinanceReport<SalesByLocationSummary> reportView,
			boolean isLocation, boolean isCustomer) {
		this.reportView = reportView;
		this.isLocation = isLocation;
		this.isCustomer = isCustomer;

	}

	public SalesByLocationsummaryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", Global.get().messages().total() };
	}

	@Override
	public String getTitle() {
		String actionsting = null;
		if (isCustomer) {
			if (isLocation) {
				actionsting = messages.salesByLocationSummary(Global.get()
						.Location());
			} else {
				actionsting = messages.salesByClassSummary();
			}
		} else {
			if (isLocation) {
				actionsting = messages.purchasesbyLocationSummary(Global.get()
						.Location());
			} else {
				actionsting = messages.purchasesbyClassSummary();
			}
		}
		return actionsting;
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", Global.get().messages().total() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(SalesByLocationSummary record) {
		if (isLocation) {
			if (sectionDepth == 0) {
				addSection(new String[] { "" }, new String[] { getMessages()
						.total() }, new int[] { 1 });
			} else if (sectionDepth == 1) {
				return;
			}
			// Go on recursive calling if we reached this place
			processRecord(record);
		} else {

			parents = record.getParents();
			if (parents.size() == 1 && list.isEmpty()) {
				isHavingSubItems = false;
				if (sectionDepth == 0) {
					addSection(new String[] { "" },
							new String[] { getMessages().total() },
							new int[] { 1 });
				} else if (sectionDepth == 1) {
					return;
				}
				// Go on recursive calling if we reached this place
				processRecord(record);
			} else {
				if (!isHavingSubItems) {
					endSection();
					isHavingSubItems = true;
				}
				if (!isEmptySectionEnabled) {
					addSection(new String[] {},
							new String[] { messages.total() }, new int[] { 1 });
					isEmptySectionEnabled = true;
				}
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

				String name = null;

				for (int x = rest; x < parents.size(); x++) {
					name = parents.get(x);
					last.add(name);
					list.add(name);
					addSection(new String[] { name },
							new String[] { messages.reportTotal(name) },
							new int[] { 1 }, record.getDepthsByName().get(name)
									.intValue());
					// startSection(name,depth);
					depth++;
					haveSubs = false;
				}
				if (haveSubs) {

					name = getLast(); // get last one and + Other
					addSection(new String[] { name }, new String[] {},
							new int[] {}, record.getDepth() + 1);
					// startSection(name, depth);
					depth++;
					haveSubs = false;
				}

				return;
			}
		}
	}

	private String getLast() {
		return sectionName = last.get(last.size() - 1) + "-" + messages.other();
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 150;
		case 1:
			return 300;
		}
		return -1;
	}

	@Override
	public Object getColumnData(SalesByLocationSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getRecordSectionName(record);
		case 1:
			return record.getTotal();
		}
		return null;
	}

	@Override
	public void resetVariables() {
		list.clear();
		last.clear();
		parents.clear();
		depth = 0;
		isEmptySectionEnabled = false;
		isHavingSubItems = true;
		super.resetVariables();
	}

	private String getRecordSectionName(SalesByLocationSummary record) {
		return record.getLocationName() == null ? getMessages().notSpecified()
				: record.getLocationName();
	}

	@Override
	public ClientFinanceDate getStartDate(SalesByLocationSummary obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(SalesByLocationSummary obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub
	}

}
