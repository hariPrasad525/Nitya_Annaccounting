package com.nitya.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;

/**
 * A VATGroup is a sub class of VATItemGroup. It consists of list of VATItems
 * and the groupRate to indicate the average of the vat rates of the list
 * 
 * @author Chandan
 */
public class TAXGroup extends TAXItemGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2055386258774310462L;
	/**
	 * VAT group consists of a list of VAT Items. {@link TAXItem}
	 */
	List<TAXItem> taxItems = new ArrayList<TAXItem>();

	double groupRate;

	public TAXGroup() {
	}

	public TAXGroup(Company company) {
		super(company);
	}

	/**
	 * @return the groupRate
	 */
	public double getGroupRate() {
		return groupRate;
	}

	/**
	 * @param groupRate
	 *            the groupRate to set
	 */
	public void setGroupRate(double groupRate) {
		this.groupRate = groupRate;
	}

	/**
	 * @return the vatItems
	 */
	public List<TAXItem> getTAXItems() {
		return taxItems;
	}

	/**
	 * @param vatItems
	 *            the vatItems to set
	 */
	public void setTAXItems(List<TAXItem> taxItems) {
		this.taxItems = taxItems;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		// if (getCompany() != null
		// && getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {
		// TAXCode taxCode = new TAXCode((TAXItemGroup) this);
		// session.saveOrUpdate(taxCode);
		// }
		super.onSave(session);
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}

		// if (getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {
		//
		// Query query = session.getNamedQuery("getTaxCode.by.id")
		// .setParameter("id", this.id)
		// .setEntity("company", getCompany());
		// TAXCode taxCode = (TAXCode) query.uniqueResult();
		// if (taxCode != null) {
		//
		// taxCode.setName(this.getName());
		// taxCode.setDescription(this.getDescription());
		// taxCode.setActive(this.isActive());
		// session.saveOrUpdate(taxCode);
		// }
		// this.isSalesType = true;
		// }

		session.getNamedQuery("updateTaxCodeSalesTaxRate")
				.setParameter("id", this.getID())
				.setParameter("salesTaxRate", this.groupRate)
				.setLong("companyId", getCompany().getID()).executeUpdate();
		session.getNamedQuery("updateTaxCodePurchaseTaxRate")
				.setParameter("id", this.getID())
				.setParameter("purchaseTaxRate", this.groupRate)
				.setLong("companyId", getCompany().getID()).executeUpdate();

		super.onSave(session);
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.TAX_GROUP);
		ChangeTracker.put(accounterCore);

		return super.onDelete(arg0);
	}

	@Override
	public int getObjType() {
		return IAccounterCore.TAXGROUP;
	}
}
