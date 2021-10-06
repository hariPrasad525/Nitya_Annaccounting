package com.nitya.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientJob;
import com.nitya.accounter.web.client.core.ClientLocation;
import com.nitya.accounter.web.client.core.reports.ProfitAndLossByClass;
import com.nitya.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;
import com.nitya.accounter.web.client.ui.reports.ISectionHandler;
import com.nitya.accounter.web.client.ui.reports.Section;

public class ProfitAndLossByLocationServerReport extends
		AbstractFinaneReport<ProfitAndLossByLocation> {
	protected List<String> sectiontypes = new ArrayList<String>();
	protected List<String> types = new ArrayList<String>();
	private String curentParent;

	protected Double totalincome = 0.0D;
	protected Double totalCGOS = 0.0D;
	protected Double grosProft = 0.0D;
	protected Double totalexpese = 0.0D;
	protected Double netIncome = 0.0D;
	protected Double otherIncome = 0.0D;
	protected Double otherExpense = 0.0D;
	protected Double otherNetIncome = 0.0D;
	public static final int CLASS = 1;
	public static final int LOCATION = 2;
	public static final int JOB = 3;
	private double rowTotal = 0;
	private int category_type;
	private boolean isLocation;
	public static int noColumns;

	public static ArrayList<ClientLocation> locations = null;
	public static ArrayList<ClientAccounterClass> classes = null;
	public static ArrayList<ClientJob> jobs = null;

	public ProfitAndLossByLocationServerReport(long startDate, long endDate,
			boolean isLocation, int generationType, ClientCompany clientCompany) {
		super(startDate, endDate, generationType);
		this.columnstoHide.add(3);
		this.columnstoHide.add(5);
		this.isLocation = isLocation;
		if (isLocation) {
			this.noColumns = clientCompany.getLocations().size() + 2;
			this.locations = clientCompany.getLocations();
		} else {
			// this.classes = clientCompany.getAccounterClasses();
			this.noColumns = clientCompany.getAccounterClasses().size() + 2;
		}
	}

	public ProfitAndLossByLocationServerReport(
			IFinanceReport<ProfitAndLossByLocation> profitAndLossByLocationReport,
			int category_type) {
		this.reportView = profitAndLossByLocationReport;
		this.category_type = category_type;
	}

	public ProfitAndLossByLocationServerReport(ClientCompany company,
			long startDate, int type, long endDate, int generationType) {
		super(startDate, endDate, generationType);
		this.category_type = type;
		if (category_type == JOB) {
			this.jobs = company.getJobs();
			this.noColumns = jobs.size() + 2;
		} else if (category_type == CLASS) {
			this.classes = getHeaderTitles(company);
			this.noColumns = classes.size() + 2;
		} else if (category_type == LOCATION) {
			this.locations = company.getLocations();
			this.noColumns = locations.size() + 2;
		}
	}

	private ArrayList<ClientAccounterClass> getHeaderTitles(
			ClientCompany company) {
		ArrayList<ClientAccounterClass> accounterClasses = new ArrayList<ClientAccounterClass>(
				company.getAccounterClasses());

		ArrayList<ClientAccounterClass> reportHeaders = new ArrayList<ClientAccounterClass>();
		// sort by depth
		Collections.sort(accounterClasses,
				new Comparator<ClientAccounterClass>() {
					@Override
					public int compare(ClientAccounterClass o1,
							ClientAccounterClass o2) {
						int res = o1.getPath().compareTo(o2.getPath());
						return true ? (-1 * res) : (res);
					}
				});
		long previousparentID = 0;
		int count = 0;

		for (ClientAccounterClass clientAccounterClass : accounterClasses) {
			String className = clientAccounterClass.getClassName();
			if (previousparentID != clientAccounterClass.getParent()
					&& previousparentID != 0) {
				String concat = className.concat("-Other");
				clientAccounterClass.setModifiedName(concat);
				reportHeaders.add(clientAccounterClass);
				ClientAccounterClass totalobj = new ClientAccounterClass();
				totalobj.setModifiedName(messages.total() + "(" + className
						+ ")");
				reportHeaders.add(totalobj);
				previousparentID = 0;
			} else if (accounterClasses.size() - 1 == count) {
				String concat = className.concat("-Other");
				clientAccounterClass.setModifiedName(concat);
				reportHeaders.add(clientAccounterClass);
				ClientAccounterClass totalobj = new ClientAccounterClass();
				totalobj.setModifiedName(messages.total() + "(" + className
						+ ")");
				reportHeaders.add(totalobj);
			} else {
				reportHeaders.add(clientAccounterClass);
				previousparentID = clientAccounterClass.getParent();
			}
			count++;
		}
		return reportHeaders;
	}

	@Override
	public String[] getDynamicHeaders() {
		String[] headers = new String[noColumns];
		headers[0] = getMessages().categoryNumber();
		if (category_type == JOB) {
			for (int i = 0; i < jobs.size(); i++) {
				headers[i + 1] = jobs.get(i).getJobName();
			}
		} else if (category_type == CLASS) {
			for (int i = 0; i < classes.size(); i++) {
				headers[i + 1] = classes.get(i).getModifiedName() == null ? classes
						.get(i).getClassName() : classes.get(i)
						.getModifiedName();
			}
		} else {
			for (int i = 0; i < locations.size(); i++) {
				headers[i + 1] = locations.get(i).getLocationName();
			}
		}
		headers[noColumns - 1] = getMessages().total();
		return headers;
	}

	@Override
	public String getTitle() {
		if (category_type == JOB) {
			return getMessages().profitAndLossByJob();
		} else if (category_type == CLASS) {
			return getMessages().profitAndLossbyClass();
		} else {
			return getMessages().profitAndLoss() + "  By  "
					+ Global.get().Location();
		}

	}

	@Override
	public String[] getColunms() {
		String[] headers = new String[noColumns];
		headers[0] = getMessages().categoryNumber();
		if (category_type == JOB) {
			for (int i = 0; i < jobs.size(); i++) {
				headers[i + 1] = jobs.get(i).getJobName();
			}
		} else if (category_type == CLASS) {
			for (int i = 0; i < classes.size(); i++) {
				headers[i + 1] = classes.get(i).getModifiedName() == null ? classes
						.get(i).getClassName() : classes.get(i)
						.getModifiedName();
			}
		} else {
			for (int i = 0; i < locations.size(); i++) {
				headers[i + 1] = locations.get(i).getLocationName();
			}
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
	public void processRecord(ProfitAndLossByLocation record) {

		if (this.handler == null)
			initHandler();
		if (sectionDepth == 0) {
			addTypeSection("", getMessages().netProfit());
		}
		addOrdinaryIncomeOrExpenseTypes(record);

		if (closePrevSection(record.getParentAccount() == 0 ? record
				.getAccountName() : getAccountNameById(record
				.getParentAccount()))) {
			processRecord(record);
		} else {
			addSection(record);
			return;
		}
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return -1;
		case 1:
			return 100;
		case 2:
			return 110;
		case 3:
			return 120;
		case 4:
			return 100;
		case 5:
			return 150;
		default:
			return -1;
		}
	}

	public String getAccountNameById(long id) {
		for (ProfitAndLossByLocation balance : this.records)
			if (balance.getAccountId() == id)
				return balance.getAccountName();
		return null;
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

	public void addTypeSection(String sectionType, String title,
			String bottomTitle) {
		if (!sectiontypes.contains(sectionType)) {
			int[] nocolumn = new int[noColumns - 1];
			for (int i = 1; i < noColumns; i++) {
				nocolumn[i - 1] = i;
			}
			addSection(new String[] { title }, new String[] { bottomTitle },
					nocolumn);
			types.add(title);
			sectiontypes.add(sectionType);
		}
	}

	private void initHandler() {
		intivalues();
		this.handler = new ISectionHandler<ProfitAndLossByLocation>() {

			@Override
			public void OnSectionAdd(Section<ProfitAndLossByLocation> section) {
				if (section.title.equals(getMessages().grossProfit())) {
					section.data[0] = "";
				}
			}

			@Override
			public void OnSectionEnd(Section<ProfitAndLossByLocation> section) {
				if (section.title.equals(getMessages().income())) {
					totalincome = Double.valueOf(section.data[noColumns - 1]
							.toString());
				}
				if (section.title.equals(getMessages().costOfGoodSold())) {
					totalCGOS = Double.valueOf(section.data[noColumns - 1]
							.toString());
				}
				if (section.title.equals(getMessages().otherExpense())) {
					otherExpense = Double.valueOf(section.data[noColumns - 1]
							.toString());
				}
				if (section.footer.equals(getMessages().grossProfit())) {
					grosProft = totalincome - totalCGOS - otherExpense;
					section.data[noColumns - 1] = grosProft;
				}
				if (section.title.equals(getMessages().expense())) {
					totalexpese = (Double) section.data[noColumns - 1];
				}
				if (section.footer.equals(getMessages().netProfit())) {
					netIncome = grosProft - totalexpese;
					section.data[noColumns - 1] = netIncome;
				}
				if (section.title.equals(getMessages().otherIncome())) {
					otherIncome = Double.valueOf(section.data[noColumns - 1]
							.toString());
				}
				if (section.title.equals(getMessages().otherIncomeOrExpense())) {
					otherNetIncome = otherIncome - otherExpense;
					section.data[noColumns - 1] = otherNetIncome;
				}
			}
		};
	}

	private void intivalues() {
		totalincome = 0.0D;
		totalCGOS = 0.0D;
		grosProft = 0.0D;
		totalexpese = 0.0D;
		netIncome = 0.0D;
		otherIncome = 0.0D;
		otherExpense = 0.0D;
		otherNetIncome = 0.0D;
	}

	@Override
	public Object getColumnData(ProfitAndLossByLocation record, int columnIndex) {
		Double value = 0.0;
		if (columnIndex == 0) {
			rowTotal = 0;
			return record.getAccountNumber() + "-" + record.getAccountName();
		} else if (columnIndex == noColumns - 1) {
			return rowTotal;
		} else {
			long location_id = 0;
			if (category_type == JOB) {
				location_id = jobs.get(columnIndex - 1).getID();
			} else if (category_type == CLASS) {
				value = getcolumnvalue(record, columnIndex);
				// location_id = classes.get(columnIndex - 1).getID();
			} else {
				location_id = locations.get(columnIndex - 1).getID();
			}

			if (!(category_type == CLASS)) {
				Map<Long, Double> map = record.getMap();
				value = map.get(location_id);
			}
			if (value != null) {
				rowTotal = value;
			}
			return value;
		}
	}

	private Double getcolumnvalue(ProfitAndLossByLocation record,
			int columnIndex) {
		for (ProfitAndLossByClass profitAndLossByClass : record.getRecords()) {
			if (profitAndLossByClass.getIndex() == columnIndex) {
				return profitAndLossByClass.getTotal();
			}
		}
		return null;

	}

	@Override
	public void resetVariables() {
		this.types.clear();
		this.sectiontypes.clear();
		curentParent = "";
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

	@Override
	public ClientFinanceDate getStartDate(ProfitAndLossByLocation obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(ProfitAndLossByLocation obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub
	}

	public void addOrdinaryIncomeOrExpenseTypes(ProfitAndLossByLocation record) {

		if (record.getAccountType() == ClientAccount.TYPE_INCOME
				|| record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			if (!sectiontypes.contains(getMessages().grossProfit())) {
				addTypeSection(getMessages().grossProfit(), "", getMessages()
						.grossProfit());

			}
			if (record.getAccountType() == ClientAccount.TYPE_INCOME)
				if (!sectiontypes.contains(getMessages().income())) {

					addTypeSection(getMessages().income(), getMessages()
							.incomeTotals());
				}
			if (record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				if (!sectiontypes.contains(getMessages().costOfGoodSold())) {
					closeOtherSections();
					closeSection(types.indexOf(getMessages().income()));
					addTypeSection(getMessages().costOfGoodSold(),
							getMessages().cogsTotal());
				}
		}

		if (record.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {

			if (!sectiontypes.contains(getMessages().otherExpense())) {
				for (int i = types.size() - 2; i > 0; i--) {
					closeSection(i);
				}
				addTypeSection(getMessages().otherExpense(), getMessages()
						.otherExpenseTotals());
			}
		}

		if (record.getAccountType() == ClientAccount.TYPE_EXPENSE) {

			if (!sectiontypes.contains(getMessages().expense())) {
				closeAllSection();
				addTypeSection(getMessages().expense(), getMessages()
						.expenseTotals());
			}
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
					if (sectionDepth > 0)
						endSection();
					return true;
				}
			}
		return false;
	}

	public boolean addSection(ProfitAndLossByLocation record) {
		if (isParent(record)) {
			types.add(record.getAccountName());
			curentParent = record.getAccountName();
			int[] nocolumn = new int[noColumns - 1];
			for (int i = 1; i < noColumns; i++) {
				nocolumn[i - 1] = i;
			}
			addSection(
					record.getAccountNumber() + "-" + record.getAccountName(),
					record.getAccountName() + "  " + getMessages().total(),
					nocolumn);
			return true;
		}
		return false;
	}

	public boolean isParent(ProfitAndLossByLocation record) {
		for (ProfitAndLossByLocation balance : this.records) {
			if (balance.getParentAccount() != 0) {
				if (balance.getParentAccount() == record.getAccountId())
					return true;
			}
		}
		return false;
	}

	public void closeSection(int index) {
		if (index >= 0) {
			types.remove(index);
			curentParent = "";
			endSection();
		}
	}

	protected String getPreviousReportDateRange(Object object) {
		return ((ProfitAndLossByLocation) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((ProfitAndLossByLocation) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((ProfitAndLossByLocation) object).getEndDate();
	}

	public ClientFinanceDate getLastMonth(ClientFinanceDate date) {
		int month = date.getMonth() - 1;
		int year = date.getYear();

		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return new ClientFinanceDate(date.getYear(), date.getMonth() - 1,
				lastDay);
		// return lastDay;
	}
}
