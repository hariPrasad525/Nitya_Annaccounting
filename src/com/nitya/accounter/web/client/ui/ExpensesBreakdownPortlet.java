package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientPortletConfiguration;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.reports.ExpenseList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.reports.CompanyAndFinancialReportsAction;
import com.nitya.accounter.web.client.ui.reports.DateRangePortletToolBar;
import com.nitya.accounter.web.client.ui.reports.PortletToolBar;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;
import com.nitya.accounter.web.client.ui.vendors.ExpensesAction;

public class ExpensesBreakdownPortlet extends GraphPointsPortlet {
	private PortletToolBar toolBar;
	private StyledPanel graphPanel;

	public double allExpensesAmount = 0.00;
	public double cashExpenseAmount = 0.00;
	public double employeeExpenseAmount = 0.0;
	public double ccExpenseAmount = 0.00;

	public Label allExpAmtLabel;
	public Label cashExpAmtLabel;
	public Label empExpAmtLabel;
	public Label ccExpAmtLabel;

	public ExpensesBreakdownPortlet(ClientPortletConfiguration pc) {
		super(pc, messages.expenses(), "", "100%");
		this.setConfiguration(pc);
		this.getElement().setId("ExpensesBreakdownPortlet");
	}

	@Override
	public void helpClicked() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void goToClicked() {
		new ExpensesAction(null).run(null, true);
	}

	@Override
	public void createBody() {
		StyledPanel vPanel = new StyledPanel("vPanel");
		FlexTable fTable = new FlexTable();

		Button addExpenseBtn = new Button(messages.addExpenses());
		addExpenseBtn.addStyleName("addAccountPortlet");
		addExpenseBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				History.newItem(HistoryTokens.RECORDEXPENSES, true);
			}
		});

		Anchor allExpLabel = new Anchor(messages.allExpenses());
		Anchor cashExpLabel = new Anchor(messages.cashExpenses());
		Anchor ccExpLabel = new Anchor(messages.creditCardExpenses());
		cashExpLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);
		if (Accounter.getUser().getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_YES
				|| Accounter.getUser().getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_YES) {
			allExpLabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ExpenseList expenseList = new ExpenseList();
					expenseList.setStartDate(toolBar.getStartDate());
					expenseList.setEndDate(toolBar.getEndDate());
					expenseList.setDateRange(toolBar.getSelectedDateRange());
					UIUtils.runAction(expenseList,
							CompanyAndFinancialReportsAction.expense());
				}
			});

			cashExpLabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ExpenseList expenseList = new ExpenseList();
					expenseList.setStartDate(toolBar.getStartDate());
					expenseList.setEndDate(toolBar.getEndDate());
					expenseList.setDateRange(toolBar.getSelectedDateRange());
					expenseList
							.setTransactionType(ClientTransaction.TYPE_CASH_EXPENSE);
					UIUtils.runAction(expenseList,
							CompanyAndFinancialReportsAction.expense());
				}
			});

			// Label empExpLabel =
			// getLabel(messages.employeeExpenses());

			ccExpLabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ExpenseList expenseList = new ExpenseList();
					expenseList.setStartDate(toolBar.getStartDate());
					expenseList.setEndDate(toolBar.getEndDate());
					expenseList.setDateRange(toolBar.getSelectedDateRange());
					expenseList
							.setTransactionType(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
					UIUtils.runAction(expenseList,
							CompanyAndFinancialReportsAction.expense());

				}
			});
		}
		ccExpLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);

		Label empExpLabel = new Label();

		allExpAmtLabel = getAmountLabel(DataUtils
				.getAmountAsStringInPrimaryCurrency(allExpensesAmount));
		cashExpAmtLabel = getAmountLabel(DataUtils
				.getAmountAsStringInPrimaryCurrency(cashExpenseAmount));
		cashExpAmtLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);
		empExpAmtLabel = getAmountLabel(DataUtils
				.getAmountAsStringInPrimaryCurrency(employeeExpenseAmount));
		ccExpAmtLabel = getAmountLabel(DataUtils
				.getAmountAsStringInPrimaryCurrency(ccExpenseAmount));
		ccExpAmtLabel.getElement().getStyle().setMarginLeft(50, Unit.PX);
		fTable.addStyleName("expense_label_tabel");
		fTable.setWidget(0, 0, allExpLabel);
		fTable.setWidget(1, 0, allExpAmtLabel);

		fTable.setWidget(0, 1, cashExpLabel);
		fTable.setWidget(1, 1, cashExpAmtLabel);

		fTable.setWidget(2, 1, ccExpLabel);
		fTable.setWidget(3, 1, ccExpAmtLabel);

		// These should be enabled when user select to track employee expenses.
		if (Global.get().preferences().isHaveEpmloyees()
				&& Global.get().preferences().isTrackEmployeeExpenses()) {
			fTable.setWidget(2, 0, empExpLabel);
			fTable.setWidget(3, 0, empExpAmtLabel);
		}
		StyledPanel topPanel = new StyledPanel("topPanel");
		if (Accounter.getUser().canDoInvoiceTransactions()
				|| Accounter.getUser().getPermissions().getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES) {
			topPanel.add(addExpenseBtn);
		}
		vPanel.add(fTable);
		toolBarInitilization();
		graphPanel = new StyledPanel("graphPanel");
		topPanel.add(toolBar);
		this.body.add(topPanel);
		this.body.add(graphPanel);
		this.body.add(vPanel);
	}

	Label getAmountLabel(String title) {
		Label label = new Label(title);
		label.addStyleName("amountLabelPortlet");
		label.getElement().getStyle().setMarginLeft(10, Unit.PX);
		return label;
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}

	public void updateAmountLabels() {
		cashExpAmtLabel.setText(DataUtils
				.getAmountAsStringInPrimaryCurrency(cashExpenseAmount));
		ccExpAmtLabel.setText(DataUtils
				.getAmountAsStringInPrimaryCurrency(ccExpenseAmount));
		empExpAmtLabel.setText(DataUtils
				.getAmountAsStringInPrimaryCurrency(employeeExpenseAmount));
		allExpAmtLabel.setText(DataUtils
				.getAmountAsStringInPrimaryCurrency(allExpensesAmount));
	}

	private void toolBarInitilization() {
		toolBar = new DateRangePortletToolBar() {
			@Override
			protected void initOrSetConfigDataToPortletConfig() {
				if (ExpensesBreakdownPortlet.this.getConfiguration()
						.getPortletMap().get(DATE_RANGE) != null) {
					portletConfigData.put(DATE_RANGE,
							ExpensesBreakdownPortlet.this.getConfiguration()
									.getPortletMap().get(DATE_RANGE));
				} else {
					portletConfigData.put(DATE_RANGE,
							messages.financialYearToDate());
				}
			}

			@Override
			public void setDefaultDateRange(String defaultDateRange) {
				dateRangeItemCombo.setSelected(defaultDateRange);
				dateRangeChanged(defaultDateRange);
				ExpensesBreakdownPortlet.this.updateData(startDate.getDate(),
						endDate.getDate());
			}

			@Override
			protected void refreshPortletData() {
				ExpensesBreakdownPortlet.this.clearGraph();
				dateRangeItemCombo.setSelected(portletConfigData
						.get(Portlet.DATE_RANGE));
				ExpensesBreakdownPortlet.this.getConfiguration().setPortletMap(
						portletConfigData);
				dateRangeChanged(portletConfigData.get(Portlet.DATE_RANGE));
				ExpensesBreakdownPortlet.this.updateData(startDate.getDate(),
						endDate.getDate());
				updateConfiguration();
			}
		};
	}

	public void updateData(long startDate, long endDate) {

		AccounterAsyncCallback<ExpensePortletData> callback = new AccounterAsyncCallback<ExpensePortletData>() {

			@Override
			public void onResultSuccess(ExpensePortletData result) {
				if (result != null) {
					updateLabelsData(result);
					if (!result.getAccountDetails().isEmpty()) {
						updateGraphData(result);
					}
				} else {
					Label label = new Label(messages.noRecordsToShow());
					graphPanel.add(label);
					label.addStyleName("no_records_label");
				}
				completeInitialization();
			}

			@Override
			public void onException(AccounterException exception) {
				completeInitialization();
			}
		};

		Accounter.createHomeService().getAccountsAndValues(startDate, endDate,
				callback);
	}

	protected void updateLabelsData(ExpensePortletData result) {
		cashExpenseAmount = result.getCashExpenseTotal();
		ccExpenseAmount = result.getCreditCardExpensesTotal();
		allExpensesAmount = result.getAllExpensesTotal();
		updateAmountLabels();
	}

	protected void updateGraphData(ExpensePortletData result) {
		final List<String> accountsNames = new ArrayList<String>();
		final List<Double> accountBalances = new ArrayList<Double>();
		Iterator<AccountDetail> iterator = result.getAccountDetails()
				.iterator();
		int i = 0;
		double amount = 0;
		while (iterator.hasNext()) {
			AccountDetail accountDetail = iterator.next();
			Double accountBalance = accountDetail.getAmount();
			if (i < 11) {
				accountsNames.add(accountDetail.getName());
				accountBalances.add(accountBalance);
			} else {
				amount = amount + accountBalance;
				if (i == result.getAccountDetails().size() - 2) {
					accountsNames.add(messages.other());
					accountBalances.add(amount);
				}
			}
			i++;
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				GraphChart chart = new GraphChart();
				chart.accountNames = accountsNames;
				graphPanel
						.add(chart.createAccountExpenseChart(accountBalances));
			}
		};
		VisualizationUtils.loadVisualizationApi(runnable,
				AnnotatedTimeLine.PACKAGE);
	}

	public void clearGraph() {
		graphPanel.clear();
	}

	@Override
	public void refreshClicked() {
		// TODO Auto-generated method stub

	}
}