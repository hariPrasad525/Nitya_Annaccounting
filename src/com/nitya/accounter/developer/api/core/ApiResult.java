package com.nitya.accounter.developer.api.core;

public class ApiResult {
	public static int SUCCESS = 200;
	public static int FAIL = 404;

	private int status;
	private Object result;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
