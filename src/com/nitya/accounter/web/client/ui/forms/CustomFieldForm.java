package com.nitya.accounter.web.client.ui.forms;

import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCustomField;
import com.nitya.accounter.web.client.core.ClientCustomFieldValue;
import com.nitya.accounter.web.client.core.ClientPayee;

public class CustomFieldForm extends DynamicForm {
	public CustomFieldForm() {
		super("customFields");
	}

	public void createControls(ClientCompany company,
			Set<ClientCustomFieldValue> customFieldValues, int payeeType) {
		clear();
		ArrayList<ClientCustomField> customFields = company.getCustomFields();
		for (ClientCustomField c : customFields) {
			if (c.isShowCustomer() && payeeType == ClientPayee.TYPE_CUSTOMER) {
				TextItem t = new TextItem(c.getName(), "name");
				add(t);
			}
			if (c.isShowVendor() && payeeType == ClientPayee.TYPE_VENDOR) {
				TextItem t = new TextItem(c.getName(), "name");
				add(t);
			}
			if (c.isShowEmployee() && payeeType == ClientPayee.TYPE_EMPLOYEE) {
				TextItem t = new TextItem(c.getName(), "name");
				add(t);
			}
		}
		if (customFieldValues != null) {
			for (ClientCustomFieldValue cv : customFieldValues) {
				ClientCustomField cf = company.getClientCustomField(cv
						.getCustomField());
				for (Widget widget : getChildren()) {
					if (widget instanceof FormItem) {
						@SuppressWarnings("unchecked")
						FormItem<String> t = (FormItem<String>) widget;
						if (t.getTitle().equals(cf.getName())) {
							t.setValue(cv.getValue());
							break;
						}
					}
				}
			}
		}
	}

	public void updateValues(Set<ClientCustomFieldValue> list,
			ClientCompany company, int payeeType) {

		if (list.isEmpty()) {
			for (Widget widget : getChildren()) {
				if (widget instanceof FormItem) {
					FormItem<String> textItem = (FormItem<String>) widget;
					ClientCustomField field = company
							.getCustomFieldByTitle(textItem.getTitle());
					if (payeeType == ClientPayee.TYPE_CUSTOMER) {
						if (field != null && field.isShowCustomer()) {
							ClientCustomFieldValue clientCustomFieldValue = new ClientCustomFieldValue();
							clientCustomFieldValue
									.setCustomField(field.getID());
							clientCustomFieldValue
									.setValue(textItem.getValue());
							list.add(clientCustomFieldValue);
						}
					} else if (payeeType == ClientPayee.TYPE_EMPLOYEE) {
						if (field != null && field.isShowEmployee()) {
							ClientCustomFieldValue clientCustomFieldValue = new ClientCustomFieldValue();
							clientCustomFieldValue
									.setCustomField(field.getID());
							clientCustomFieldValue
									.setValue(textItem.getValue());
							list.add(clientCustomFieldValue);
						}
					} else {
						if (field != null && field.isShowVendor()) {
							ClientCustomFieldValue clientCustomFieldValue = new ClientCustomFieldValue();
							clientCustomFieldValue
									.setCustomField(field.getID());
							clientCustomFieldValue
									.setValue(textItem.getValue());
							list.add(clientCustomFieldValue);
						}
					}
				}
			}
		} else {
			for (Widget widget : getChildren()) {
				if (widget instanceof FormItem) {
					FormItem<String> textItem = (FormItem<String>) widget;

					ClientCustomField field = company
							.getCustomFieldByTitle(textItem.getTitle());
					if (payeeType == ClientPayee.TYPE_CUSTOMER) {
						if (field != null && field.isShowCustomer()) {

							ClientCustomFieldValue clientCustomFieldValue = null;
							for (ClientCustomFieldValue f : list) {
								if (f.getCustomField() == field.getID()) {
									clientCustomFieldValue = f;
									break;
								}
							}
							if (clientCustomFieldValue == null) {// New Custom
																	// Field
								clientCustomFieldValue = new ClientCustomFieldValue();
								clientCustomFieldValue.setCustomField(field
										.getID());
								clientCustomFieldValue.setValue(textItem
										.getValue());
								list.add(clientCustomFieldValue);
							} else {
								clientCustomFieldValue.setValue(textItem
										.getValue());
							}
						}
					} else if (payeeType == ClientPayee.TYPE_EMPLOYEE) {

						if (field != null && field.isShowEmployee()) {

							ClientCustomFieldValue clientCustomFieldValue = null;
							for (ClientCustomFieldValue f : list) {
								if (f.getCustomField() == field.getID()) {
									clientCustomFieldValue = f;
									break;
								}
							}
							if (clientCustomFieldValue == null) {// New Custom
																	// Field
								clientCustomFieldValue = new ClientCustomFieldValue();
								clientCustomFieldValue.setCustomField(field
										.getID());
								clientCustomFieldValue.setValue(textItem
										.getValue());
								list.add(clientCustomFieldValue);
							} else {
								clientCustomFieldValue.setValue(textItem
										.getValue());
							}
						}

					} else {
						if (field != null && field.isShowVendor()) {

							ClientCustomFieldValue clientCustomFieldValue = null;
							for (ClientCustomFieldValue f : list) {
								if (f.getCustomField() == field.getID()) {
									clientCustomFieldValue = f;
									break;
								}
							}
							if (clientCustomFieldValue == null) {// New Custom
																	// Field
								clientCustomFieldValue = new ClientCustomFieldValue();
								clientCustomFieldValue.setCustomField(field
										.getID());
								clientCustomFieldValue.setValue(textItem
										.getValue());
								list.add(clientCustomFieldValue);
							} else {
								clientCustomFieldValue.setValue(textItem
										.getValue());
							}
						}
					}

				}
			}
		}
	}

}
