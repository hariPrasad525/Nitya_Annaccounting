package com.nitya.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.ui.Portlet;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;

public abstract class TopPayeesBySalesPortletToolBar extends PortletToolBar {
	protected SelectCombo dateRangeItemCombo;
	private List<String> dateRangesList;
	protected SelectCombo limitCombo;
	protected String dateRange;
	protected String limit;

	public TopPayeesBySalesPortletToolBar() {
		createControls();
	}

	private void createControls() {
		initOrSetConfigDataToPortletConfig();
		dateRangesList = new ArrayList<String>();
		String[] dateRangeArray = { messages.all(), messages.thisWeek(),
				messages.thisMonth(), messages.lastWeek(),
				messages.lastMonth(), messages.thisFinancialYear(),
				messages.lastFinancialYear(), messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(), messages.financialYearToDate() };
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangesList.add(dateRangeArray[i]);
		}
		dateRangeItemCombo = new SelectCombo(messages.dateRange());
		dateRangeItemCombo.initCombo(dateRangesList);
		setDefaultDateRange(portletConfigData.get(Portlet.DATE_RANGE));
		dateRangeItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						portletConfigData.put(Portlet.DATE_RANGE, selectItem);
						refreshPortletData();
					}
				});
		limitCombo = new SelectCombo(messages.limit());
		List<String> limitList = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			limitList.add(String.valueOf((i + 1) * 5));
		}
		limitCombo.initCombo(limitList);
		limitCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						portletConfigData.put(Portlet.LIMIT, selectItem);
						refreshPortletData();
					}
				});
		limitCombo.setSelected(portletConfigData.get(Portlet.LIMIT));
		addItems(dateRangeItemCombo, limitCombo);
	}
}
