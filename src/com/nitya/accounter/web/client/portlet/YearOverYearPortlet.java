package com.nitya.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPortletConfiguration;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.GraphChart;
import com.nitya.accounter.web.client.ui.GraphPointsPortlet;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.YearOverYearPortletData;

public class YearOverYearPortlet extends GraphPointsPortlet {

	public static final int YEAR_OVER_YEAR_INCOME = 1;
	public static final int YEAR_OVER_YEAR_EXPENSE = 2;
	private YearOverYearToolBar toolBar;
	private StyledPanel graphPanel;
	private int chartType;
	private ClientAccount accountByName;

	public YearOverYearPortlet(ClientPortletConfiguration pc, int chartType) {
		super(pc, "", "", "100%");
		this.chartType = chartType;
		this.getElement().setId("YearOverYearPortlet");
		setPortletTitle();
	}

	private void setPortletTitle() {
		if (chartType == YEAR_OVER_YEAR_INCOME) {
			setPortletTitle(messages.yearOverYearIncome());
		} else {
			setPortletTitle(messages.yearOverYearExpense());
		}
	}

	@Override
	public void createBody() {
		graphPanel = new StyledPanel("graphPanel");
		createToolBar();
		this.body.add(graphPanel);
	}

	private void createToolBar() {
		toolBar = new YearOverYearToolBar(chartType) {

			@Override
			protected void refreshPortletData() {
				YearOverYearPortlet.this.clearGrid();
				dateRangeItemCombo.setSelected(portletConfigData
						.get(DATE_RANGE));
				accountCombo.setSelected(Accounter
						.getCompany()
						.getAccount(
								Long.parseLong(portletConfigData
										.get(ACCOUNT_ID))).getName());

				YearOverYearPortlet.this.getConfiguration().setPortletMap(
						portletConfigData);
				dateRangeChanged(portletConfigData.get(DATE_RANGE));
				YearOverYearPortlet.this.updateData(
						getDateRangeType(portletConfigData.get(DATE_RANGE)),
						startDate, endDate,
						Long.parseLong(portletConfigData.get(ACCOUNT_ID)));
				updateConfiguration();
			}

			@Override
			protected void initOrSetConfigDataToPortletConfig() {
				if (YearOverYearPortlet.this.getConfiguration().getPortletMap()
						.get(ACCOUNT_ID) != null) {
					accountId = Long
							.parseLong(YearOverYearPortlet.this
									.getConfiguration().getPortletMap()
									.get(ACCOUNT_ID));
					portletConfigData
							.put(ACCOUNT_ID, String.valueOf(accountId));
				} else {
					portletConfigData.put(ACCOUNT_ID,
							String.valueOf(getAccountId(accountId)));
				}

				if (YearOverYearPortlet.this.getConfiguration().getPortletMap()
						.get(DATE_RANGE) != null) {
					portletConfigData
							.put(DATE_RANGE,
									YearOverYearPortlet.this.getConfiguration()
											.getPortletMap().get(DATE_RANGE));
				} else {
					portletConfigData.put(DATE_RANGE, messages.thisMonth());
				}

			}

			@Override
			public void setDefaultDateRange(String defaultDateRange) {
				dateRangeItemCombo.setSelected(defaultDateRange);
				dateRangeChanged(defaultDateRange);
				YearOverYearPortlet.this.updateData(
						getDateRangeType(defaultDateRange), getStartDate(),
						getEndDate(), accountId);
			}
		};
		this.body.add(toolBar);
	}

	protected void updateData(int dateRangeType, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long accountId) {
		AsyncCallback<ArrayList<YearOverYearPortletData>> callback = new AsyncCallback<ArrayList<YearOverYearPortletData>>() {

			@Override
			public void onSuccess(ArrayList<YearOverYearPortletData> result) {
				if (result != null && (!result.isEmpty()))
					updateGraphData(result);
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		};

		Accounter.createHomeService().getAccountsBalancesByDate(startDate,
				endDate, accountId, dateRangeType, callback);
	}

	private long getAccountId(long accountId) {
		if (accountId == 0) {
			if (chartType == YEAR_OVER_YEAR_EXPENSE) {
				if (Accounter.getCompany().getAccounts(
						ClientAccount.TYPE_EXPENSE) != null) {
					accountByName = Accounter.getCompany()
							.getAccounts(ClientAccount.TYPE_EXPENSE).get(0);
				}
			} else {
				if (Accounter.getCompany().getAccounts(
						ClientAccount.TYPE_INCOME) != null) {
					accountByName = Accounter.getCompany()
							.getAccounts(ClientAccount.TYPE_INCOME).get(0);
				}
			}
			if (accountByName != null) {
				accountId = accountByName.getID();
			} else {
				Label label = new Label(messages.noRecordsToShow());
				graphPanel.add(label);
				return 0;
			}
		}
		return accountId;
	}

	protected void updateGraphData(
			final ArrayList<YearOverYearPortletData> result) {
		final List<Double> balances = new ArrayList<Double>();
		final List<String> monthNames = new ArrayList<String>();
		Iterator<YearOverYearPortletData> iterator = result.iterator();
		while (iterator.hasNext()) {
			YearOverYearPortletData accountDetail = iterator.next();
			balances.add(accountDetail.getAmount());
			monthNames.add(String.valueOf(accountDetail.getMonth()));
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				GraphChart chart = new GraphChart();
				if (result != null) {
					chart.setTitle(result.get(0).getName());
				}
				chart.accountNames = monthNames;
				graphPanel.add(chart.createYearOverYearChart(balances));
				completeInitialization();
			}

		};
		VisualizationUtils.loadVisualizationApi(runnable,
				AnnotatedTimeLine.PACKAGE);
	}

	protected void clearGrid() {
		graphPanel.clear();
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
