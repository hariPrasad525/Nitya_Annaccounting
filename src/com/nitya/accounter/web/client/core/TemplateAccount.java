/**
 * 
 */
package com.nitya.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.UIUtils;

/**
 * @author Prasanna Kumar G
 * 
 */
public class TemplateAccount implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String type;

	private String name;

	private boolean defaultValue;

	private boolean isSystemOnly;

	private String countries;

	private String cashFlowType;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the defaultValue
	 */
	public boolean getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the isSystemOnly
	 */
	public boolean isSystemOnly() {
		return isSystemOnly;
	}

	/**
	 * @param isSystemOnly
	 *            the isSystemOnly to set
	 */
	public void setSystemOnly(boolean isSystemOnly) {
		this.isSystemOnly = isSystemOnly;
	}

	/**
	 * @return the contries
	 */
	public String[] getCountries() {
		if (countries != null) {
			return countries.split(",");
		} else {
			return null;
		}
	}

	/**
	 * @param contries
	 *            the contries to set
	 */
	public void setCountries(String[] contries) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < contries.length; i++) {
			if (i != 0)
				sb.append(",");
			sb.append(contries[i]);
		}
		this.countries = sb.toString();
	}

	/**
	 * @return the cashFlowType
	 */
	public String getCashFlowType() {
		return cashFlowType;
	}

	/**
	 * @param cashFlowType
	 *            the cashFlowType to set
	 */
	public void setCashFlowType(String cashFlowType) {
		this.cashFlowType = cashFlowType;
	}

	public int getCashFlowAsInt() {
		AccounterMessages messages=Global.get().messages();
		if (cashFlowType == null) {
			return ClientAccount.CASH_FLOW_CATEGORY_FINANCING;
		}
		if (cashFlowType.equals(messages.financing())) {
			return ClientAccount.CASH_FLOW_CATEGORY_FINANCING;
		} else if (cashFlowType.equals(messages.investing())) {
			return ClientAccount.CASH_FLOW_CATEGORY_INVESTING;
		} else if (cashFlowType.equals(messages.operating())) {
			return ClientAccount.CASH_FLOW_CATEGORY_OPERATING;
		} else {
			return ClientAccount.CASH_FLOW_CATEGORY_FINANCING;
		}
	}

	/**
	 * @return
	 */
	public int getTypeAsInt() {
		for (int type : UIUtils.accountTypes) {
			if (this.type.equals(Utility.getAccountTypeString(type)))
				return type;
		}
		return 0;
	}

}
