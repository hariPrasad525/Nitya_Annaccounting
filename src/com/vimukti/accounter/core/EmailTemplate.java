package com.vimukti.accounter.core;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class EmailTemplate extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * emailBody
	 * 
	 */
	private String emailTemplateName;

	private String emailBody;

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
	public String getName() {
		return emailTemplateName;
	}

	@Override
	public void setName(String name) {
		this.emailTemplateName = name;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.EMAIL_TEMPLATE;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		Query query = session
				.getNamedQuery("getEmailTemplate")
				.setParameter("companyId",
						((EmailTemplate) clientObject).getCompany().getID())
				.setParameter("emailTemplateName", this.emailTemplateName,
						EncryptedStringType.INSTANCE)
				.setLong("id", this.getID());
		List list = query.list();

		if (list != null || list.size() > 0 || list.get(0) != null) {
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {

				String object = (String) iterator.next();
				if (this.getEmailTemplateName().equals(object)) {
					throw new AccounterException(
							AccounterException.ERROR_NAME_CONFLICT);
					// "Branding Theme already exist with this Name");
				}
			}
		}
		return true;
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {

		if (isOnSaveProccessed)
			return true;
		super.onSave(arg0);
		isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.EMAIL_TEMPLATE);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() throws AccounterException {
		if (this.emailTemplateName == null
				|| this.emailTemplateName.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().emailSentToYou());
		}

		Set<EmailTemplate> emailTemplates = getCompany().getEmailTemplates();
		for (EmailTemplate emailTemplate : emailTemplates) {
			if (emailTemplate.getName()
					.equalsIgnoreCase(getEmailTemplateName())
					&& emailTemplate.getID() != getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_NULL, Global.get()
								.messages().email()
								+ " " + Global.get().messages().Template());
			}
		}
	}
}
