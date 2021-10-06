package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class UserDefinedPayheadItem implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	int version;

	UserDefinedPayHead payHead;

	double value;

	public UserDefinedPayheadItem() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
	}

	@Override
	public void selfValidate() throws AccounterException {
	}

	public UserDefinedPayHead getPayHead() {
		return payHead;
	}

	public void setPayHead(UserDefinedPayHead payHead) {
		this.payHead = payHead;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
