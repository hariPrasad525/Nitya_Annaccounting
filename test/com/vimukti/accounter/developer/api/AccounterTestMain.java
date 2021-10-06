package com.vimukti.accounter.developer.api;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.developer.api.core.ApiResult;
import com.vimukti.accounter.developer.api.core.ITest;
import com.vimukti.accounter.developer.api.report.CrudTest;

public class AccounterTestMain {
	private static List<ITest> tests = new ArrayList<ITest>();

	public static void main(String[] args) {
	}

	private static void test() {
		for (ITest test : tests) {
			try {
				test.before();
				test.test();
			} catch (Exception e) {
				ApiResult result = test.getResult();
				System.out.println(result.getResult());
				e.printStackTrace();
			}
		}
	}

	private static void loadTest() {
		tests.add(new CrudTest());
	}
}
