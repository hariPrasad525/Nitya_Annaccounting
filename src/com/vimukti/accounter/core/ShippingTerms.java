package com.vimukti.accounter.core;

import java.util.List;

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
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class ShippingTerms extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7376956338052948369L;

	String name;
	String description;

	boolean isDefault;

	public ShippingTerms() {
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.SHIPPING_TERM);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(arg0);
		this.isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(arg0);
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		if (!UserUtils.canDoThis(ShippingTerms.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		ShippingTerms shippingTerms = (ShippingTerms) clientObject;
		Query query = session
				.getNamedQuery("getShippingTerms.by.Name")
				.setParameter("name", shippingTerms.name,
						EncryptedStringType.INSTANCE)
				.setEntity("company", shippingTerms.getCompany());
		List list = query.list();
		if (list != null && list.size() > 0) {
			ShippingTerms newShippingTerms = (ShippingTerms) list.get(0);
			if (shippingTerms.getID() != newShippingTerms.getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "ShippingTerms already exists with this name");
			}
		}

		return true;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.SHIPPING_TERMS;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.shippingTerms()).gap();
		w.put(messages.name(), this.name);
		w.put(messages.description(), this.description);

	}

	@Override
	public void selfValidate() throws AccounterException {
		if (name == null || name.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().shippingTerm());
		}
	}
}
