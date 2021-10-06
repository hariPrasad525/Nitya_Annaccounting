package com.nitya.accounter.web.client.ui.grids;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.core.ClientContact;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;

public class ContactGrid extends ListGrid<ClientContact> {

	boolean isEditMode;

	public ContactGrid() {
		super(false);
	}

	public void initContacts(Set<ClientContact> contactsList) {
		ClientContact rec[] = new ClientContact[contactsList.size()];
		int i = 0;

		ClientContact temp = null;
		for (ClientContact cont : contactsList) {

			rec[i] = new ClientContact();
			rec[i].setPrimary(cont.isPrimary());
			rec[i].setBusinessPhone(cont.getBusinessPhone());
			rec[i].setName(cont.getName());
			rec[i].setEmail(cont.getEmail());
			if (cont.isPrimary()) {
				temp = rec[i];
			}
			rec[i++].setTitle(cont.getTitle());
		}
		this.isEditMode = true;
		this.setRecords(Arrays.asList(rec));
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 50;
		case 2:
			return 150;
		case 3:
			return 150;
		case 4:
			return 150;
		case 5:
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		default:
			return -1;
		}
	}

	@Override
	protected int getColumnType(int col) {
		if (col == 0) {
			return ListGrid.COLUMN_TYPE_CHECK;
		}
		if (col == 5)
			return ListGrid.COLUMN_TYPE_IMAGE;
		// if (col == 6)
		// return ListGrid.COLUMN_TYPE_IMAGE;

		return ListGrid.COLUMN_TYPE_TEXTBOX;
	}

	@Override
	protected Object getColumnValue(ClientContact contact, int col) {
		switch (col) {
		case 0:
			return contact.isPrimary();
		case 1:
			return contact.getName();
		case 2:
			return contact.getTitle();
		case 3:
			return contact.getBusinessPhone();
		case 4:
			return contact.getEmail();
		case 5:
			return Accounter.getFinanceMenuImages().delete();
			// case 6:
			// return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		}
		return "";
	}

	@Override
	protected String[] getColumns() {
		messages = messages;
		return new String[] { messages.primary(), messages.contactName(),
				messages.title(), messages.businessPhone(), messages.email(),
				" " };
	}

	@Override
	protected String[] getSelectValues(ClientContact obj, int col) {
		return null;
	}

	@Override
	protected boolean isEditable(ClientContact obj, int row, int col) {
		return true;
	}

	@Override
	protected void onClick(ClientContact contact, int row, int col) {
		switch (col) {
		case 0:
			boolean isSelected = ((CheckBox) this.getWidget(row, col))
					.getValue();
			List<ClientContact> contacts = getRecords();
			if (isSelected) {
				checkedUncheckedCheckBox(false);
				for (ClientContact clientContact : contacts) {
					if (!clientContact.equals(contact)) {
						clientContact.setPrimary(false);
					}
				}

			}
			contact.setPrimary(isSelected);

			updateData(contact);
			break;
		case 5:
			deleteRecord(contact);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDoubleClick(ClientContact obj) {

	}

	@Override
	protected void onValueChange(ClientContact obj, int col, Object value) {
		// it is not using any where

	}

	@Override
	protected int sort(ClientContact obj1, ClientContact obj2, int index) {
		switch (index) {

		default:
			break;
		}
		return 0;
	}

	private boolean isNullContact(ClientContact contact) {
		for (int colValue = 1; colValue <= 4; colValue++) {
			Object value = this.getColumnValue(contact, colValue);
			if (value != null && value != "") {
				return false;
			}
		}
		return true;
	}

	@Override
	public void editComplete(ClientContact contact, Object value, int col) {
		switch (col) {
		case 0:
			return;
		case 1:
			contact.setName(value.toString());
			break;
		case 2:
			contact.setTitle(value.toString());
			break;
		case 3:
			contact.setBusinessPhone(getvalidatePhoneValue(value.toString()));
			break;
		case 4:
			String email = value.toString();
			contact.setEmail(getValidMail(email));
			break;
		default:
			break;
		}
		updateRecord(contact, currentRow, col);
		super.editComplete(contact, value, col);
	}

	private String getValidMail(String email) {
		if (!UIUtils.isValidEmail(email)) {
			Accounter.showError(messages.invalidEmail());
			return "";
		} else
			return email;

	}

	private String getvalidatePhoneValue(String valueString) {
		try {
			Long phone = Long.parseLong(valueString);
			return String.valueOf(phone);
		} catch (Exception e) {
			if (valueString.length() != 0) {
				Accounter.showError(messages.invalidBusinessPhoneVal());
				return "";
			}
		}
		return "";

	}

	@Override
	public void addData(ClientContact obj) {
		super.addData(obj);
		if (!isEditMode)
			if (this.getRecords().size() == 1) {
				((CheckBox) this.getWidget(0, 0)).setValue(true);
				obj.setPrimary(true);
			}

	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "primary";
		case 1:
			return "contactName";
		case 2:
			return "title";
		case 3:
			return "businessPhone";
		case 4:
			return "email";
		default:
			return "";
		}

	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "primary-value";
		case 1:
			return "contactName-value";
		case 2:
			return "title-value";
		case 3:
			return "businessPhone-value";
		case 4:
			return "email-value";
		default:
			return "";
		}
	}
}

