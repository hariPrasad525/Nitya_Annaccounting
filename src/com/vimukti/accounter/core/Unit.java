package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class Unit extends CreatableObject implements IAccounterServerCore,
		Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Measurement measurement;
	private String type;
	private double factor;
	private boolean isDefault;

	public Unit() {
	}

	public Unit(String type, double factor) {
		setType(type);
		setFactor(factor);
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	public Unit clone() {
		Unit unitClone = (Unit) this.clone();
		return unitClone;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) {
		return true;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		this.setCompany(measurement.getCompany());
		return super.onSave(session);
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() throws AccounterException {
		if (type == null || type.trim().length() == 0) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().unitName());
		}
		if (DecimalUtil.isEquals(factor, 0.0)) {
			throw new AccounterException(AccounterException.ERROR_AMOUNT_ZERO,
					Global.get().messages().factor());
		}
	}

}
