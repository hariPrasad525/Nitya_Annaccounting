package com.vimukti.accounter.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class PortletConfiguration extends CreatableObject implements
		IAccounterServerCore, INamedObject {
	/**
	 * 
	 */
	private int column;
	private String portletName;
	private static final long serialVersionUID = 1L;
	private Map<String, String> portletData = new HashMap<String, String>();

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getObjType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public String getPortletName() {
		return portletName;
	}

	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.portletConfiguration()).gap();
	}

	public Map<String, String> getPortletMap() {
		return portletData;
	}

	public void setPortletMap(Map<String, String> portletKey) {
		this.portletData = portletKey;
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}

}
