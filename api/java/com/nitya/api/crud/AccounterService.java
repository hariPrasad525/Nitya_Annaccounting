package com.nitya.api.crud;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAttachment;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.api.core.ApiCallback;
import com.nitya.api.core.ApiRequest;

public class AccounterService extends RequestService {

	public AccounterService(int serializationType, String apiKey,
			String secretKey, String autherizedTocken, long companyId) {
		super(serializationType, apiKey, secretKey, autherizedTocken, companyId);
	}

	public void createUserSecret(String companyPassword,
			ApiCallback<Boolean> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "usersecret");
		map.put("password", companyPassword);
		ApiRequest<Boolean> request = new ApiRequest<Boolean>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_OPERATIONS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public void encryptCompany(String newCompanyPassword, String passwordHint,
			ApiCallback<Boolean> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "encryptcompany");
		map.put("password", newCompanyPassword);
		map.put("hint", passwordHint == null ? "" : passwordHint);
		ApiRequest<Boolean> request = new ApiRequest<Boolean>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_OPERATIONS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public void create(IAccounterCore createdObject, ApiCallback<Long> callback) {
		String xml = "";
		try {
			xml = serializer.serialize(createdObject);
		} catch (Exception e) {
			callback.onFail(e.getMessage());
			return;
		}

		ApiRequest<Long> request = new ApiRequest<Long>(xml,
				buildQueryString(null), getPath(RequestUtil.REQUEST_CRUD),
				RequestUtil.METHOD_PUT, callback);
		addRequest(request);
	}

	public void update(IAccounterCore createdObject, ApiCallback<Long> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", createdObject.getObjectType()
				.getServerClassSimpleName());
		map.put("id", Long.toString(createdObject.getID()));
		String xml = "";
		try {
			xml = serializer.serialize(createdObject);
		} catch (Exception e) {
			callback.onFail(e.getMessage());
			return;
		}
		ApiRequest<Long> request = new ApiRequest<Long>(xml,
				buildQueryString(map), getPath(RequestUtil.REQUEST_CRUD),
				RequestUtil.METHOD_POST, callback);
		addRequest(request);
	}

	public void delete(IAccounterCore createdObject,
			ApiCallback<Boolean> callback) {
		delete(createdObject.getObjectType(), createdObject.getID(), callback);
	}

	public void delete(AccounterCoreType type, long id,
			ApiCallback<Boolean> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type.getServerClassSimpleName());
		map.put("id", Long.toString(id));
		ApiRequest<Boolean> request = new ApiRequest<Boolean>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_CRUD),
				RequestUtil.METHOD_DELETE, callback);
		addRequest(request);
	}

	public <T> void get(AccounterCoreType type, long id, ApiCallback<T> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type.getServerClassSimpleName());
		map.put("id", Long.toString(id));
		ApiRequest<T> request = new ApiRequest<T>(buildQueryString(map),
				getPath(RequestUtil.REQUEST_CRUD), RequestUtil.METHOD_GET,
				callback);
		addRequest(request);
	}

	public <T> void get(AccounterCoreType type, String name,
			ApiCallback<T> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type.getServerClassSimpleName());
		map.put("name", name);
		ApiRequest<T> request = new ApiRequest<T>(buildQueryString(map),
				getPath(RequestUtil.REQUEST_CRUD), RequestUtil.METHOD_GET,
				callback);
		addRequest(request);
	}

	public <T> void getList(AccounterCoreType type,
			ApiCallback<List<T>> callback, String... paramPairs) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type.getServerClassSimpleName());
		for (int i = 0; i < paramPairs.length;) {
			map.put(paramPairs[i++], paramPairs[i++]);
		}
		ApiRequest<List<T>> request = new ApiRequest<List<T>>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_LISTS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public <T> void getReports(AccounterReportType reportType,
			ApiCallback<List<T>> callback, String... paramPairs) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", reportType.getValue());
		for (int i = 0; i < paramPairs.length;) {
			map.put(paramPairs[i++], paramPairs[i++]);
		}
		ApiRequest<List<T>> request = new ApiRequest<List<T>>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_REPORTS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public void getAvailableReportsList(
			ApiCallback<Map<String, List<String>>> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "reportslist");
		ApiRequest<Map<String, List<String>>> request = new ApiRequest<Map<String, List<String>>>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_OPERATIONS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public void uploadFile(File file, ApiCallback<ClientAttachment> callback) {

		ApiRequest<String> request = new ApiRequest<String>(
				buildQueryString(null),
				getPath(RequestUtil.REQUEST_UPLOAD_FILE),
				RequestUtil.METHOD_POST, new ApiCallback<String>() {

					@Override
					public void onSuccess(String obj) {
						System.out.println(obj);
					}

					@Override
					public void onFail(String reason) {
						System.err.println(reason);
					}
				});
		request.setFile(file);
		addRequest(request);
	}
}
