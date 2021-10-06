package com.nitya.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
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
 * A VATCode is the entity which actually applies VAT. In a transaction we need
 * to apply the suitable VATCode for the line items involved in it which then
 * calculates the VAT amount for that transaction and notes it into @link
 * VATRateCalculation}
 * 
 * @author Chandan
 * 
 */
public class TAXCode extends CreatableObject implements IAccounterServerCore,
		INamedObject, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1014067769682467798L;
	boolean isImported;

	String name;
	String description;
	boolean isTaxable;
	boolean isActive;
	@ReffereredObject
	TAXItemGroup TAXItemGrpForPurchases;
	@ReffereredObject
	TAXItemGroup TAXItemGrpForSales;

	boolean isDefault;

	private double salesTaxRate;
	private double purchaseTaxRate;

	public TAXCode() {
		// TODO Auto-generated constructor stub
	}

	public TAXCode(Company company) {
		setCompany(company);
	}

	public TAXCode(TAXItemGroup taxItemGroup) {
		this(taxItemGroup.getCompany());
		this.name = taxItemGroup.getName();
		this.description = taxItemGroup.getDescription();
		this.isActive = taxItemGroup.isActive;
		this.TAXItemGrpForSales = taxItemGroup;
		this.isTaxable = true;
		this.TAXItemGrpForPurchases = taxItemGroup;
	}

	/**
	 * @return the isImported
	 */
	public boolean isImported() {
		return isImported;
	}

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
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isTaxable
	 */
	public boolean isTaxable() {
		return isTaxable;
	}

	/**
	 * @param isTaxable
	 *            the isTaxable to set
	 */
	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
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

	/**
	 * @return the vATItemGrpForPurchases
	 */
	public TAXItemGroup getTAXItemGrpForPurchases() {
		return TAXItemGrpForPurchases;
	}

	/**
	 * @param itemGrpForPurchases
	 *            the vATItemGrpForPurchases to set
	 */
	public void setTAXItemGrpForPurchases(TAXItemGroup itemGrpForPurchases) {
		TAXItemGrpForPurchases = itemGrpForPurchases;
	}

	/**
	 * @return the vATItemGrpForSales
	 */
	public TAXItemGroup getTAXItemGrpForSales() {
		return TAXItemGrpForSales;
	}

	/**
	 * @param itemGrpForSales
	 *            the vATItemGrpForSales to set
	 */
	public void setTAXItemGrpForSales(TAXItemGroup itemGrpForSales) {
		TAXItemGrpForSales = itemGrpForSales;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.TAX_CODE);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (isImported) {
			return false;
		}
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;

		updateTaxRates();
		setIsECsalesEntry();
		return false;
	}

	private void updateTaxRates() {
		if (this.getTAXItemGrpForSales() != null) {
			if (this.getTAXItemGrpForSales() instanceof TAXItem) {
				this.salesTaxRate = ((TAXItem) this.getTAXItemGrpForSales())
						.getTaxRate();
			} else {
				this.salesTaxRate = ((TAXGroup) this.getTAXItemGrpForSales())
						.getGroupRate();
			}
		}
		if (this.getTAXItemGrpForPurchases() != null) {
			if (this.getTAXItemGrpForPurchases() instanceof TAXItem) {
				this.purchaseTaxRate = ((TAXItem) this
						.getTAXItemGrpForPurchases()).getTaxRate();
			} else {
				this.purchaseTaxRate = ((TAXGroup) this
						.getTAXItemGrpForPurchases()).getGroupRate();
			}
		}
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		setIsECsalesEntry();
		updateTaxRates();
		ChangeTracker.put(this);
		return false;
	}

	private void setIsECsalesEntry() {

		if (this.getTAXItemGrpForSales() instanceof TAXGroup) {
			List<TAXItem> taxItems = ((TAXGroup) this.getTAXItemGrpForSales())
					.getTAXItems();
			if (!taxItems.isEmpty() && taxItems.size() >= 2) {
				// String vatRetunrnName = ((TAXGroup) this
				// .getTAXItemGrpForSales()).getTAXItems().get(0)
				// .getVatReturnBox().getName();
				// String vatRetunrnName1 = ((TAXGroup) this
				// .getTAXItemGrpForSales()).getTAXItems().get(1)
				// .getVatReturnBox().getName();

			}
		}
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		if (!UserUtils.canDoThis(TAXCode.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		TAXCode taxCode = (TAXCode) clientObject;
		// Query query = session.createQuery("from VATCode V where V.name=?")
		// .setParameter(0, vatCode.name);
		Query query = session.getNamedQuery("getTAXCodeWithSameName")
				.setParameter("name", this.name, EncryptedStringType.INSTANCE)
				.setParameter("id", this.getID())
				.setParameter("companyId", taxCode.getCompany().getID());
		List list = query.list();
		if (list != null && list.size() > 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_CONFLICT);
			// "A VATCode already exists with this name");
		}
		return true;
	}

	private void validateTaxItem(TAXItemGroup taxItemGrpForSales2,
			boolean isSales) throws AccounterException {

		if (taxItemGrpForSales2 != null) {
			if (taxItemGrpForSales2 instanceof TAXItem) {
				TAXAgency taxAgency = ((TAXItem) taxItemGrpForSales2)
						.getTaxAgency();

				if (taxAgency != null) {
					if (isSales) {
						if (taxAgency.getSalesLiabilityAccount() == null) {
							throw new AccounterException(
									AccounterException.ERROR_PLEASE_SELECT,
									Global.get().messages().other()
											+ Global.get().messages()
													.salesTaxItem());
						}
					} else if (taxAgency.getPurchaseLiabilityAccount() == null) {
						throw new AccounterException(
								AccounterException.ERROR_PLEASE_SELECT, Global
										.get().messages()
										.otherPurchaseTaxItem());
					}
				}
			} else {
				List<TAXItem> taxItems = ((TAXGroup) taxItemGrpForSales2)
						.getTAXItems();
				for (TAXItem item : taxItems) {
					if (item.getTaxAgency() != null) {
						if (isSales) {
							if (item.getTaxAgency().getSalesLiabilityAccount() == null) {
								throw new AccounterException(
										AccounterException.ERROR_PLEASE_SELECT,
										Global.get().messages().other()
												+ Global.get().messages()
														.salesTaxItem());
							}
						} else if (item.getTaxAgency()
								.getPurchaseLiabilityAccount() == null) {
							throw new AccounterException(
									AccounterException.ERROR_PLEASE_SELECT,
									Global.get().messages()
											.otherPurchaseTaxItem());
						}
					}
				}
			}
		}

	}

	public double getSalesTaxRate() {
		return salesTaxRate;
	}

	public void setSalesTaxRate(double salesTaxRate) {
		this.salesTaxRate = salesTaxRate;
	}

	public double getPurchaseTaxRate() {
		return purchaseTaxRate;
	}

	public void setPurchaseTaxRate(double purchaseTaxRate) {
		this.purchaseTaxRate = purchaseTaxRate;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.TAXCODE;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.taxCode()).gap();

		w.put(messages.name(), this.name);

		w.put(messages.description(), this.description).gap();

		w.put(messages.isTaxable(), this.isTaxable);

		w.put(messages.isActive(), this.isActive).gap();

		if (this.TAXItemGrpForPurchases != null)
			w.put(messages.taxItemForPurchases(),
					this.TAXItemGrpForPurchases.getName()).gap();

		if (this.TAXItemGrpForSales != null)
			w.put(messages.taxItemForSales(), this.TAXItemGrpForSales.getName());

		w.put(messages.defaultWare(), this.isDefault).gap();

		w.put(messages.salesTax(), this.salesTaxRate);

		w.put(messages.purchase() + " " + messages.taxRate(),
				this.purchaseTaxRate).gap();

	}

	@Override
	public void selfValidate() throws AccounterException {
		if (getName() == null || getName().trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_PLEASE_ENTER,
					Global.get().messages().taxCode());
		}
		if (isTaxable()) {
			if (Global.get().preferences().isTrackPaidTax()) {
				if (getTAXItemGrpForSales() == null
						&& getTAXItemGrpForPurchases() == null) {
					throw new AccounterException(
							AccounterException.ERROR_PLEASE_SELECT, Global
									.get().messages().salesOrPurchaseItem());
				} else if (getTAXItemGrpForSales() != null) {
					validateTaxItem(getTAXItemGrpForSales(), true);
				} else {
					validateTaxItem(getTAXItemGrpForPurchases(), false);
				}
			} else if (getTAXItemGrpForSales() == null) {
				throw new AccounterException(
						AccounterException.ERROR_PLEASE_SELECT, Global.get()
								.messages().salesItem());
			}
		}

	}
}
