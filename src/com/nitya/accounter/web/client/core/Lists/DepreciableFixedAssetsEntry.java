package com.nitya.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DepreciableFixedAssetsEntry implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long id;
	String fixedAssetName;
	double amountToBeDepreciated;

	public long getID(){
		return id;
	}

	public void setID(long id){
		this.id=id;
	}

	public String getFixedAssetName() {
		return fixedAssetName;
	}

	public void setFixedAssetName(String fixedAssetName) {
		this.fixedAssetName = fixedAssetName;
	}

	public double getAmountToBeDepreciated() {
		return amountToBeDepreciated;
	}

	public void setAmountToBeDepreciated(double amountToBeDepreciated) {
		this.amountToBeDepreciated = amountToBeDepreciated;
	}

}
