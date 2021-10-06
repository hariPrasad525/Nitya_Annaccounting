package com.nitya.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.Features;

public class Subscription implements IsSerializable {
	public static final int BEFORE_PAID_FETURE = 1;
	public static final int FREE_CLIENT = 2;
	public static final int PREMIUM_USER = 3;

	private long id;
	private Set<String> features = new HashSet<String>();
	private int type;

	public Subscription() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<String> getFeatures() {
		Set<String> newFeatures = new HashSet<String>();
		newFeatures.add(Features.PAY_ROLL);
		newFeatures.add(Features.ATTACHMENTS);
		newFeatures.add(Features.BILLABLE_EXPENSE);
		newFeatures.add(Features.BUDGET);
		newFeatures.add(Features.CREATE_COMPANY);
		newFeatures.add(Features.CLASS);
		newFeatures.add(Features.CREDITS_CHARGES);
		newFeatures.add(Features.DASHBOARD_WIDGETS);
		newFeatures.add(Features.DRAFTS);
		newFeatures.add(Features.ENCRYPTION);
		newFeatures.add(Features.EXTRA_REPORTS);
		newFeatures.add(Features.FIXED_ASSET);
		newFeatures.add(Features.HISTORY);
		newFeatures.add(Features.HOSTORICAL_UNITPRICES);
		newFeatures.add(Features.IMPORT);
		newFeatures.add(Features.IMPORT_BANK_STATEMENTS);
		newFeatures.add(Features.INVENTORY);
		newFeatures.add(Features.INVITE_USERS);
		newFeatures.add(Features.JOB_COSTING);
		newFeatures.add(Features.LOCATION);
		newFeatures.add(Features.MERGING);
		newFeatures.add(Features.MULTI_CURRENCY);
		newFeatures.add(Features.PURCHASE_ORDER);
		newFeatures.add(Features.RECURRING_TRANSACTIONS);
		newFeatures.add(Features.SALSE_ORDER);
		newFeatures.add(Features.TRANSACTION_NAVIGATION);
		newFeatures.add(Features.TRANSACTIONS);
		newFeatures.add(Features.USER_ACTIVITY);
		return newFeatures;
	}

	public void setFeatures(Set<String> features) {
		this.features = features;
	}

	public static int getStringToType(String type) {
		if (type.equals("One User Monthly Subscription")) {
			return 0;
		} else if (type.equals("One User Yearly Subscription")) {
			return 1;
		} else if (type.equals("Two Users Monthly Subscription")) {
			return 2;
		} else if (type.equals("Two Users Yearly Subscription")) {
			return 3;
		} else if (type.equals("Five Users Monthly Subscription")) {
			return 4;
		} else if (type.equals("Five Users Yearly Subscription")) {
			return 5;
		} else if (type.equals("Unlimited Users Monthly Subscription")) {
			return 6;
		} else if (type.equals("Unlimited Users Yearly Subscription")) {
			return 7;
		}
		return 1;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isPaidUser() {
		//Kumar Kasimala - TODO
		//return type == PREMIUM_USER;
		return true;
	}

	public static Subscription getInstance(int subscriptionType) {
		Session currentSession = HibernateUtil.getCurrentSession();
		Subscription object = (Subscription) currentSession
				.getNamedQuery("get.subscription")
				.setParameter("type", subscriptionType).uniqueResult();
		return object;
	}
}
