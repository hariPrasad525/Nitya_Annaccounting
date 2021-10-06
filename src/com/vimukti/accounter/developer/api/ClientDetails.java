package com.vimukti.accounter.developer.api;

import java.util.Date;
import java.util.Set;

public class ClientDetails {

	private Set<ApiCompany> companies;
	private String autherizedTocken;

	private int type;
	private Set<String> features;
	private Date createdDate;
	private Date lastModified;
	private Date expiredDate;
	private Date gracePeriodDate;
	private Set<String> members;
	private int premiumType;
	private int durationType;

	public ClientDetails(String encode, Set<ApiCompany> companyIds) {
		autherizedTocken = encode;
		companies = companyIds;
	}

	public Set<ApiCompany> getCompanies() {
		return companies;
	}

	public String getAutherizedTocken() {
		return autherizedTocken;
	}

	public Long getCompanyId(String companyName) {
		for (ApiCompany company : companies) {
			if (company.getName().equals(companyName)) {
				return company.getId();
			}
		}
		return null;// companies.get(companyName);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Set<String> getFeatures() {
		return features;
	}

	public void setFeatures(Set<String> features) {
		this.features = features;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public Date getGracePeriodDate() {
		return gracePeriodDate;
	}

	public void setGracePeriodDate(Date gracePeriodDate) {
		this.gracePeriodDate = gracePeriodDate;
	}

	public Set<String> getMembers() {
		return members;
	}

	public void setMembers(Set<String> members) {
		this.members = members;
	}

	public int getPremiumType() {
		return premiumType;
	}

	public void setPremiumType(int premiumType) {
		this.premiumType = premiumType;
	}

	public int getDurationType() {
		return durationType;
	}

	public void setDurationType(int durationType) {
		this.durationType = durationType;
	}
}
