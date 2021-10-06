package com.nitya.accounter.web.client.core.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DepreciableFixedAssetsList implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map<Long, List<DepreciableFixedAssetsEntry>> accountViceFixedAssets;
	List<Long> fixedAssetIDs;

	public DepreciableFixedAssetsList() {
	}

	// public Map<String, Double> getAccountViceFixedAssets() {
	// return accountViceFixedAssets;
	// }
	//
	// public void setAccountViceFixedAssets(
	// Map<String, Double> accountViceFixedAssets) {
	// this.accountViceFixedAssets = accountViceFixedAssets;
	// }

	public Map<Long, List<DepreciableFixedAssetsEntry>> getAccountViceFixedAssets() {
		return accountViceFixedAssets;
	}

	public void setAccountViceFixedAssets(
			Map<Long, List<DepreciableFixedAssetsEntry>> accountViceFixedAssets) {
		this.accountViceFixedAssets = accountViceFixedAssets;
	}

	public List<Long> getFixedAssetIDs() {
		return fixedAssetIDs;
	}

	public void setFixedAssetIDs(List<Long> fixedAssetIDs) {
		this.fixedAssetIDs = fixedAssetIDs;
	}

}
