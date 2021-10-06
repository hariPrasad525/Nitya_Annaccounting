/**
 * 
 */
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
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * A Parent Class for VATGroup and VATItem. It contains all the information that
 * is common to these sub classes.
 * 
 * @author Chandan
 */

public class TAXItemGroup extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8121892431883668109L;

	/**
	 * Name of the Tax Group which is unique for every TaxGroup
	 */
	private String name;

	/**
	 * Description about the VAT item (About its VAT codes, rates, VAT agencies
	 * etc).
	 */
	private String description;

	boolean isActive;
	boolean isPercentage;

	boolean isDefault;

	public TAXItemGroup() {
		// TODO Auto-generated constructor stub
	}

	public TAXItemGroup(Company company) {
		setCompany(company);
	}

	/**
	 * @return the isPercentage
	 */
	public boolean isPercentage() {
		return isPercentage;
	}

	/**
	 * @param isPercentage
	 *            the isPercentage to set
	 */
	public void setPercentage(boolean isPercentage) {
		this.isPercentage = isPercentage;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		// if (getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {
		// TAXCode code = (TAXCode) session
		// .getNamedQuery("getTAXCode.for.TAXItemGroup")
		// .setParameter("id", this.id)
		// .setEntity("company", getCompany()).uniqueResult();
		// if (code != null) {
		// session.delete(code);
		// }
		// }
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

		if (!UserUtils.canDoThis(TAXItemGroup.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		TAXItemGroup taxItemGroup = (TAXItemGroup) clientObject;
		// Query query =
		// session.createQuery("from VATItemGroup V where V.name =: name")
		// .setParameter("name", vatItemGroup.name);
		Query query = session
				.getNamedQuery("getTaxItemGroupWithSameName")
				.setParameter("name", this.getName(),
						EncryptedStringType.INSTANCE)
				.setParameter("id", this.getID())
				.setParameter("companyId", taxItemGroup.getCompany().getID());
		List list = query.list();
		if (list != null && list.size() > 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_CONFLICT);
			// "A VATItem or VATGroup already exists with this name");
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int getObjType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.taxGroup()).gap();

		w.put(messages.name(), this.name);
		w.put(messages.description(), this.description);
		w.put(messages.isActive(), this.isActive);
		w.put(messages.defaultWare(), this.isDefault);

	}

	@Override
	public void selfValidate() throws AccounterException {
		if (name.trim().length() == 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL);
		}
	}
}
