package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class Bank extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8192082953624184711L;

	String name;

	public Bank() {
	}

	/**
	 * @param id
	 *            the id to set
	 */
	// public void setID(long id){
	// this.id = id;
	// }
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	// public void setName(String name) {
	// this.name = name;
	// }
	@Override
	public String toString() {

		return this.name;
	}

	public boolean equals(Bank bank) {

		if (bank.getID() == this.getID()
				&& this.getVersion() == bank.getVersion()
				&& this.name.equals(bank.name))
			return true;
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		if (!UserUtils.canDoThis(Bank.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		Bank bank = (Bank) clientObject;
		Query query = session.getNamedQuery("getNameofBank.from.Bank")
				.setParameter("name", bank.name, EncryptedStringType.INSTANCE)
				.setEntity("company", bank.getCompany());
		List list = query.list();
		if (list != null && list.size() > 0) {
			Bank newBank = (Bank) list.get(0);
			if (bank.getID() != newBank.getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "Bank name is alreay in use Please enter Unique name");

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
		return IAccounterCore.BANK;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.bank()).gap();

		if (this.name != null) {
			w.put(messages.name(), this.name);
		}

	}

	@Override
	public void selfValidate() throws AccounterException {

		if (name == null || name.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().bank());
		}
	}

}
