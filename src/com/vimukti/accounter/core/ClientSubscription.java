package com.vimukti.accounter.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.Global;

public class ClientSubscription implements IsSerializable {

	public static final int ONE_USER = 1;
	public static final int TWO_USERS = 2;
	public static final int FIVE_USERS = 3;
	public static final int UNLIMITED_USERS = 4;
	public static final int TRIAL_USER = 5;

	public static final int MONTHLY_USER = 1;
	public static final int YEARLY_USER = 2;

	private long id;
	private Subscription subscription;
	private Date createdDate;
	private Date lastModified;
	private Date expiredDate;
	private Date gracePeriodDate;
	private Set<String> members = new HashSet<String>();
	private int premiumType;
	private int durationType;
	private String paypalSubscriptionProfileId;

	public ClientSubscription() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
		this.lastModified = createdDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Set<String> getMembers() {
		return members;
	}

	public void setMembers(Set<String> members) {
		this.members = members;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public int getPremiumType() {
		return ClientSubscription.UNLIMITED_USERS; //Kumar Kasimala -TODO
	}

	public void setPremiumType(int premiumType) {
		this.premiumType = premiumType;
	}

	public String getExpiredDateAsString() {
		if (expiredDate == null) {
			return "Unlimited";
		}
		if (Global.get().preferences() != null) {
			String dateInSelectedFormat = Utility
					.getDateInSelectedFormat(new FinanceDate(expiredDate));
			return dateInSelectedFormat;
		}
		SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat(
				"MMM dd, yyyy");
		return dateformatMMDDYYYY.format(expiredDate);

	}

	public boolean isInTracePeriod() {
		if (gracePeriodDate == null) {
			return false;
		}
		return gracePeriodDate.after(new Date());
	}

	public boolean isGracePeriodExpired() {
		if (gracePeriodDate == null) {
			return false;
		}
		return gracePeriodDate.before(new Date());
	}

	public boolean isExpired() {
		return expiredDate == null ? false : expiredDate.before(new Date());
	}

	public Date getGracePeriodDate() {
		return gracePeriodDate;
	}

	public String getGracePeriodDateAsString() {
		if (gracePeriodDate == null) {
			return "";
		}
		if (Global.get().preferences() != null) {
			String dateInSelectedFormat = Utility
					.getDateInSelectedFormat(new FinanceDate(gracePeriodDate));
			return dateInSelectedFormat;
		}
		SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat(
				"MMM dd, yyyy");
		return dateformatMMDDYYYY.format(gracePeriodDate);

	}

	public void setGracePeriodDate(Date gracePeriodDate) {
		this.gracePeriodDate = gracePeriodDate;

	}

	public int getDurationType() {
		return durationType;
	}

	public void setDurationType(int durationType) {
		this.durationType = durationType;
	}

	public String getPaypalSubscriptionProfileId() {
		return paypalSubscriptionProfileId;
	}

	public void setPaypalSubscriptionProfileId(
			String paypalSubscriptionProfileId) {
		this.paypalSubscriptionProfileId = paypalSubscriptionProfileId;
	}

	public boolean isPaidUser() {
		return getPremiumType() != TRIAL_USER && subscription.isPaidUser();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("CREATED DATE :" + createdDate);
		buffer.append("PREMIUM TYPE  :" + premiumType);
		buffer.append("EXPIRY DATE :" + expiredDate);
		buffer.append("SUBSCRIPTION TYPE :" + subscription.getType());
		buffer.append("GRACE PERIOD :" + gracePeriodDate);
		return buffer.toString();
	}
}