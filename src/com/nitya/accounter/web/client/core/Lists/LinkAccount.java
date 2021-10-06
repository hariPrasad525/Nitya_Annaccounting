package com.nitya.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LinkAccount implements IsSerializable,Serializable{

	long assetAccount;

	long linkedAccount;

	public long getAssetAccount() {
		return assetAccount;
	}

	public void setAssetAccount(long assetAccount) {
		this.assetAccount = assetAccount;
	}

	public long getLinkedAccount() {
		return linkedAccount;
	}

	public void setLinkedAccount(long linkedAccount) {
		this.linkedAccount = linkedAccount;
	}



}
