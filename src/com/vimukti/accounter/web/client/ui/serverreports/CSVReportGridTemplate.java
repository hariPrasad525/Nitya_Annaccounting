package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.DataUtils;

public class CSVReportGridTemplate<R> extends ReportGridTemplate {
	private String additionalDetails;

	public CSVReportGridTemplate(String[] columns, boolean ishowGridFooter) {
		super(columns, ishowGridFooter);
	}

	@Override
	public void addRow(Object record, int depth, Object[] values, boolean bold,
			boolean underline, boolean border) {
		if (list.size() == 0) {
			prepareBodyHead();
		}
		list.add(record);
		for (int i = 0; i < columns.length; i++) {
			if (maxDepth < depth)
				this.maxDepth = depth;
			if (values.length > i && values[i] != null) {
				if (reportView.getColumnstoHide().contains(i) && record != null) {
					this.body.append("");
				} else if (columnTypes[i] == COLUMN_TYPE_AMOUNT) {
					this.body.append(getValue(values[i]).replace(",", ""));
				} else if (columnTypes[i] == COLUMN_TYPE_PERCENTAGE) {
					if (getValue(values[i]).isEmpty()) {
						this.body.append("");
					} else {
						this.body.append(getValue(values[i]).replace(",", "")
								+ " %");
					}
				} else {
					this.body
							.append(String.valueOf(values[i]).replace(",", ""));
				}
			} else {
				this.body.append("");
			}
			this.body.append(',');
		}
		this.body.append('\n');
	}

	@Override
	public String getBody() {
		System.out.println("It is the body of CSV Report " + this.body);
		System.err.println(this.body);
		if (reportView instanceof StatementServerReport) {
			return this.additionalDetails + this.body.toString();
		} else {
			return this.body.toString();
		}
	}

	@Override
	public String getValue(Object object) {
		return (object instanceof Double ? DataUtils
				.getAmountAsStringInCurrency((Double) object,
						reportView.getCurrencySymbol()) : object.toString());
	}

	@Override
	public void initBody() {

	}

	@Override
	public void addDepthStyle(int row, int column, int depth) {

	}

	@Override
	protected int getCellWidth(int index) {
		return 0;
	}

	@Override
	protected String[] getColumns() {
		return null;
	}

	@Override
	public String getFooter() {
		return null;
	}

	@Override
	public String getHeader() {
		return null;
	}

	@Override
	public boolean isReportViewHasToCols() {
		return false;
	}

	@Override
	public void removeAllRows() {

	}

	@Override
	public void setColumnTypes(int[] columnTypes) {
		this.columnTypes = columnTypes;
	}

	@Override
	public void addCell(boolean bold, String cellValue, int depth,
			boolean underline, int cellWidth, int columnType) {

	}

	public void prepareBodyHead() {
		String[] headerTitles = reportView.getColunms();
		if (body == null) {
			body = new StringBuffer();
		}
		for (int i = 0; i < headerTitles.length; i++) {
			body.append(headerTitles[i].replace(",", "") + ",");
		}
		body.append('\n');
	}

	@Override
	public String getBody(AccounterMessages messages) {
		if (body == null || body.toString().isEmpty()) {
			body = new StringBuffer(messages.noRecordsToShow());
		}

		if (reportView instanceof StatementServerReport) {
			return this.additionalDetails + this.body.toString();
		} else {
			return this.body.toString();
		}
	}

	@Override
	public void addAdditionalDetails(String[] details) {
		StringBuffer dataBuffer = new StringBuffer();
		if (details[0].trim().length() > 0)
			dataBuffer.append(details[0] + "\n");
		if (details[1].trim().length() > 1)
			dataBuffer.append(details[1] + "\n");
		if (details[2].trim().length() > 1)
			dataBuffer.append(details[2] + "\n");
		if (details[3].trim().length() > 1)
			dataBuffer.append(details[3] + "\n");
		if (details[4].trim().length() > 1)
			dataBuffer.append(details[4] + "\n");
		if (details[5].trim().length() > 1)
			dataBuffer.append(details[5] + "\n");
		if (details[6].trim().length() > 1)
			dataBuffer.append(details[6] + "\n");
		if (details[7].trim().length() > 1)
			dataBuffer.append(details[7] + "\n");
		this.additionalDetails = dataBuffer.toString();
	}
}
