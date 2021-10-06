package com.nitya.accounter.web.client.core;

public class ClientPayStructureItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * PayHead of this PayStructure Item
	 */
	private long payHead;

	/**
	 * Rate
	 */
	private double rate;

	private long payStructure;

	private long id;

	private long effectiveFrom;

	private ClientPayHead clientPayHead;

	private int version;

	/**
	 * @return the payHead
	 */
	public long getPayHead() {
		return payHead;
	}

	/**
	 * @param payHead
	 *            the payHead to set
	 */
	public void setPayHead(long payHead) {
		this.payHead = payHead;
	}

	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * @return the payStructure
	 */
	public long getPayStructure() {
		return payStructure;
	}

	/**
	 * @param payStructure
	 *            the payStructure to set
	 */
	public void setPayStructure(long payStructure) {
		this.payStructure = payStructure;
	}

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return "Pay Structure Item";
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return null;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public boolean isEmpty() {
		ClientPayHead newValue = clientPayHead;
		if (newValue == null) {
			return true;
		}

		if (newValue.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE
				|| newValue.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_PRODUCTION) {
			ClientAttendancePayHead ph = (ClientAttendancePayHead) newValue;
			if (ph.getAttendanceType() == ClientAttendancePayHead.ATTENDANCE_ON_RATE) {
				return getRate() <= 0;
			}
		} else if (newValue.getCalculationType() != ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE && newValue.getCalculationType() != ClientPayHead.CALCULATION_TYPE_AS_TAX
				&& newValue.getCalculationType() != ClientPayHead.CALCULATION_TYPE_AS_USER_DEFINED) {
			return getRate() <= 0;
		}

		return false;
	}

	/**
	 * @return the effectiveFrom
	 */
	public long getEffectiveFrom() {
		return effectiveFrom;
	}

	/**
	 * @param effectiveFrom
	 *            the effectiveFrom to set
	 */
	public void setEffectiveFrom(long effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public ClientPayHead getClientPayHead() {
		return clientPayHead;
	}

	public void setClientPayHead(ClientPayHead newValue) {
		this.clientPayHead = newValue;
	}
}
