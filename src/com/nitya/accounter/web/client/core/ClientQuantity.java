/**
 * 
 */
package com.nitya.accounter.web.client.core;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.ui.Accounter;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientQuantity implements IAccounterCore, IsSerializable,
		Serializable, Cloneable, Comparable<ClientQuantity> {

	private long unit;
	private double value;

	/**
	 * @return the unit
	 */
	public long getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(long unit) {
		this.unit = unit;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	public ClientQuantity clone() {
		ClientQuantity quantity = (ClientQuantity) this.clone();
		quantity.unit = this.unit;
		return quantity;
	}

	@Override
	public String toString() {
		if (unit != 0) {
			return value + Accounter.getCompany().getUnitById(unit).getType();
		} else {
			return String.valueOf(value);
		}
	}

	@Override
	public int compareTo(ClientQuantity o) {
		// TODO verify this method later
		ClientUnit unitById = Accounter.getCompany().getUnitById(getUnit());
		ClientQuantity thisQty = convertToDefaultUnit(unitById);
		ClientQuantity otherQty = o.convertToDefaultUnit(unitById);
		return (int) (thisQty.getValue() - otherQty.getValue());
	}

	/**
	 * Converts the present quantity into default measurement.
	 * 
	 * @return
	 */
	public ClientQuantity convertToDefaultUnit(ClientUnit unit) {
		double conversionFactor = unit == null ? 1 : unit.getMeasurement()
				.getConversionFactor(unit.getType());

		ClientQuantity quantity = new ClientQuantity();
		quantity.setValue(value * conversionFactor);
		if (unit != null)
			quantity.setUnit(getDefaultUnitId(unit));

		return quantity;
	}

	public long getDefaultUnitId(ClientUnit unit2) {
		long id = 0;
		ClientMeasurement measurement = unit2.getMeasurement();
		List<ClientUnit> units = measurement.getUnits();
		for (ClientUnit clientUnit : units) {
			if (clientUnit.isDefault()) {
				return id = clientUnit.getId();
			}
		}
		return id;
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

}
