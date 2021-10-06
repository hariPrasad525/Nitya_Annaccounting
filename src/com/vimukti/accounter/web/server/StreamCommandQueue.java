package com.vimukti.accounter.web.server;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Collection;

import net.zschech.gwt.comet.server.CometSession;

public class StreamCommandQueue implements CometStream {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Collection<CometSession> queues;

	StreamCommandQueue(
			Collection<CometSession> queues) {
		this.queues = queues;
	}

	// @Override
	// public void put(JSONSerializable obj) {
	// queue.put(obj,stream);
	// }

	@Override
	public void put(Serializable obj) throws NotSerializableException {
		for (CometSession queue : queues) {
			if(queue.isValid()) {
				queue.enqueue(obj);
			}
		}
	}

//	void setRPCServiceInterface(Class<?> cls) {
//		for (CometSession queue : queues) {
//			queue.setRPCServiceInterface(cls);
//		}
//	}

}
