package com.nitya.accounter.core;

import java.io.Serializable;
import java.util.Comparator;

import com.google.gwt.user.client.rpc.IsSerializable;


public class AccountComparator implements Comparator, IsSerializable,
		Serializable {

	// @Override
	// public int compare(Object o1, Object o2) {
	//
	// Account a = (Account) o1;
	//
	// if (a.getName().equalsIgnoreCase("Opening Balance")) {
	// return 1;
	// }
	//
	// return 1;
	// }
	public int compare(Object a1, Object a2) {

		Account account1 = (Account) a1;
		Account account2 = (Account) a2;

		long baseType1 = account1.getBaseType();
		long baseType2 = account2.getBaseType();

		// if(baseType1>baseType)
		//
		// if (rank1 > rank2) {
		// return +1;
		// } else if (rank1 < rank2) {
		// return -1;
		// } else {
		// return 0;
		// }
		return 0;
	}

}
