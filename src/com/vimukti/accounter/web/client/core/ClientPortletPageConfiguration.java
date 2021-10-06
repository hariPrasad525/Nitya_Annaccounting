package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientPortletPageConfiguration implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7636165785304294651L;
	private long id;
	private int columns;
	private String pageName;
	List<ClientPortletConfiguration> portlets = new ArrayList<ClientPortletConfiguration>();

	public ClientPortletPageConfiguration() {
		// TODO Auto-generated constructor stub
	}

	public ClientPortletPageConfiguration(int columns, String[][] portletNames) {
		this.columns = columns;
		int index = 0;
		for (String[] columPorlets : portletNames) {
			for (String portlet : columPorlets) {
				portlets.add(new ClientPortletConfiguration(index, portlet));
			}
			index++;
		}
	}

	public List<ClientPortletConfiguration> getPortletConfigurations() {
		return portlets;
	}

	public int getColumnsCount() {
		return columns;
	}

	public void setPortletsConfiguration(
			ArrayList<ClientPortletConfiguration> configs) {
		portlets = configs;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PORTLET_PAGE_CONFIG;
	}

	@Override
	public void setID(long id) {

	}

	@Override
	public long getID() {
		return 0;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
