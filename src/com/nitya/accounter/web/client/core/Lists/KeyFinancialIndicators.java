package com.nitya.accounter.web.client.core.Lists;

import java.io.Serializable;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KeyFinancialIndicators implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map<String, Map<Integer, Double>> indicators;

	public Map<String, Map<Integer, Double>> getIndicators() {
		return indicators;
	}

	public void setIndicators(Map<String, Map<Integer, Double>> indicators) {
		this.indicators = indicators;
	}

}
