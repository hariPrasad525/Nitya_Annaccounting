package com.vimukti.accounter.web.client.ui.core;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;

/**
 */
public abstract class Action<T> implements Command {

	// @Override
	// public String mayStop() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public void onCancel() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onStop() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void start(AcceptsOneWidget panel, EventBus eventBus) {
	// // TODO Auto-generated method stub
	//
	// }
	protected AccounterMessages messages = Global.get().messages();
	protected AccounterMessages2 messages2 = Global.get().messages2();

	/**
	 * This action's text, or <code>null</code> if none.
	 */
	// private String text;
	private String viewName;
	/**
	 * This action's tool tip text, or <code>null</code> if none.
	 */
	private String toolTipText;

	// private String iconString;

	public String catagory = "";

	private boolean allowMultiple;

	protected T data;
	public List<T> listData;
	public long id;

	protected boolean isDependent;
	public boolean isCalledFromHistory;
	private ActionCallback<T> callback;

	/**
	 * setting Text for Action
	 */
	public Action() {
		this.isCalledFromHistory = false;
		setViewName(null);
	}

	/**
	 * Returns the text for this action.
	 * 
	 * @return String
	 */
	public abstract String getText();

	public String getViewModeText() {
		String viewText = getText();
		if (viewText.contains(messages.new1())) {
			viewText = viewText.replace(messages.new1(), messages.view());
		}
		return viewText;
	}

	/**
	 * Returns the Tool Tip Text for this action
	 * 
	 * @return String
	 */
	public String getToolTip() {
		return toolTipText;
	}

	/**
	 * Runs this action. Each action implementation must define the steps needed
	 * to carry out this action. The default implementation of this method in
	 * <code>Action</code> does nothing.
	 * 
	 * @return
	 * 
	 * @throws Throwable
	 */
	public abstract void run();

	public void execute() {
		run();
	}

	public void setInput(T data) {
		this.data = data;
	}

	public Object getInput() {
		return data;
	}

	/**
	 * Setter for the Text for the action
	 * 
	 * @param text
	 */
	// public void setText(String text) {
	// this. = text;
	// }

	/**
	 * Setter for Tool tip
	 * 
	 * @param toolTip
	 */
	public void setToolTip(String toolTip) {
		this.toolTipText = toolTip;
	}

	/**
	 * Setter for Icon
	 * 
	 * @param iconString
	 */
	// public void setIcon(String iconString) {
	// this.iconString = iconString;
	// }

	public String getCatagory() {
		return catagory;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}

	public boolean allowMultiple() {

		return this.allowMultiple;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof Action<?>)) {
			return false;
		}

		boolean isSameClass = object.getClass().getName()
				.equals(this.getClass().getName());

		return isSameClass;

	}

	public abstract ImageResource getBigImage();

	public abstract ImageResource getSmallImage();

	public abstract String getHistoryToken();

	public abstract String getHelpToken();

	public boolean isDependent() {
		return isDependent;
	}

	public void setDependent(boolean isDependent) {
		this.isDependent = isDependent;
	}

	public void run(T object, boolean isDependent) {

		setInput(object);

		setDependent(isDependent);
		run();
	}

	public void run(T object, long id, boolean isDependent) {

		setInput(object);
		setId(id);
		setDependent(isDependent);
		run();
	}

	public void run(List<T> object) {

		setListData(object);

		run();
	}

	public ActionCallback<T> getCallback() {
		return callback;
	}

	public void setCallback(ActionCallback<T> callback) {
		this.callback = callback;
	}

	public List<T> getListData() {
		return listData;
	}

	public void setListData(List<T> listData) {
		this.listData = listData;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
		if (viewName == null && this.getText() != null)
			if (getText().contains(messages.new1()))
				this.viewName = this.getText().replace(messages.new1(), "")
						.toLowerCase().trim();
			else
				this.viewName = this.getText().toLowerCase();
	}

	public String getViewName() {
		return viewName;
	}

	public String getEditText() {
		String editText = getText();
		if (editText.contains(messages.new1())) {
			editText = editText.replace(messages.new1(), messages.edit());
		}
		return editText;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
