package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
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

public class PriceLevel extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5616443741745926670L;

	/**
	 * This field stores the name of the Price Level
	 */
	String name;

	/**
	 * Price Level Percentage
	 */
	double percentage;

	boolean isPriceLevelDecreaseByThisPercentage;

	boolean isDefault;

	public PriceLevel() {

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}

	/**
	 * @return the isPriceLevelDecreaseByThisPercentage
	 */
	public boolean isPriceLevelDecreaseByThisPercentage() {
		return isPriceLevelDecreaseByThisPercentage;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.PRICE_LEVEL);
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
		PriceLevel priceLevel = (PriceLevel) clientObject;
		PriceLevel pleLevel = (PriceLevel) session
				.getNamedQuery("getPriceLevel.by.Name")
				.setParameter("name", priceLevel.name,
						EncryptedStringType.INSTANCE)
				.setEntity("company", priceLevel.getCompany()).uniqueResult();
		if (priceLevel != null && pleLevel != null) {
			if (priceLevel.getID() != pleLevel.getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "A ShippingMethod already exists with this name");
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
		return IAccounterCore.PRICE_LEVEL;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.priceLevel()).gap();

		w.put(messages.name(), this.name);
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (name == null && name.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().priceLevel());
		}

	}
}
