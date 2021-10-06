package com.nitya.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.Sorting;
import com.nitya.accounter.web.client.core.reports.JobProfitability;
import com.nitya.accounter.web.client.core.reports.JobProfitabilityDetailByJob;
import com.nitya.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.grids.CustomTable;
import com.nitya.accounter.web.client.ui.grids.GwtFlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * ReportGrid is Table which is used to display reports data in tree structure.
 * 
 * @author kumar kasimala
 * 
 * @param <R>
 */
public class ReportGrid<R> extends CustomTable {

	private String[] columns = {};
	List<R> list = new ArrayList<R>();

	public R selectedObjecd;
	public int maxDepth = 0;

	public AbstractReportView<R> reportView;

	private int columnTypes[];

	public static final int COLUMN_TYPE_TEXT = 1;
	public static final int COLUMN_TYPE_AMOUNT = 2;
	public static final int COLUMN_TYPE_DATE = 3;
	public static final int COLUMN_TYPE_NUMBER = 4;
	public static final int COLUMN_TYPE_PERCENTAGE = 5;
	public static final int COLUMN_TYPE_QUATITY = 6;

	Sorting<R> sorting;
	Element bodyrowElem;

	public ReportGrid(String[] columns) {
		super(false, true);
		this.columns = columns;
		sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
		setStyleName("Report Grid");
	}

	public ReportGrid(String[] columns, boolean showFooter) {
		super(false, showFooter);
		this.columns = columns;
		sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
		setStyleName("Report Grid");
	}

	/**
	 * Clear All Rows in Table
	 */
	@Override
	public void clear() {
		this.removeAllRows();
	}

	/**
	 * Add row to this table
	 * 
	 * @param record
	 *            is actual object
	 * @param depth
	 * @param values
	 * @param bold
	 *            whether row font bold or not
	 * @param underline
	 * @param border
	 */
	public void addRow(R record, int depth, Object[] values, boolean bold,
			boolean underline, boolean border) {

		list.add(record);
		int rowCount = this.getTableRowCount();

		addDefaultStyleNames(rowCount, bold, underline);

		for (int i = 0; i < columns.length; i++) {
			if (maxDepth < depth)
				this.maxDepth = depth;
			if (values.length > i && values[i] != null) {
				if (reportView != null
						&& reportView.getColumnstoHide().contains(i)
						&& record != null) {
					addCell(rowCount, i, bold, "", depth, false);
				} else if (columnTypes[i] == COLUMN_TYPE_AMOUNT) {
					addCell(rowCount, i, bold, getValue(values[i]), depth,
							underline);
				} else if (columnTypes[i] == COLUMN_TYPE_PERCENTAGE) {
					addCell(rowCount,
							i,
							bold,
							values[i] instanceof Double ? DataUtils
									.getAmountAsStrings((Double) values[i])
									+ " %" : values[i].toString(), depth, false);
				} else if (columnTypes[i] == COLUMN_TYPE_QUATITY) {
					addCell(rowCount, i, bold, getQuantityValue(values[i], i),
							depth, underline);
				} else {
					addCell(rowCount, i, bold, values[i].toString(), depth,
							false);
				}
			} else {
				addCell(rowCount, i, bold, "", depth, false);
			}

			addStyleNameByCol(rowCount, columnTypes[i], i, this.cellFormatter);
		}
		if (rowCount == 0)
			this.adjustCellsWidth(rowCount, body);

		super.fixHeader();

	}

	protected String getQuantityValue(Object object, int column) {
		return object == null ? " " : object.toString();
	}

	private void addStyleNameByCol(int rowCount, int coltype, int col,
			CellFormatter cellFormatter) {
		if (cellFormatter != null
				&& Arrays.asList(COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT,
						COLUMN_TYPE_PERCENTAGE, COLUMN_TYPE_QUATITY).contains(
						coltype)) {
			cellFormatter.addStyleName(rowCount, col, "gridDecimalCell");
		}

	}

	private String getValue(Object object) {
		return (object instanceof Double ? DataUtils
				.amountAsStringWithCurrency((Double) object, getCurrency())
				: object.toString());
	}

	protected ClientCurrency getCurrency() {
		return Accounter.getCompany().getPrimaryCurrency();
	}

	private void addDefaultStyleNames(int rowCount, boolean bold,
			boolean underline) {
		if (rowFormatter != null) {
			rowFormatter.addStyleName(rowCount, "Report grid Row");
			if (!bold && !underline) {
				rowFormatter.addStyleName(rowCount, "depth");
			}
		}
	}

	public void addFooter(Object[] values) {
		for (int i = 0; i < columns.length; i++) {
			if (values.length > i && values[i] != null) {
				if (columnTypes[i] == COLUMN_TYPE_AMOUNT) {
					this.footer.setText(0, i, getValue(values[i]));
				} else if (columnTypes[i] == COLUMN_TYPE_PERCENTAGE) {
					this.footer.setText(0, i,
							values[i] instanceof Double ? values[i].toString()
									+ " %" : values[i].toString());
				} else if (columnTypes[i] == COLUMN_TYPE_QUATITY) {
					this.footer.setText(0, i, getQuantityValue(values[i], i));
				} else {
					this.footer.setText(0, i, values[i].toString());
				}
			} else {
				this.footer.setHTML(0, i, "<div></div>");
			}
			if (this.footer.getCellFormatter() != null) {
				this.footer.getCellFormatter().setAlignment(0, i,
						HasHorizontalAlignment.ALIGN_LEFT,
						HasVerticalAlignment.ALIGN_MIDDLE);

				addStyleNameByCol(0, columnTypes[i], i,
						this.footer.getCellFormatter());
			}
		}
	}

	/**
	 * Add Header to Table
	 */
	@Override
	protected void initHeader() {
		if (rowFormatter != null) {
			int rowCount = this.header.getRowCount();

			if (columns != null) {
				for (int i = 0; i < columns.length; i++) {
					this.header.setText(0, i, columns[i].toString());
					if (this.header.getCellFormatter() != null) {
						this.header.getCellFormatter().addStyleName(rowCount,
								i, "Report Grid header Style");
						this.header.getCellFormatter().setAlignment(rowCount,
								i, HasHorizontalAlignment.ALIGN_LEFT,
								HasVerticalAlignment.ALIGN_MIDDLE);
						this.header.getCellFormatter().getElement(rowCount, i)
								.getStyle().setFontWeight(FontWeight.BOLD);

						addStyleNameByCol(0, columnTypes[i], i,
								this.header.getCellFormatter());
					}
				}
			}

			rowFormatter.addStyleName(rowCount, "report Grid Header");
		}

	}

	/**
	 * add a Cell to table
	 * 
	 * @param row
	 * @param column
	 * @param bold
	 * @param cellValue
	 * @param depth
	 * @param isPrimaryData
	 */
	private void addCell(int row, int column, boolean bold, String cellValue,
			int depth, boolean underline) {

		this.setText(row, column, cellValue);
		if (cellFormatter != null) {
			if (bold) {
				cellFormatter.addStyleName(row, column, "bold-cell");
			}

			addDepthStyle(row, column, depth);

			if (underline) {
				cellFormatter.addStyleName(row, column, "underline");
			}
			cellFormatter.addStyleName(row, column, "Report-grid-custom-font");
			cellFormatter.setAlignment(row, column,
					HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);
		}
	}

	private void addDepthStyle(int row, int column, int depth) {
		boolean canAdd = (reportView instanceof ProfitAndLossReport || reportView instanceof BalanceSheetReport);
		boolean flag = false;

		if (column == 0 && !canAdd) {
			flag = true;
		} else if (column == 1 && canAdd) {
			flag = true;
		}
		if (flag)
			cellFormatter.addStyleName(row, column, "depth" + depth);

	}

	/**
	 * get Actual object of Row
	 * 
	 * @param event
	 * @return
	 */
	public R getRecordForEvent(ClickEvent event) {
		return selectedObjecd;
	}

	@Override
	protected void cellClicked(int rowIndex, int cellIndex) {
		if (list.size() > rowIndex && list.get(rowIndex) != null) {
			selectedObjecd = list.get(rowIndex);
			// For Realted job click on single column
			if (reportView instanceof JobProfitabilitySummaryReport) {
				((JobProfitabilitySummaryReport) reportView).OnClick(
						(JobProfitability) selectedObjecd, rowIndex, cellIndex);
			} else if (reportView instanceof JobProfitabilityDetailReport) {
				((JobProfitabilityDetailReport) reportView).OnClick(
						(JobProfitabilityDetailByJob) selectedObjecd, rowIndex,
						cellIndex);
			} else if (reportView instanceof ProfitAndLossByLocationReport) {
				((ProfitAndLossByLocationReport) reportView).OnClick(
						(ProfitAndLossByLocation) selectedObjecd, rowIndex,
						cellIndex);
			} else {
				reportView.OnRecordClick(selectedObjecd);
			}
		}

	}

	@Override
	public void cellDoubleClicked(int row, int col) {

	}

	@Override
	protected int getCellWidth(int index) {
		// if (index == 0) {
		// return 200;
		// }
		// return -1;
		if (reportView != null) {
			return reportView.getColumnWidth(index);
		} else {
			return 100;
		}
	}

	@Override
	protected String[] getColumns() {
		return columns;
	}

	@Override
	public void headerCellClicked(int colIndex) {
		if (sorting != null) {
			sorting.isDecending = isDecending;
			sorting.sort(reportView.getRecords(), colIndex);
			isDecending = !sorting.isDecending;
			reportView.refresh();
		}
	}

	public void setReportView(AbstractReportView<R> reportView) {
		this.reportView = reportView;
		sorting = new Sorting<R>(reportView);
	}

	@Override
	protected void adjustCellsWidth(int row, GwtFlexTable table) {

		try {
			int parentWidth = table.getOffsetWidth();

			int[] colsUpdate = new int[nofCols];

			int cellWidth = -1;
			int colCounts = 0;
			for (int i = 0; i < nofCols; i++) {
				cellWidth = getCellWidth(i);
				if (isReportViewHasToCols() && i == nofCols - 2) {
					colCounts++;
					continue;
				}
				if (cellWidth == -2)
					continue;
				if (cellWidth == -1) {
					colsUpdate[colCounts++] = i;
				} else {
					int cellSize = table.getCellCount(row);
					if (cellSize <= i) {
						continue;
					}
					if (table.getCellFormatter() != null) {
						Element cell = table.getCellFormatter().getElement(row,
								i);
						parentWidth = parentWidth - cellWidth;
						cell.setAttribute("width", "" + cellWidth);
					}
				}
			}

			// cellWidth = parentWidth / colCounts;
			//
			// for (int col : colsUpdate) {
			// if (col != 0) {
			// Element cell = table.getCellFormatter()
			// .getElement(row, col);
			// cell.setAttribute(FinanceApplication.constants()
			// .width(), "" + cellWidth);
			// }
			// }

			if (isMultiSelectionEnable) {
				// table.getCellFormatter().getElement(row, 0)
				// .setAttribute(messages.width(), "" + 15);
			}

		} catch (Exception e) {

		}

	}

	public boolean isReportViewHasToCols() {
		return reportView instanceof CashFlowStatementReport ? true : false;
	}

	public void setColumnTypes(int columnTypes[]) {
		this.columnTypes = columnTypes;
	}

	public void setHeaderText(String text, int column) {
		this.header.setText(0, column, text);
	}

	@Override
	public void removeAllRows() {
		if (this.list != null)
			this.list.clear();
		super.removeAllRows();
	}

	public String getHeader() {
		return this.header.toString();
	}

	public String getFooter() {
		return this.footer != null ? this.footer.toString() : "";
	}

	public GwtFlexTable getFooterTable() {
		return this.footer;
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOVER:
			if (bodyrowElem != null)
				bodyrowElem.removeClassName("report-hover");
			bodyrowElem = (Element) DOM.eventGetTarget(event)
					.getParentElement();
			if (DOM.isOrHasChild(this.body.getPanel().getElement(), bodyrowElem)) {
				bodyrowElem.addClassName("report-hover");

			}
			break;
		case Event.ONMOUSEOUT:
			if (bodyrowElem != null)
				bodyrowElem.removeClassName("report-hover");
			break;
		}
		super.onBrowserEvent(event);
	}

	public void removeEmptyStyle() {
		this.body.removeStyleName("no_records");
	}

	// protected void addEmptyMessage(String msg) {
	// this.body.setText(0, 0, msg);
	// this.cellFormatter.setHorizontalAlignment(0, 0,
	// HasHorizontalAlignment.ALIGN_CENTER);
	// }

	public void setColumns(String[] columns) {
		this.columns = columns;
	}
}
