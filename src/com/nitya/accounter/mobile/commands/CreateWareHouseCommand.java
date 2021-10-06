package com.nitya.accounter.mobile.commands;

import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.Warehouse;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.NumberRequirement;
import com.nitya.accounter.mobile.requirements.PhoneRequirement;
import com.nitya.accounter.mobile.requirements.StringRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientContact;
import com.nitya.accounter.web.client.core.ClientWarehouse;

public class CreateWareHouseCommand extends AbstractCommand {
	private final static String WAREHOUSECODE = "warehouseCode";
	private final static String WAREHOUSE_NAME = "warehousename";
	private final static String CONTACT_NAME = "contactname";
	private final static String CONTACT_NUM = "contactnumber";
	private final static String MOBILE_NUM = "mobilenumber";
	private final static String DDI_NUMBER = "ddinumber";
	private final static String IS_DEFAULT = "isdefaultwarehouse";
	private final static String ADDRESS = "Address";
	private final static String STREET = "Street";
	private final static String CITY = "City";
	private final static String STATE = "State";
	private final static String COUNTRY = "Country";
	private final static String POSTAL_CODE = "Postalcode";

	ClientWarehouse warehouse;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().wareHouse()));
				return "warehouseList";
			}
			ClientWarehouse clientWarehouse = CommandUtils.getWareHouse(
					context.getCompany(), string);
			if (clientWarehouse == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().wareHouse()));
				return "warehouseList " + string;
			}
			warehouse = clientWarehouse;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(WAREHOUSE_NAME).setValue(string);
			}
			warehouse = new ClientWarehouse();
		}
		return null;
	}

	private void setValues() {
		get(WAREHOUSECODE).setValue(warehouse.getWarehouseCode());
		get(WAREHOUSE_NAME).setValue(warehouse.getName());
		get(MOBILE_NUM).setValue(warehouse.getMobileNumber());
		get(DDI_NUMBER).setValue(warehouse.getDDINumber());
		get(IS_DEFAULT).setValue(warehouse.isDefaultWarehouse());
		ClientAddress address = warehouse.getAddress();
		get(ADDRESS).setValue(address.getAddress1());
		get(CITY).setValue(address.getCity());
		get(COUNTRY).setValue(address.getCountryOrRegion());
		get(STREET).setValue(address.getStreet());
		get(STATE).setValue(address.getStateOrProvinence());
		get(POSTAL_CODE).setValue(address.getZipOrPostalCode());

		ClientContact contact = warehouse.getContact();
		get(CONTACT_NAME).setValue(contact == null ? null : contact.getName());
		get(CONTACT_NUM).setValue(
				contact == null ? null : contact.getBusinessPhone());
	}

	@Override
	protected String getWelcomeMessage() {
		return warehouse.getID() == 0 ? getMessages().create(
				getMessages().wareHouse()) : getMessages().updating(
				getMessages().wareHouse());
	}

	@Override
	protected String getDetailsMessage() {
		return warehouse.getID() == 0 ? getMessages().readyToCreate(
				getMessages().wareHouse()) : getMessages().readyToUpdate(
				getMessages().wareHouse());
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	public String getSuccessMessage() {
		return warehouse.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().wareHouse()) : getMessages().updateSuccessfully(
				getMessages().wareHouse());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NumberRequirement(WAREHOUSECODE, getMessages()
				.pleaseEnter(getMessages().warehouseCode()), getMessages()
				.warehouseCode(), false, true));
		list.add(new StringRequirement(WAREHOUSE_NAME, getMessages()
				.pleaseEnter(getMessages().warehouseName()), getMessages()
				.warehouseName(), false, true) {
			@Override
			public void setValue(Object value) {
				if (CreateWareHouseCommand.this
						.isWareHouseExists((String) value)) {
					addFirstMessage(getMessages().alreadyExist());
					return;
				}
				addFirstMessage(getMessages().pleaseEnter(
						getMessages().warehouseName()));
				super.setValue(value);
			}
		});
		list.add(new StringRequirement(CONTACT_NAME, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contactName(),
				true, true));
		list.add(new PhoneRequirement(CONTACT_NUM, getMessages().pleaseEnter(
				getMessages().contactNumber()), getMessages().contactNumber(),
				true, true));
		list.add(new PhoneRequirement(MOBILE_NUM, getMessages().pleaseEnter(
				getMessages().mobileNumber()), getMessages().mobileNumber(),
				true, true));
		list.add(new NumberRequirement(DDI_NUMBER, getMessages().pleaseEnter(
				getMessages().ddiNumber()), getMessages().ddiNumber(), true,
				true));
		list.add(new BooleanRequirement(IS_DEFAULT, true) {

			@Override
			protected String getTrueString() {
				return getMessages().defaultWareHouse();
			}

			@Override
			protected String getFalseString() {
				return getMessages().notDefault();
			}
		});
		list.add(new StringRequirement(ADDRESS, getMessages().pleaseEnter(
				getMessages().address()), getMessages().address(), true, true));
		list.add(new StringRequirement(STREET, getMessages().pleaseEnter(
				getMessages().streetName()), getMessages().streetName(), true,
				true));
		list.add(new StringRequirement(CITY, getMessages().pleaseEnter(
				getMessages().city()), getMessages().city(), true, true));
		list.add(new StringRequirement(STATE, getMessages().pleaseEnter(
				getMessages().state()), getMessages().state(), true, true));
		list.add(new StringRequirement(COUNTRY, getMessages().pleaseEnter(
				getMessages().country()), getMessages().country(), true, true));
		list.add(new StringRequirement(POSTAL_CODE, getMessages().pleaseEnter(
				getMessages().postalCode()), getMessages().postalCode(), true,
				true));
	}

	protected boolean isWareHouseExists(String value) {
		Set<Warehouse> warehouses = getCompany().getWarehouses();
		for (Warehouse warehouse : warehouses) {
			if (warehouse.getID() != this.warehouse.getID()
					&& warehouse.getName().equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String warehouseCode = get(WAREHOUSECODE).getValue();
		warehouse.setWarehouseCode(warehouseCode);
		String warehouseName = get(WAREHOUSE_NAME).getValue();
		warehouse.setName(warehouseName);
		String mobileNumber = get(MOBILE_NUM).getValue();
		warehouse.setMobileNumber(mobileNumber);
		String ddinumber = get(DDI_NUMBER).getValue();
		warehouse.setDDINumber(ddinumber);
		Boolean isDefaultWarehouse = get(IS_DEFAULT).getValue();
		warehouse.setDefaultWarehouse(isDefaultWarehouse);

		ClientAddress address = new ClientAddress();
		address.setType(ClientAddress.TYPE_WAREHOUSE);
		String address1 = get(ADDRESS).getValue();
		address.setAddress1(address1);
		String city = get(CITY).getValue();
		address.setCity(city);
		String country = get(COUNTRY).getValue();
		address.setCountryOrRegion(country);
		String street = get(STREET).getValue();
		address.setStreet(street);
		String state = get(STATE).getValue();
		address.setStateOrProvinence(state);
		String postalCode = get(POSTAL_CODE).getValue();
		address.setZipOrPostalCode(postalCode);
		warehouse.setAddress(address);

		ClientContact contact = new ClientContact();
		String contactName = get(CONTACT_NAME).getValue();
		contact.setName(contactName);
		String contactNum = get(CONTACT_NUM).getValue();
		contact.setBusinessPhone(contactNum);
		warehouse.setContact(contact);

		create(warehouse, context);
		return super.onCompleteProcess(context);
	}
}
