package com.nitya.accounter.setup.server;

public abstract interface SIDManager {
	public abstract String generateSID();

	public abstract boolean isValidSID(String paramString);
}