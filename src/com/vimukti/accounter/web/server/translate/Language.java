package com.vimukti.accounter.web.server.translate;

import org.json.JSONException;

import com.vimukti.accounter.core.AuditWriter;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Language implements IAccounterServerCore {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String tooltip;
	private String code;

	public Language() {
	}

	public void setLangugeName(String langugeName) {
		this.name = langugeName;
	}

	public String getLangugeName() {
		return name;
	}

	public void setLanguageTooltip(String languageTooltip) {
		this.tooltip = languageTooltip;
	}

	public String getLanguageTooltip() {
		return tooltip;
	}

	public void setLanguageCode(String languageCode) {
		this.code = languageCode;
	}

	public String getLanguageCode() {
		return code;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
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
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}
}
