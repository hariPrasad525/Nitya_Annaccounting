package com.vimukti.accounter.main;

import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

public class CompanyPreferenceThreadLocal {
	
	private static ThreadLocal<ClientCompanyPreferences> companyPreferenceLocal = new ThreadLocal<ClientCompanyPreferences>();

	public static ClientCompanyPreferences get() {
		return companyPreferenceLocal.get();
	}

	public static void set(ClientCompanyPreferences companyPreference) {
		companyPreferenceLocal.set(companyPreference);
	}
}
