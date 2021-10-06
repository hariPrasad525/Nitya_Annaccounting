package com.vimukti.accounter.admin.server;

import java.sql.Timestamp;

import com.vimukti.accounter.admin.core.AdminUser;

public class AdminThreadLocal {

	private static ThreadLocal<AdminUser> threadLocal = new ThreadLocal<AdminUser>();

	public static void set(AdminUser user) {
		threadLocal.set(user);
	}

	public static AdminUser get() {
		return threadLocal.get();
	}

	public static Timestamp currentTime() {
		return new Timestamp(System.currentTimeMillis());
	}

}
