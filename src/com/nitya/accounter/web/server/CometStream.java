package com.nitya.accounter.web.server;

import java.io.NotSerializableException;
import java.io.Serializable;

public interface CometStream {

	/**
	 * Used for sending object's that are not JSONSerializable
	 * 
	 * @param obj
	 * @param method
	 */
	public void put(Serializable obj) throws NotSerializableException;
}
