package com.nitya.api.crud;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.nitya.accounter.developer.api.ApiSerializationFactory;
import com.nitya.api.core.ApiCallback;
import com.nitya.api.core.ApiRequest;
import com.nitya.api.process.RequestPeocesser;

public class RequestService {
	private String apiKey;
	private String secretKey;
	private int serializationType;
	private long companyId;

	public ApiSerializationFactory serializer;
	private String autherizedTocken;

	RequestService(int serializationType, String apiKey, String secretKey,
			String autherizedTocken, long companyId) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.companyId = companyId;
		this.serializationType = serializationType;
		this.autherizedTocken = autherizedTocken;
		serializer = RequestUtil.getSerializer(serializationType);
	}

	public <T> void createRequest(String xml, String queryString,
			int requestType, int method, ApiCallback<T> callback) {
		ApiRequest<T> request = new ApiRequest<T>(xml, queryString,
				getPath(requestType), method, callback);
		addRequest(request);
	}

	public String getPath(int type) {
		return RequestUtil.getPath(type, serializationType);
	}

	public String buildQueryString(Map<String, String> map) {
		if (map == null) {
			map = new HashMap<String, String>();
		}
		map.put("ApiKey", apiKey);
		map.put("authentication", autherizedTocken);
		map.put("CompanyId", Long.toString(companyId));
		map.put("Expire", RequestUtil.getDateString(new Date()));
		return RequestUtil.makeQueryString(map);
	}

	public void addRequest(ApiRequest<?> request) {
		request.setSerializationType(serializationType);
		request.setSecretKey(secretKey);
		RequestPeocesser.add(request);
	}
}
