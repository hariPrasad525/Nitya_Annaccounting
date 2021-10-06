package com.nitya.accounter.web.client.ui.win8portlets;

import java.util.ArrayList;
import java.util.List;

public class MoneyComingOrGoingPortletData extends PortletData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Double> values = new ArrayList<Double>();

	public MoneyComingOrGoingPortletData() {
	}

	public List<Double> getValues() {
		return values;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}
}
