package com.vimukti.accounter.web.client.ui.win8portlets;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientPayee;

public class WhoIOweOrOwesMePorletData extends PortletData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ClientPayee> payees = new ArrayList<ClientPayee>();

	public WhoIOweOrOwesMePorletData() {
	}

	public List<ClientPayee> getPayees() {
		return payees;
	}

	public void setPayees(List<ClientPayee> payees) {
		this.payees = payees;
	}
}
