package com.nitya.api.core;

public interface ApiCallback<T> {

	public void onSuccess(T obj);

	public void onFail(String reason);
}
