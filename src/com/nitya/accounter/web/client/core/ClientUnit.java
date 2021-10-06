/**
 * 
 */
package com.nitya.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientUnit implements IAccounterCore, Serializable,
		IsSerializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private ClientMeasurement measurement;
	private String type;
	private double factor;
	private boolean isDefault;

	/**
	 * Creates new Instance
	 */
	public ClientUnit() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates new Instance
	 */
	public ClientUnit(String unitType, double factor) {
		this.type = unitType;
		this.factor = factor;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the measurement
	 */
	public ClientMeasurement getMeasurement() {
		return measurement;
	}

	/**
	 * @param measurement
	 *            the measurement to set
	 */
	public void setMeasurement(ClientMeasurement measurement) {
		this.measurement = measurement;
	}

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
	 * @return the factor
	 */
	public double getFactor() {
		return factor;
	}

	/**
	 * @param factor
	 *            the factor to set
	 */
	public void setFactor(double factor) {
		this.factor = factor;
	}

	public ClientUnit clone() {
		ClientUnit unit = this.clone();
		unit.measurement = this.measurement.clone();
		return unit;
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
	public String getName() {
		return type;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.UNIT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isEmpty() {
		if ((this.type == null || this.type.trim().length() == 0)
				&& (this.factor == 0)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		if (this.type != null && !type.equals("")) {
			buffer.append(type).append(",");
		}
		if (this.factor != 0.0) {
			buffer.append(factor);
		}

		return buffer.toString();
	}
}
