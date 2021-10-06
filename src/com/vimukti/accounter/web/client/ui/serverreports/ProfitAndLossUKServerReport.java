//package com.vimukti.accounter.web.client.ui.serverreports;
//
//import com.vimukti.accounter.web.client.core.ClientAccount;
//import com.vimukti.accounter.web.client.core.reports.TrialBalance;
//import com.vimukti.accounter.web.client.ui.reports.ISectionHandler;
//import com.vimukti.accounter.web.client.ui.reports.Section;
//
//public class ProfitAndLossUKServerReport extends ProfitAndLossServerReport {
//
//	public ProfitAndLossUKServerReport(long startDate, long endDate,
//			int generationType) {
//		super(startDate, endDate, generationType);
//	}
//
//	@Override
//	public void processRecord(TrialBalance record) {
//		if (this.handler == null)
//			initHandler();
//
//		if (sectionDepth == 0) {
//			addTypeSection("", getMessages().netProfit());
//		}
//		addOrdinaryIncomeOrExpenseTypes(record);
//		// if (record.getBaseType() ==
//		// ClientAccount.BASETYPE_ORDINARY_INCOME_OR_EXPENSE) {
//		// addOrdinaryIncomeOrExpenseTypes(record);
//		// } else if (record.getBaseType() ==
//		// ClientAccount.BASETYPE_OTHER_INCOME_OR_EXPENSE) {
//		// addOtherIncomeOrExpenseTypes(record);
//		// } else {
//		// closeAllSection();
//		// }
//
//		if (closePrevSection(record.getParentAccount() == 0 ? record
//				.getAccountName() : getAccountNameById(record
//				.getParentAccount()))) {
//			processRecord(record);
//		} else {
//			addSection(record);
//			return;
//		}
//
//	}
//
//	protected void initHandler() {
//
//		initValues();
//		this.handler = new ISectionHandler<TrialBalance>() {
//
//			@Override
//			public void OnSectionAdd(Section<TrialBalance> section) {
//				if (section.title == getMessages().grossProfit()) {
//					section.data[0] = "";
//				}
//
//				if (section.title.equals(getMessages().indirectCosts())) {
//					grid.addRow(null, 0,
//							new Object[] { " ", " ", " ", " ", " " }, false,
//							false, false);
//				}
//			}
//
//			public void OnSectionEnd(Section<TrialBalance> section) {
//				if (section.title == null)
//					section.title = "";
//
//				if (section.title.equals(getMessages().revenueIncome())) {
//					totalincome = Double.valueOf(section.data[3].toString());
//					totalincome2 = Double.valueOf(section.data[5].toString());
//				}
//				if (section.title.equals(getMessages()
//						.directProductsMaterialCosts())) {
//					totalCGOS = Double.valueOf(section.data[3].toString());
//					totalCGOS2 = Double.valueOf(section.data[5].toString());
//				}
//				if (section.title.equals(getMessages().otherDirectCosts())) {
//					otherExpense = Double.valueOf(section.data[3].toString());
//					otherExpense2 = Double.valueOf(section.data[5].toString());
//				}
//				if (section.title.equals(getMessages().indirectCosts())) {
//					totalexpese = (Double) section.data[3];
//					totalexpese2 = (Double) section.data[5];
//				}
//
//				if (section.title.equals(getMessages().otherIncome())) {
//					otherIncome = Double.valueOf(section.data[3].toString());
//					otherIncome2 = Double.valueOf(section.data[5].toString());
//				}
//
//				if (section.title.equals(getMessages().otherIncomeOrExpense())) {
//					otherNetIncome = otherIncome - otherExpense;
//					section.data[3] = otherNetIncome;
//					otherNetIncome2 = otherIncome2 - otherExpense2;
//					section.data[5] = otherNetIncome2;
//				}
//				if (section.footer == null)
//					section.footer = "";
//				if (section.footer.equals(getMessages().grossProfit())) {
//					grid.addRow(null, 0,
//							new Object[] { " ", " ", " ", " ", " " }, false,
//							false, false);
//					grosProft = totalincome - totalCGOS - otherExpense;
//					section.data[3] = grosProft;
//					grosProft2 = totalincome2 - totalCGOS2 - otherExpense2;
//					section.data[5] = grosProft2;
//				}
//				if (section.footer.equals(getMessages().netProfit())) {
//					netProfit = grosProft - totalexpese;
//					section.data[3] = netProfit;
//					netProfit2 = grosProft2 - totalexpese2;
//					section.data[5] = netProfit2;
//				}
//			}
//		};
//
//	}
//
//	public void addOrdinaryIncomeOrExpenseTypes(TrialBalance record) {
//		// if (!sectiontypes.contains(FinanceApplication.constants()
//		// .ordinaryIncomeOrExpense())) {
//		// closeAllSection();
//		// addTypeSection(FinanceApplication.constants()
//		// .ordinaryIncomeOrExpense(), FinanceApplication
//		// .constants().netOrdinaryIncome());
//		// }
//		if (record.getAccountType() == ClientAccount.TYPE_INCOME
//				|| record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
//			if (!sectiontypes.contains(getMessages().grossProfit())) {
//				addTypeSection(getMessages().grossProfit(), "", getMessages()
//						.grossProfit());
//
//			}
//			if (record.getAccountType() == ClientAccount.TYPE_INCOME)
//				if (!sectiontypes.contains(getMessages().revenueIncome())) {
//
//					addTypeSection(getMessages().revenueIncome(),
//							getMessages().revenueIncomeTotal());
//				}
//			if (record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD)
//				if (!sectiontypes.contains(getMessages()
//						.directProductsMaterialCosts())) {
//					closeOtherSections();
//					closeSection(types.indexOf(getMessages().revenueIncome()));
//					addTypeSection(
//							getMessages().directProductsMaterialCosts(),
//							getMessages()
//									.directProductsAndMaterialCostsTotal());
//
//				}
//
//		}
//
//		if (record.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {
//
//			if (!sectiontypes.contains(getMessages().otherDirectCosts())) {
//				closeOtherSections();
//				closeSection(types.indexOf(getMessages()
//						.directProductsMaterialCosts()));
//				addTypeSection(getMessages().otherDirectCosts(),
//						getMessages().otherDirectCostsTotal());
//			}
//		}
//
//		if (record.getAccountType() == ClientAccount.TYPE_EXPENSE) {
//
//			if (!sectiontypes.contains(getMessages().indirectCosts())) {
//				closeAllSection();
//				addTypeSection(getMessages().indirectCosts(), getMessages()
//						.indirectCostsTotal());
//			}
//		}
//
//	}
//
//}
