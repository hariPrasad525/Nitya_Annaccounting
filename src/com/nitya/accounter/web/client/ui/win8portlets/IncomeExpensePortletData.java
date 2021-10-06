package com.nitya.accounter.web.client.ui.win8portlets;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.IncomeExpensePortletInfo;

public class IncomeExpensePortletData extends PortletData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<IncomeExpensePortletInfo> expensePortletInfos = new ArrayList<IncomeExpensePortletInfo>();

	public IncomeExpensePortletData() {
	}

	public List<IncomeExpensePortletInfo> getExpensePortletInfos() {
		return expensePortletInfos;
	}

	public void setExpensePortletInfos(
			List<IncomeExpensePortletInfo> expensePortletInfos) {
		this.expensePortletInfos = expensePortletInfos;
	}
}
