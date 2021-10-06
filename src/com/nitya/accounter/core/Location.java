package com.nitya.accounter.core;

import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class Location extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	private static final long serialVersionUID = 1L;
	private String locationName;
	private String title;
	private String companyName;

	private Address address;
	private String email;
	private String phone;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return false;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Override
	public String getName() {
		return locationName;
	}

	@Override
	public void setName(String name) {
		this.locationName = name;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.LOCATION;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.location()).gap();

		w.put(messages.name(), this.locationName);

		if (this.companyName != null)
			w.put(messages.companyName(), this.companyName).gap();

		if (this.address != null)
			w.put(messages.address(), this.address.address1);

		w.put(messages.email(), this.email);
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.LOCATION);
		ChangeTracker.put(accounterCore);
		return super.onDelete(arg0);
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (this.locationName == null || this.locationName.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().name());
		}

		Set<Location> locations = getCompany().getLocations();
		for (Location location : locations) {
			if (location.getName().equalsIgnoreCase(getLocationName())
					&& location.getID() != getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_ALREADY_EXIST, Global
								.get().messages().name());
			}
		}
	}

}
