package com.vimukti.accounter.web.client.ui;

public class AccounterInitialiser {

	public static final String PASSWORD_CRED_RESOURCE = "ACCOUNTER";

	Accounter accounter;

	// public AccounterInitialiser(Accounter accounter) {
	// accounter.loadCompany();
	// }

	public AccounterInitialiser() {
		// TODO Auto-generated constructor stub
	}

	public void initalize() {
		Accounter.loadCompany();
	}

	public Boolean isIpad() {
		return false;
	}

}
