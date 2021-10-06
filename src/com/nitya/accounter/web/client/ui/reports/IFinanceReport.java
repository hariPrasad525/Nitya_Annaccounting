package com.nitya.accounter.web.client.ui.reports;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientFinanceDate;

public interface IFinanceReport<R> {

	boolean isServerSide();

	public ISectionHandler<R> getSectionHanlder();

	public void addRow(R record, int depth, Object[] values, boolean bold,
			boolean underline, boolean border);

	public void addFooter(Object[] values);

	public void endAllSections();

	public void addSection(String sectionTitle, String footerTitle,
			int[] sumColumns);

	public void addSection(String[] sectionTitles, String[] footerTitles,
			int[] sumColumns);

	public String getTitle();

	public String[] getColunms();

	public int[] getColumnTypes();

	public void processRecord(R record);

	public boolean isWiderReport();

	public abstract Object getColumnData(R record, int columnIndex);

	public void endSection();

	public boolean isIshowGridFooter();

	public void setIshowGridFooter(boolean ishowGridFooter);

	public String getDefaultDateRange();

	public int getColumnWidth(int index);

	public ClientFinanceDate getStartDate(R obj);

	public ClientFinanceDate getEndDate(R obj);

	public void refresh();

	public List<Integer> getColumnstoHide();

	public void setColumnstoHide(List<Integer> columnstoHide);

	public void setHandler(ISectionHandler<R> handler);

	public void initRecords(List<R> records);

	public void updateTotals(Object[] values);

	public void resetVariables();

	public void setRecords(List<R> records);

	public List<R> getRecords();

	public void removeAllRows();

	public ClientFinanceDate getCurrentFiscalYearStartDate();

	public ClientFinanceDate getCurrentFiscalYearEndDate();

	public void setStartAndEndDates(ClientFinanceDate startDate2,
			ClientFinanceDate endDate2);

	public String[] getDynamicHeaders();

	String getQuantityValue(Object object, int column);

	// public String getDateByCompanyType(ClientFinanceDate date);
	// public ClientFinanceDate getEndDate();
	//
	// public ClientFinanceDate getStartDate() ;
}
