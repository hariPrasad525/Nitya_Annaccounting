package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientContact;

public abstract class CustomerContactRequirement extends
		AbstractTableRequirement<ClientContact> {
	private static final String CONTACT_NAME = "contactName";
	private static final String TITLE = "contacttitle";
	private static final String BUSINESS_PHONE = "conatctbusinessPhone";
	private static final String EMAIL = "contactemail";
	private static final String IS_PRIMARY = "conatctisPrimary";

	public CustomerContactRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, true, isOptional,
				isAllowFromContext);
		setDefaultValue(new ArrayList<ClientContact>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new NameRequirement(CONTACT_NAME, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contactName(),
				true, true));

		list.add(new NameRequirement(TITLE, getMessages().pleaseEnter(
				getMessages().title()), getMessages().title(), true, true));

		list.add(new PhoneRequirement(BUSINESS_PHONE, getMessages()
				.pleaseEnter(getMessages().businessPhone()), getMessages()
				.businessPhone(), true, true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new BooleanRequirement(IS_PRIMARY, true) {

			@Override
			protected String getTrueString() {
				return getMessages().primary() + getMessages().contact();
			}

			@Override
			protected String getFalseString() {
				return getMessages().notPrimary() + getMessages().contact();
			}
		});
	}

	@Override
	protected void getRequirementsValues(ClientContact obj) {
		String contactName = get(CONTACT_NAME).getValue();
		obj.setName(contactName);
		String email = get(EMAIL).getValue();
		obj.setEmail(email);
		Boolean isPrimary = get(IS_PRIMARY).getValue();
		obj.setPrimary(isPrimary);
		String phone = get(BUSINESS_PHONE).getValue();
		obj.setBusinessPhone(phone);
		String title = get(TITLE).getValue();
		obj.setTitle(title);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientContact obj) {
		get(CONTACT_NAME).setDefaultValue(obj.getName());
		get(EMAIL).setDefaultValue(obj.getEmail());
		get(IS_PRIMARY).setDefaultValue(obj.isPrimary());
		get(BUSINESS_PHONE).setDefaultValue(obj.getBusinessPhone());
		get(TITLE).setDefaultValue(obj.getTitle());
	}

	@Override
	protected ClientContact getNewObject() {
		return new ClientContact();
	}

	@Override
	protected Record createFullRecord(ClientContact t) {
		Record record = new Record(t);
		record.add(getMessages().primary(), t.isPrimary());
		record.add(getMessages().contactName(), t.getName());
		record.add(getMessages().title(), t.getTitle());
		record.add(getMessages().businessPhone(), t.getBusinessPhone());
		record.add(getMessages().email(), t.getEmail());
		return record;
	}

	@Override
	protected abstract List<ClientContact> getList();

	@Override
	protected Record createRecord(ClientContact t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<ClientContact> contacts = getValue();
		return contacts.isEmpty() ? getMessages().add()
				+ getMessages().contact() : getMessages().addMore(
				getMessages().contacts());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().contacts());
	}

	@Override
	protected boolean contains(List<ClientContact> oldValues, ClientContact t) {
		for (ClientContact clientContact : oldValues) {
			if (clientContact.getName() != null
					&& clientContact.getName().equals(t.getName())) {
				return true;
			}
		}
		return false;
	}
}
