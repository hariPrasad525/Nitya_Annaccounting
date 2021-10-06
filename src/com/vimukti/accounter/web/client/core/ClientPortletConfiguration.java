package com.vimukti.accounter.web.client.core;

import java.util.HashMap;
import java.util.Map;

public class ClientPortletConfiguration implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1019383886264751411L;
	public int column;
	private String portletName;
	private long id;
	private Map<String, String> portletData = new HashMap<String, String>();

	// private HashMap<String, String> store = new HashMap<String, String>();
	public ClientPortletConfiguration() {
	}

	public ClientPortletConfiguration(int column, String portlet) {
		this.column = column;
		this.portletName = portlet;
	}

	public String getName() {
		return this.portletName;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {

	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PORTLET_CONFIG;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Map<String, String> getPortletMap() {
		return portletData;
	}

	public void setPortletMap(Map<String, String> portletKey) {
		this.portletData = portletKey;
	}

}
