package com.nitya.accounter.core;

import java.util.Collection;

/**
 * 
 * A Interface Implemented by AuditWriterImpl class
 * 
 * @author Vimukti
 * 
 */

public interface AuditWriter {
	public AuditWriter put(String key, String value);

	public AuditWriter put(String key, boolean value);

	public AuditWriter put(String key, int value);

	public AuditWriter put(String key, double value);

	public AuditWriter put(String key, Double value);

	public AuditWriter gap();

	public AuditWriter put(String key,
			Collection<? extends IAccounterServerCore> value);

}
