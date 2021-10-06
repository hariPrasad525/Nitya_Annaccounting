package com.nitya.accounter.web.client.core;

public class ClientUserDefinedPayHead extends ClientPayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.USERDEFINED_PAY_HEAD;
	}
}
