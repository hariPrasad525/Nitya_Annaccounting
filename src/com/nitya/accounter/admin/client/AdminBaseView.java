package com.nitya.accounter.admin.client;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.AbstractBaseView;
import com.nitya.accounter.web.client.ui.core.ButtonBar;
import com.nitya.accounter.web.client.ui.core.CancelButton;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.IEditableView;
import com.nitya.accounter.web.client.ui.core.SaveAndCloseButton;
import com.nitya.accounter.web.client.ui.core.SaveAndNewButtom;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

public abstract class AdminBaseView<T extends IAccounterCore> extends
		AbstractBaseView<T> implements IEditableView {

	private ButtonBar buttonBar;

	protected int accountType;
	private EditMode mode;
	// private boolean isInViewMode;

	protected SaveAndCloseButton saveAndCloseButton;

	protected SaveAndNewButtom saveAndNewButton;

	protected CancelButton cancelButton;

	public AdminBaseView() {
		super();
	}

	protected abstract String getViewTitle();

	@Override
	public void init() {
		super.init();
		createView();
		createButtons(getButtonBar());
		// this.accountType = getCompany().getAccountingType();

	}

	public static boolean checkIfNotNumber(String in) {
		try {
			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return true;
		}
		return false;
	}

	private void createView() {

		// buttonBar.setStyleName("button_bar");
		//
		// super.add(buttonBar);
	}

	/**
	 * Return list of all DynamicForm items in this view
	 * 
	 * @return
	 */
	public abstract List<DynamicForm> getForms();

	@Override
	public void fitToSize(int height, int width) {
		// canvas.setHeight(height - 125 + "px");
		// canvas.setWidth(width - 15 + "px");
	}

	public ButtonBar getButtonBar() {
		return this.buttonBar;
	}

	/**
	 * This method will be called my all sub classes to add items to this view.
	 */
	public void add(Widget child) {
		int index = this.getWidgetIndex(buttonBar.asWidget());
		// Insert widgets above button bar
		super.insert(child, index);
	}

	public void setData(T data) {
		super.setData(data);
		if (data == null || data.getID() == 0) {
			this.setMode(EditMode.CREATE);
		} else {
			this.setMode(EditMode.VIEW);
		}
	}

	protected void createButtons(ButtonBar buttonBar) {
		this.saveAndCloseButton = new SaveAndCloseButton(this);
		this.saveAndNewButton = new SaveAndNewButtom(this);
		this.cancelButton = new CancelButton(this);
		if (!isInViewMode()) {
			buttonBar.add(saveAndCloseButton);
			buttonBar.add(saveAndNewButton);
		}
		buttonBar.add(cancelButton);
	}

	@Override
	public boolean canEdit() {
		return getMode() == EditMode.VIEW;
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	public EditMode getMode() {
		return mode;
	}

	public void setMode(EditMode mode) {
		if (this.mode == mode) {
			return;
		}
		this.mode = mode;
		getManager().updateButtons();
		if (!isInViewMode()) {
		}
	}

	public boolean isInViewMode() {
		return this.mode == EditMode.VIEW;
	}

}
