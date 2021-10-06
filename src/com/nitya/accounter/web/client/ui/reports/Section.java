package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.ui.serverreports.BalanceSheetServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ProfitAndLossByLocationServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ProfitAndLossServerReport;

public class Section<R> {

	private IFinanceReport<R> financeReport;

	public String title, footer;

	String[] titles, footers;
	int[] sumColumnsIndexes;

	public Object[] data = null;
	public boolean isaddFooter = true, ishowGridFooter;

	ISectionHandler<R> handler;

	private ReportGrid<R> reportGrid;
	
	private boolean ended = false;

	public Section(String title, String footer, int[] sumColums,
			int colsLength, IFinanceReport<R> report) {
		this.title = title;
		this.footer = footer;
		this.financeReport = report;
		data = new Object[colsLength];
		this.sumColumnsIndexes = sumColums;
		this.handler = report.getSectionHanlder();
	}

	public Section(String[] titles, String[] footers, int[] sumColums,
			int colsLength, IFinanceReport<R> report) {
		this.titles = titles;
		this.footers = footers;
		this.financeReport = report;
		data = new Object[colsLength];
		if (financeReport != null && !financeReport.isServerSide()) {
			updateTitleandFooterValuesonClient();
		} else {
			updateTitleandFooterValuesonServer();
		}
		this.sumColumnsIndexes = sumColums;
		if (report != null) {
			this.handler = report.getSectionHanlder();
		}
	}

	public Section(String[] titles, String[] footers, int[] sumColums,
			int colsLength, ReportGrid<R> report) {
		this.titles = titles;
		this.footers = footers;
		this.reportGrid = report;
		data = new Object[colsLength];
		this.sumColumnsIndexes = sumColums;
	}

	private void updateTitleandFooterValuesonServer() {
		if (financeReport instanceof BalanceSheetServerReport
				|| financeReport instanceof ProfitAndLossServerReport) {
			this.title = ((titles[0] == "" || titles[0] == null) ? titles[1]
					: titles[0]);
			this.footer = ((footers[0] == "" || footers[0] == null) ? footers[1]
					: footers[0]);
		} else if (financeReport instanceof ProfitAndLossByLocationServerReport) {
			this.title = titles[0] == null ? "" : titles[0];
			this.footer = footers[0] == null ? "" : footers[0];
		}
	}

	public boolean isEnded() {
		return ended;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	public void startSection() {
		startSection(0);
	}

	public void startSection(int depth) {
		if (handler != null) {
			handler.OnSectionAdd(this);
		}
		if (titles == null) {
			titles = new String[] { title };
		}
		if (isTitleEmpty())
			if (financeReport != null) {
				financeReport.addRow(null, depth, titles, true, false, false);
			} else {
				reportGrid.addRow(null, depth, titles, true, false, false);
			}

	}

	public void update(Object[] values) {
		for (int index : sumColumnsIndexes) {
			this.data[index] = sum((this.data[index] == ""
					|| this.data[index] == null ? 0.0 : this.data[index]),
					(values[index] == "" || values[index] == null ? 0.0
							: values[index]));
		}
	}

	private boolean isTitleEmpty() {
		for (String str : titles) {
			if (str == null)
				continue;
			str = str.trim();
			if (str.length() > 0)
				return true;
		}
		return false;
	}

	private double sum(Object a, Object b) {
		double da = a == null ? 0.0 : (Double) a;
		double db = b == null ? 0.0 : (Double) b;
		return da + db;
	}

	public void endSection() {
		updateFooterInData();

		if (handler != null) {
			handler.OnSectionEnd(this);
		}
		if (ishowGridFooter)
			if (financeReport != null) {
				financeReport.addFooter(data);
			} else {
				reportGrid.addFooter(data);
			}

		if (isaddFooter)
			if (financeReport != null) {
				financeReport.addRow(null, 2, data, true, true, true);
			} else {
				reportGrid.addRow(null, 2, data, true, true, true);
			}
		this.ended = true;
	}

	private void updateFooterInData() {
		if (footers == null) {
			footers = new String[] { footer };
		}
		int j = 0;
		for (String ft : footers) {
			// if (ft != null & ft.equals("")) {
			data[j] = ft;
			j++;
			// }
		}
	}

	private void updateTitleandFooterValuesonClient() {
		if (financeReport instanceof com.nitya.accounter.web.client.ui.reports.BalanceSheetReport
				|| financeReport instanceof com.nitya.accounter.web.client.ui.reports.ProfitAndLossReport) {
			this.title = ((titles[0] == "" || titles[0] == null) ? titles[1]
					: titles[0]);
			this.footer = ((footers[0] == "" || footers[0] == null) ? footers[1]
					: footers[0]);
		}
	}
}