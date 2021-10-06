package com.nitya.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

/**
 * VendorGroup refers to the category of the vendors. By this we can know to
 * which group a particular vendor belong to.
 * 
 * @author Chandan
 * 
 */

public class VendorGroup extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6315811900319594794L;
	/**
	 * VendorGroup Category Name
	 */
	String name;

	boolean isDefault;

	/**
	 * @return the version
	 */
	public VendorGroup() {
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
	 * @return the vendorGroupName
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
		accounterCore.setObjectType(AccounterCoreType.VENDOR_GROUP);
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
		super.onSave(arg0);
		ChangeTracker.put(this);
		return false;
	}

	public boolean equals(VendorGroup obj) {
		if (this.getID() == obj.getID() && this.name == obj.name) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		if (!UserUtils.canDoThis(VendorGroup.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		VendorGroup vendorGroup = (VendorGroup) clientObject;
		Query query = session
				.getNamedQuery("getVendorGroup.by.name")
				.setParameter("name", vendorGroup.name,
						EncryptedStringType.INSTANCE)
				.setEntity("company", vendorGroup.getCompany());
		List list = query.list();
		if (list != null && list.size() > 0) {
			VendorGroup newVendorGroup = (VendorGroup) list.get(0);
			if (vendorGroup.getID() != newVendorGroup.getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "SupplierGroup already exists with this name");
			}
		}
		return true;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.VENDOR_GROUP;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.group());
		w.put(messages.item(), this.name);
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (name.trim().length() == 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL);
		}
	}
}
