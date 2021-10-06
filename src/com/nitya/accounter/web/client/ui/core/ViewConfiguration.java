package com.nitya.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.ui.AbstractBaseView;

/**
 * 
 * @author Fernandez
 * 
 */
public class ViewConfiguration {

	IsSerializable object;

	AccounterAsyncCallback<Object> callback;

	boolean disableSaveAndNew;

	boolean isEditAndDisabled;

	boolean initWithPayee;

	ClientPayee payeeObject;

	String title;
	public String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title != null ? title : "";
	}

	List<AbstractBaseView> historyList = new ArrayList<AbstractBaseView>();

	/**
	 * @return the object
	 */
	public IsSerializable getObject() {
		return object;
	}

	/**
	 * @param editableObject
	 *            the object to set
	 */
	public void setEditObject(IsSerializable editableObject) {
		this.object = editableObject;
		setEdit(true);
	}

	/**
	 * @return the callback
	 */
	public AccounterAsyncCallback<Object> getCallback() {
		return callback;
	}

	/**
	 * @param callback
	 *            the callback to set
	 */
	public void setCallback(AccounterAsyncCallback<Object> callback) {
		this.callback = callback;
		if (callback != null)
			setDisableSaveAndNew(true);
	}

	/**
	 * @return the enableSaveAndNew
	 */
	public boolean isDisableSaveAndNew() {
		return disableSaveAndNew;
	}

	/**
	 * @param enableSaveAndNew
	 *            the enableSaveAndNew to set
	 */
	public void setDisableSaveAndNew(boolean enableSaveAndNew) {
		this.disableSaveAndNew = enableSaveAndNew;
	}

	/**
	 * @return the isEdit
	 */
	public boolean isEdit() {
		return isEditAndDisabled;
	}

	/**
	 * @param isEdit
	 *            the isEdit to set
	 */
	public void setEdit(boolean isEdit) {
		this.isEditAndDisabled = isEdit;
	}

	/**
	 * @return the initWithPayee
	 */
	public boolean isInitWithPayee() {
		return initWithPayee;
	}

	/**
	 * @param initWithPayee
	 *            the initWithPayee to set
	 */
	public void setInitWithPayee(boolean initWithPayee) {
		this.initWithPayee = initWithPayee;
	}

	/**
	 * @return the payeeObject
	 */
	public ClientPayee getPayeeObject() {
		return payeeObject;
	}

	/**
	 * @param payeeObject
	 *            the payeeObject to set
	 */
	public void setPayeeObject(ClientPayee payeeObject) {
		this.payeeObject = payeeObject;
	}

	/**
	 * @return the historyList
	 */

	public List<AbstractBaseView> getHistoryList() {
		return historyList;
	}

	/**
	 * @param historyList
	 *            the historyList to set
	 */

	public void setHistoryList(List<AbstractBaseView> historyList) {
		this.historyList = historyList;
	}

	public static ViewConfiguration getDefaultConfiguration() {

		return new ViewConfiguration();
	}

}
