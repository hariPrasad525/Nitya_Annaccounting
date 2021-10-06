package com.vimukti.accounter.core;

public class Developer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private Client client;
	private String apiKey;
	private String secretKey;
	private String applicationName;
	private String description;
	private String integrationUrl;
	private String applicationType;
	private String applicationUse;
	private String developerEmailId;
	private String contact;
	public long succeedRequests;
	public long failureRequests;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIntegrationUrl() {
		return integrationUrl;
	}

	public void setIntegrationUrl(String integrationUrl) {
		this.integrationUrl = integrationUrl;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getApplicationUse() {
		return applicationUse;
	}

	public void setApplicationUse(String applicationUse) {
		this.applicationUse = applicationUse;
	}

	public String getDeveloperEmailId() {
		return developerEmailId;
	}

	public void setDeveloperEmailId(String developerEmailId) {
		this.developerEmailId = developerEmailId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

}
