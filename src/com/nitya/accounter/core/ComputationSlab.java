package com.nitya.accounter.core;

import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ComputationSlab implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Slab Types */

	public static final int TYPE_PERCENTAGE = 1;
	public static final int TYPE_VALUE = 2;

	private FinanceDate effectiveFrom;

	private double fromAmount;

	private double toAmount;

	private int slabType;

	private double value;

	/**
	 * @return the effectiveFrom
	 */
	public FinanceDate getEffectiveFrom() {
		return effectiveFrom;
	}

	/**
	 * @param effectiveFrom
	 *            the effectiveFrom to set
	 */
	public void setEffectiveFrom(FinanceDate effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	/**
	 * @return the fromAmount
	 */
	public double getFromAmount() {
		return fromAmount;
	}

	/**
	 * @param fromAmount
	 *            the fromAmount to set
	 */
	public void setFromAmount(double fromAmount) {
		this.fromAmount = fromAmount;
	}

	/**
	 * @return the toAmount
	 */
	public double getToAmount() {
		return toAmount;
	}

	/**
	 * @param toAmount
	 *            the toAmount to set
	 */
	public void setToAmount(double toAmount) {
		this.toAmount = toAmount;
	}

	/**
	 * @return the slabType
	 */
	public int getSlabType() {
		return slabType;
	}

	/**
	 * @param slabType
	 *            the slabType to set
	 */
	public void setSlabType(int slabType) {
		this.slabType = slabType;
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
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

}
