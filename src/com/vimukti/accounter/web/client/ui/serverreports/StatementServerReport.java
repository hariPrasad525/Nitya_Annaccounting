package com.vimukti.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public class StatementServerReport extends
		AbstractFinaneReport<PayeeStatementsList> {
	private String monthName = "";
	private String sectionName = "";
	private final List<String> types = new ArrayList<String>();
	private final List<String> sectiontypes = new ArrayList<String>();
	private String curentParent;
	public int precategory = 1001;
	public String customerId;
	private final boolean isVendor;
	private double payeeBalance = 0.0D;

	private String currencySymbol;

	public StatementServerReport(boolean isVendor,
			IFinanceReport<PayeeStatementsList> reportView) {
		this.isVendor = isVendor;
		this.reportView = reportView;
		payeeBalance = 0.0D;
	}

	public StatementServerReport(boolean isVendor, long startDate,
			long endDate, int generationType) {
		super(startDate, endDate, generationType);
		this.isVendor = isVendor;
		payeeBalance = 0.0D;
	}

	@Override
	public Object getColumnData(PayeeStatementsList record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getDateByCompanyType(record.getTransactionDate());
		case 1:
			return Utility.getTransactionName(record.getTransactiontype());
		case 2:
			return record.getTransactionNumber();
		case 3:
			if (record.getTotal() == 0) {
				return record.getBalance();
			} else {
				Boolean positive = isVendor ? record.isPositiveForVendor()
						: record.isPositiveForCustomer();
				if (positive != null) {
					double total = (positive ? 1 : -1) * record.getTotal();
					return total;
				}
			}
			return record.getTotal();
		case 4:
			if (sectionName == null
					|| !sectionName.equals("" + record.getPayeeId())) {
				sectionName = "" + record.getPayeeId();
				payeeBalance = 0.0D;
			}
			if (record.getTotal() == 0) {
				payeeBalance = record.getBalance();
			} else {
				Boolean positive = isVendor ? record.isPositiveForVendor()
						: record.isPositiveForCustomer();
				if (positive != null) {
					double total = (positive ? 1 : -1) * record.getTotal();
					payeeBalance += total;
				}
			}
			return payeeBalance;
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_DATE, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().date(), getMessages().type(),
				getMessages().noDot(), getMessages().amount(),
				getMessages().balance() };
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 1)
			return 160;
		else if (index == 0)
			return 70;
		else if (index == 2)
			return 70;
		else if (index == 3)
			return 100;
		else if (index == 4)
			return 150;
		else
			return -1;
	}

	@Override
	public ClientFinanceDate getEndDate(PayeeStatementsList obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(PayeeStatementsList obj) {
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
	public String getTitle() {
		return messages.payeeStatement(Global.get().Customer());
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

	@Override
	public void processRecord(PayeeStatementsList record) {
		if (sectionDepth == 0) {
			addSection(new String[] { "" }, new String[] { "", "",
					getMessages().total() }, new int[] { 3 });
		} else if (sectionDepth == 1) {
			this.monthName = getMonthName(record.getTransactionDate()
					.getMonth());
			addSection(new String[] { monthName + " "
					+ record.getTransactionDate().getYear() }, new String[] {
					"", "", getMessages().reportTotal(monthName) },
					new int[] { 3 });
		} else if (sectionDepth == 2) {
			if (!monthName.equals(getMonthName(record.getTransactionDate()
					.getMonth()))) {
				endSection();
			} else {
				return;
			}
		}
		processRecord(record);

	}

	/**
	 * get the month Name By Number
	 * 
	 * @param month
	 *            Number
	 * @return {@link String} Month Name
	 */
	private String getMonthName(int month) {
		String monthNameByNumber = DateUtills.getMonthNameByNumber(month);
		return monthNameByNumber;
	}

	/**
	 * add Type Section
	 * 
	 * @param title
	 */
	public void addTypeSection(String title, String bottomTitle) {
		if (!sectiontypes.contains(title)) {
			addSection(new String[] { title }, new String[] { "", "", "",
					getMessages().total() }, new int[] { 4 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	public void closeAllSection() {
		for (int i = types.size() - 1; i > 0; i--) {
			closeSection(i);
		}
	}

	public void closeOtherSections() {
		for (int i = types.size() - 1; i > 0; i--) {
			closePrevSection(types.get(i));
		}
	}

	public boolean closePrevSection(String title) {
		if (curentParent != null && curentParent != "")
			if (!title.equals(curentParent)) {
				if (!sectiontypes.contains(curentParent)) {
					types.remove(types.size() - 1);
					if (types.size() > 0) {
						curentParent = types.get(types.size() - 1);
					}
					endSection();
					return true;
				}
			}
		return false;
	}

	public void closeSection(int index) {
		types.remove(index);
		curentParent = "";
		endSection();
	}

	@Override
	public void resetVariables() {
		// sectionDepth = 0;
		sectionName = "";
		precategory = 1001;
		types.clear();
		sectiontypes.clear();
		curentParent = "";
		payeeBalance = 0.0D;
		super.resetVariables();
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().date(), getMessages().type(),
				getMessages().noDot(), getMessages().amount(),
				getMessages().balance() };
	}

	@Override
	public void initRecords(List<PayeeStatementsList> records) {
		resetVariables();
		super.initRecords(records);

	}

	@Override
	public String getCurrencySymbol() {
		if (currencySymbol != null) {
			return currencySymbol;
		}
		return super.getCurrencySymbol();
	}

	public void setCurrency(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
}
