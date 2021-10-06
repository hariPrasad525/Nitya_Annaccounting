package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart.Type;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart.PieOptions;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.core.Calendar;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;

public class GraphChart {

	AccounterMessages messages = Global.get().messages();

	ArrayList<Double> graph_Values = new ArrayList<Double>();
	ArrayList<Object> x_Axis_Labels = new ArrayList<Object>();
	public List<String> accountNames;

	public static final int BANK_ACCOUNT_CHART_TYPE = 1;
	public static final int ACCOUNTS_RECEIVABLE_CHART_TYPE = 2;
	public static final int ACCOUNTS_PAYABLE_CHART_TYPE = 3;
	public static final int EXPENSE_CHART_TYPE = 4;
	private static final int ACCOUNTS_EXPENSE_CHART_TYPE = 5;
	private static final int ACCOUNT_BALANCES_CHART_TYPE = 6;
	private static final int INCOME_EXPENSE_BREAKDOWN_CHART_TYPE = 7;
	private static final int YEAR_OVER_YEAR_CHART_TYPE = 8;

	int chartType = 0;
	int MAX_REVENUE = 0;
	int MIN_REVENUE = 0;
	int WIDTH = 0;
	int HEIGHT = 0;
	public List<Double> expenseValues = new ArrayList<Double>();
	public List<Double> incomeValues = new ArrayList<Double>();
	private String title;

	public void initializeGraphValues() {
		if (this.graph_Values == null) {
			graph_Values = new ArrayList<Double>();
			for (int i = 0; i <= 30; i++) {
				graph_Values.add(i, 0.0);
			}
		}

		// graph_Values.add(1, 500.08);
		// graph_Values.add(2, 1000.37);
		// graph_Values.add(3, 400.);
		// graph_Values.add(4, 800.);
		// graph_Values.add(5, 400.);
		// graph_Values.add(6, 200.00);
		// graph_Values.add(7, 300.00);
		// graph_Values.add(8, 100.00);
		// graph_Values.add(9, 500.);
		// graph_Values.add(10, 800.);
		// graph_Values.add(11, 400.);
		// graph_Values.add(12, 100.00);
		// graph_Values.add(13, 500.);
		// graph_Values.add(14, 800.);
		// graph_Values.add(15, 400.);
		// graph_Values.add(16, 200.00);
		// graph_Values.add(17, 300.00);
		// graph_Values.add(18, 100.00);
		// graph_Values.add(19, 500.);
		// graph_Values.add(20, 800.);
		// graph_Values.add(21, 300.00);
		// graph_Values.add(22, 100.00);
		// graph_Values.add(23, 500.);
		// graph_Values.add(24, 800.);
		// graph_Values.add(25, 400.);
		// graph_Values.add(26, 200.00);
		// graph_Values.add(27, 300.00);
		// graph_Values.add(28, 100.00);
		// graph_Values.add(29, 500.);
		// graph_Values.add(30, 800.);

	}

	public LineChart createBankingChart(List<Double> graph_Values) {

		this.graph_Values = (ArrayList<Double>) graph_Values;
		if (this.graph_Values == null || this.graph_Values.size() == 0)
			initializeGraphValues();

		createLabels(BANK_ACCOUNT_CHART_TYPE);
		LineChart chart = new LineChart(createTable(BANK_ACCOUNT_CHART_TYPE),
				createOptionsToBankingChart());
		return chart;
	}

	public LineChart createBankingAccountsListChart(List<Double> graph_Values) {

		this.graph_Values = (ArrayList<Double>) graph_Values;
		if (this.graph_Values == null || this.graph_Values.size() == 0)
			initializeGraphValues();

		createLabels(BANK_ACCOUNT_CHART_TYPE);
		LineChart chart = new LineChart(createTable(BANK_ACCOUNT_CHART_TYPE),
				createOptionsToBankingAccountsListChart());
		return chart;
	}

	public ColumnChart createAccountReceivableChart(List<Double> graph_Values) {

		this.graph_Values = (ArrayList<Double>) graph_Values;
		if (this.graph_Values == null || this.graph_Values.size() == 0)
			initializeGraphValues();

		createLabels(ACCOUNTS_RECEIVABLE_CHART_TYPE);
		ColumnChart chart = new ColumnChart(
				createTable(ACCOUNTS_RECEIVABLE_CHART_TYPE),
				createOptionsToAccountReceivableChart());
		return chart;
	}

	public PieChart createAccountExpenseChart(List<Double> graph_values) {
		this.graph_Values = (ArrayList<Double>) graph_values;
		if (this.graph_Values == null || this.graph_Values.size() == 0)
			initializeGraphValues();

		createLabels(ACCOUNTS_EXPENSE_CHART_TYPE);
		PieChart chart = new PieChart(createTable(ACCOUNTS_EXPENSE_CHART_TYPE),
				createOptionsForPieChart());
		return chart;

	}

	public PieChart createAccountBalancesChart(List<Double> graph_values) {
		this.graph_Values = (ArrayList<Double>) graph_values;
		if (this.graph_Values == null || this.graph_Values.size() == 0)
			initializeGraphValues();

		createLabels(ACCOUNT_BALANCES_CHART_TYPE);
		PieChart chart = new PieChart(createTable(ACCOUNT_BALANCES_CHART_TYPE),
				createOptionsForPieChart());
		return chart;

	}

	public ColumnChart createIncomeAndExpenseBreakdownChart(
			List<Double> incomeValues, List<Double> expenseValues) {
		this.incomeValues = incomeValues;
		this.expenseValues = expenseValues;
		if (this.incomeValues == null || this.incomeValues.size() == 0) {
			initializeValues(incomeValues);
		}
		if (this.expenseValues == null || this.expenseValues.size() == 0) {
			initializeValues(expenseValues);
		}
		createLabels(INCOME_EXPENSE_BREAKDOWN_CHART_TYPE);

		ColumnChart columnChart = new ColumnChart(
				createTable(INCOME_EXPENSE_BREAKDOWN_CHART_TYPE),
				createIncomeAndExpenseOptionsForBarChart());
		return columnChart;
	}

	public Widget createYearOverYearChart(List<Double> balances) {
		this.graph_Values = (ArrayList<Double>) balances;
		initializeGraphValues();
		ColumnChart columnChart = new ColumnChart(
				createTable(YEAR_OVER_YEAR_CHART_TYPE),
				createYearOverYearOptionsForBarChart());
		return columnChart;
	}

	private Options createYearOverYearOptionsForBarChart() {
		Options options = Options.create();
		options.setWidth(444);
		// options.setHeight(225);
		options.setType(Type.BARS);
		options.setTitle(getTitle());
		options.setLegend(LegendPosition.NONE);
		options.setColors("#6CA92F");

		AxisOptions axisOptions = AxisOptions.create();
		options.setVAxisOptions(axisOptions);

		return options;
	}

	private Options createIncomeAndExpenseOptionsForBarChart() {
		Options options = Options.create();
		options.setWidth(444);
		// options.setHeight(225);
		options.setType(Type.BARS);
		options.setLegend(LegendPosition.RIGHT);
		options.setColors("#6CA92F", "#FF4000");

		AxisOptions axisOptions = AxisOptions.create();
		options.setVAxisOptions(axisOptions);

		return options;
	}

	private Options createOptionsForPieChart() {
		Options options = (PieOptions) Options.create();
		// options.setWidth(444);
		// options.setHeight(225);
		options.setLegend(LegendPosition.RIGHT);
		options.setColors("#6CA92F", "#e0440e", "#91AB56", "#40640e",
				"#07891D", "#800000", "#628906", "#0000FF", "#800080",
				"#A52A2A", "#0000A0", "#800000", "#FF4000");

		AxisOptions axisOptions = AxisOptions.create();
		options.setVAxisOptions(axisOptions);

		return options;
	}

	public void initializeValues(List<Double> values) {
		if (values == null) {
			values = new ArrayList<Double>();
		}

		for (int i = 0; i <= 30; i++) {
			values.add(i, 0.0);
		}
	}

	public LineChart createAccountPayableChart(List<Double> graph_Values) {

		this.graph_Values = (ArrayList<Double>) graph_Values;
		if (this.graph_Values == null || this.graph_Values.size() == 0)
			initializeGraphValues();

		createLabels(ACCOUNTS_PAYABLE_CHART_TYPE);
		LineChart chart = new LineChart(
				createTable(ACCOUNTS_PAYABLE_CHART_TYPE),
				createOptionsToAccountPayableChart());
		return chart;
	}

	private Options createOptionsToAccountReceivableChart() {

		Options options = Options.create();
		// options.setWidth(444);
		// options.setHeight(225);
		options.setLegend(LegendPosition.NONE);
		// options.setMin(100);
		options.setColors("#6CA92F");
		// options.set3D(true);

		AxisOptions axisOptions = AxisOptions.create();
		// axisOptions.setBaseline(0);
		axisOptions.setMinValue(100);
		options.setVAxisOptions(axisOptions);

		return options;
	}

	private Options createOptionsToBankingChart() {
		Options options = Options.create();
		options.setWidth(430);
		options.setHeight(225);
		options.setLegend(LegendPosition.NONE);
		// options.setMin(100);
		options.setColors("#6CA92F");
		// options.setSmoothLine(true);
		AxisOptions axisOptions = AxisOptions.create();
		// axisOptions.setBaseline(0);
		axisOptions.setMinValue(100);
		options.setVAxisOptions(axisOptions);
		return options;
	}

	private Options createOptionsToBankingAccountsListChart() {
		Options options = Options.create();
		// options.setWidth(630);
		// options.setHeight(225);
		options.setLegend(LegendPosition.NONE);
		// options.setMin(100);
		options.setColors("#6CA92F");
		// options.setSmoothLine(true);
		AxisOptions axisOptions = AxisOptions.create();
		// axisOptions.setBaseline(0);
		axisOptions.setMinValue(100);
		options.setVAxisOptions(axisOptions);
		return options;
	}

	private Options createOptionsToAccountPayableChart() {

		Options options = Options.create();

		// options.setDisplayAnnotations(true);
		// options.setDisplayLegendValues(false);
		// options.setDisplayZoomButtons(false);
		// options.setDisplayExactValues(true);
		// options.setAllowHtml(true);
		// options.setWindowMode(WindowMode.OPAQUE);
		// options.setWidth(425);
		// options.setHeight(225);
		options.setLegend(LegendPosition.NONE);
		// options.setMin(100);
		options.setColors("#6CA92F");

		AxisOptions axisOptions = AxisOptions.create();
		// axisOptions.setBaseline(0);
		axisOptions.setMinValue(100);
		options.setVAxisOptions(axisOptions);

		return options;
	}

	private AbstractDataTable createTable(int chartType) {
		DataTable data = DataTable.create();

		if (chartType == INCOME_EXPENSE_BREAKDOWN_CHART_TYPE) {
			data.addColumn(ColumnType.STRING, messages.date());
			data.addColumn(ColumnType.NUMBER, messages.income());
			data.addColumn(ColumnType.NUMBER, messages.expense());
		} else if (chartType == ACCOUNTS_PAYABLE_CHART_TYPE) {
			data.addColumn(ColumnType.STRING, messages.date());
			data.addColumn(ColumnType.NUMBER);

		} else if (chartType == ACCOUNTS_RECEIVABLE_CHART_TYPE) {
			data.addColumn(ColumnType.STRING, messages.date());
			data.addColumn(ColumnType.NUMBER, messages.revenue());
		} else {
			data.addColumn(ColumnType.STRING, messages.date());
			data.addColumn(ColumnType.NUMBER, messages.balance());
		}

		if (chartType == BANK_ACCOUNT_CHART_TYPE) {
			data.addRows(4);
			data = addGraphPoints(chartType, data, 4);
		} else if (chartType == ACCOUNTS_RECEIVABLE_CHART_TYPE) {
			data.addRows(6);
			data = addGraphPoints(chartType, data, 6);
		} else if (chartType == ACCOUNTS_PAYABLE_CHART_TYPE) {
			data.addRows(30);
			data = addGraphPoints(chartType, data, 30);
		} else if (chartType == ACCOUNTS_EXPENSE_CHART_TYPE) {
			data.addRows(accountNames.size());
			data = addGraphPoints(chartType, data, accountNames.size());
		} else if (chartType == ACCOUNT_BALANCES_CHART_TYPE) {
			data.addRows(accountNames.size());
			data = addGraphPoints(chartType, data, accountNames.size());
		} else if (chartType == INCOME_EXPENSE_BREAKDOWN_CHART_TYPE) {
			data.addRows(accountNames.size());
			data = addIncomeAndExpenseGraphPoints(chartType, data,
					accountNames.size());
		} else if (chartType == YEAR_OVER_YEAR_CHART_TYPE) {
			data.addRows(accountNames.size());
			data = addYearOverYearGraphPoints(chartType, data,
					accountNames.size());
		}
		return data;
	}

	private DataTable addYearOverYearGraphPoints(int chartType2,
			DataTable data, int size) {
		for (int i = 0; i < size; i++) {
			data.setValue(i, 0,
					getMonthAsString(Integer.parseInt(accountNames.get(i))));
			data.setValue(i, 1, DecimalUtil.round(graph_Values.get(i)));
		}
		return data;
	}

	private DataTable addIncomeAndExpenseGraphPoints(int chartType,
			DataTable data, int size) {
		for (int i = 0; i < size; i++) {
			data.setValue(i, 0, (String) x_Axis_Labels.get(i));
			data.setValue(i, 1, DecimalUtil.round(incomeValues.get(i)));
			data.setValue(i, 2, DecimalUtil.round(expenseValues.get(i)));
		}
		return data;
	}

	private DataTable addGraphPoints(int chartType, DataTable data, int size) {
		for (int i = 0; i < size; i++) {
			data.setValue(i, 0, (String) x_Axis_Labels.get(i));
			data.setValue(i, 1, DecimalUtil.round(graph_Values.get(i)));
		}
		return data;
	}

	private void createLabels(int chartType) {

		ClientFinanceDate date = new ClientFinanceDate();

		if (chartType == BANK_ACCOUNT_CHART_TYPE) {

			Calendar cal = Calendar.getInstance(); // GregorianCalendar();
			cal.setTime(date.getDateAsObject());
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 3);

			for (int i = 0; i <= 3; i++) {

				/*
				 * creating x-axis labels. Ex: Jan-01, Jan-03, Jan-05, ... Here
				 * x-axis label are combination of date and month. We using date
				 * values at each two intervals like date 1, 3, 5..
				 */

				x_Axis_Labels.add(i, getMonthAsString(cal.getTime().getMonth())
						+ " " + cal.getTime().getDate());

				cal.set(Calendar.DAY_OF_MONTH,
						cal.get(Calendar.DAY_OF_MONTH) + 1);
			}
		}

		// Graph for Accounts Receivable
		if (chartType == ACCOUNTS_RECEIVABLE_CHART_TYPE) {

			// For Account Receivable we have to show 6 points in graph
			for (int i = 6; i > 0; i--) {

				// creating x-axis labels. Ex: June, May, Apr, ...
				x_Axis_Labels.add(6 - i, getMonthAsString(date.getMonth() - i
						+ 1));
			}
		}

		// Graph for Accounts Payable
		if (chartType == ACCOUNTS_PAYABLE_CHART_TYPE) {

			Calendar cal = Calendar.getInstance();
			cal.setTime(date.getDateAsObject());

			for (int i = 0; i < 30; i++) {

				// creating x-axis labels. Ex: 1-Jan, 3-Jan, 5-Jan, ...
				x_Axis_Labels.add(i, getMonthAsString(cal.getTime().getMonth())
						+ " " + cal.getTime().getDate());

				cal.set(Calendar.DAY_OF_MONTH,
						cal.get(Calendar.DAY_OF_MONTH) + 1);
			}
		}
		if (chartType == ACCOUNTS_EXPENSE_CHART_TYPE) {
			x_Axis_Labels.addAll(accountNames);
		}
		if (chartType == INCOME_EXPENSE_BREAKDOWN_CHART_TYPE) {
			for (String monthNo : accountNames) {
				int i = Integer.parseInt(monthNo);
				x_Axis_Labels.add(getMonthAsString(i));
			}
		}
	}

	public String getMonthAsString(int month) {
		switch (month) {
		case 0:
			return DayAndMonthUtil.jan();
		case 1:
			return DayAndMonthUtil.feb();
		case 2:
			return DayAndMonthUtil.mar();
		case 3:
			return DayAndMonthUtil.apr();
		case 4:
			return DayAndMonthUtil.mayS();
		case 5:
			return DayAndMonthUtil.jun();
		case 6:
			return DayAndMonthUtil.jul();
		case 7:
		case -5:
			return DayAndMonthUtil.aug();
		case 8:
		case -4:
			return DayAndMonthUtil.sep();
		case 9:
		case -3:
			return DayAndMonthUtil.oct();
		case 10:
		case -2:
			return DayAndMonthUtil.nov();
		case 11:
		case -1:
			return DayAndMonthUtil.dec();

		}
		return "";

	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
