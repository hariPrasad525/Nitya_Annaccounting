package com.nitya.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.reports.PaySheet;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.reports.AbstractReportView;
import com.nitya.accounter.web.client.ui.reports.PaySheetReport;
import com.nitya.accounter.web.client.ui.reports.ReportGrid;

public class PaySheetServerReport extends AbstractFinaneReport<PaySheet> {

	protected ArrayList<ClientPayHead> payheads = new ArrayList<ClientPayHead>();
	protected int noColumns = 2;
	private ArrayList<String> sectiontypes = new ArrayList<String>();
	private List<String> types = new ArrayList<String>();
	private double rowTotal;

	public PaySheetServerReport(PaySheetReport paySheetReport) {
		this.reportView = paySheetReport;
		Accounter.createPayrollService().getPayheads(0, 0,
				new AsyncCallback<PaginationList<ClientPayHead>>() {

					@Override
					public void onSuccess(PaginationList<ClientPayHead> result) {
						payheads = result;
						noColumns = result.size() + 2;
						initGrid();
						ReportGrid<PaySheet> g = ((AbstractReportView<PaySheet>) PaySheetServerReport.this.reportView).grid;
						g.setColumns(getColunms());
						g.setColumnTypes(getColumnTypes());
						PaySheetServerReport.this.reportView.refresh();
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	public PaySheetServerReport(long startDate, long endDate,
			int generationType, ArrayList<ClientPayHead> payheadsList) {
		super(startDate, endDate, generationType);
		payheads = payheadsList;
		noColumns = payheadsList.size() + 2;
		initGrid();
//		ReportGrid<PaySheet> g = ((AbstractReportView<PaySheet>) PaySheetServerReport.this.reportView).grid;
//		g.setColumns(getColunms());
//		g.setColumnTypes(getColumnTypes());
//		PaySheetServerReport.this.reportView.refresh();
	}

	@Override
	public String[] getDynamicHeaders() {
		String[] headers = new String[noColumns];
		headers[0] = getMessages().categoryNumber();
		for (int i = 0; i < payheads.size(); i++) {
			headers[i + 1] = payheads.get(i).getName();
		}
		headers[noColumns - 1] = getMessages().total();
		return headers;
	}

	@Override
	public String getTitle() {
		return messages.paySheet();
	}

	@Override
	public String[] getColunms() {
		String[] headers = new String[noColumns];
		headers[0] = getMessages().categoryNumber();
		for (int i = 0; i < payheads.size(); i++) {
			headers[i + 1] = payheads.get(i).getName();
		}
		headers[noColumns - 1] = getMessages().total();
		return headers;
	}

	@Override
	public int[] getColumnTypes() {
		int[] columnTypes = new int[noColumns];
		columnTypes[0] = COLUMN_TYPE_TEXT;
		for (int i = 1; i < noColumns - 1; i++) {
			columnTypes[i] = COLUMN_TYPE_AMOUNT;
		}
		columnTypes[noColumns - 1] = COLUMN_TYPE_AMOUNT;
		return columnTypes;
	}

	@Override
	public void processRecord(PaySheet record) {

		if (this.handler == null)
			// initHandler();
			if (sectionDepth == 0) {
				addTypeSection("", getMessages().total());
			}

	}

	@Override
	public void resetVariables() {
		this.types.clear();
		this.sectiontypes.clear();
	}

	public void addTypeSection(String title, String bottomTitle) {
		if (!sectiontypes.contains(title)) {
			int[] nocolumn = new int[noColumns - 1];
			for (int i = 1; i < noColumns; i++) {
				nocolumn[i - 1] = i;
			}
			addSection(new String[] { title }, new String[] { bottomTitle },
					nocolumn);
			types.add(title);
			sectiontypes.add(title);
		}
	}

	@Override
	public Object getColumnData(PaySheet record, int columnIndex) {

		if (columnIndex == 0) {
			rowTotal = 0;
			return record.getEmployee();
		} else if (columnIndex == noColumns - 1) {
			return rowTotal;
		} else {
			long location_id = 0;
			location_id = payheads.get(columnIndex - 1).getID();

			Map<Long, Double> map = record.getMap();
			Double value = map.get(location_id);
			if (value != null) {
				rowTotal += value;
			}
			return value;
		}
	}

	@Override
	public ClientFinanceDate getStartDate(PaySheet obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(PaySheet obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
