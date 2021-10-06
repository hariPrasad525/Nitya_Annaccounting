package com.vimukti.api.crud;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.developer.api.ApiCompany;
import com.vimukti.accounter.developer.api.ClientDetails;
import com.vimukti.api.core.ApiCallback;
import com.vimukti.api.core.ApiRequest;
import com.vimukti.api.process.RequestPeocesser;

public class Accounter {

	private String apiKey;
	private String secretKey;
	private ClientDetails clientDetails;
	private int serializationType;

	private Accounter(int serializationType, String apiKey, String secretKey,
			ClientDetails obj) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.clientDetails = obj;
		this.serializationType = serializationType;

	}

	public AccounterService openCompany(String companyName) {
		Long companyId = clientDetails.getCompanyId(companyName);
		if (companyId == null) {
			return null;
		}
		AccounterService service = new AccounterService(serializationType,
				apiKey, secretKey, clientDetails.getAutherizedTocken(),
				companyId);
		return service;
	}

	public Set<ApiCompany> companiesList() {
		return clientDetails.getCompanies();
	}

	public static void login(final int serializationType, final String apiKey,
			final String password, final String secretKey,
			final ApiCallback<Accounter> callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ApiKey", apiKey);
		params.put("Expire", RequestUtil.getDateString(new Date()));
		params.put("password", password);
		String queryString = RequestUtil.makeQueryString(params);
		ApiRequest<ClientDetails> request = new ApiRequest<ClientDetails>(
				queryString, RequestUtil.getPath(RequestUtil.REQUEST_LOGIN,
						serializationType), RequestUtil.METHOD_GET,
				new ApiCallback<ClientDetails>() {

					@Override
					public void onSuccess(ClientDetails obj) {
						callback.onSuccess(new Accounter(serializationType,
								apiKey, secretKey, obj));
					}

					@Override
					public void onFail(String reason) {
						callback.onFail(reason);
					}
				});
		request.setSerializationType(serializationType);
		request.setSecretKey(secretKey);
		RequestPeocesser.add(request);

	}

	public Set<String> getNonEncryptedCompanies() {
		return null;
	}

	public Set<String> getEncryptedCompanies() {
		return null;
	}
}
