package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.CreateClassDialog;

public class ClassListCombo extends CustomCombo<ClientAccounterClass> {

	private ValueCallBack<ClientAccounterClass> newClassHandler;

	public ClassListCombo(String title) {
		super(title, false, 1, "classListCombo");
	}

	public ClassListCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1, "classListCombo");

		ArrayList<ClientAccounterClass> accounterClasses = getCompany()
				.getaccounterClassesWithChilds();
		initCombo(accounterClasses);
	}

	@Override
	protected String getDisplayName(ClientAccounterClass object) {
		if (object != null)
			return object.getClassName() != null ? object.getClassName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientAccounterClass object, int col) {

		switch (col) {
		case 0:
			return getspaces(object);
		}
		return null;
	}

	@Override
	protected void setSelectedItem(ClientAccounterClass object, int row) {
		if (object == null) {
			setValue("");
			return;
		}
		StringBuffer buffer = new StringBuffer();
		ClientAccounterClass parentClass = object;
		while (parentClass.getParent() != 0) {
			buffer.append(parentClass.getClassName());
			parentClass = getCompany().getAccounterClass(
					parentClass.getParent());
			buffer.append(":");
		}
		buffer.append(parentClass.getClassName());
		buffer = getReverseBuffer(buffer.toString());
		setValue(buffer.toString());
	}

	/**
	 * get the reverse of StringBuffer
	 * 
	 * @param actvalString
	 * @return {@link StringBuffer} Reverse
	 */
	private StringBuffer getReverseBuffer(String actvalString) {
		String[] split = actvalString.split(":");
		StringBuffer buffer = new StringBuffer();
		for (int i = split.length - 1; i > 0; --i) {
			buffer.append(split[i]);
			buffer.append(':');
		}
		buffer.append(split[0]);
		return buffer;
	}

	/**
	 * 
	 * @param object
	 *            ClientAccounterClass
	 * @return {@link String}
	 */
	private String getspaces(ClientAccounterClass object) {
		StringBuffer buffer = new StringBuffer();
		if (object.getDepth() != 0) {
			for (int i = 0; i < object.getDepth(); i++) {
				buffer.append("     ");
			}
		}
		buffer.append(object.getClassName());
		return buffer.toString();
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.className();
	}

	@Override
	public void onAddNew() {
		CreateClassDialog classDialog = new CreateClassDialog(null,
				messages.createClass(), "");
		classDialog
				.addSuccessCallback(new ValueCallBack<ClientAccounterClass>() {

					@Override
					public void execute(
							final ClientAccounterClass accounterClass) {
						Accounter.createCRUDService().create(accounterClass,
								new AsyncCallback<Long>() {

									@Override
									public void onSuccess(Long result) {
										accounterClass.setID(result);
										Accounter.getCompany()
												.processUpdateOrCreateObject(
														accounterClass);
										setComboItem(accounterClass);
									}

									@Override
									public void onFailure(Throwable caught) {
										caught.printStackTrace();
									}
								});
					}
				});
	}

	@Override
	public void addItem(ClientAccounterClass object) {
		super.addItem(object);
		comboItems.clear();
		ArrayList<ClientAccounterClass> accounterClasses = getCompany()
				.getAccounterClasses();
		initCombo(accounterClasses);
	}

	/**
	 * @param valueCallBack
	 */

	public void addNewAccounterClassHandler(
			ValueCallBack<ClientAccounterClass> newClassHandler) {
		this.newClassHandler = newClassHandler;
	}
}
