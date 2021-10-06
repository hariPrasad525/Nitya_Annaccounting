package com.nitya.accounter.main;

import com.nitya.accounter.core.EU.CipherCouple;

public class CipherThreadLocal {
	private static ThreadLocal<CipherCouple> threadLocal = new ThreadLocal<CipherCouple>();

	public static CipherCouple get() {
		return threadLocal.get();
	}

	public static void set(CipherCouple couple) {
		threadLocal.set(couple);
	}
}
