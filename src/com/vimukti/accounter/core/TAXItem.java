package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.countries.NewZealand;

/**
 * A VATItem is a sub class of VATItemGroup. It consists of VATagency, VAT
 * Return box and VAT rate. It is an entity which involves directly in the
 * transaction when the user want to make a vat liable transaction without any
 * involvement of items or accounts. The VATReturnBox reference is used to mark
 * this vat item to go into its corresponding vat box while filing the VAT.
 * 
 * @author Chandan
 */

public class TAXItem extends TAXItemGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1558444105253843076L;

	/**
	 * VAT agency Every VAT Item consists of VAT Agency. Whenever this VATItem
	 * is used in any transactions the balance for the VAT agency which we have
	 * to pay will increases.
	 */
	TAXAgency taxAgency;

	/**
	 * The rate for assigned for this particular VAT Item.
	 */
	double taxRate;

	/**
	 * The return box where the amount goes when we use this VAT Item in any
	 * transaction.
	 */
	VATReturnBox vatReturnBox;

	private Set<TAXGroup> groups = new HashSet<TAXGroup>();

	public TAXItem() {
	}

	public TAXItem(Company company) {
		super(company);
	}

	/**
	 * @return the vatReturnBox
	 */
	public VATReturnBox getVatReturnBox() {
		return vatReturnBox;
	}

	/**
	 * @param vatReturnBox
	 *            the vatReturnBox to set
	 */
	public void setVatReturnBox(VATReturnBox vatReturnBox) {
		this.vatReturnBox = vatReturnBox;
	}

	/**
	 * @return the vatAgency
	 */
	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the vatRate
	 */
	public double getTaxRate() {
		return taxRate;
	}

	/**
	 * @param vatRate
	 *            the vatRate to set
	 */
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
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
	public boolean onDelete(Session session) throws CallbackException {

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.TAXITEM);
		ChangeTracker.put(accounterCore);

		return super.onDelete(session);
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		/*
		 * if (this.vatReturnBox != null && (this.vatReturnBox.name
		 * .equals(AccounterServerConstants.UK_DOMESTIC_SALES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.UK_EC_SALES_GOODS) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.UK_EC_SALES_SERVICES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.UK_NOT_REGISTERED_SALES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.IRELAND_DOMESTIC_SALES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.IRELAND_EXEMPT_SALES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.IRELAND_NOT_REGISTERED_SALES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.IRELAND_EC_SALES_GOODS))) {
		 * this.isSalesType = true;
		 * 
		 * } else { this.isSalesType = false; }
		 */
		// if (Company.getCompany() != null
		// && Company.getCompany().getAccountingType() ==
		// Company.ACCOUNTING_TYPE_US) {
		// this.isSalesType = true;
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

		/*
		 * if (this.vatReturnBox != null && (this.vatReturnBox.name
		 * .equals(AccounterServerConstants.UK_DOMESTIC_SALES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.UK_EC_SALES_GOODS) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.UK_EC_SALES_SERVICES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.UK_NOT_REGISTERED_SALES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.IRELAND_DOMESTIC_SALES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.IRELAND_EXEMPT_SALES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.IRELAND_NOT_REGISTERED_SALES) ||
		 * this.vatReturnBox.name
		 * .equals(AccounterServerConstants.IRELAND_EC_SALES_GOODS))) {
		 * this.isSalesType = true;
		 * 
		 * } else { this.isSalesType = false; }
		 */

		session.getNamedQuery("updateTaxCodeSalesTaxRate")
				.setParameter("id", this.getID())
				.setParameter("companyId", getCompany().getID())
				.setParameter("salesTaxRate", this.taxRate).executeUpdate();
		session.getNamedQuery("updateTaxCodePurchaseTaxRate")
				.setParameter("id", this.getID())
				.setParameter("companyId", getCompany().getID())
				.setParameter("purchaseTaxRate", this.taxRate).executeUpdate();

		for (TAXGroup group : getGroups()) {
			double groupRate = 0.00D;
			for (TAXItem item : group.getTAXItems()) {
				groupRate = groupRate + item.getTaxRate();
			}
			group.setGroupRate(groupRate);
			ChangeTracker.put(group);
		}

		super.onSave(session);
		// ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (!goingToBeEdit) {
			checkNullValues();
		}
		if (clientObject.getID() != 0) {
			isNZDefaultTaxItem(clientObject);
		}
		return super.canEdit(clientObject, goingToBeEdit);
	}

	private void checkNullValues() throws AccounterException {
		if (getName() == null || getName().trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().taxItem());
		}

		if (DecimalUtil.isLessThan(getTaxRate(), 0)) {
			throw new AccounterException(
					AccounterException.ERROR_PERCENTAGE_LESSTHAN_0);
		} else if (DecimalUtil.isGreaterThan(getTaxRate(), 100)) {
			throw new AccounterException(
					AccounterException.ERROR_PERCENTAGE_GRATER_100);

		}
		if (getTaxAgency() == null) {
			throw new AccounterException(
					AccounterException.ERROR_PLEASE_SELECT, Global.get()
							.messages().taxAgencie());
		}
	}

	private void isNZDefaultTaxItem(IAccounterServerCore clientObject)
			throws AccounterException {
		if (getCompany().getCountryPreferences() instanceof NewZealand) {
			if (((TAXItem) clientObject).isDefault()) {
				throw new AccounterException(
						AccounterException.ERROR_DELETING_DEFAULT_TAX_ITEM);
			}
		}
	}

	public boolean canDelete(IAccounterServerCore serverCore)
			throws AccounterException {
		isNZDefaultTaxItem(serverCore);
		return true;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public int getObjType() {
		return IAccounterCore.TAXITEM;
	}

	/**
	 * @return the groups
	 */
	public Set<TAXGroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(Set<TAXGroup> groups) {
		this.groups = groups;
	}

}
