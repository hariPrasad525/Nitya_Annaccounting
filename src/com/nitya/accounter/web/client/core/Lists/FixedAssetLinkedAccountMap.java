package com.nitya.accounter.web.client.core.Lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FixedAssetLinkedAccountMap implements IsSerializable,Serializable {

	List<LinkAccount> assetLinkedAccounts = new ArrayList<LinkAccount>();

	public List<LinkAccount> getFixedAssetLinkedAccounts() {
		return assetLinkedAccounts;
	}

	public void setFixedAssetLinkedAccounts(
			List<LinkAccount> fixedAssetLinkedAccounts) {
		this.assetLinkedAccounts = fixedAssetLinkedAccounts;
	}

	public Set<Long> keySet() {
		Set<Long> keySet = new HashSet<Long>();
		for (LinkAccount linkAccount : assetLinkedAccounts) {
			keySet.add(linkAccount.assetAccount);
		}
		return keySet;
	}

	public Long get(long account) {
		for (LinkAccount linkAccount : assetLinkedAccounts) {
			if (linkAccount.assetAccount == account) {
				return linkAccount.linkedAccount;
			}
		}
		return null;
	}


}
