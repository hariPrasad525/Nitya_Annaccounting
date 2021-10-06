package com.nitya.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.Sorting;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public abstract class ReportGridTemplate<R> {

	public String decimalCharacter = Global.get().preferences()
			.getDecimalCharacter();

	protected String[] columns = {};

	List<R> list = new ArrayList<R>();

	public R selectedObjecd;
	public int maxDepth = 0;

	String header, footer;

	StringBuffer body = null;

	public AbstractFinaneReport<R> reportView;
	protected int columnTypes[];
	public static final int COLUMN_TYPE_TEXT = 1;
	public static final int COLUMN_TYPE_AMOUNT = 2;
	public static final int COLUMN_TYPE_DATE = 3;
	public static final int COLUMN_TYPE_NUMBER = 4;
	public static final int COLUMN_TYPE_PERCENTAGE = 5;

	Sorting<R> sorting;

	public ReportGridTemplate(String[] columns, boolean ishowGridFooter) {
		this.columns = columns;
	}

	public void clear() {
		this.removeAllRows();
	}

	public abstract void addRow(R record, int depth, Object[] values,
			boolean bold, boolean underline, boolean border);

	public abstract String getValue(Object object);

	public void addFooter(Object[] values) {

	}

	public void initHeader() {

	}

	public abstract void initBody();

	public abstract String getBody();

	public abstract String getBody(AccounterMessages messages);

	public abstract void addCell(boolean bold, String cellValue, int depth,
			boolean underline, int cellWidth, int columnType);

	public abstract void addDepthStyle(int row, int column, int depth);

	protected abstract int getCellWidth(int index);

	protected abstract String[] getColumns();

	public void setReportView(AbstractFinaneReport<R> abstractFinaneReport) {
		this.reportView = abstractFinaneReport;
		this.columnTypes = this.reportView.getColumnTypes();
	}

	public abstract boolean isReportViewHasToCols();

	public abstract void setColumnTypes(int columnTypes[]);

	public void setHeaderText(String text, int column) {

	}

	public abstract void removeAllRows();

	public abstract String getHeader();

	public abstract String getFooter();

	public String getFooterTable() {
		return footer;
	}

	public void init() {

	}

	public abstract void prepareBodyHead();
	public void addAdditionalDetails(String[] details){
	}
}
