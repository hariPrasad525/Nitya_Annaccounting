package com.vimukti.accounter.web.client.core;

public class ClientEmailTemplate implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * TemplateId
	 * 
	 */

	private long id;

	/**
	 * emailBody
	 * 
	 */

	private String emailBody;

	/**
	 * templateName
	 * 
	 * @return
	 */

	private String emailTemplateName;

	private int version;

	public ClientEmailTemplate() {
		// TODO Auto-generated constructor stub
	}

	public long getTemplateId() {
		return id;
	}

	public void setTemplateId(long templateId) {
		this.id = templateId;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getEmailTemplateName() {
		return emailTemplateName;
	}

	public void setEmailTemplateName(String emailTemplateName) {
		this.emailTemplateName = emailTemplateName;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return emailTemplateName;
	}

	@Override
	public String getDisplayName() {
		return emailTemplateName;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMAIL_TEMPLATE;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

}
