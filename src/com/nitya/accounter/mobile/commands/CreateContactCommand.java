package com.nitya.accounter.mobile.commands;

import java.util.List;

import com.nitya.accounter.core.Customer;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.CustomerRequirement;
import com.nitya.accounter.mobile.requirements.EmailRequirement;
import com.nitya.accounter.mobile.requirements.NameRequirement;
import com.nitya.accounter.mobile.requirements.PhoneRequirement;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientContact;
import com.nitya.accounter.web.client.core.ClientCustomer;

public class CreateContactCommand extends AbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (!string.isEmpty()) {
			Customer customer = (Customer) CommandUtils.getServerObjectById(
					Long.parseLong(string), AccounterCoreType.CUSTOMER);
			get("Customer").setValue(customer);
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().contact());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement("Name", getMessages().pleaseEnter(
				getMessages().name()), getMessages().name(), false, true));

		list.add(new StringRequirement("Title", getMessages().pleaseEnter(
				getMessages().title()), getMessages().title(), true, true));

		list.add(new PhoneRequirement("BusinessPhone", getMessages()
				.pleaseEnter(getMessages().businessPhone()), getMessages()
				.businessPhone(), true, true));

		list.add(new EmailRequirement("Email", getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new CustomerRequirement("Customer", getMessages().pleaseEnter(
				getMessages().customer()), "Related Customer", false, true,
				null) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientContact contact = new ClientContact();
		String name = get("Name").getValue();
		String title = get("Title").getValue();
		String phone = get("BusinessPhone").getValue();
		String email = get("Email").getValue();
		contact.setName(name);
		contact.setTitle(title);
		contact.setBusinessPhone(phone);
		contact.setEmail(email);
		Customer customer = get("Customer").getValue();
		ClientCustomer clientCustomer = (ClientCustomer) CommandUtils
				.getClientObjectById(customer.getID(),
						AccounterCoreType.CUSTOMER, getCompanyId());
		clientCustomer.addContact(contact);
		create(clientCustomer, context);
		return null;
	}
}
