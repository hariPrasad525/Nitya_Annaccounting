package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class WareHouseView extends BaseView<ClientWarehouse> {

	private DynamicForm leftSideForm, rightSideForm;
	private TextItem wareHouseNameItem, contactNameItem, contactNumberItem,
			mobileNumberItem, DDINumberItem, addressItem, streetItem, cityItem,
			stateItem, countryItem, postalCodeItem, warehouseCodeItem;
	private StyledPanel vPanel;
	private CheckboxItem defaultWareHouse;

	@Override
	public void init() {
		super.init();
		this.getElement().setId("WareHouseView");
		createControls();
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientWarehouse());
		} else {
			initWarehouseData(getData());
		}
		super.initData();
	}

	private void initWarehouseData(ClientWarehouse warehouse) {

		warehouseCodeItem.setValue(warehouse.getWarehouseCode());
		wareHouseNameItem.setValue(warehouse.getName());

		ClientContact clientContact = warehouse.getContact();
		if (clientContact != null) {

			if (clientContact.getName() != null) {
				contactNameItem.setValue(clientContact.getName());
			}
			if (clientContact.getBusinessPhone() != null) {
				contactNumberItem.setValue(clientContact.getBusinessPhone());
			}

		}

		ClientAddress address = warehouse.getAddress();
		if (address != null) {

			if (address.getAddress1() != null) {
				addressItem.setValue(address.getAddress1());
			}
			if (address.getCity() != null) {
				cityItem.setValue(address.getCity());
			}
			if (address.getCountryOrRegion() != null) {
				countryItem.setValue(address.getCountryOrRegion());
			}
			if (address.getStateOrProvinence() != null) {
				stateItem.setValue(address.getStateOrProvinence());
			}
			if (address.getStreet() != null) {
				streetItem.setValue(address.getStreet());
			}
			if (address.getZipOrPostalCode() != null) {
				postalCodeItem.setValue(address.getZipOrPostalCode());
			}

		}
		if (warehouse.getMobileNumber() != null) {
			mobileNumberItem.setValue(warehouse.getMobileNumber());
		}
		if (warehouse.getDDINumber() != null) {
			DDINumberItem.setValue(warehouse.getDDINumber());
		}
		if (warehouse.isDefaultWarehouse()) {
			defaultWareHouse.setValue(true);
		}
		// defaultWareHouse.setValue(warehouse.isDefaultWarehouse());

	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();

		data.setName(wareHouseNameItem.getValue());
		data.setWarehouseCode(warehouseCodeItem.getValue());

		objectExist(data, result);

		result.add(leftSideForm.validate());
		return result;
	}

	private void objectExist(ClientWarehouse warehouse, ValidationResult result) {

		List<ClientWarehouse> list = Accounter.getCompany().getWarehouses();
		if (list == null || list.isEmpty())
			return;
		boolean checkName = false, checkCode = false;
		if (warehouse.getWarehouseCode() == null
				|| warehouse.getWarehouseCode().isEmpty()) {
			result.addError(warehouseCodeItem,
					messages.pleaseEnterCodeItShouldNotBeEmpty());
		} else {
			checkCode = true;
		}
		if (warehouse.getName() == null || warehouse.getName().isEmpty()) {
			result.addError(wareHouseNameItem,
					messages.pleaseEnterNameItShouldNotBeEmpty());
		} else {
			checkName = true;
		}
		if (checkName || checkCode) {
			for (ClientWarehouse old : list) {
				if (old.getID() == warehouse.getID()) {
					continue;
				}

				if (checkName
						&& warehouse.getName().equalsIgnoreCase(old.getName())) {
					result.addError(wareHouseNameItem, messages
							.objAlreadyExistsWithName(messages.wareHouse()));
				}
				if (checkCode
						&& warehouse.getWarehouseCode().equalsIgnoreCase(
								old.getWarehouseCode())) {
					result.addError(warehouseCodeItem,
							messages.warehouseAlreadyExistsWithCode());
				}
			}
		}
	}

	private void createControls() {
		Label lab1 = new Label(messages.wareHouse());
		lab1.setStyleName("label-title");

		vPanel = new StyledPanel("vPanel");
		vPanel.add(lab1);

		DynamicForm leftSideForm = getLeftSideForm();
		DynamicForm rightSideForm = getRightSideForm();
		StyledPanel mainHLay = getTopLayout();
		if (mainHLay != null) {
			mainHLay.add(leftSideForm);
			mainHLay.add(rightSideForm);
			vPanel.add(mainHLay);
		} else {
			vPanel.add(leftSideForm);
			vPanel.add(rightSideForm);
		}

		// this.setSize("100%", "100%");
		this.add(vPanel);

	}

	protected StyledPanel getTopLayout(){
		return new StyledPanel("mainHLay");
	}

	private DynamicForm getRightSideForm() {

		rightSideForm = new DynamicForm("rightSideForm");

		addressItem = new TextItem(messages.address(), "addressItem");
		addressItem.setEnabled(!isInViewMode());

		streetItem = new TextItem(messages.streetName(), "streetItem");
		streetItem.setEnabled(!isInViewMode());

		cityItem = new TextItem(messages.city(), "cityItem");
		cityItem.setEnabled(!isInViewMode());

		stateItem = new TextItem(messages.state(), "stateItem");
		stateItem.setEnabled(!isInViewMode());

		countryItem = new TextItem(messages.country(), "countryItem");
		countryItem.setEnabled(!isInViewMode());

		postalCodeItem = new TextItem(messages.postalCode(), "postalCodeItem");
		postalCodeItem.setEnabled(!isInViewMode());

		rightSideForm.add(addressItem, streetItem, cityItem, stateItem,
				countryItem, postalCodeItem);
		return rightSideForm;
	}

	private DynamicForm getLeftSideForm() {

		leftSideForm = new DynamicForm("leftSideForm");
		// leftSideForm.setWidth("100%");

		warehouseCodeItem = new TextItem(messages.warehouseCode(),
				"warehouseCodeItem");
		warehouseCodeItem.setRequired(true);
		warehouseCodeItem.setEnabled(!isInViewMode());

		wareHouseNameItem = new TextItem(messages.warehouseName(),
				"wareHouseNameItem");
		wareHouseNameItem.setRequired(true);
		wareHouseNameItem.setEnabled(!isInViewMode());

		contactNameItem = new TextItem(messages.contactName(),
				"contactNameItem");
		contactNameItem.setEnabled(!isInViewMode());

		contactNumberItem = new TextItem(messages.contactNumber(),
				"contactNumberItem");
		contactNumberItem.setEnabled(!isInViewMode());

		mobileNumberItem = new TextItem(messages.mobileNumber(),
				"mobileNumberItem");
		mobileNumberItem.setEnabled(!isInViewMode());

		DDINumberItem = new TextItem(messages.ddiNumber(), "DDINumberItem");
		DDINumberItem.setEnabled(!isInViewMode());

		defaultWareHouse = new CheckboxItem("checkBox", "wareHouseCheckbox");
		defaultWareHouse.setTitle(messages.defaultWareHouse());
		defaultWareHouse.setEnabled(!isInViewMode());

		leftSideForm.add(warehouseCodeItem, wareHouseNameItem, contactNameItem,
				contactNumberItem, mobileNumberItem, DDINumberItem,
				defaultWareHouse);

		return leftSideForm;
	}

	@Override
	public void onEdit() {

		AccounterAsyncCallback<Boolean> editCallback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					enableFormItems();
				}
			}

			@Override
			public void onException(AccounterException exception) {
				Accounter.showError(exception.getMessage());
			}
		};
		this.rpcDoSerivce.canEdit(AccounterCoreType.WAREHOUSE, data.getID(),
				editCallback);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		addressItem.setEnabled(!isInViewMode());
		streetItem.setEnabled(!isInViewMode());
		cityItem.setEnabled(!isInViewMode());
		countryItem.setEnabled(!isInViewMode());
		stateItem.setEnabled(!isInViewMode());
		postalCodeItem.setEnabled(!isInViewMode());
		warehouseCodeItem.setEnabled(!isInViewMode());
		wareHouseNameItem.setEnabled(!isInViewMode());
		contactNameItem.setEnabled(!isInViewMode());
		contactNumberItem.setEnabled(!isInViewMode());
		mobileNumberItem.setEnabled(!isInViewMode());
		DDINumberItem.setEnabled(!isInViewMode());
		defaultWareHouse.setEnabled(!isInViewMode());

	}

	private void updateData() {

		data.setWarehouseCode(warehouseCodeItem.getValue());
		data.setName(wareHouseNameItem.getValue().toString());
		data.setMobileNumber(mobileNumberItem.getValue().toString());
		data.setDDINumber(DDINumberItem.getValue().toString());
		data.setDefaultWarehouse((Boolean) defaultWareHouse.getValue());

		ClientAddress address = new ClientAddress();
		address.setType(ClientAddress.TYPE_WAREHOUSE);
		address.setAddress1(addressItem.getValue());
		address.setCity(cityItem.getValue());
		address.setCountryOrRegion(countryItem.getValue());
		address.setStreet(streetItem.getValue());
		address.setStateOrProvinence(stateItem.getValue());
		address.setZipOrPostalCode(postalCodeItem.getValue());
		data.setAddress(address);

		ClientContact contact = new ClientContact();
		contact.setName(contactNameItem.getValue());
		contact.setBusinessPhone(contactNumberItem.getValue());

		data.setContact(contact);
	}

	@Override
	public ClientWarehouse saveView() {
		ClientWarehouse saveView = super.saveView();
		if (saveView != null) {
			updateData();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {

		updateData();
		saveOrUpdate(getData());

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return messages.wareHouse();
	}

	@Override
	public List getForms() {
		// currently not using
		return null;
	}

	@Override
	public void printPreview() {

	}

	@Override
	public void print() {

	}

	@Override
	public void setFocus() {
		this.warehouseCodeItem.setFocus();

	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		String error = AccounterExceptions.getErrorString(exception);
		Accounter.showError(error);
	}
}
