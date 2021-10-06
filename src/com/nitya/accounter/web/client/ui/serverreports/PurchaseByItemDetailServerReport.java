package com.nitya.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.ReportUtility;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class PurchaseByItemDetailServerReport extends
		AbstractFinaneReport<SalesByCustomerDetail> {
	private String sectionName = " ";
	private List<String> list = new ArrayList<String>();
	private List<String> parents = new ArrayList<String>();
	private List<String> last = new ArrayList<String>();
	private int depth;
	private boolean haveSubs;

	public PurchaseByItemDetailServerReport(
			IFinanceReport<SalesByCustomerDetail> reportView) {
		this.reportView = reportView;
	}

	public PurchaseByItemDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(SalesByCustomerDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return getDateByCompanyType(record.getDate());
		case 2:
			return ReportUtility.getTransactionName(record.getType());
		case 3:
			return record.getNumber();
		case 4:
			return record.getQuantity();
		case 5:
			return record.getUnitPrice();
		case 6:
			if (getPreferences().isTrackDiscounts()) {
				return record.getDiscount();
			} else {
				return record.getAmount();
			}
		case 7:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

	@Override
	public int[] getColumnTypes() {
		if (getPreferences().isTrackDiscounts()) {
			return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
					COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_PERCENTAGE,
					COLUMN_TYPE_AMOUNT };
		} else {
			return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
					COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
		}
	}

	@Override
	public String[] getColunms() {
		if (getPreferences().isTrackDiscounts()) {
			return new String[] { getMessages().item(), getMessages().date(),
					getMessages().type(), getMessages().noDot(),
					getMessages().quantity(), getMessages().unitPrice(),
					getMessages().discount(), getMessages().amount() };
		} else {
			return new String[] { getMessages().item(), getMessages().date(),
					getMessages().type(), getMessages().noDot(),
					getMessages().quantity(), getMessages().unitPrice(),
					getMessages().amount() };
		}
	}

	@Override
	public String getTitle() {
		return getMessages().purchaseByItemDetail();
	}

	@Override
	public int getColumnWidth(int index) {

		switch (index) {
		case 1:
			return 85;
		case 2:
			return 100;
		case 3:
			return 70;
		case 4:
			return 100;
		case 5:
			return 120;
		case 6:
			return 120;
		case 7:
			return 120;
		case 0:
			return 250;

		default:
			return -1;
		}

	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// if (navigateObjectName == null) {
		//
		// onSuccess(this.financeTool.getPurchasesByItemDetail(start, end));
		// } else {
		// onSuccess(this.financeTool.getPurchasesByItemDetail(
		// navigateObjectName, start, end));
		// }
		// } catch (DAOException e) {
		// }
	}

	@Override
	public void processRecord(SalesByCustomerDetail record) {

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

		String name = null;

		for (int x = rest; x < parents.size(); x++) {
			name = parents.get(x);
			last.add(name);
			list.add(name);
			if (getPreferences().isTrackDiscounts()) {
			addSection(new String[] { name },
					new String[] { "", " ", messages.reportTotal(name) },
					new int[] { 7 }, record.getItemsDepthMap().get(name)
							.intValue());
			}else{
				addSection(new String[] { name },
						new String[] { "", " ", messages.reportTotal(name) },
						new int[] { 6 }, record.getItemsDepthMap().get(name)
								.intValue());
			}
			// startSection(name,depth);
			depth++;
			haveSubs = false;
		}
		if (haveSubs) {

			name = getLast(); // get last one and + Other
			if (getPreferences().isTrackDiscounts()) {
			addSection(new String[] { name },
					new String[] { "", " ", messages.reportTotal(name) },
					new int[] { 7 }, record.getItemsDepthMap().get(name)
							.intValue());
			}else{
			addSection(new String[] { name },
					new String[] { "", " ", messages.reportTotal(name) },
					new int[] { 6 }, record.getDepth());
			}
			// startSection(name, depth);
			depth++;
			haveSubs = false;
		}

		return;

		// // if (sectionDepth == 0) {
		// // addSection(new String[] { "", "" }, new String[] { "", "", "", "",
		// // "", "", getMessages().total()}, new int[] { 7 });
		// // } else
		// if (sectionDepth == 0) {
		// this.sectionName = record.getItemName();
		// addSection(new String[] { sectionName }, new String[] { "", "", "",
		// "", "", "", getMessages().total() }, new int[] { 7 });
		// } else if (sectionDepth == 1) {
		// // No need to do anything, just allow adding this record
		// if (!sectionName.equals(record.getItemName())) {
		// endSection();
		// } else {
		// return;
		// }
		// }
		// // Go on recursive calling if we reached this place
		// processRecord(record);
	}

	private String getLast() {
		return last.get(last.size() - 1) + "-" + messages.other();
	}

	public void print() {
		// String gridhtml = grid.toString();
		// String headerhtml = grid.getHeader();
		// String footerhtml = grid.getFooter();
		//
		// gridhtml = gridhtml.replaceAll(headerhtml, "");
		// gridhtml = gridhtml.replaceAll(footerhtml, "");
		// headerhtml = headerhtml.replaceAll("td", "th");
		// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
		// headerhtml.indexOf("</tbody>"));
		// footerhtml = footerhtml.substring(footerhtml.indexOf("<tr>"),
		// footerhtml.indexOf("</tbody"));
		// footerhtml = footerhtml.replaceAll("<tr>",
		// "<tr class=\"listgridfooter\">");
		//
		// String firsRow = "<tr class=\"ReportGridRow\">"
		// + "" + "</tr>";
		// String lastRow = "<tr class=\"ReportGridRow\">"
		// + grid.rowFormatter.getElement(grid.getRowCount() - 1)
		// .getInnerHTML() + "</tr>";
		//
		// headerhtml = headerhtml + firsRow;
		// footerhtml = lastRow + footerhtml;
		//
		// gridhtml = gridhtml.replace(firsRow, headerhtml);
		// gridhtml = gridhtml.replace(lastRow, footerhtml);
		// gridhtml = gridhtml.replaceAll("<tbody>", "");
		// gridhtml = gridhtml.replaceAll("</tbody>", "");
		//
		// String dateRangeHtml =
		// "<div style=\"font-family:sans-serif;\"><strong>"
		// + this.getStartDate()
		// + " - "
		// + this.getEndDate() + "</strong></div>";
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

	public int sort(SalesByCustomerDetail obj1, SalesByCustomerDetail obj2,
			int col) {

		int ret = obj1.getItemName().toLowerCase()
				.compareTo(obj2.getItemName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());

		case 1:
			return obj1.getDate().compareTo(obj2.getDate());

		case 3:
			return UIUtils.compareInt(Integer.parseInt(obj1.getNumber()),
					Integer.parseInt(obj2.getNumber()));

		case 0:
			return obj1.getItemName().toLowerCase()
					.compareTo(obj2.getItemName().toLowerCase());

		case 4:
			return UIUtils.compareTo(obj1.getQuantity(), obj2.getQuantity());
		case 5:
			return UIUtils.compareDouble(obj1.getUnitPrice(),
					obj2.getUnitPrice());

		case 6:
			if (getPreferences().isTrackDiscounts()) {
				return UIUtils.compareDouble(obj1.getDiscount(),
						obj2.getDiscount());
			} else {
				UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
			}
		case 7:
			UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());

		}
		return 0;
	}

	@Override
	public void resetVariables() {
		sectionDepth = 0;
		sectionName = "";
		list.clear();
		last.clear();
		parents.clear();
		depth = 0;
		super.resetVariables();
	}

	@Override
	public String[] getDynamicHeaders() {
		if (getPreferences().isTrackDiscounts()) {
			return new String[] { getMessages().item(), getMessages().date(),
					getMessages().type(), getMessages().noDot(),
					getMessages().quantity(), getMessages().unitPrice(),
					getMessages().discount(), getMessages().amount() };
		} else {
			return new String[] { getMessages().item(), getMessages().date(),
					getMessages().type(), getMessages().noDot(),
					getMessages().quantity(), getMessages().unitPrice(),
					getMessages().amount() };
		}
	}

}
