package com.nitya.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.TrialBalance;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;
import com.nitya.accounter.web.client.ui.reports.ISectionHandler;
import com.nitya.accounter.web.client.ui.reports.Section;

public class BalanceSheetServerReport extends
		AbstractFinaneReport<TrialBalance> {

	private List<String> types = new ArrayList<String>();
	private List<String> sectiontypes = new ArrayList<String>();
	private String curentParent;
	private double currentAssetsTotal;
	private double fixedAssetsTotal;
	private double otherAssetsTotal;
	private double assetsTotal;
	private double currentLiabilityTotal;
	private double longTermLiabilityTotal;
	private double equitiesTotal;
	private double liabilityAndEquityTotal;

	public BalanceSheetServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
		this.columnstoHide.add(3);
	}

	public BalanceSheetServerReport(IFinanceReport<TrialBalance> reportView) {
		this.columnstoHide.add(3);
		this.reportView = reportView;
	}

	@Override
	public void initData() {
		this.makeReportRequest(startDate.getDate(), endDate.getDate());
	}

	@Override
	public String getDefaultDateRange() {
		return getMessages().financialYearToDate();
	}

	@Override
	public Object getColumnData(TrialBalance record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if (getPreferences().getUseAccountNumbers() == true)
				return record.getAccountNumber();
			else
				return null;
		case 1:
			return record.getAccountName();
		case 2:
			return record.getAmount();
		case 3:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] {
				getMessages().categoryNumber(),
				"",
				getDateByCompanyType(getStartDate()) + "-"
						+ getDateByCompanyType(getEndDate()), "" };
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] {
				getMessages().categoryNumber(),
				"",
				getDateByCompanyType(getStartDate()) + "-"
						+ getDateByCompanyType(getEndDate()), "" };
	}

	@Override
	public String getTitle() {
		return getMessages().balanceSheet();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		//
		// initValues();
		// try {
		// onSuccess(this.financeTool.getBalanceSheetReport(start, end));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }

	}

	@Override
	public void processRecord(TrialBalance record) {

		if (this.handler == null)
			iniHandler();
		// if (sectionDepth == 0) {
		// addTypeSection(" ");
		// }
		addAssetTypes(record);
		// if (record.getBaseType() == ClientAccount.BASETYPE_ASSET) {
		// addAssetTypes(record);
		// } else if (record.getBaseType() == ClientAccount.BASETYPE_LIABILITY)
		// {
		// addLiablityTypes(record);
		// } else if (record.getBaseType() == ClientAccount.BASETYPE_EQUITY) {
		// if (!sectiontypes.contains(FinanceApplication.constants()
		// .equity())) {
		// closeOtherSections();
		// for (int i : new int[] { types.size() - 1, types.size() - 2 }) {
		// closeSection(i);
		// }
		// if (!sectiontypes.contains("Liabilities and Equity")) {
		// addTypeSection("Liabilities and Equity");
		// }
		// addTypeSection(FinanceApplication.constants().equity());
		//
		// }
		// }
		if (closePrevSection(record.getParentAccount() == 0 ? record
				.getAccountName() : getAccountNameById(record
				.getParentAccount()))) {
			processRecord(record);
		} else {
			addSection(record);
			return;
		}

	}

	public String getAccountNameById(long id) {
		for (TrialBalance balance : this.records)
			if (balance.getAccountId() == id)
				return balance.getAccountName();
		return null;
	}

	public void closeAllSection() {
		for (int i = types.size() - 1; i >= 0; i--) {
			closeSection(i);
		}
	}

	/**
	 * closes all Accounts sections
	 */
	public void closeOtherSections() {
		for (int i = types.size() - 1; i > 0; i--) {
			closePrevSection(types.get(i));
		}
	}

	public void addAssetTypes(TrialBalance record) {
		// if (!sectiontypes.contains(FinanceApplication.constants()
		// .asset())) {
		// closeAllSection();
		// addTypeSection("Asset");
		// }

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_ASSET
				|| record.getSubBaseType() == ClientAccount.SUBBASETYPE_FIXED_ASSET
				|| record.getSubBaseType() == ClientAccount.SUBBASETYPE_OTHER_ASSET) {
			if (!sectiontypes.contains(getMessages().assets())) {
				addTypeSection(getMessages().assets(), "", getMessages()
						.assetsTotal());
			}

			if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_ASSET) {
				if (!sectiontypes.contains(getMessages().currentAssets())) {
					addTypeSection(getMessages().currentAssets());
				}
			}
			if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_FIXED_ASSET) {
				if (!sectiontypes.contains(getMessages().fixedAssets())) {
					closeSection(types.indexOf(getMessages().currentAssets()));
					addTypeSection(getMessages().fixedAssets());
				}
			}
			if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_OTHER_ASSET) {
				if (!sectiontypes.contains(getMessages().otherAssets())) {
					closeSection(types.indexOf(getMessages().currentAssets()));
					closeSection(types.indexOf(getMessages().fixedAssets()));
					addTypeSection(getMessages().otherAssets());
				}
			}
		}

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_LIABILITY
				|| record.getSubBaseType() == ClientAccount.SUBBASETYPE_LONG_TERM_LIABILITY
				|| record.getSubBaseType() == ClientAccount.SUBBASETYPE_EQUITY) {
			if (!sectiontypes.contains(getMessages().liabilitiesandEquity())) {
				closeAllSection();
				addTypeSection(getMessages().liabilitiesandEquity(), "",
						getMessages().liabilitiesandEquityTotal());
			}

			if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_LIABILITY) {
				if (!sectiontypes.contains(getMessages().currentLiabilities())) {
					addTypeSection(getMessages().currentLiabilities());
				}
			}
			if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_LONG_TERM_LIABILITY) {
				if (!sectiontypes.contains(getMessages().longTermLiabilities())) {
					closeSection(types.indexOf(getMessages()
							.currentLiabilities()));
					addTypeSection(getMessages().longTermLiabilities());
				}
			}
			if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_EQUITY) {
				if (!sectiontypes.contains(getMessages().equity())) {
					closeSection(types.indexOf(getMessages()
							.currentLiabilities()));
					closeSection(types.indexOf(getMessages()
							.longTermLiabilities()));
					addTypeSection(getMessages().equity());
				}
			}
		}

	}

	// public void addAssetTypes(TrialBalance record) {
	//
	// if (!sectiontypes.contains(FinanceApplication.constants()
	// .netAssets())) {
	// addTypeSection(FinanceApplication.constants().netAssets(),
	// "", FinanceApplication.constants().netAssets());
	// }
	//
	// }

	public void addLiablityTypes(TrialBalance record) {

		if (!sectiontypes.contains(getMessages().liabilities())) {
			if (!sectiontypes.contains(getMessages().liabilitiesandEquity())) {
				closeOtherSections();
				closeAllSection();
				addTypeSection(getMessages().liabilitiesandEquity());
			}
			addTypeSection(getMessages().liabilities());
		}
		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_LIABILITY) {
			if (!sectiontypes.contains(getMessages().currentLiabilities())) {
				addTypeSection(getMessages().currentLiabilities());
			}
		}

		if (record.getSubBaseType() == ClientAccount.SUBBASETYPE_LONG_TERM_LIABILITY) {
			if (!sectiontypes.contains(getMessages().longTermLiabilities())) {
				closeOtherSections();
				closeSection(types.indexOf(getMessages().currentLiabilities()));
				addTypeSection(getMessages().longTermLiabilities());
			}
		}

	}

	public boolean isParent(TrialBalance record) {
		for (TrialBalance balance : this.records) {
			if (balance.getParentAccount() != 0)
				if (balance.getParentAccount() == record.getAccountId())
					return true;
		}
		return false;
	}

	public boolean addSection(TrialBalance record) {
		if (isParent(record)) {
			types.add(record.getAccountName());
			curentParent = record.getAccountName();
			addSection(new String[] { "", record.getAccountName() },
					new String[] {
							"",
							record.getAccountName() + " "
									+ getMessages().total() }, new int[] { 3 });
			return true;
		}
		return false;
	}

	public boolean closePrevSection(String title) {
		if (curentParent != null && curentParent != "") {
			if (!title.equals(curentParent)) {
				if (!sectiontypes.contains(curentParent)) {
					types.remove(types.size() - 1);
					if (types.size() > 0) {
						curentParent = types.get(types.size() - 1);
					}
					if (sectionDepth > 0) {
						endSection();
					}
					return true;
				}
			}
		}
		// else {
		// if (sectiontypes.contains(title)) {
		// types.remove(types.size() - 1);
		// if (types.size() > 0) {
		// curentParent = types.get(types.size() - 1);
		// }
		// endSection();
		// return true;
		// }
		// }
		// if (sectiontypes.contains(title)) {
		// types.remove(title);
		// sectiontypes.remove(title);
		// endSection();
		// return true;
		//
		// }
		return false;
	}

	public void closeSection(int index) {
		if (index >= 0) {
			types.remove(index);
			curentParent = "";
			endSection();
		}
	}

	/**
	 * add Type Section
	 * 
	 * @param title
	 */
	public void addTypeSection(String title) {
		if (!sectiontypes.contains(title)) {
			addSection(new String[] { "", title }, new String[] { "",
					title + " " + getMessages().total() }, new int[] { 3 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	/**
	 * add Type Section
	 * 
	 * @param title
	 */
	public void addTypeSection(String title, String bottomTitle) {
		if (!sectiontypes.contains(title)) {
			addSection(new String[] { "", title }, new String[] { "",
					bottomTitle }, new int[] { 3 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	public void addTypeSection(String sectionType, String title,
			String bottomTitle) {
		if (!sectiontypes.contains(sectionType)) {
			addSection(new String[] { "", title, }, new String[] { "",
					bottomTitle }, new int[] { 3 });
			types.add(title);
			sectiontypes.add(sectionType);
		}
	}

	/**
	 * Reset Variables in Report,
	 */
	@Override
	public void resetVariables() {
		this.types.clear();
		this.sectiontypes.clear();
		curentParent = "";
		initValues();
	}

	private void iniHandler() {

		initValues();
		ISectionHandler<TrialBalance> sectionHandler = new ISectionHandler<TrialBalance>() {

			// private double shareHoldersFund;

			@Override
			public void OnSectionAdd(Section<TrialBalance> section) {

				if (section.title.equals(getMessages().capitalAndReserves())) {
					grid.addRow(null, 0,
							new Object[] { " ", " ", " ", " ", " " }, false,
							false, false);
				}
			}

			@Override
			public void OnSectionEnd(Section<TrialBalance> section) {
				// prevent null pointer exception with title value
				if (section.title == null)
					section.title = "";
				if (section.title.equals(getMessages().currentAssets())) {
					currentAssetsTotal = Double.valueOf(section.data[3]
							.toString());
				}
				if (section.title.equals(getMessages().fixedAssets())) {
					fixedAssetsTotal = Double.valueOf(section.data[3]
							.toString());
				}
				if (section.title.equals(getMessages().otherAssets())) {
					otherAssetsTotal = Double.valueOf(section.data[3]
							.toString());
				}
				if (section.footer.equals(getMessages().assetsTotal())) {
					assetsTotal = currentAssetsTotal + fixedAssetsTotal
							+ otherAssetsTotal;
					section.data[3] = assetsTotal;
				}

				if (section.title.equals(getMessages().currentLiabilities())) {
					currentLiabilityTotal = Double.valueOf(section.data[3]
							.toString());
				}
				if (section.title.equals(getMessages().longTermLiabilities())) {
					longTermLiabilityTotal = Double.valueOf(section.data[3]
							.toString());
				}
				if (section.title.equals(getMessages().equity())) {
					equitiesTotal = Double.valueOf(section.data[3].toString());
				}
				if (section.footer.equals(getMessages()
						.liabilitiesandEquityTotal())) {
					liabilityAndEquityTotal = currentLiabilityTotal
							+ longTermLiabilityTotal + equitiesTotal;
					section.data[3] = liabilityAndEquityTotal;
				}

			}

		};
		setHandler(sectionHandler);

	}

	protected void initValues() {
		currentAssetsTotal = 0.0D;
		fixedAssetsTotal = 0.0D;
		otherAssetsTotal = 0.0D;
		assetsTotal = 0.0D;
		currentLiabilityTotal = 0.0D;
		longTermLiabilityTotal = 0.0D;
		equitiesTotal = 0.0D;
		liabilityAndEquityTotal = 0.0D;

	}

	public ClientFinanceDate getEndDate(TrialBalance obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TrialBalance obj) {
		return obj.getStartDate();
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((TrialBalance) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((TrialBalance) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((TrialBalance) object).getEndDate();
	}

	@Override
	public int getColumnWidth(int index) {

		switch (index) {
		case 0:
			return 75;
		case 2:
			return 130;
		case 3:
			return 110;

		default:
			break;
		}
		// if (index == 1)
		// return 310;
		// else if (index == 2)
		// return 270;
		// else
		return 200;
	}

}
