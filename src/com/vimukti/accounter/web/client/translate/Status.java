package com.vimukti.accounter.web.client.translate;

import java.io.Serializable;

public class Status implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int total;
	String lang;
	int approved;
	int translated;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public int getApproved() {
		return approved;
	}

	public void setApproved(int approved) {
		this.approved = approved;
	}

	public int getTranslated() {
		return translated;
	}

	public void setTranslated(int translated) {
		this.translated = translated;
	}
}
