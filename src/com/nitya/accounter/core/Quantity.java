/**
 * 
 */
package com.nitya.accounter.core;

import java.util.Set;

/**
 * This class contains Quantity calculation methods and price calculation
 * method. <br>
 * <b><i>Unit</i></b> may be null. <br>
 * <br>
 * This class is an <b>immutable</b> class.
 * 
 * @author Srikanth.J
 * 
 */
public class Quantity implements Comparable<Quantity> {

	private Unit unit;
	private double value;

	/**
	 * 
	 */
	public Quantity() {
	}

	/**
	 * Gives the result into default measurement.
	 * 
	 * @param quantity
	 * @return this plus givenQuantity
	 * @exception IllegalArgumentException
	 *                thrown if the unit types mismatched.
	 */
	public Quantity add(Quantity quantity) {
		Unit otherUnit = quantity.getUnit();
		if (unit == null ^ otherUnit == null) {
			// one unit is null and another one is not null.
			throw new IllegalArgumentException(
					"Can't able to add, null type mismatch");
		}

		if (unit != null
				&& !otherUnit.getMeasurement().getName()
						.equals(unit.getMeasurement().getName())) {
			// unit available, but the both are not belongs to same Measurement.
			throw new IllegalArgumentException(
					"Can't able to add different Unit types");
		}

		/*
		 * convert the quantities to default measure
		 */
		Quantity thisQuantity = convertToDefaultUnit();
		Quantity otherQuantity = quantity.convertToDefaultUnit();

		/*
		 * add the default quantities to make result quantity.
		 */
		Quantity resultQuantity = new Quantity();
		if (unit != null) {
			resultQuantity.setUnit(getDefaultUnit(unit));
		}
		resultQuantity.setValue(thisQuantity.getValue()
				+ otherQuantity.getValue());

		return resultQuantity;
	}

	/**
	 * Gives the result into default measurement.
	 * 
	 * @param quantity
	 * @return this plus givenQuantity
	 * @exception IllegalArgumentException
	 *                thrown if the unit types mismatched.
	 */
	public Quantity multiply(Quantity quantity) {
		Unit otherUnit = quantity.getUnit();
		if (unit == null ^ otherUnit == null) {
			// one unit is null and another one is not null.
			throw new IllegalArgumentException(
					"Can't able to add, null type mismatch");
		}

		if (unit != null
				&& !otherUnit.getMeasurement().getName()
						.equals(unit.getMeasurement().getName())) {
			// unit available, but the both are not belongs to same Measurement.
			throw new IllegalArgumentException(
					"Can't able to add different Unit types");
		}

		/*
		 * convert the quantities to default measure
		 */
		Quantity thisQuantity = convertToDefaultUnit();
		Quantity otherQuantity = quantity.convertToDefaultUnit();

		/*
		 * add the default quantities to make result quantity.
		 */
		Quantity resultQuantity = new Quantity();
		if (unit != null) {
			resultQuantity.setUnit(getDefaultUnit(unit));
		}
		resultQuantity.setValue(thisQuantity.getValue()
				* otherQuantity.getValue());

		return resultQuantity;
	}

	/**
	 * Gives the result into default measurement.
	 * 
	 * @param quantity
	 * @return this minus givenQuantity
	 * @exception IllegalArgumentException
	 *                thrown if the unit types mismatched.
	 */
	public Quantity subtract(Quantity quantity) {
		// multiply the value with -1. or change sign.
		Quantity qty = new Quantity();
		qty.setValue(-quantity.getValue());
		qty.setUnit(quantity.getUnit());
		return add(qty);
	}

	/**
	 * Calculates price for the available quantity based on given unitPrice[for
	 * default unit].
	 * 
	 * <br>
	 * <b>NOTE</b> <i>: No Currency details included here.</i>
	 * 
	 * @param defaultUnitPrice
	 * @return
	 */
	public double calculatePrice(double defaultUnitPrice) {
		Quantity defaultQty = convertToDefaultUnit();
		return defaultQty.getValue() * defaultUnitPrice;
	}

	/**
	 * Calculates price for the available quantity based on given unitPrice[for
	 * This Quantity Unit]
	 * 
	 * <br>
	 * <b>NOTE</b> <i>: No Currency details included here.</i>
	 * 
	 * @param unitPrice
	 * @return
	 */
	public double calculate(double unitPrice) {
		return getValue() * unitPrice;
	}

	/**
	 * Converts the present quantity into default measurement.
	 * 
	 * @return
	 */
	public Quantity convertToDefaultUnit() {
		double conversionFactor = getUnit() == null ? 1 : getUnit()
				.getMeasurement().getConversionFactor(getUnit().getType());

		Quantity quantity = new Quantity();
		quantity.setValue(value * conversionFactor);
		if (unit != null)
			quantity.setUnit(getDefaultUnit(unit));
		return quantity;
	}

	private Unit getDefaultUnit(Unit unit) {
		Unit defaultUnit1 = new Unit();
		Measurement measurement = unit.getMeasurement();
		Set<Unit> units = measurement.getUnits();
		for (Unit unit1 : units) {
			if (unit1.isDefault()) {
				return defaultUnit1 = unit1;
			}
		}
		return defaultUnit1;
	}

	public Unit getUnit() {
		return unit;
	}

	public double getValue() {
		return value;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getValue());
		if (unit != null) {
			sb.append(' ').append(unit.getType()).append(" of ")
					.append(unit.getMeasurement().getName());
		}
		return sb.toString();
	}

	@Override
	public int compareTo(Quantity other) {
		Quantity thisQty = convertToDefaultUnit();
		Quantity otherQty = other.convertToDefaultUnit();
		return (int) (thisQty.getValue() - otherQty.getValue());
	}

	public boolean isEmpty() {
		return value == 0;
	}

	public boolean isPositive() {
		return value > 0;
	}

	public Quantity copy() {
		Quantity qty = new Quantity();
		qty.setValue(getValue());
		qty.setUnit(getUnit());
		return qty;
	}

	public int hashCode() {
		int hash = 7;
		hash = (int) (31 * hash + value);
		hash = 31 * hash + (null == unit ? 0 : unit.hashCode());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Quantity)) {
			return false;
		}
		Quantity qty = (Quantity) obj;
		return getValue() == qty.getValue() && getUnit() != null
				&& qty.getUnit() != null
				&& getUnit().getID() == qty.getUnit().getID();
	}

	public Quantity reverse() {
		Quantity copy = copy();
		copy.setValue(-copy.getValue());
		return copy;
	}

}
