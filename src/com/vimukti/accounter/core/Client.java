package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.web.server.translate.Language;

/**
 * This Client Saved in ServerDatabase
 * 
 * @author nagaraju.p
 * 
 */
public class Client {
	public static final String PASSWORD_HASH_STRING = "Accounter";
	private long id;
	private String firstName;
	private String lastName;
	private String fullName;
	private String emailId;
	private String password;
	private boolean isActive;
	private Set<User> users = new HashSet<User>();
	private String phoneNumber;
	private String country;
	private boolean isSubscribedToNewsLetters;
	private boolean isRequirePasswordReset = false;
	private int loginCount;
	private long lastLoginTime;
	private Set<Language> languages;
	private boolean isDeleted;
	private boolean isEmailBounced;

	private ClientSubscription clientSubscription;
	private byte[] passwordRecoveryKey;
	private boolean isPremiumTrailDone;
	private LicensePurchase licensePurchase;
	private UserPermissions permissions;

	// NEW FILEDS

	private int subscriptionType;
	/**
	 * CLIENT CRAETED DATE
	 */
	private FinanceDate createdDate;
	/**
	 * Number of Transactions Created
	 */
	private int transactionsCreatedCount;
	/**
	 * Number of Transactions Updated
	 */
	private int transactionsUpdatedCount;
	/**
	 * Number of Companies Created
	 */
	private int companiesCount;
	/**
	 * Last Updated Date(when he changes password etc)
	 */
	private FinanceDate updateDate;
	/**
	 * Number of Times opened companies
	 */
	private int openedCompaniesCount;
	/**
	 * Number of Times Changed password
	 */
	private int changedPasswordCount;
	/**
	 * Number of Transactions created From Each Device
	 */
	private int transactionCreatedFromDeviceCount;
	/**
	 * Number of Transactions updated From Each Device
	 */
	private int transactionUpdatedFromDeviceCount;

	/**
	 * Number of Companies Created From Each Device
	 */
	private int companiesFromDeviceCount;
	/**
	 * Number of Times logged In From Each Device
	 */
	private int loginFromDeviceCount;
	/**
	 * Number of Companies he is User of which are created by Premium Users
	 */
	private int premiumCompaniesCount;

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> companies) {
		this.users = companies;
	}

	public void setPhoneNo(String phoneNumber) {
		this.setPhoneNumber(phoneNumber);

	}

	public void setCountry(String country) {
		this.country = country;

	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCountry() {
		return country;
	}

	public boolean isSubscribedToNewsLetters() {
		return isSubscribedToNewsLetters;
	}

	public void setSubscribedToNewsLetters(boolean isSubscribedToNewsLetters) {
		this.isSubscribedToNewsLetters = isSubscribedToNewsLetters;
	}

	public boolean isRequirePasswordReset() {
		return isRequirePasswordReset;
	}

	public void setRequirePasswordReset(boolean isRequirePasswordReset) {
		this.isRequirePasswordReset = isRequirePasswordReset;
	}

	/**
	 * Converts Client Object to User Object
	 */
	public User toUser() {
		User user = new User();
		user.setClient(this);
		return user;
	}

	/**
	 * Returns ServerCompany From ID
	 * 
	 * @param companyID
	 * @return
	 */
	public Company getCompany(long companyID) {
		Iterator<User> iterator = users.iterator();
		while (iterator.hasNext()) {
			Company next = iterator.next().getCompany();
			if (next.getID() == companyID) {
				return next;
			}
		}
		return null;
	}

	/**
	 * Returns ServerCompany From ID
	 * 
	 * @param companyID
	 * @return
	 */
	public List<Company> getCompanies() {
		Iterator<User> iterator = users.iterator();
		List<Company> companies = new ArrayList<Company>();
		while (iterator.hasNext()) {
			Company next = iterator.next().getCompany();
			companies.add(next);
		}
		return companies;
	}

	public long getID() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setLanguages(Set<Language> languages) {
		this.languages = languages;
	}

	public Set<Language> getLanguages() {
		return languages;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isEmailBounced() {
		return isEmailBounced;
	}

	public void setEmailBounced(boolean isEmailBounced) {
		this.isEmailBounced = isEmailBounced;
	}

	public ClientSubscription getClientSubscription() {
		return clientSubscription;
	}

	public void setClientSubscription(ClientSubscription clientSubscription) {
		this.setSubscriptionType(clientSubscription.getSubscription().getType());
		this.clientSubscription = clientSubscription;
	}

	public byte[] getPasswordRecoveryKey() {
		return passwordRecoveryKey;
	}

	public void setPasswordRecoveryKey(byte[] passwordRecoveryKey) {
		this.passwordRecoveryKey = passwordRecoveryKey;
	}

	public int getTransactionsCreatedCount() {
		return transactionsCreatedCount;
	}

	public void setTransactionsCreatedCount(int transactionsCreatedCount) {
		this.transactionsCreatedCount = transactionsCreatedCount;
	}

	public int getTransactionsUpdatedCount() {
		return transactionsUpdatedCount;
	}

	public void setTransactionsUpdatedCount(int transactionsUpdatedCount) {
		this.transactionsUpdatedCount = transactionsUpdatedCount;
	}

	public int getCompaniesCount() {
		return companiesCount;
	}

	public void setCompaniesCount(int companiesCount) {
		this.companiesCount = companiesCount;
	}

	public FinanceDate getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(FinanceDate updateDate) {
		this.updateDate = updateDate;
	}

	public int getOpenedCompaniesCount() {
		return openedCompaniesCount;
	}

	public void setOpenedCompaniesCount(int openedCompaniesCount) {
		this.openedCompaniesCount = openedCompaniesCount;
	}

	public int getChangedPasswordCount() {
		return changedPasswordCount;
	}

	public void setChangedPasswordCount(int changedPasswordCount) {
		this.changedPasswordCount = changedPasswordCount;
	}

	public int getTransactionCreatedFromDeviceCount() {
		return transactionCreatedFromDeviceCount;
	}

	public void setTransactionCreatedFromDeviceCount(
			int transactionCreatedFromDeviceCount) {
		this.transactionCreatedFromDeviceCount = transactionCreatedFromDeviceCount;
	}

	public int getTransactionUpdatedFromDeviceCount() {
		return transactionUpdatedFromDeviceCount;
	}

	public void setTransactionUpdatedFromDeviceCount(
			int transactionUpdatedFromDeviceCount) {
		this.transactionUpdatedFromDeviceCount = transactionUpdatedFromDeviceCount;
	}

	public int getLoginFromDeviceCount() {
		return loginFromDeviceCount;
	}

	public void setLoginFromDeviceCount(int loginFromDeviceCount) {
		this.loginFromDeviceCount = loginFromDeviceCount;
	}

	public int getCompaniesFromDeviceCount() {
		return companiesFromDeviceCount;
	}

	public void setCompaniesFromDeviceCount(int companiesFromDeviceCount) {
		this.companiesFromDeviceCount = companiesFromDeviceCount;
	}

	public FinanceDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(FinanceDate createdDate) {
		this.createdDate = createdDate;
	}

	public int getPremiumCompaniesCount() {
		return premiumCompaniesCount;
	}

	public void setPremiumCompaniesCount(int premiumCompaniesCount) {
		this.premiumCompaniesCount = premiumCompaniesCount;
	}

	public int getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(int subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public boolean isPremiumTrailDone() {
		return isPremiumTrailDone;
	}

	public void setPremiumTrailDone(boolean isPremiumTrailDone) {
		this.isPremiumTrailDone = isPremiumTrailDone;
	}

	/**
	 * @return the licensePurchase
	 */
	public LicensePurchase getLicensePurchase() {
		return licensePurchase;
	}

	/**
	 * @param licensePurchase
	 *            the licensePurchase to set
	 */
	public void setLicensePurchase(LicensePurchase licensePurchase) {
		this.licensePurchase = licensePurchase;
	}

	public UserPermissions getPermissions() {
		return permissions;
	}

	public void setPermissions(UserPermissions permissions) {
		this.permissions = permissions;
	}

}
