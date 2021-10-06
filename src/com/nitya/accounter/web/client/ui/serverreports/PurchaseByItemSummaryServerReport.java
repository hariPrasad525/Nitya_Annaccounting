package com.nitya.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class PurchaseByItemSummaryServerReport extends
		AbstractFinaneReport<SalesByCustomerDetail> {
	private List<String> list = new ArrayList<String>();
	private List<String> parents = new ArrayList<String>();
	private List<String> last = new ArrayList<String>();
	private int depth;
	private boolean haveSubs;
	private boolean isEmptySectionEnabled = false;
	private String sectionName = " ";
	private boolean isHavingSubItems = true;

	public PurchaseByItemSummaryServerReport(
			IFinanceReport<SalesByCustomerDetail> reportView) {
		this.reportView = reportView;
	}

	public PurchaseByItemSummaryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(SalesByCustomerDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getItemName();
			// case 1:
			// return record.getItemGroup();
		case 1:
			return record.getQuantity();
		case 2:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().item(), getMessages().quantity(),
				getMessages().amount() };
	}

	@Override
	public String getTitle() {
		return getMessages().purchaseByItemSummary();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// onSuccess(this.financeTool.getPurchasesByItemSummary(start, end));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void processRecord(SalesByCustomerDetail record) {
		parents = record.getParents();

		if (parents.size() == 1 && list.isEmpty()) {
			isHavingSubItems = false;
			if (sectionDepth == 0) {
				addSection(new String[] { "", "" }, new String[] { "",
						getMessages().total() }, new int[] { 2 });
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
						new String[] { " ", messages.total() }, new int[] { 2 });
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
						new String[] { "", messages.reportTotal(name) },
						new int[] { 2 }, record.getItemsDepthMap().get(name)
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

		// if (sectionDepth == 0) {
		// addSection(new String[] { "", "" }, new String[] { "",
		// getMessages().total() }, new int[] { 2 });
		// } else if (sectionDepth == 1) {
		// return;
		// }
		// // Go on recursive calling if we reached this place
		// processRecord(record);
	}

	private String getLast() {
		return (sectionName = last.get(last.size() - 1)) + "-"
				+ messages.other();
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

	public void print() {
		// String gridhtml = grid.toString();
		// String headerhtml = grid.getHeader();
		//
		// gridhtml = gridhtml.replaceAll(headerhtml, "");
		// gridhtml = gridhtml.replaceAll(grid.getFooter(), "");
		//
		// headerhtml = headerhtml.replaceAll("td", "th");
		// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
		// headerhtml.indexOf("</tbody>"));
		//
		// String firsRow = "<tr class=\"ReportGridRow depth\">" + "" + "</tr>";
		// headerhtml = headerhtml + firsRow;
		// gridhtml = gridhtml.replace(firsRow, headerhtml);
		// gridhtml = gridhtml.replaceAll("<tbody>", "");
		// gridhtml = gridhtml.replaceAll("</tbody>", "");
		//
		// String dateRangeHtml =
		// "<div style=\"font-family:sans-serif;\"><strong>"
		// + this.getStartDate()
		// + " - "
		// + this.getEndDate()
		// + "</strong></div>";
		//
		// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	@Override
	public ClientFinanceDate getEndDate(SalesByCustomerDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(SalesByCustomerDetail obj) {
		return obj.getStartDate();
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((BaseReport) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 1)
			return 200;
		else if (index == 2)
			return 300;
		else
			return -1;

	}

	public int sort(SalesByCustomerDetail obj1, SalesByCustomerDetail obj2,
			int col) {
		switch (col) {
		case 0:
			return obj1.getItemName().toLowerCase()
					.compareTo(obj2.getItemName().toLowerCase());
			// case 1:
			// return obj1.getItemGroup().toLowerCase().compareTo(
			// obj2.getItemGroup().toLowerCase());
		case 1:
			return UIUtils.compareTo(obj1.getQuantity(), obj2.getQuantity());
		case 2:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());

		}
		return 0;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().item(), getMessages().quantity(),
				getMessages().amount() };
	}

}