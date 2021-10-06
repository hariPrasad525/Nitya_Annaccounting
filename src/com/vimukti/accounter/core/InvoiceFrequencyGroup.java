package com.vimukti.accounter.core;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InvoiceFrequencyGroup extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	/**
	 *
	 */
	private static final long serialVersionUID = 5174008021450044426L;

	/**
	 * Item Group Name
	 */
	String name;

	boolean isDefault;

	List<Item> items;

	transient List<Item> tempitems;

	public InvoiceFrequencyGroup() {
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

	/**
	 * return false; return false;
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.INVOICE_FREQUENCY);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		super.onLoad(arg0, arg1);
		this.tempitems = new ArrayList<Item>();
		if (this.items != null)
			this.tempitems.addAll(this.items);
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(arg0);
		try {
			checkNameConflictsOrNull();
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		this.isOnSaveProccessed = true;

		if (this.items != null) {
			for (Item item : this.items) {
				item.setInvoiceFrequencyGroup(this);
				HibernateUtil.getCurrentSession().saveOrUpdate(item);
			}
		}
		return false;
	}

	private void checkNameConflictsOrNull() throws AccounterException {
		if (name.trim().length() == 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL);
		}
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(arg0);
		if (this.items != null) {
			for (Item item : this.items) {
				tempitems.remove(item);
				HibernateUtil.getCurrentSession().saveOrUpdate(item);
			}
			for (Item item : this.tempitems) {
				item.setInvoiceFrequencyGroup(null);
				HibernateUtil.getCurrentSession().saveOrUpdate(item);
			}
		}
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		/*if (!UserUtils.canDoThis(InvoiceFrequencyGroup.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}*/

		InvoiceFrequencyGroup itemGroup = (InvoiceFrequencyGroup) clientObject;
		// Query query = session.createQuery("from ItemGroup I where I.name=?")
		// .setParameter(0, itemGroup.name);
		Query query = session
				.getNamedQuery("getInvocieFrequencyWithSameName")
				.setParameter("name", itemGroup.name,
						EncryptedStringType.INSTANCE)
				.setParameter("id", itemGroup.getID())
				.setParameter("companyId", itemGroup.getCompany().getID());
		List list = query.list();
		if (list != null && list.size() > 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_CONFLICT);
			// "An ItemGroup already exsits with this name");
		}

		return true;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.INVOICE_FREQUENCY;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		AccounterMessages2 messages2 = Global.get().messages2();

		w.put(messages.type(), messages2.invoiceGroup()).gap().gap();
		w.put(messages.name(), this.name);

		if (this.items != null) {
			w.put(messages.details(), this.items);
		}
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub

	}
}
