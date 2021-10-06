package com.vimukti.accounter.text;

import java.util.HashMap;
import java.util.Map;

public class CommandContext {

	public static final String EMAIL_ID = "email";

	public static String CLIENT = "client";

	Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 
	 * @param name
	 * @param object
	 */
	public void put(String name, Object object) {
		map.put(name, object);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		return (T) map.get(name);
	}
}
