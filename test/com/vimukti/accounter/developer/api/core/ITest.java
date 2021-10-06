package com.vimukti.accounter.developer.api.core;

public interface ITest {

	public void before() throws Exception;

	public void test() throws Exception;

	public ApiResult getResult();
}
