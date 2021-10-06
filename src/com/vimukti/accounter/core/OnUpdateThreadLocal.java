package com.vimukti.accounter.core;

public class OnUpdateThreadLocal {
	private static ThreadLocal<Boolean> threadLocal = new ThreadLocal<Boolean>();

	public static void set(boolean isOnUpdateProccessed) {
		threadLocal.set(isOnUpdateProccessed);
	}

	public static boolean get() {
		return threadLocal.get() == null ? false : threadLocal.get();
	}
}
