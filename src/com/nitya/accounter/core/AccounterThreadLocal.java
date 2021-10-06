/**
 * 
 */
package com.nitya.accounter.core;

import java.sql.Timestamp;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccounterThreadLocal {

	private static ThreadLocal<User> threadLocal = new ThreadLocal<User>();

	public static void set(User user) {
		threadLocal.set(user);
	}

	public static User get() {
		return threadLocal.get();
	}

	public static Timestamp currentTime() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static Company getCompany() {
		User user = get();
		if (user == null) {
			return null;
		}
		return user.getCompany();
	}

}
