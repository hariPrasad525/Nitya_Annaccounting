package com.nitya.accounter.web.client.translate;

import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;

public class ClientLanguage implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String tooltip;
	private String code;

	public ClientLanguage() {

	}

	public ClientLanguage(String languageTooltip, String langugeName,
			String languageCode) {
		this.setLangugeName(langugeName);
		this.setLanguageTooltip(languageTooltip);
		this.setLanguageCode(languageCode);
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

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

	public void setLangugeName(String langugeName) {
		this.name = langugeName;
	}

	public String getLanguageName() {
		return name;
	}

	public void setLanguageCode(String languageCode) {
		this.code = languageCode;
	}

	public String getLanguageCode() {
		return code;
	}

	public void setLanguageTooltip(String languageTooltip) {
		this.tooltip = languageTooltip;
	}

	public String getLanguageTooltip() {
		return tooltip;
	}

	public ClientLanguage clone() {
		ClientLanguage language = (ClientLanguage) this.clone();
		return language;

	}
}
