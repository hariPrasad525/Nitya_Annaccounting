package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.ui.Portlet;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;

public abstract class DateRangePortletToolBar extends PortletToolBar {
	protected SelectCombo dateRangeItemCombo;
	protected List<String> dateRangesList;

	public DateRangePortletToolBar() {
		init();
	}

	protected void init() {
		createControls();
	}

	private void createControls() {
		initOrSetConfigDataToPortletConfig();
		dateRangesList = new ArrayList<String>();
		for (int i = 0; i < allDateRangeArray.length; i++) {
			dateRangesList.add(allDateRangeArray[i]);
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
		addItems(dateRangeItemCombo);
	}

}
