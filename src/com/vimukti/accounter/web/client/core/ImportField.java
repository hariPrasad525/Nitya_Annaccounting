package com.vimukti.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.imports.FinanceDateField;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public abstract class ImportField implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the Field in the File
	 */
	private String columnName;

	/**
	 * Name of the Field
	 */
	private String name;

	/**
	 * Tells whether this Field is Required or not
	 */
	private boolean isRequired;

	private String desplayName;

	public ImportField(String name, String displayName) {
		this(name, displayName, false);

	}

	public ImportField(String name, String displayName, boolean isRequired) {
		this.name = name;
		this.desplayName = displayName;
		this.isRequired = isRequired;
	}

	public ImportField() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName
	 *            the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
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
	 * @return the isRequired
	 */
	public boolean isRequired() {
		return isRequired;
	}

	/**
	 * @param isRequired
	 *            the isRequired to set
	 */
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getDesplayName() {
		return desplayName;
	}

	public void setDesplayName(String desplayName) {
		this.desplayName = desplayName;
	}

	protected abstract Object getValue();

	public boolean isValid() {
		return isRequired ? getValue() != null : true;
	}

	public abstract boolean validate(String value);

	public String getValueAsString() {
		if (getValue() == null) {
			return null;
		}
		return getValue().toString();
	}

	public boolean isFinanceDate() {
		return this instanceof FinanceDateField;
	}

}
