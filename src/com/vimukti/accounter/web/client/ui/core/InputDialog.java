package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.vendors.ManageSupportListView;

/**
 * InputDialog is especially for take input from user using one or more text
 * fields.create its object where you want get some value from user.
 * 
 * @author kumar kasimala
 * 
 */

public class InputDialog extends BaseDialog {

	/**
	 * Instance variables
	 */
	private DynamicForm form;
	private final List<TextItem> textItems = new ArrayList<TextItem>();
	private TextItem textItem;
	private final String[] itemsNames;
	private final GroupDialog<?> parent;
	private ManageSupportListView parentView;

	public InputDialog(GroupDialog<?> parentDialog, String title, String desc,
			String... itemNames) {
		super(title, desc);
		this.getElement().setId("InputDialog");
		this.itemsNames = itemNames;
		initialise();
		this.parent = parentDialog;
	}

	public InputDialog(ManageSupportListView vendorGroupListDialog,
			String dialogueTitle, String... itemNames) {
		super(dialogueTitle, "");
		this.getElement().setId("InputDialog");
		this.itemsNames = itemNames;
		initialise();
		this.parent = null;
		this.parentView = vendorGroupListDialog;
	}

	/**
	 * Create GUI controls for this Dialog
	 */
	private void initialise() {
		// mainPanel.setSpacing(3);
		form = new DynamicForm("form");
		// form.setWidth("100%");
		StyledPanel layout = new StyledPanel("layout");
		for (String item : itemsNames)
			addTextItem(item);
		form.add(textItems.toArray(new TextItem[1]));
		layout.add(form);
		// okbtn.setWidth("60px");
		// cancelBtn.setWidth("60px");

		setBodyLayout(layout);
		// setSize("300", "100");
	}

	/**
	 * Add items to this Dialog.It will create one TextItem and append to this
	 * Dialog.
	 * 
	 * @param title
	 */
	private void addTextItem(String title) {
		textItem = new TextItem(title, "textItem");
		textItem.setRequired(true);
		textItems.add(textItem);
		// form.setFields(textItems.toArray(new TextItem[1]));
	}

	/**
	 * get All Items that you have added on this Dialog
	 * 
	 * @return
	 */
	public List<TextItem> getTextItems() {
		return this.textItems;
	}

	/**
	 * Get value of textItem by passing it position or location
	 * 
	 * @param index
	 * @return
	 */
	public String getTextValueByIndex(int index) {
		if (this.textItems.get(index) != null
				&& this.textItems.get(index).getValue() != null)
			return this.textItems.get(index).getValue().toString();
		return "";
	}

	/**
	 * set value of textItem by passing it position or location
	 * 
	 * @param index
	 * @return
	 */
	public void setTextItemValue(int index, String value) {
		this.textItems.get(index).setValue(value);
	}

	public DynamicForm getForm() {
		return this.form;
	}

	public void setFocus() {
		cancelBtn.setFocus(true);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		if (parentView != null) {
			result.add(parentView.validate());
		} else {
			result.add(parent.validate());
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		if (parentView != null) {
			return parentView.onOK();
		}
		return parent.onOK();
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
