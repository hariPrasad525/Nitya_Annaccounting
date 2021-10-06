/**
 * 
 */
package com.nitya.accounter.core;

import java.util.Iterator;
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
 * Warehouse POJO.
 * 
 * @author Srikanth.J
 * 
 */
public class Warehouse extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 640523202925694992L;

	private Address address;

	private String name;
	private String warehouseCode;
	private Contact contact;

	private boolean isDefaultWarehouse;
	private String DDINumber;
	private String mobileNumber;

	public Warehouse(String warehouseCode, String name, Address address,
			boolean isDefault) {
		this.name = name;
		this.warehouseCode = warehouseCode;
		this.isDefaultWarehouse = isDefault;
		this.address = address;
	}

	public Warehouse() {

	}

	public Address getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		if (!UserUtils.canDoThis(Warehouse.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		Query query = session
				.getNamedQuery("getWarehouse")
				.setParameter("companyId",
						((Warehouse) clientObject).getCompany().getID())
				.setParameter("name", this.name, EncryptedStringType.INSTANCE)
				.setLong("id", this.getID());
		List list = query.list();

		if (list != null || list.size() > 0 || list.get(0) != null) {
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {

				String object = (String) iterator.next();
				if (this.getName().equals(object)) {
					throw new AccounterException(
							AccounterException.ERROR_NAME_CONFLICT);

				}
			}
		}
		return true;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Contact getContact() {
		return contact;
	}

	@Override
	public boolean onSave(Session s) throws CallbackException {
		if (isOnSaveProccessed)
			return true;
		super.onSave(s);
		isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(s);
		return false;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.WAREHOUSE);
		ChangeTracker.put(accounterCore);

		return false;
	}

	public boolean isDefaultWarehouse() {
		return isDefaultWarehouse;
	}

	public void setDefaultWarehouse(boolean isDefaultWarehouse) {
		this.isDefaultWarehouse = isDefaultWarehouse;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getDDINumber() {
		return DDINumber;
	}

	public void setDDINumber(String dDINumber) {
		DDINumber = dDINumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.WAREHOUSE;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.wareHouse()).gap();

		if (this.address != null)
			w.put(messages.address(), this.address.toString());

		w.put(messages.name(), this.name).gap();

		w.put(messages.warehouseCode(), this.warehouseCode).gap();

		if (this.contact != null)
			w.put(messages.contact(), this.contact.getName());

		w.put(messages.defaultWareHouse(), this.isDefaultWarehouse).gap();

		w.put(messages.number(), this.DDINumber).gap();

		if (this.mobileNumber != null)
			w.put(messages.mobileNumber(), this.mobileNumber).gap();

	}

	@Override
	public void selfValidate() throws AccounterException {
		if (warehouseCode == null || warehouseCode.trim().length() == 0) {
			throw new AccounterException(
					AccounterException.ERROR_WAREHOUSE_CODE_NULL, Global.get()
							.messages().warehouseCode());
		}

		if (name == null || name.trim().length() == 0) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().wareHouse());
		}
	}
}
