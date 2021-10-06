package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.serverreports.AbstractFinaneReport.Alignment;

public class PDFReportGridTemplate<R> extends ReportGridTemplate {
	private String additionalDetails = "";

	public PDFReportGridTemplate(String[] columns, boolean ishowGridFooter) {
		super(columns, ishowGridFooter);
	}

	@Override
	public void addRow(Object record, int depth, Object[] values, boolean bold,
			boolean underline, boolean border) {
		if (list.size() == 0) {
			initBody();
			prepareBodyHead();
		}
		list.add(record);

		this.body.append("<tr class='ReportGridRow'>");
		for (int i = 0; i < columns.length; i++) {
			if (maxDepth < depth)
				this.maxDepth = depth;
			if (values.length > i && values[i] != null) {
				if (reportView.getColumnstoHide().contains(i) && record != null) {
					addCell(bold, "", depth, false,
							reportView.getColumnWidth(i), columnTypes[i]);
				} else if (columnTypes[i] == COLUMN_TYPE_AMOUNT) {
					addCell(bold, getValue(values[i]), depth, underline,
							reportView.getColumnWidth(i), columnTypes[i]);
				} else if (columnTypes[i] == COLUMN_TYPE_PERCENTAGE) {
					if (getValue(values[i]) == "") {
						addCell(bold, "", depth, false,
								reportView.getColumnWidth(i), columnTypes[i]);
					} else {
						addCell(bold, getValue(values[i]) + " %", depth, false,
								reportView.getColumnWidth(i), columnTypes[i]);
					}
				} else {
					addCell(bold, String.valueOf(values[i]), depth, false,
							reportView.getColumnWidth(i), columnTypes[i]);
				}
			} else {
				addCell(bold, "", depth, false, reportView.getColumnWidth(i),
						columnTypes[i]);
			}

		}
		this.body.append("</tr>");
	}

	@Override
	public String getBody() {
		if (body == null || body.toString().isEmpty()) {
			this.body.append(Global.get().messages().noRecordsToShow());
		} else {
			this.body.append("</table></div></div></td></tr></table>");
		}
		return this.additionalDetails + this.body.toString();
	}

	@Override
	public void prepareBodyHead() {
		String[] headerTites = reportView.getColunms();
		body.append("<tr class=\"gridHeaderRow\"> ");
		for (int i = 0; i < headerTites.length; i++) {
			body.append("<th style=\"vertical-align: middle;padding-left:10px;\"class=\"ReportGridheaderStyle depth2");
			if (columnTypes[i] == COLUMN_TYPE_AMOUNT
					|| columnTypes[i] == COLUMN_TYPE_DATE
					|| columnTypes[i] == COLUMN_TYPE_NUMBER
					|| columnTypes[i] == COLUMN_TYPE_PERCENTAGE) {
				body.append(" gridDecimalCell");
			}

			Alignment headerAlign = reportView.getHeaderHAlign(i);
			if (headerAlign == null) {
				int columnType = columnTypes[i];
				if (columnType == COLUMN_TYPE_NUMBER
						|| columnType == COLUMN_TYPE_AMOUNT
						|| columnType == COLUMN_TYPE_DATE
						|| columnType == COLUMN_TYPE_TEXT) {
					headerAlign = Alignment.H_ALIGN_CENTER;
				} else if (columnType == COLUMN_TYPE_PERCENTAGE) {
					headerAlign = Alignment.H_ALIGN_RIGHT;
				} else {
					headerAlign = Alignment.H_ALIGN_CENTER;
				}
			}
			switch (headerAlign) {
			case H_ALIGN_CENTER:
				body.append("\" align=\"left\"");
				break;
			case H_ALIGN_LEFT:
				body.append("\" align=\"left\"");
				break;
			case H_ALIGN_RIGHT:
				body.append("\" align=\"left\"");
				break;
			default:
				body.append("\" align=\"left\"");
				break;
			}

			body.append(" width=\"" + reportView.getColumnWidth(i) + "\">"
					+ headerTites[i] + "</th>");
		}
		body.append("<tr><td><br/><br/></td><tr><th class=\"gridHeaderLastTd\" width=\"17px\"><div class=\"gwt-Label\"></div></th></tr>");
	}

	@Override
	public String getValue(Object object) {

		return (object instanceof Double ? DataUtils
				.getAmountAsStringInCurrency((Double) object,
						reportView.getCurrencySymbol()) : object.toString());
	}

	@Override
	public void initBody() {
		this.body = new StringBuffer(
				"<table style=\"width: 100%; height: 307px;\" class=\"ReportGrid\" cellpadding=\"0\" cellspacing=\"0\"><tr><td class=\"gridHeaderParent\" style=\"vertical-align: top;\" align=\"left\"></td></tr><tr><td style=\"vertical-align: top;\" align=\"left\"><div style=\"width: 100%; height: 285px;\" class=\"gridBodyContainer\"><div style=\"position: relative;\"><table  style=\"width: 100%;\" class=\"gridBody\"><colgroup><col></colgroup>");
	}

	// @Override
	// public void initHeader() {
	// header = "<html><table><tr class='ReportGridHeader'>";
	// if (columns != null) {
	// for (int i = 0; i < columns.length; i++) {
	// this.header = this.header + "<td style='width:"
	// + reportView.getColumnWidth(i)
	// + "'class='ReportGridheaderStyle'>"
	// + columns[i].toString() + "</td>";
	// }
	// this.header = this.header + "</tr></table>";
	// }
	// }

	@Override
	public void addCell(boolean bold, String cellValue, int depth,
			boolean underline, int cellWidth, int columnType) {
		this.body.append("<td style=\"vertical-align: middle;\" class=\"");

		if (bold) {
			body.append("bold ");
		}

		if (underline) {
			body.append("underline ");
		}
		if (!underline && cellValue != null) {
			body.append("depth");
			body.append(depth);
			body.append(' ');
		}

		if (columnType == COLUMN_TYPE_AMOUNT || columnType == COLUMN_TYPE_DATE
				|| columnType == COLUMN_TYPE_NUMBER
				|| columnType == COLUMN_TYPE_PERCENTAGE) {
			body.append("gridDecimalCell ");
		}

		body.append("ReportGridcustomFont\" title=\"");
		body.append(cellValue);
		if (columnType == COLUMN_TYPE_AMOUNT) {
			body.append("\" align=\"left\" ");
		} else if (columnType == COLUMN_TYPE_TEXT) {
			body.append("\" align=\"left\" ");
		} else {
			body.append("\" align=\"left\" ");
		}

		if (cellWidth != -1) {
			body.append(" width=\"");
			body.append(cellWidth);
			body.append("px\"");
		}
		body.append('>');
		body.append(cellValue);
		body.append("</td>");
	}

	@Override
	public void addDepthStyle(int row, int column, int depth) {
		boolean canAdd = (reportView instanceof ProfitAndLossServerReport || reportView instanceof BalanceSheetServerReport);

		boolean flag = false;

		if (column == 0 && !canAdd) {
			flag = true;
		} else if (column == 1 && canAdd) {
			flag = true;
		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0) {
			return 200;
		}
		return reportView.getColumnWidth(index);
	}

	@Override
	protected String[] getColumns() {
		return this.reportView.getColunms();
	}

	@Override
	public String getFooter() {
		return "</html>";
	}

	@Override
	public String getHeader() {
		return null;
	}

	@Override
	public boolean isReportViewHasToCols() {
		return reportView instanceof CashFlowStatementServerReport ? true
				: false;
	}

	@Override
	public void removeAllRows() {

	}

	@Override
	public void setColumnTypes(int[] columnTypes) {
		this.columnTypes = columnTypes;
	}

	@Override
	public String getBody(AccounterMessages messages) {

		if (body == null || body.toString().isEmpty()) {
			body = new StringBuffer("<html><body><center><p>"
					+ messages.noRecordsToShow()
					+ "</p></center></body></html>");
		} else {
			this.body.append("</table></div></div></td></tr></table>");
		}
		return this.additionalDetails + this.body.toString();
	}

	@Override
	public void addAdditionalDetails(String[] details) {
		StringBuffer dataBuffer = new StringBuffer();
		dataBuffer.append("<table><tr><td style=\"width:100%;\"><table>");
		for (int i = 0; i < details.length; i++) {
			if (details[i].trim().length() > 0) {
				dataBuffer.append("<tr><td>" + details[i] + "</td></tr>");
			}
			if (i == (details.length / 2)) {
				dataBuffer.append("</table></td><td  style=\"width:100%;\">");
				dataBuffer.append("<table style=\"width: 260px;\">");
			}
		}
		dataBuffer.append("</table></td></tr></table>");
		this.additionalDetails = dataBuffer.toString();
	}

}
