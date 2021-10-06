/**
 * 
 */
package com.vimukti.accounter.main;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ServerAllocationFactory {

	private static IServerAllocator serverAllocator;

	/**
	 * 
	 * Returns ServerAllocator
	 * 
	 * @return
	 */
	public static IServerAllocator getServerAllocator() {
		if (serverAllocator == null) {
			serverAllocator = new ServerAllocatorImpl();
		}
		return serverAllocator;
	}
}
