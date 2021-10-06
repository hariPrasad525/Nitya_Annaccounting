package com.vimukti.accounter.developer.api.core;

import com.nitya.accounter.developer.api.core.ApiResult;

public interface ITest {

	public void before() throws Exception;

	public void test() throws Exception;

	public ApiResult getResult();
}
