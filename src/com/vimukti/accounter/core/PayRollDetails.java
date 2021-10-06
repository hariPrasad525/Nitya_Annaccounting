package com.vimukti.accounter.core;

public class PayRollDetails {

	private long workingDays = 30;

	private long holiDays = 6;

	public long getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(long workingDays) {
		this.workingDays = workingDays;
	}

	public long getHoliDays() {
		return holiDays;
	}

	public void setHoliDays(long holiDays) {
		this.holiDays = holiDays;
	}
}
