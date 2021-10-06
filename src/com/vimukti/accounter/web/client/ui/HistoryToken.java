/**
 * 
 */
package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.AccounterCoreType;

/**
 * @author Prasanna Kumar G
 * 
 */
public class HistoryToken {

	private String token;
	private AccounterCoreType type;
	private long value;

	/**
	 * Creates new Instance
	 */
	public HistoryToken(String history) throws Exception {
		parseToke(history);
	}

	/**
	 * @param token
	 */
	private void parseToke(String history) throws Exception {

		String[] split = history.split("\\?");
		this.token = split[0];

		if (split.length == 1) {
			return;
		}

		split = split[1].split(":");

		String typeString = split[0];

		this.type = AccounterCoreType.valueOf(typeString.toUpperCase());

		this.value = Long.parseLong(split[1]);
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the type
	 */
	public AccounterCoreType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(AccounterCoreType type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(long value) {
		this.value = value;
	}
}
