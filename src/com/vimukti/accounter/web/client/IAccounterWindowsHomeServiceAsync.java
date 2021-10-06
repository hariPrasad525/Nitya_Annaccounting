package com.vimukti.accounter.web.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.CompanyDetails;
import com.vimukti.accounter.web.client.core.SignupDetails;
import com.vimukti.accounter.web.client.core.SubscriptionDetails;

public interface IAccounterWindowsHomeServiceAsync {

	void login(String email, String password, boolean rememberMe,
			AsyncCallback<ArrayList<CompanyDetails>> callback);

	void signup(SignupDetails details, AsyncCallback<Boolean> callback);

	void activate(String activationCode, AsyncCallback<Boolean> callback);

	void forgotPassword(String email, AsyncCallback<Boolean> callback);

	void deleteCompany(long companyId, boolean fromAllUsers,
			AsyncCallback<Boolean> callback);

	void getCompanies(AsyncCallback<ArrayList<CompanyDetails>> callback);

	void logout(AsyncCallback<Boolean> callback);

	void updateClient(String password, String confirmPassword,
			AsyncCallback<ArrayList<CompanyDetails>> callback);

	void getCompany(Long companyId,
			AsyncCallback<CompanyAndFeatures> accounterAsyncCallback);

	void getSubscriptionDetails(String email,
			AsyncCallback<SubscriptionDetails> callback);

}
