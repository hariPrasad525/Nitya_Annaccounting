package com.nitya.accounter.migration;

import java.util.HashMap;
import java.util.Map;

import com.nitya.accounter.core.IAccounterServerCore;

public class LocalIdProvider {

	private Map<IAccounterServerCore, String> _localIDs = new HashMap<IAccounterServerCore, String>();

	private int _lastLocalID = 0;

	public String getOrCreate(IAccounterServerCore obj) {
		String _lid = _localIDs.get(obj);
		if (_lid != null) {
			return _lid;
		}
		_lastLocalID += 1;
		_lid = String.valueOf(_lastLocalID);
		_localIDs.put(obj, String.valueOf(_lastLocalID));
		return _lid;
	}

	public String getOrCreate() {
		_lastLocalID += 1;
		return String.valueOf(_lastLocalID);
	}
}
