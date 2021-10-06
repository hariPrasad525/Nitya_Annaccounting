package com.nitya.accounter.web.client.ui;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.Label;

public class CustomLabel extends Label {

	LinkedHashMap<String, String> map;

	public CustomLabel(String string) {
		super(string);
		map = new LinkedHashMap<String, String>();
	}

	public void setField(String k, String v) {
		map.put(k, v);
	}

	public String getField(String k) {
		if (map.containsKey(k)) {
			return map.get(k);
		}
		return null;
	}
}
