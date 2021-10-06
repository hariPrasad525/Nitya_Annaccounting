package com.vimukti.api.core;

import java.io.File;

import org.apache.commons.httpclient.methods.RequestEntity;

public class ApiRequest<T> {

	public static final int METHOD_GET = 1;
	public static final int METHOD_PUT = 2;
	public static final int METHOD_DELETE = 3;
	public static final int METHOD_POST = 4;
	public static final int HTTP_POST = 5;

	private String xml;
	private String queryString;
	private String path;
	private int method;
	private String secretKey;
	private int serializationType;
	private ApiCallback<T> callback;
	private File file;

	public ApiRequest(String xml, String queryString, String path, int method,
			ApiCallback<T> callback) {
		this.xml = xml;
		this.queryString = queryString;
		this.path = path;
		this.method = method;
		this.callback = callback;
	}

	public ApiRequest(String queryString, String path, int method,
			ApiCallback<T> callback) {
		this(null, queryString, path, method, callback);
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public ApiCallback<T> getCallback() {
		return callback;
	}

	public void setCallback(ApiCallback<T> callback) {
		this.callback = callback;
	}

	public int getSerializationType() {
		return serializationType;
	}

	public void setSerializationType(int serializationType) {
		this.serializationType = serializationType;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
