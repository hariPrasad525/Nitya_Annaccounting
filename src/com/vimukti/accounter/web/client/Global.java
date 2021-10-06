package com.vimukti.accounter.web.client;

public class Global {

	public static IGlobal context;

	public static IGlobal get() {
		return context;
	}

	public static void set(IGlobal context) {
		Global.context = context;
	}
}
