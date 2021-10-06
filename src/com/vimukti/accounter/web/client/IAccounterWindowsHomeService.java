package com.vimukti.accounter.web.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.CompanyDetails;
import com.vimukti.accounter.web.client.core.SignupDetails;
import com.vimukti.accounter.web.client.core.StartupException;
import com.vimukti.accounter.web.client.core.SubscriptionDetails;
import com.vimukti.accounter.web.client.exception.AccounterException;

public interface IAccounterWindowsHomeService extends RemoteService {

	ArrayList<CompanyDetails> login(String email, String password,
			boolean rememberMe) throws StartupException;

	boolean signup(SignupDetails details) throws StartupException;

	boolean activate(String activationCode) throws StartupException;

	boolean forgotPassword(String email) throws StartupException;

	boolean deleteCompany(long companyId, boolean fromAllUsers)
			throws StartupException;

	ArrayList<CompanyDetails> getCompanies() throws StartupException;

	boolean logout() throws StartupException;

	ArrayList<CompanyDetails> updateClient(String password,
			String confirmPassword) throws StartupException;

	CompanyAndFeatures getCompany(Long companyId) throws AccounterException;

	SubscriptionDetails getSubscriptionDetails(String emai)
			throws AccounterException;
}
