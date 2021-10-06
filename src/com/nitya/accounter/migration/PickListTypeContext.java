package com.nitya.accounter.migration;

import java.util.HashMap;
import java.util.Map;

public class PickListTypeContext {

	private Map<String, Long> picklistObjs = new HashMap<String, Long>();

	public boolean isEmpty() {
		return picklistObjs.isEmpty();
	}

	public void put(String identity, String instanceIdentity, Long id) {
		picklistObjs.put(identity + instanceIdentity, id);
	}

	public Long get(String objName, String value) {
		return picklistObjs.get(objName + value);
	}
}
