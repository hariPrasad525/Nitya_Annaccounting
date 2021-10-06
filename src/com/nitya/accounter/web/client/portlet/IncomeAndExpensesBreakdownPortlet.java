package com.nitya.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPortletConfiguration;
import com.nitya.accounter.web.client.core.IncomeExpensePortletInfo;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.GraphChart;
import com.nitya.accounter.web.client.ui.GraphPointsPortlet;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.reports.DateRangePortletToolBar;

public class IncomeAndExpensesBreakdownPortlet extends GraphPointsPortlet {

	private DateRangePortletToolBar toolBar;
	private StyledPanel graphPanel;

	public IncomeAndExpensesBreakdownPortlet(ClientPortletConfiguration pc) {
		super(pc, messages.incomeAndExpenseAccounts(), "", "100%");
		this.getElement().setId("IncomeAndExpensesBreakdownPortlet");
	}

	@Override
	public void createBody() {
		graphPanel = new StyledPanel("graphPanel");
		createToolBar();
		this.body.add(graphPanel);
	}

	private void createToolBar() {
		toolBar = new DateRangePortletToolBar() {
			@Override
			protected void init() {
				super.init();
				dateRangesList = new ArrayList<String>();
				for (int i = 0; i < monthDateRangeArray.length; i++) {
					dateRangesList.add(monthDateRangeArray[i]);
				}
				dateRangeItemCombo.initCombo(dateRangesList);
			}

			@Override
			protected void refreshPortletData() {
				IncomeAndExpensesBreakdownPortlet.this.clearGrid();
				dateRangeItemCombo.setSelected(portletConfigData
						.get(DATE_RANGE));
				IncomeAndExpensesBreakdownPortlet.this.getConfiguration()
						.setPortletMap(portletConfigData);
				dateRangeChanged(portletConfigData.get(DATE_RANGE));
				IncomeAndExpensesBreakdownPortlet.this.updateData(
						getDateRangeType(portletConfigData.get(DATE_RANGE)),
						startDate, endDate);
				updateConfiguration();
			}

			@Override
			protected void initOrSetConfigDataToPortletConfig() {
				if (IncomeAndExpensesBreakdownPortlet.this.getConfiguration()
						.getPortletMap().get(DATE_RANGE) != null) {
					portletConfigData.put(
							DATE_RANGE,
							IncomeAndExpensesBreakdownPortlet.this
									.getConfiguration().getPortletMap()
									.get(DATE_RANGE));
				} else {
					portletConfigData.put(DATE_RANGE, messages.thisMonth());
				}
			}

			@Override
			public void setDefaultDateRange(String defaultDateRange) {
				dateRangeItemCombo.setSelected(defaultDateRange);
				dateRangeChanged(defaultDateRange);
				IncomeAndExpensesBreakdownPortlet.this.updateData(
						getDateRangeType(defaultDateRange), getStartDate(),
						getEndDate());
			}

		};
		this.body.add(toolBar);
	}

	protected void updateData(int dateRangeType, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		AsyncCallback<ArrayList<IncomeExpensePortletInfo>> callback = new AsyncCallback<ArrayList<IncomeExpensePortletInfo>>() {

			@Override
			public void onSuccess(ArrayList<IncomeExpensePortletInfo> result) {
				if (result != null && (!result.isEmpty()))
					updateGraphData(result);
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		};
		Accounter.createHomeService().getIncomeExpensePortletInfo(
				dateRangeType, startDate, endDate, callback);
	}

	protected void updateGraphData(ArrayList<IncomeExpensePortletInfo> result) {
		final List<Double> incomeBalances = new ArrayList<Double>();
		final List<Double> expenseBalances = new ArrayList<Double>();
		final List<String> monthNames = new ArrayList<String>();
		Iterator<IncomeExpensePortletInfo> iterator = result.iterator();
		while (iterator.hasNext()) {
			IncomeExpensePortletInfo accountDetail = iterator.next();
			incomeBalances.add(accountDetail.getIncome());
			expenseBalances.add(accountDetail.getExpense());
			monthNames.add(accountDetail.getMonth() + "");
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				GraphChart chart = new GraphChart();
				chart.accountNames = monthNames;
				graphPanel.add(chart.createIncomeAndExpenseBreakdownChart(
						incomeBalances, expenseBalances));

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
