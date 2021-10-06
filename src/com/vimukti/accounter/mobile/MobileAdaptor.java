/**
 * 
 */
package com.vimukti.accounter.mobile;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface MobileAdaptor {

	enum AdaptorType {
		CHAT, APPLICATION, MOBILE
	};

	public String postProcess(Result result);
}
