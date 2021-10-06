/**
 * 
 */
package com.nitya.accounter.web.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.nitya.accounter.web.client.core.AccountsTemplate;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.CountryPreferences;
import com.nitya.accounter.web.client.core.TemplateAccount;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IAccounterCompanyInitializationService extends RemoteService {

	boolean initalizeCompany(ClientCompanyPreferences preferences,
			String password, String passwordHint,
			ArrayList<TemplateAccount> accountsTemplates)
			throws AccounterException;

	public ArrayList<AccountsTemplate> getAccountsTemplate()
			throws AccounterException;

	public CompanyAndFeatures getCompany() throws AccounterException;

	public String getCountry();

	boolean isCompanyNameExists(String companyName) throws AccounterException;

	CountryPreferences getCountryPreferences(String countryName);

}
